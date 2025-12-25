 package com.example.controller;

  import java.io.IOException;
  import java.util.HashMap;
  import java.util.Map;

  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;
  import org.springframework.web.multipart.MultipartFile;

  import com.example.common.Result;
  import com.example.exception.CustomerException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

  @RestController
  @RequestMapping("/files")
  public class FileController {
      /**
       * 文件下载
       */
      @GetMapping("/download/{fileName}")
      public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
          //构建文件路径并检查文件是否存在
          String filePath = System.getProperty("user.dir") + "/files/download/"; //当前项目根路径：
          String realPath = filePath + fileName;
          boolean exist = FileUtil.exist(realPath);
          if (!exist) {
              throw new CustomerException("文件不存在");
          }
          //读取文件内容（字节流）
          byte[] bytes = FileUtil.readBytes(realPath);
          //设置响应头并返回文件
          ServletOutputStream os = response.getOutputStream();
          //输出流对象把文件写出到客户端
          os.write(bytes);
          os.flush();
          os.close();
      }

      /**
       * 文件上传
       */
      @PostMapping("/upload")
      public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
          //构建文件存储路径,路径不存在则创建路径
          String filePath = System.getProperty("user.dir") + "/files/";
          if(!FileUtil.isDirectory(filePath)){
              FileUtil.mkdir(filePath);
          }
          byte[] bytes = file.getBytes();//读取文件字节流
          String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();//生成文件名：时间戳_文件名
          //内容写入文件并返回存储地址
          FileUtil.writeBytes(bytes, filePath + fileName);
          String url = "http://localhost:9090/files/download/" + fileName;
          return Result.success(url);
      }

      @PostMapping("/wang/upload")
      public Map<String, Object> wangEditUpload(MultipartFile file) throws IOException {
          String flag = System.currentTimeMillis() + "";
          String filename = file.getOriginalFilename();
          try{
              String filePath = System.getProperty("user.dir") + "/files/";
              //文件存储形式：时间戳-文件名
              FileUtil.writeBytes(file.getBytes() , filePath + flag + "-" + filename);
              Thread.sleep(1L);
          } catch (Exception e) {
              // 文件上传失败
          }
          String http = "http://localhost:9090/files/download/";
          HashMap<String, Object> resMap = new HashMap<>();
          //wangEditor上传图片成功后，需要返回的参数
          resMap.put("errno", 0);
          resMap.put("data" , CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + filename)));
          return resMap;
      }
  }