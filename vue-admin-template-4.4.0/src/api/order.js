import request from '@/utils/request'

export function GetOrderInfo(pageNum, pageSize, vo) {
  return request({
    url: '/orders/page',
    method: 'post',
    params: { pageNum, pageSize },
    data: vo
  })
}

export function updateOrderInfo(order, comIds) {
  return request({
    url: '/orders',
    method: 'put',
    data: order,
    params: { comIds }
  })
}

export function delOrder(id) {
  return request({
    url: '/orders',
    method: 'delete',
    params: { id }
  })
}

export function addNewOrder(order, component) {
  return request({
    url: `/orders/${component}`,
    method: 'post',
    data: order
  })
}
