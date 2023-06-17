package com.sym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sym.common.Result;
import com.sym.entity.Car;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sym.vo.CarAndUserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sym
 * @since 2023-06-07
 */
public interface ICarService extends IService<Car> {

    Result deleteUserCar(Integer carId, HttpServletRequest request);

    IPage CarUserPage(CarAndUserVo vo,IPage page,HttpServletRequest request);

    Result updateInfo(Car car, HttpServletRequest request);

    List<Car> getOwnCar(HttpServletRequest request);
}
