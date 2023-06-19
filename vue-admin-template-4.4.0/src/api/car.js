import request from '@/utils/request'

export function getCarInfoByPage(obj, pageNum, pageSize) {
  return request({
    method: 'post',
    url: '/cars/page',
    data: obj,
    params: { pageNum, pageSize }
  })
}

export function updateCarsInfo(obj) {
  return request({
    method: 'put',
    url: '/cars',
    data: obj
  })
}

export function deleteCar(carId) {
  return request({
    method: 'delete',
    url: '/cars',
    params: { carId }
  })
}

export function addCar(car) {
  return request({
    method: 'post',
    url: '/cars',
    data: car
  })
}

export function getOwnCar() {
  return request({
    method: 'get',
    url: '/cars'
  })
}

export function getPageOwnCar(pageNum, pageSize) {
  return request({
    method: 'get',
    url: '/cars/ownpage',
    params: { pageNum, pageSize }
  })
}
