package com.badfox.osstest.controller;

import com.badfox.osstest.pojo.DeptPO;
import com.badfox.osstest.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MysqlController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/getAllData")
    public String getAllDept() {
        List<DeptPO> list = deptService.selectList();
        list.forEach(System.out::println);
        return list.toString();
    }

    @GetMapping("/getData")
    public String getDept(@RequestParam("curr") int curr, @RequestParam("size") int size) {
        List<DeptPO> list = deptService.selectPageList(curr, size);
        list.forEach(System.out::println);
        return list.toString();
    }

}
