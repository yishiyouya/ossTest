package com.badfox.osstest.util.generator.system.service.impl;

import com.badfox.osstest.util.generator.system.entity.Productinfo;
import com.badfox.osstest.util.generator.system.mapper.ProductinfoMapper;
import com.badfox.osstest.util.generator.system.service.IProductinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author badfox
 * @since 2022-11-08
 */
@Service
public class ProductinfoServiceImpl extends ServiceImpl<ProductinfoMapper, Productinfo> implements IProductinfoService {

}
