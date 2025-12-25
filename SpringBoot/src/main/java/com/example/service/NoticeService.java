package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if("USER".equals(currentUser.getRole())){
            throw new CustomerException("500" , "您的角色暂无权限");
        }
        notice.setTime(DateUtil.now());//获取当前时间为发布公告时间
        noticeMapper.insert(notice);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以删除公告");
        }
        noticeMapper.deleteById(id);
    }

    public void update(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以修改公告");
        }
        noticeMapper.updateById(notice);
    }

    public List<Notice> selectAll(Notice notice){
        return  noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        PageHelper.startPage(pageNum,pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }
}