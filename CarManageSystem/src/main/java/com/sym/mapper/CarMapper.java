package com.sym.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.entity.Car;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sym.vo.CarAndUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {
    IPage<CarAndUserVo> selectPageVo(@Param("vo") CarAndUserVo carAndUserVo,@Param("page") IPage page);

    IPage<CarAndUserVo> selectUserPageVo(@Param("id") Integer tokenId,@Param("page") IPage ipage);
}
