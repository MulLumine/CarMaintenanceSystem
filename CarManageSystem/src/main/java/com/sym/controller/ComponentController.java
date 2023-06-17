package com.sym.controller;

import com.sym.common.Result;
import com.sym.common.ResultCodeEnum;
import com.sym.entity.Component;
import com.sym.service.impl.ComponentServiceImpl;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/components")
public class ComponentController {
    private RedisTemplate redisTemplate;
    private ComponentServiceImpl componentService;
    private CacheManager cacheManager;

    public ComponentController(RedisTemplate redisTemplate, ComponentServiceImpl componentService, CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.componentService = componentService;
        this.cacheManager = cacheManager;
    }

    //管理员新增零件信息
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public Result addComponent(@RequestBody Component component) {
        componentService.save(component);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    //管理员修改零件信息
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public Result updateComponent(@RequestBody Component component){
        componentService.updateById(component);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    //管理删除零件信息
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public Result deleteComponent(@RequestParam Integer id) {
        componentService.removeById(id);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }

    //管理员查询零件信息
    @PostMapping("/page")
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public Result selectByPage(@RequestBody Component component,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        return componentService.getPageInfo(component,pageNum,pageSize);
    }

    //查询所有零件的信息
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin','common','superAdmin')")
    public Result selectAllCom() {
        List<Component> list = componentService.list();
        return Result.success(list);
    }
}
