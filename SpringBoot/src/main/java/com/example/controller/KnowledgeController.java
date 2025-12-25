package com.example.controller;

import com.example.common.Result;
import com.example.entity.Knowledge;
import com.example.service.KnowledgeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Resource
    KnowledgeService knowledgeService;

    @PostMapping("/add")
    public Result add(@RequestBody Knowledge knowledge) {
        knowledgeService.add(knowledge);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete (@PathVariable Integer id) {
        knowledgeService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update (@RequestBody Knowledge knowledge) {
        knowledgeService.update(knowledge);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Knowledge knowledge){
        List<Knowledge> knowledgeList = knowledgeService.selectAll(knowledge);
        return Result.success(knowledgeList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1" ) Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Knowledge knowledge){
        PageInfo<Knowledge> knowledgePageInfo = knowledgeService.selectPage(pageNum, pageSize, knowledge);
        return Result.success(knowledgePageInfo);
    }
}
