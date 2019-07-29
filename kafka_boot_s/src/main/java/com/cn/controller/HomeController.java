package com.cn.controller;


import com.cn.dto.Dto;
import com.cn.dto.DtoUtil;
import com.cn.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static List<User> users = new ArrayList<>();

    static{
        users.add(new User(1,"张三"));
        users.add(new User(2,"李四"));
        users.add(new User(3,"王五"));
        users.add(new User(4,"赵柳"));
    }
    @GetMapping(value = "/home")
    public Dto home(){
        for (int i = 0;i<10;i++){
            kafkaTemplate.send("dmservice","dm","spring代码中生成的日志----"+i);
        }
        return DtoUtil.returnSuccess("ok");
    }
    @ApiOperation(value = "用户查询方法",notes = "查询用户列表")
    @RequestMapping(value = "/sel",method = RequestMethod.GET)
    public Dto query(){
        return DtoUtil.returnSuccess("ok",users);
    }

    @ApiOperation(value = "根据编号查询用户方法",notes = "根据编号查询用户信息")
    @ApiImplicitParam(value = "用户编号",required = true,name = "id",dataType = "int",paramType = "path")
    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public Dto get(@PathVariable("id") int id){
        for (User u:users){
            if(u.getId()==id){
                return DtoUtil.returnSuccess("ok",u);
            }
        }
        return DtoUtil.returnSuccess("input");
    }

    @ApiOperation(value = "添加用户方法",notes = "添加用户信息")
    @ApiImplicitParam(value = "用户对象",required = true,name = "user",dataType = "User")
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public  Dto insert(@RequestBody User user){
        users.add(user);
        return DtoUtil.returnSuccess("ok");
    }

    @ApiOperation(value = "修改用户方法",notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户编号",required = true,name = "id",dataType = "int",paramType = "path"),
            @ApiImplicitParam(value = "用户对象",required = true,name = "user",dataType = "User")
    })
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public Dto update(@PathVariable("id") int id , @RequestBody User user){
        for (int i = 0;i<users.size();i++){
            User u = users.get(i);
            if(u.getId()==id){
                users.set(i,user);
                return DtoUtil.returnSuccess("ok");
            }
        }
        return DtoUtil.returnSuccess("input");
    }

    @ApiOperation(value = "删除用户方法",notes = "删除用户信息")
    @ApiImplicitParam(value = "用户编号",required = true,name = "id",dataType = "int",paramType = "path")
    @RequestMapping(value = "/remove/{id}",method = RequestMethod.DELETE)
    public Dto remove(@PathVariable("id") int id){
        for (User u:users){
            if(u.getId()==id){
                users.remove(u);
                return DtoUtil.returnSuccess("ok");
            }
        }
        return DtoUtil.returnSuccess("input");
    }
}
