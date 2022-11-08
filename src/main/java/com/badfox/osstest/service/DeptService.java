package com.badfox.osstest.service;

import com.badfox.osstest.pojo.DeptPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeptService extends IService<DeptPO> {
    List<DeptPO> selectList();
    List<DeptPO> selectPageList(int curr, int size);

    List<DeptPO> selectNameLambda(String name);

    List<DeptPO> selectParentCodeLambda(String parentCode);

}
