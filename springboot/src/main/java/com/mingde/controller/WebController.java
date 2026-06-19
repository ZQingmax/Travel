package com.mingde.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.mingde.common.Result;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Orders;
import com.mingde.entity.Travels;
import com.mingde.entity.User;
import com.mingde.mapper.ArticleMapper;
import com.mingde.mapper.OrdersMapper;
import com.mingde.mapper.TourismMapper;
import com.mingde.mapper.TravelsMapper;
import com.mingde.service.AdminService;
import com.mingde.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;  // 新增的导入
@RestController
public class WebController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private TravelsMapper travelsMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TourismMapper tourismMapper;

    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello () {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if(RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        }else if(RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/count")
    public Result count() {
        long travelsCount = travelsMapper.selectAll(null).size();
        long articleCount = articleMapper.selectAll(null).size();
        long tourismCount = tourismMapper.selectAll(null).size();
        long ordersCount = ordersMapper.selectAll(null).size();
        Dict dict = Dict.create().set("travelsCount", travelsCount)
                .set("articleCount", articleCount)
                .set("tourismCount", tourismCount)
                .set("ordersCount", ordersCount);
        return Result.success(dict);
    }

    @GetMapping("/ordersData")
    public Result selectOrdersData() {
        List<Dict> dictList = new ArrayList<>();
        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -31);
        DateTime end = DateUtil.offsetDay(date, 0);
        // 最近一个月的字符串日期集合 ["2025-02-25", "2025-03-26", ...]
        List<String> dateList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate)
                .sorted(Comparator.naturalOrder()).toList();
        List<Orders> ordersList = ordersMapper.selectAll(null);
        for (String day : dateList) {
            // 计算出每一天的销售数量
            Integer ordersNum = ordersList.stream().filter(orders -> orders.getTime().contains(day))  // 日期是订单的下单日期
                    .map(Orders::getNum).reduce(Integer::sum).orElse(0);
            Dict dict = Dict.create().set("name", day).set("value", ordersNum);
            dictList.add(dict);
        }
        System.out.println(dictList);
        return Result.success(dictList);
    }

    @GetMapping("/travelsData")
    public Result selectTravelsData() {
        List<Dict> dictList = new ArrayList<>();
        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -31);
        DateTime end = DateUtil.offsetDay(date, -1);
        // 最近一个月的字符串日期集合 ["2025-02-25", "2025-03-26", ...]
        List<String> dateList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR)
                .stream()
                .map(DateUtil::formatDate)
                .sorted(Comparator.naturalOrder()).toList();
        List<Travels> travelsList = travelsMapper.selectAll(null);
        for (String day : dateList) {
            // 计算出每一天的销售额
            long count = travelsList.stream().filter(travels -> travels.getTime().contains(day)).count();
            Dict dict = Dict.create().set("name", day).set("value", count);
            dictList.add(dict);
        }
        System.out.println(dictList);
        return Result.success(dictList);
    }
}
