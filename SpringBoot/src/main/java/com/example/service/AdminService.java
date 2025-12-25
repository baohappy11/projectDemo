package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public void add(Admin admin) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以添加管理员");
        }
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("账号已存在");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("123");
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以删除管理员");
        }
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以批量删除管理员");
        }
        for (Admin admin : list) {
            adminMapper.deleteById(admin.getId());
        }
    }

     public List<Admin> selectAll(Admin admin){
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以查看管理员信息");
        }
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以查看管理员信息");
        }
        PageHelper.startPage(pageNum,pageSize);//开启分页查询
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public void update(Admin admin) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以修改管理员信息");
        }
        adminMapper.updateById(admin);
    }

    public Admin login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (dbAdmin == null) {
            throw new CustomerException("账号不存在");
        }
        if(!dbAdmin.getPassword().equals(account.getPassword())){
            throw new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "ADMIN", dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public Admin selectById(String userid) {
        return adminMapper.selectById(userid);
    }

    public void updatePassword(Account account) {
        //判断新密码和确认密码是否一致
        if (!account.getNewPassword().equals(account.getNewConfirmPassword())) {
            throw new CustomerException("500", "您两次输入的密码不一致");
        }
        //校验原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if (!account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500", "原密码输入错误");
        }
        //更新密码
        Admin admin = adminMapper.selectById(currentUser.getId().toString());
        admin.setPassword(account.getNewPassword());
        adminMapper.updateById(admin);
    }
}
