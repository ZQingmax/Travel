package com.mingde.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mingde.common.Constants;
import com.mingde.common.enums.ResultCodeEnum;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.exception.CustomException;
import com.mingde.service.AdminService;
import com.mingde.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }

        Account account = getAccount(token);
        verifyToken(token, account.getPassword());
        request.setAttribute("currentAccount", account);
        checkRolePermission(request, account);
        return true;
    }

    private Account getAccount(String token) {
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String[] userRole = audience.split("-");
            if (userRole.length != 2) {
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }
            Integer userId = Integer.valueOf(userRole[0]);
            String role = userRole[1];
            Account account = null;
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(userId);
            } else if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(userId);
            }
            if (account == null) {
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }
            return account;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
    }

    private void verifyToken(String token, String password) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(password)).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
    }

    private void checkRolePermission(HttpServletRequest request, Account account) {
        if (!requiresAdmin(request)) {
            return;
        }
        if (!RoleEnum.ADMIN.name().equals(account.getRole())) {
            throw new CustomException("403", "无权访问");
        }
    }

    private boolean requiresAdmin(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (path.startsWith("/admin") || path.startsWith("/count") || path.startsWith("/ordersData") || path.startsWith("/travelsData")) {
            return true;
        }
        if (path.startsWith("/alipay/refund")) {
            return true;
        }
        if (path.startsWith("/user/select") || path.startsWith("/user/add") || path.startsWith("/user/delete")) {
            return true;
        }
        if (path.startsWith("/notice/") || path.startsWith("/article/") || path.startsWith("/routes/") || path.startsWith("/tourism/")) {
            return !"GET".equalsIgnoreCase(method);
        }
        if (path.startsWith("/orders/selectAll") || path.startsWith("/feedback/selectAll") || path.startsWith("/comment/selectPage")) {
            return true;
        }
        return false;
    }
}
