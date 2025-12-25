package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Reservation;
import com.example.exception.CustomerException;
import com.example.mapper.ReservationMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Resource
    ReservationMapper reservationMapper;

    /**
     * 用户提交预约
     * 自动设置：当前用户ID、创建时间、状态="待审核"
     */
    public void add(Reservation reservation) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 权限校验：只有USER角色可以提交预约
        if (!"USER".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有用户可以提交预约");
        }

        // 自动设置字段
        reservation.setUserId(currentUser.getId());      // 设置当前用户ID
        reservation.setCreateTime(DateUtil.now());       // 设置创建时间
        reservation.setStatus("待审核");                  // 默认状态为"待审核"

        reservationMapper.insert(reservation);
    }

    /**
     * 删除预约
     * 权限控制：用户只能删除自己的预约，管理员可以删除所有
     */
    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();

        // 如果是用户，需要校验是否是自己的预约
        if ("USER".equals(currentUser.getRole())) {
            // 先查询预约信息
            Reservation query = new Reservation();
            query.setId(id);
            List<Reservation> list = reservationMapper.selectAll(query);
            if (list.isEmpty()) {
                throw new CustomerException("404", "预约不存在");
            }
            Reservation reservation = list.get(0);
            // 校验是否是当前用户的预约
            if (!currentUser.getId().equals(reservation.getUserId())) {
                throw new CustomerException("403", "您只能删除自己的预约");
            }
        }

        reservationMapper.deleteById(id);
    }

    /**
     * 更新预约
     * 权限控制：用户只能更新自己的预约，管理员可以更新所有
     */
    public void update(Reservation reservation) {
        Account currentUser = TokenUtils.getCurrentUser();

        // 如果是用户，需要校验是否是自己的预约
        if ("USER".equals(currentUser.getRole())) {
            Reservation query = new Reservation();
            query.setId(reservation.getId());
            List<Reservation> list = reservationMapper.selectAll(query);
            if (list.isEmpty()) {
                throw new CustomerException("404", "预约不存在");
            }
            Reservation oldReservation = list.get(0);
            if (!currentUser.getId().equals(oldReservation.getUserId())) {
                throw new CustomerException("403", "您只能修改自己的预约");
            }
        }

        reservationMapper.updateById(reservation);
    }

    /**
     * 查询所有（支持条件查询）
     */
    public List<Reservation> selectAll(Reservation reservation) {
        Account currentUser = TokenUtils.getCurrentUser();

        // 如果是用户，只能查看自己的预约
        if ("USER".equals(currentUser.getRole())) {
            reservation.setUserId(currentUser.getId());
        }

        return reservationMapper.selectAll(reservation);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reservation> selectPage(Integer pageNum, Integer pageSize, Reservation reservation) {
        Account currentUser = TokenUtils.getCurrentUser();

        // 如果是用户，只能查看自己的预约
        if ("USER".equals(currentUser.getRole())) {
            reservation.setUserId(currentUser.getId());
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Reservation> list = reservationMapper.selectAll(reservation);
        return PageInfo.of(list);
    }

    /**
     * 审核预约（管理员专用）
     * @param id 预约ID
     * @param status 审核结果：已通过、已拒绝
     * @param remark 审核备注
     */
    public void audit(Integer id, String status, String remark) {
        Account currentUser = TokenUtils.getCurrentUser();

        // 权限校验：只有管理员可以审核
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new CustomerException("403", "只有管理员可以审核预约");
        }

        // 校验状态参数
        if (!"已通过".equals(status) && !"已拒绝".equals(status)) {
            throw new CustomerException("400", "审核状态只能是：已通过 或 已拒绝");
        }

        // 执行审核
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setStatus(status);
        reservation.setRemark(remark);
        reservationMapper.audit(reservation);
    }
}
