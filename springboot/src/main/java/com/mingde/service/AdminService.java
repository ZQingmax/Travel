package com.mingde.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.Constants;
import com.mingde.common.enums.ResultCodeEnum;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Admin;
import com.mingde.exception.CustomException;
import com.mingde.mapper.AdminMapper;
import com.mingde.utils.AuthUtils;
import com.mingde.utils.PasswordUtils;
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        AuthUtils.requireAdmin();
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (ObjectUtil.isNotNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {
            admin.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        admin.setPassword(PasswordUtils.encode(admin.getPassword()));
        if (ObjectUtil.isEmpty(admin.getName())) {
            admin.setName(admin.getUsername());
        }
        admin.setRole(RoleEnum.ADMIN.name());
        adminMapper.insert(admin);
    }

    public void updateById(Admin admin) {
        Account currentUser = AuthUtils.currentUser();
        if (!AuthUtils.isAdmin(currentUser) || !currentUser.getId().equals(admin.getId())) {
            AuthUtils.requireAdmin();
        }
        if (ObjectUtil.isNotEmpty(admin.getPassword()) && !PasswordUtils.isEncoded(admin.getPassword())) {
            admin.setPassword(PasswordUtils.encode(admin.getPassword()));
        }
        admin.setRole(null);
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            adminMapper.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll(Admin admin) {
        AuthUtils.requireAdmin();
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Admin> list = adminMapper.selectAll(admin);
        list.forEach(item -> item.setPassword(null));
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!PasswordUtils.matches(account.getPassword(), dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        if (!PasswordUtils.isEncoded(dbAdmin.getPassword())) {
            dbAdmin.setPassword(PasswordUtils.encode(account.getPassword()));
            adminMapper.updateById(dbAdmin);
        }
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + dbAdmin.getRole(), dbAdmin.getPassword());
        dbAdmin.setToken(token);
        dbAdmin.setPassword(null);
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        AuthUtils.requireOwnerOrAdmin(dbAdmin.getId());
        if (!PasswordUtils.matches(account.getPassword(), dbAdmin.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbAdmin.setPassword(PasswordUtils.encode(account.getNewPassword()));
        adminMapper.updateById(dbAdmin);
    }
}
