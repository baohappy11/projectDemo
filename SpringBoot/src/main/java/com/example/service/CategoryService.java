package com.example.service;

import com.example.entity.Account;
import com.example.entity.Category;
import com.example.exception.CustomerException;
import com.example.mapper.CategoryMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    public void add(Category category) {
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            throw new CustomerException("500" , "您的角色暂无权限该操作");
        }
        categoryMapper.insert(category);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以删除分类");
        }
        categoryMapper.deleteById(id);
    }

    public void update(Category category) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以修改分类");
        }
        categoryMapper.updateById(category);
    }

    public List<Category> selectAll(Category category){
        return  categoryMapper.selectAll(category);
    }

    public PageInfo<Category> selectPage(Integer pageNum, Integer pageSize, Category category) {
        PageHelper.startPage(pageNum,pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }

}
