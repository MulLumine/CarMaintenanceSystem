package com.sym.service;

import com.sym.common.Result;
import com.sym.entity.Component;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
public interface IComponentService extends IService<Component> {

    Result getPageInfo(Component component,Integer pageNum,Integer pageSize);
}
