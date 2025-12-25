package com.example.controller;

import com.example.common.Result;
import com.example.entity.Reservation;
import com.example.service.ReservationService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Resource
    ReservationService reservationService;

    /**
     * 新增预约（用户提交预约）
     * POST /reservation/add
     * 请求体：{"title":"标题", "content":"内容", "reservationTime":"2025-12-28 14:00:00"}
     */
    @PostMapping("/add")
    public Result add(@RequestBody Reservation reservation) {
        reservationService.add(reservation);
        return Result.success();
    }

    /**
     * 删除预约
     * DELETE /reservation/delete/1
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        reservationService.deleteById(id);
        return Result.success();
    }

    /**
     * 更新预约
     * PUT /reservation/update
     * 请求体：{"id":1, "title":"新标题", ...}
     */
    @PutMapping("/update")
    public Result update(@RequestBody Reservation reservation) {
        reservationService.update(reservation);
        return Result.success();
    }

    /**
     * 查询所有（支持条件查询）
     * GET /reservation/selectAll?title=Java&status=待审核
     */
    @GetMapping("/selectAll")
    public Result selectAll(Reservation reservation) {
        List<Reservation> reservationList = reservationService.selectAll(reservation);
        return Result.success(reservationList);
    }

    /**
     * 分页查询
     * GET /reservation/selectPage?pageNum=1&pageSize=10&title=Java&status=待审核
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Reservation reservation) {
        PageInfo<Reservation> reservationPageInfo = reservationService.selectPage(pageNum, pageSize, reservation);
        return Result.success(reservationPageInfo);
    }

    /**
     * 审核预约（管理员专用）
     * PUT /reservation/audit
     * 请求体：{"id":1, "status":"已通过", "remark":"主题很好"}
     */
    @PutMapping("/audit")
    public Result audit(@RequestBody Reservation reservation) {
        reservationService.audit(reservation.getId(), reservation.getStatus(), reservation.getRemark());
        return Result.success();
    }
}
