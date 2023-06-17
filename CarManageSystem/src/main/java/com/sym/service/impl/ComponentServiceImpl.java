package com.sym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.Component;
import com.sym.mapper.ComponentMapper;
import com.sym.service.IComponentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements IComponentService {

    @Override
    public Result getPageInfo(Component component,Integer pageNum,Integer pageSize) {
        IPage<Component> basePage = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Component> queryWrapper = new LambdaQueryWrapper<>();
        //le小于等于，ge大于等于
        queryWrapper.le(component.getNumber()!=null,Component::getNumber,component.getNumber())

                .le(component.getMaxOutprice()!=null,Component::getOutprice,component.getMaxOutprice())
                .ge(component.getOutprice()!=null,Component::getOutprice,component.getOutprice())

                .ge(component.getInprice()!=null,Component::getInprice,component.getInprice())
                .le(component.getMaxInprice()!=null,Component::getInprice,component.getMaxInprice())
                .like(component.getDescription()!=null&&component.getDescription()!="",Component::getDescription,component.getDescription());
        IPage<Component> iPage = this.page(basePage,queryWrapper);
        return Result.success(iPage, ResultCodeEnum.SELECT_SUCCESS);
    }
}
