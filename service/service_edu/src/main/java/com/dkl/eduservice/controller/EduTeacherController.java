package com.dkl.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkl.commomutils.R;
import com.dkl.eduservice.entity.EduTeacher;
import com.dkl.eduservice.entity.vo.TeacherQuery;
import com.dkl.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author chddald
 * @since 2021-03-29
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    //访问地址：http://localhost:8001/eduservice/teacher/findAll
    //把Service注入
    @Autowired
    private EduTeacherService teacherService;

    //1查询讲师表里的所有数据
    //rest风格  查询get  添加post  修改put 删除delete
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    //2逻辑删除讲师方法
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //3分页查询讲师的方法
    //current 当前页
    //limit 每页记录数
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                             @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法的时候，底层封装，把分页的所有数据封装到pageTeacher对象中去
        teacherService.page(pageTeacher, null);
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        long total = pageTeacher.getTotal();//总记录数

        /*Map map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);*/

        return  R.ok().data("total", total).data("rows", records);
    }

    //4条件查询带分页的方法
    @ApiOperation(value = "按条件分页查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                                  @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        int i = 10/0;

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)){
            //构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher,wrapper);
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        long total = pageTeacher.getTotal();//总记录数
        return  R.ok().data("total", total).data("rows", records);
    }

    //5添加讲师的方法
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@ApiParam(name = "teacher", value = "讲师对象", required = true)
                        @RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //6根据讲师ID查询回显
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable Long id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //7讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = teacherService.updateById(eduTeacher);
        if(b){
            return R.ok();
        }else {
            return R.error();
        }
    }
}

