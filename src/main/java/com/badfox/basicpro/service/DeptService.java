package com.badfox.basicpro.service;

import com.badfox.basicpro.pojo.DeptPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeptService extends IService<DeptPO> {
    DeptPO selectOne(String id);

    List<DeptPO> selectList();
    List<DeptPO> selectPageList(int curr, int size);

    List<DeptPO> selectPageList(int curr, int size, String code);

    List<DeptPO> selectNameLambda(String name);

    List<DeptPO> selectParentCodeLambda(String parentCode);

}
