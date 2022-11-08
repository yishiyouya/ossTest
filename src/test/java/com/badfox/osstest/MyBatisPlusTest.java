package com.badfox.osstest;

import com.badfox.osstest.pojo.DeptPO;
import com.badfox.osstest.service.DeptService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class MyBatisPlusTest {

    @Autowired
    private DeptService deptService;

    @Test
    public void testDept() {
        List<DeptPO> list = deptService.selectList();
        list.forEach(System.out::println);
    }

    @Test
    public void testDeptLambda() {
        List<DeptPO> list = deptService.selectNameLambda("公司");
        list.forEach(System.out::println);
    }

    @Test
    public void testSaveOrUpdateBatch() {
        Map<String, String> oldMap = getOldMap("C105001");
        System.out.println("oldMap: " + oldMap);
        List<DeptPO> newList = getNewList(oldMap);
        System.out.println("handled: " + oldMap);
        List<String> delIds = new ArrayList<>();
        oldMap.forEach((k, v) -> {
            if (StringUtils.hasText(v)) {
                System.out.println("remove: " + v);
                delIds.add(v);
            }});
        boolean suFlag = deptService.saveOrUpdateBatch(newList);
        deptService.removeBatchByIds(delIds);
        System.out.println(suFlag);
    }

    @Test
    public List<DeptPO> getNewList(Map<String, String> oldMap) {
        DeptPO deptPO1 = new DeptPO();
        deptPO1.setCode("C105001001");
        deptPO1.setId(oldMap.get(deptPO1.getCode()));
        deptPO1.setName("信息部-11");
        deptPO1.setParentCode("C105001");

        DeptPO deptPO2 = new DeptPO();
        deptPO2.setCode("C105001002");
        deptPO2.setId(oldMap.get(deptPO2.getCode()));
        deptPO2.setName("信息部-22");
        deptPO2.setParentCode("C105001");

        DeptPO deptPO3 = new DeptPO();
        deptPO3.setCode("C105001003");
        deptPO3.setId(oldMap.get(deptPO3.getCode()));
        deptPO3.setName("信息部-33");
        deptPO3.setParentCode("C105001");
        List<DeptPO> list = Arrays.asList(deptPO1, deptPO2, deptPO3);
        /*List<DeptPO> list = Arrays.asList(deptPO1, deptPO2);*/
        //存在的置空
        list.forEach(n -> oldMap.put(n.getCode(), ""));
        return list;
    }

    public Map<String, String> getOldMap(String parentCode) {
        Map<String, String> oldMap = new HashMap<>();
        List<DeptPO> list = deptService.selectParentCodeLambda(parentCode);
        list.forEach(n -> oldMap.put(n.getCode(), n.getId()));
        return oldMap;
    }


    @Test
    public void testUpdAllDept() {
        Wrapper<DeptPO> wrapper = new QueryWrapper<>();
        DeptPO deptPO = new DeptPO();
        deptPO.setUpdTime(LocalDateTime.now());
        boolean removed = deptService.update(deptPO, wrapper);
        System.out.println(removed);
    }

}
