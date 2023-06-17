import request from '@/utils/request'

export function getComponentsInfoByPage(component, pageNum, pageSize) {
  return request({
    url: '/components/page',
    method: 'post',
    params: { pageNum, pageSize },
    data: component
  })
}

export function addComponents(component) {
  return request({
    url: '/components',
    method: 'post',
    data: component
  })
}

export function deleteComponents(id) {
  return request({
    url: '/components',
    method: 'delete',
    params: { id }
  })
}

export function updateComponents(component) {
  return request({
    url: '/components',
    method: 'put',
    data: component
  })
}

export function getAll() {
  return request({
    url: '/components',
    method: 'get'
  })
}
