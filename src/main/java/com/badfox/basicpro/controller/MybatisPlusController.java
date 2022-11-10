package com.badfox.basicpro.controller;

import com.badfox.basicpro.comm.ReqInfo;
import com.badfox.basicpro.pojo.DeptPO;
import com.badfox.basicpro.comm.ResultVo;
import com.badfox.basicpro.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "MybatisPlus接口")
@Validated
@RestController
public class MybatisPlusController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/getAllData")
    @ApiOperation(value="allData")
    public String getAllDept() {
        List<DeptPO> list = deptService.selectList();
        list.forEach(System.out::println);
        return list.toString();
    }

    @GetMapping("/getData")
    @ApiOperation(value="分页查询", notes = "机构信息-分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="curr", value="当前页数"),
            @ApiImplicitParam(name="size", value="记录数")
    })
    public List<DeptPO> getDept(@RequestParam("curr") int curr, @RequestParam("size") int size) {
        List<DeptPO> list = deptService.selectPageList(curr, size);
        list.forEach(System.out::println);
        return list;
    }

    @GetMapping("/getOneDept")
    @ApiOperation(value="单条记录查询", notes = "机构信息-单条记录查询")
    @ApiImplicitParam(name="id", value="id")
    public DeptPO getOneDept(@RequestParam("id") String id) {
        DeptPO deptPO = deptService.selectOne(id);
        return deptPO;
    }

    @GetMapping("/getOneDeptVo")
    @ApiOperation(value="单条记录查询", notes = "机构信息-单条记录查询")
    @ApiImplicitParam(name="id", value="id")
    public ResultVo getOneDeptVo(@RequestParam("id") String id) {
        DeptPO deptPO = deptService.selectOne(id);
        return new ResultVo(deptPO);
    }

    @PostMapping("/getDeptVos")
    @ApiOperation(value="记录查询", notes = "机构信息-记录查询by ReqInfo")
    @ApiImplicitParam(name="ReqInfo", value="ReqInfo")
    public List<DeptPO> getDeptVos(@Valid @RequestBody ReqInfo req) {
        List<DeptPO> list = deptService.selectPageList(req.getCurr(), req.getSize(), req.getCode());
        return list;
    }
}
