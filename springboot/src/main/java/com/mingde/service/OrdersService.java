package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Orders;
import com.mingde.entity.Tourism;
import com.mingde.exception.CustomException;
import com.mingde.mapper.OrdersMapper;
import com.mingde.mapper.TourismMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersService {

    private static final String STATUS_PENDING_PAY = "待支付";
    private static final String STATUS_CANCELED = "已取消";

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private TourismMapper tourismMapper;

    @Transactional
    public void add(Orders orders) {
        Account currentUser = AuthUtils.currentUser();
        if (orders.getTourismId() == null || orders.getNum() == null || orders.getNum() <= 0) {
            throw new CustomException("400", "订单参数错误");
        }
        Tourism tourism = tourismMapper.selectById(orders.getTourismId());
        if (tourism == null) {
            throw new CustomException("500", "商品不存在");
        }
        int updated = tourismMapper.decreaseStore(tourism.getId(), orders.getNum());
        if (updated == 0) {
            throw new CustomException("500", "库存不足");
        }

        orders.setUserId(currentUser.getId());
        orders.setTime(DateUtil.now());
        orders.setStatus(STATUS_PENDING_PAY);
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        orders.setName(tourism.getName());
        orders.setTourismImg(tourism.getImg());
        orders.setTourismPrice(tourism.getPrice());
        orders.setTotal(tourism.getPrice().multiply(BigDecimal.valueOf(orders.getNum())));
        ordersMapper.insert(orders);
    }

    public void updateById(Orders orders) {
        Orders dbOrder = ordersMapper.selectById(orders.getId());
        if (dbOrder == null) {
            throw new CustomException("404", "订单不存在");
        }
        Account currentUser = AuthUtils.currentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            AuthUtils.requireOwnerOrAdmin(dbOrder.getUserId());
            if (!STATUS_CANCELED.equals(orders.getStatus()) || !STATUS_PENDING_PAY.equals(dbOrder.getStatus())) {
                throw new CustomException("403", "无权修改订单");
            }
            Orders update = new Orders();
            update.setId(dbOrder.getId());
            update.setStatus(STATUS_CANCELED);
            ordersMapper.updateById(update);
            return;
        }
        orders.setUserId(null);
        orders.setTourismId(null);
        orders.setTourismPrice(null);
        orders.setTotal(null);
        ordersMapper.updateById(orders);
    }

    public void updateFromPayment(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public void deleteById(Integer id) {
        Orders dbOrder = ordersMapper.selectById(id);
        if (dbOrder == null) {
            return;
        }
        AuthUtils.requireOwnerOrAdmin(dbOrder.getUserId());
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    public Orders selectById(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders != null) {
            AuthUtils.requireOwnerOrAdmin(orders.getUserId());
        }
        return orders;
    }

    public List<Orders> selectAll(Orders orders) {
        AuthUtils.requireAdmin();
        return ordersMapper.selectAll(orders);
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = AuthUtils.currentUser();
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {
            orders.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    public Orders selectOwnByOrderNo(String orderNo) {
        Orders orders = ordersMapper.selectByOrderNo(orderNo);
        if (orders != null) {
            AuthUtils.requireOwnerOrAdmin(orders.getUserId());
        }
        return orders;
    }
}
