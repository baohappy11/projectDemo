package com.example.mapper;

import com.example.entity.Reservation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ReservationMapper {
    // 查询所有（支持条件查询：标题、状态、用户ID）
    List<Reservation> selectAll(Reservation reservation);

    // 新增预约
    void insert(Reservation reservation);

    // 更新预约
    void updateById(Reservation reservation);

    // 删除预约（简单SQL用注解）
    @Delete("delete from `reservation` where id = #{id}")
    void deleteById(Integer id);

    // 审核预约（更新状态和备注）
    @Update("update `reservation` set status = #{status}, remark = #{remark} where id = #{id}")
    void audit(Reservation reservation);
}
