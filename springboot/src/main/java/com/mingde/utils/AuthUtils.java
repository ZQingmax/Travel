package com.mingde.utils;

import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.exception.CustomException;

public class AuthUtils {

    private AuthUtils() {
    }

    public static Account currentUser() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            throw new CustomException("401", "请先登录");
        }
        return currentUser;
    }

    public static boolean isAdmin(Account account) {
        return account != null && RoleEnum.ADMIN.name().equals(account.getRole());
    }

    public static boolean isUser(Account account) {
        return account != null && RoleEnum.USER.name().equals(account.getRole());
    }

    public static void requireAdmin() {
        if (!isAdmin(currentUser())) {
            throw new CustomException("403", "无权访问");
        }
    }

    public static void requireOwnerOrAdmin(Integer ownerId) {
        Account currentUser = currentUser();
        if (!isAdmin(currentUser) && (ownerId == null || !ownerId.equals(currentUser.getId()))) {
            throw new CustomException("403", "无权操作他人数据");
        }
    }
}
