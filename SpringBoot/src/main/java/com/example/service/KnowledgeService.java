package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Knowledge;
import com.example.exception.CustomerException;
import com.example.mapper.KnowledgeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService {

    @Resource
    KnowledgeMapper knowledgeMapper;

    public void add(Knowledge knowledge) {
        Account currentUser = TokenUtils.getCurrentUser();//获取当前用户
        knowledge.setUserId(currentUser.getId());//将当前用户id给knowledge的userId字段
        knowledge.setTime(DateUtil.now());//获取当前时间为发布公告时间
        knowledgeMapper.insert(knowledge);
    }

    public void deleteById(Integer id) {
        knowledgeMapper.deleteById(id);
    }

    public void update(Knowledge knowledge) {
        knowledgeMapper.updateById(knowledge);
    }

    public List<Knowledge> selectAll(Knowledge knowledge){
        return  knowledgeMapper.selectAll(knowledge);
    }

    public PageInfo<Knowledge> selectPage(Integer pageNum, Integer pageSize, Knowledge knowledge) {
        PageHelper.startPage(pageNum,pageSize);
        List<Knowledge> list = knowledgeMapper.selectAll(knowledge);
        return PageInfo.of(list);
    }
}