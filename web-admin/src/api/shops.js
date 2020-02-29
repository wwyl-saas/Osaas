import request from '@/utils/request_staging'

export function fetchShopList(query) {
  return request({
    url: '/api/admin/merchant/shop/list',
    method: 'get',
    params: query
  })
}
