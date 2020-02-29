import request from '@/utils/request_staging'

export function fetchMemberList(query) {
  return request({
    url: '/api/customer/list',
    method: 'get',
    params: query
  })
}
