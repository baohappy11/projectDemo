package com.example.mapper;

import com.example.entity.Knowledge;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface KnowledgeMapper {
    List<Knowledge> selectAll(Knowledge knowledge);

    void insert(Knowledge knowledge);

    void updateById(Knowledge knowledge);

    @Delete("delete from `knowledge` where id = #{id}")
    void deleteById(Integer id);
}