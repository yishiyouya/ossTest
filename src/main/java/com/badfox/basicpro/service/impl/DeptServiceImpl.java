package com.badfox.basicpro.service.impl;

import com.badfox.basicpro.mapper.DeptMapper;
import com.badfox.basicpro.pojo.DeptPO;
import com.badfox.basicpro.service.DeptService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptPO> implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public DeptPO selectOne(String id) {
        LambdaQueryWrapper<DeptPO> deptPOLambdaQueryWrapper = Wrappers.<DeptPO>lambdaQuery();
        deptPOLambdaQueryWrapper.eq(DeptPO::getId, id);
        return  this.getOne(deptPOLambdaQueryWrapper);
    }

    @Override
    public List<DeptPO> selectList() {
        QueryWrapper<DeptPO> queryWapper = new QueryWrapper<>();
        List<DeptPO> list = deptMapper.selectList(queryWapper);
        return list;
    }

    @Override
    public List<DeptPO> selectPageList(int curr, int size) {
        Page<DeptPO> pageReq = new Page<>(curr, size);
        Page<DeptPO> page =
                new LambdaQueryChainWrapper<>(deptMapper).page(pageReq);
        List<DeptPO> list = page.getRecords();
        return list;
    }

    @Override
    public List<DeptPO> selectPageList(int curr, int size, String code) {
        Page<DeptPO> pageReq = new Page<>(curr, size);
        LambdaQueryWrapper<DeptPO> deptPOLambdaQueryWrapper = Wrappers.<DeptPO>lambdaQuery();
        deptPOLambdaQueryWrapper.like(DeptPO::getCode, code);

        Page<DeptPO> page = deptMapper.selectPage(pageReq, deptPOLambdaQueryWrapper);
        List<DeptPO> list = page.getRecords();
        return list;
    }

    @Override
    public List<DeptPO> selectNameLambda(String name) {
        LambdaQueryWrapper<DeptPO> deptPOLambdaQueryWrapper = Wrappers.<DeptPO>lambdaQuery();
        deptPOLambdaQueryWrapper.like(DeptPO::getName, name);
        List<DeptPO> deptPOS = deptMapper.selectList(deptPOLambdaQueryWrapper);
        return deptPOS;
    }

    @Override
    public List<DeptPO> selectParentCodeLambda(String parentCode) {
        LambdaQueryWrapper<DeptPO> deptPOLambdaQueryWrapper = Wrappers.<DeptPO>lambdaQuery();
        deptPOLambdaQueryWrapper.eq(DeptPO::getParentCode, parentCode);
        List<DeptPO> deptPOS = deptMapper.selectList(deptPOLambdaQueryWrapper);
        return deptPOS;
    }

}
