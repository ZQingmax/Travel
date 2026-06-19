package com.mingde.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.mingde.common.Result;
import com.mingde.common.config.AliPayConfig;
import com.mingde.entity.Orders;
import com.mingde.exception.CustomException;
import com.mingde.service.OrdersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private OrdersService ordersService;

    @GetMapping("/pay")
    public void pay(String orderNo, HttpServletResponse httpResponse) throws Exception {
        Orders orders = ordersService.selectOwnByOrderNo(orderNo);
        if (orders == null) {
            throw new CustomException("404", "\u672a\u627e\u5230\u8ba2\u5355");
        }

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("total_amount", orders.getTotal());
        bizContent.set("subject", orders.getName());
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws Exception {
        if (!"TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }

        String sign = params.get("sign");
        String content = AlipaySignature.getSignCheckContentV1(params);
        boolean checkSignature = AlipaySignature.rsa256CheckContent(
                content, sign, aliPayConfig.getAlipayPublicKey(), CHARSET);
        if (!checkSignature) {
            return;
        }

        String tradeNo = params.get("out_trade_no");
        Orders orders = ordersService.selectByOrderNo(tradeNo);
        if (orders == null) {
            return;
        }
        orders.setStatus("\u5f85\u53d1\u8d27");
        orders.setPayTime(params.get("gmt_payment"));
        orders.setPayNo(params.get("trade_no"));
        ordersService.updateFromPayment(orders);
    }

    @PutMapping("/refund")
    public Result refund(String orderNo) {
        Orders orders = ordersService.selectOwnByOrderNo(orderNo);
        if (ObjectUtil.isNull(orders)) {
            throw new CustomException("404", "\u672a\u627e\u5230\u8ba2\u5355");
        }

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("refund_amount", orders.getTotal());
        bizContent.set("trade_no", orders.getPayNo());
        bizContent.set("out_request_no", IdUtil.fastSimpleUUID());
        request.setBizContent(bizContent.toString());

        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                Orders dbOrder = ordersService.selectByOrderNo(orderNo);
                dbOrder.setStatus("\u5df2\u9000\u6b3e");
                ordersService.updateFromPayment(dbOrder);
            } else {
                throw new CustomException("500", "\u9000\u6b3e\u5931\u8d25");
            }
        } catch (AlipayApiException e) {
            throw new CustomException("500", "\u9000\u6b3e\u5931\u8d25");
        }

        return Result.success();
    }
}