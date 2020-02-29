import request from '@/utils/request_staging'

export function fetchEmployeeList(query) {
  return request({
    url: '/api/admin/user/list',
    method: 'get',
    params: query
  })
}
export function fetchPostList(query) {
  return request({
    url: '/api/admin/merchant/posttitle/list',
    method: 'get',
    params: query
  })
}
