import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/users/userlogin',
    method: 'post',
    data
  })
}

export function reg(user) {
  return request({
    url: '/users/register',
    method: 'post',
    data: user
  })
}

export function getInfo() {
  return request({
    url: '/users/decodeToken',
    method: 'get'
  })
}

export function getMoreInfo() {
  return request({
    url: '/users',
    method: 'get'
  })
}
export function logout() {
  return request({
    url: '/users/exitlogin',
    method: 'get'
  })
}

export function userlogout() {
  return request({
    url: '/users/exitlogin',
    method: 'get'
  })
}
export function getUserInfo(pageNum, pageSize, user) {
  return request({
    url: '/users/page',
    method: 'post',
    params: { pageNum, pageSize },
    data: user
  })
}

export function testThe() {
  return request({
    url: '/users/test',
    method: 'get'
  })
}

export function getInfoByToken() {
  return request({
    url: '/users/decodeToken',
    method: 'get'
  })
}

export function delUser(id) {
  return request({
    url: '/users',
    method: 'delete',
    params: { id }
  })
}

export function updateUserInfo(user) {
  return request({
    url: '/users',
    method: 'put',
    data: user
  })
}

export function changeUserRole(user) {
  return request({
    url: '/users/setAdmin',
    method: 'put',
    data: user
  })
}
