package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.util.ObjectUtil;
import com.mingde.common.Constants;
import com.mingde.common.enums.ResultCodeEnum;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.User;
import com.mingde.exception.CustomException;
import com.mingde.mapper.UserMapper;
import com.mingde.utils.AuthUtils;
import com.mingde.utils.PasswordUtils;
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        user.setPassword(PasswordUtils.encode(user.getPassword()));
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public void updateById(User user) {
        Account currentUser = AuthUtils.currentUser();
        if (!AuthUtils.isAdmin(currentUser)) {
            user.setId(currentUser.getId());
        }
        if (ObjectUtil.isNotEmpty(user.getPassword()) && !PasswordUtils.isEncoded(user.getPassword())) {
            user.setPassword(PasswordUtils.encode(user.getPassword()));
        }
        user.setRole(null);
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll(User user) {
        AuthUtils.requireAdmin();
        return userMapper.selectAll(user);
    }

    public PageResult<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        Page<User> page = PageUtils.page(pageNum, pageSize);
        IPage<User> result = userMapper.selectPage(page, user);
        result.getRecords().forEach(item -> item.setPassword(null));
        return PageUtils.toResult(result);
    }

    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!PasswordUtils.matches(account.getPassword(), dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        if (!PasswordUtils.isEncoded(dbUser.getPassword())) {
            dbUser.setPassword(PasswordUtils.encode(account.getPassword()));
            userMapper.updateById(dbUser);
        }
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());
        dbUser.setToken(token);
        dbUser.setPassword(null);
        return dbUser;
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        AuthUtils.requireOwnerOrAdmin(dbUser.getId());
        if (!PasswordUtils.matches(account.getPassword(), dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(PasswordUtils.encode(account.getNewPassword()));
        userMapper.updateById(dbUser);
    }
}
