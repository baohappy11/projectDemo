package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.example.entity.Account;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public void add(User user) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以添加用户");
        }
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomerException("账号已存在");
        }
        if(StrUtil.isBlank(user.getPassword())){
            user.setPassword("123");
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以删除用户");
        }
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以批量删除用户");
        }
        for (User user : list) {
            userMapper.deleteById(user.getId());
        }
    }

     public List<User> selectAll(User user){
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以查看用户信息");
        }
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以查看用户信息");
        }
        PageHelper.startPage(pageNum,pageSize);//开启分页查询
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public void update(User user) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以修改用户信息");
        }
        userMapper.updateById(user);
    }

    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }
        if(!dbUser.getPassword().equals(account.getPassword())){
            throw new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "USER", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public void register(User user) {
        this.add(user);
    }

    public User selectById(String userid) {
        return userMapper.selectById(userid);
    }

    public void updatePassword(Account account) {
        //判断新密码和确认密码是否一致
        if(!account.getNewPassword().equals(account.getNewConfirmPassword())) {
            throw new CustomerException("500" , "您两次输入的密码不一致");
        }
        //校验原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if(!account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500" , "原密码输入错误");
        }
        //更新密码
        User user = userMapper.selectById(currentUser.getId().toString());
        user.setPassword(account.getNewPassword());
        userMapper.updateById(user);
    }
}
