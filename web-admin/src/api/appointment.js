import request from '@/utils/request_staging'

export function fetchAppointmentList(query) {
  return request({
    url: '/api/admin/appointment/list',
    method: 'get',
    params: query
  })
}
export function getAppointmentStatus() {
  return request({
    url: 'api/drop/list/appoint/status',
    method: 'get'
  })
}
export function getShops() {
  return request({
    url: 'api/drop/list/shops',
    method: 'get'
  })
}
export function getShopUsers(shopId) {
  return request({
    url: 'api/drop/list/shop/users',
    method: 'get',
    params: { shopId }
  })
}
export function fetchArticle(id) {
  return request({
    url: '/product/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/product/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/product/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/product/update',
    method: 'post',
    data
  })
}
