package com.sym.mapper;

import com.sym.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
