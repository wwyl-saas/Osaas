import request from '@/utils/request_staging'

export function fetchCouponList(query) {
  return request({
    url: '/api/admin/coupon/list',
    method: 'get',
    params: query
  })
}
