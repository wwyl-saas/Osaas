import request from '@/utils/request_staging'

export function fetchOrderList(query) {
  return request({
    url: '/api/admin/order/get',
    method: 'get',
    params: query
  })
}
export function getOrderStatus() {
  return request({
    url: 'api/drop/list/order/status',
    method: 'get'
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
