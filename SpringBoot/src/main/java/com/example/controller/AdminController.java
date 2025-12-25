package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController //注册这个类为controller的Bean，返回的数据都会是JSON格式
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) { //@RequestBody接收前端传来的json参数
        adminService.add(admin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete (@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch (@RequestBody List<Admin> list) {
        adminService.deleteBatch(list);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update (@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Admin admin){
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1" ) Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin){
        PageInfo<Admin> adminPageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(adminPageInfo);//返回分页对象
    }

    @GetMapping("/export")
    public void exportData(HttpServletResponse response,Admin admin) throws Exception {
        String ids = admin.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            admin.setIdsArr(idsArr);
        }
        //创建查询所有数据的列表
        List<Admin> list = adminService.selectAll(admin);
        // 通过工具类创建writer对象，创建xlsx格式的
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        //写数据到writer
        writer.write(list);
        //写出到客户端下载（写出到Servlet）,response为HttpServletResponse对象,设置输出流的头信息，不能中文，重新编码
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition","attachment;filename=" + fileName +".xlsx");
        //输出文件
        ServletOutputStream os =response.getOutputStream();
        writer.flush(os, true);
        writer.close();//关闭writer，释放内存
        os.close();//关闭输出Servlet流
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("账号" , "username");
        reader.addHeaderAlias("姓名" , "name");
        reader.addHeaderAlias("电话" , "phone");
        reader.addHeaderAlias("邮箱" , "email");
        List<Admin> list = reader.readAll(Admin.class);
        for (Admin admin : list) {
            adminService.add(admin);
        }
        return Result.success();
    }
}
