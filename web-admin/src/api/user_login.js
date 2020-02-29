import request from '@/utils/request_staging'
export function login(data) {
  return request({
    url: 'api/admin/auth/login/psw',
    method: 'post',
    data
  })
}

