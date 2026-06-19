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
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private TourismMapper tourismMapper;


    @Transactional
    public void add(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        orders.setTime(DateUtil.now());
        orders.setStatus("待支付");
        orders.setOrderNo(IdUtil.getSnowflakeNextIdStr()); // 雪花算法生成订单号
        //直接下单之前 判断库存
        Integer tourismId = orders.getTourismId();
        Tourism tourism = tourismMapper.selectById(tourismId);
        if(tourism == null){
            throw new CustomException("500","商品不存在");
        }
        if(tourism.getStore() < orders.getNum()){
            throw new CustomException("500","库存不足");
        }
        ordersMapper.insert(orders);
        tourism.setStore(tourism.getStore() - orders.getNum());
        tourismMapper.updateById(tourism);
    }

    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(currentUser.getRole().equals(RoleEnum.USER.name())){
            orders.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }
}
