import request from '@/utils/request_staging'

export function fetchClassifyList(query) {
  return request({
    url: '/api/admin/category/list',
    method: 'get',
    params: query
  })
}

export function fetchDetailList(query) {
  return request({
    url: '/api/admin/goods/list',
    method: 'get',
    params: query
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

export function createClassify(data) {
  return request({
    url: '/api/admin/category/create',
    method: 'post',
    data
  })
}

export function updateClassify(data) {
  return request({
    url: '/api/admin/category/update',
    method: 'post',
    data
  })
}

export function deleteClassify(id) {
  return request({
    url: '/api/admin/category/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function changeStatus(row) {
  return request({
    url: '/api/admin/category/status/update',
    method: 'post',
    params: {
      id: row.id,
      enable: row.enable
    }
  })
}
