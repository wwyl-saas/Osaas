import Taro from '@tarojs/taro'
import {API_AUTH_TRY_LOGIN, applicationId} from '../constants/api'
import {dispatchAuthorized} from '../actions/users'

const CODE_SUCCESS = 0
const CODE_AUTH_TRY = 4001
const CODE_AUTH_EXPIRED = 4002

function getStorage(key) {
  return Taro.getStorage({ key }).then(res => res.data).catch(() => '')
}


function wxLogin (options) {
  return Taro.login().then(response => {
    console.log('微信登录成功', response);
    return AuthTryLogin(response, options)
  }).catch(err => {
    console.log('微信登录失败', err)
    Taro.showToast({
      title: '发生错误，请重试!',
      icon: 'none'
    })
  })
}

function AuthTryLogin (response, options) {
  return new Promise((resolve, reject) => {
    console.log(reject)
    const header = { 'applicationId': applicationId }
    header['content-type'] = 'application/json' // 默认值,
    Taro.request({
      url: API_AUTH_TRY_LOGIN,
      method: 'POST',
      data: {
        loginCode: response.code
      },
      header
    }).then((res) => {
      const { code, data, msg } = res.data
      if (code === CODE_SUCCESS) { // 成功
        Taro.setStorage({
          key: 'token',
          data: data && data.token
        })
        Taro.setStorage({
          key: 'customer',
          data: data && data.customer
        })
        dispatchAuthorized(true)
        // 等token设置成功后 promise抛出回调
        resolve(options)
      } else {
        Taro.showToast({
          title: msg,
          icon: 'none'
        })
      }
    }).catch((err) => {
      console.log('没有授权', err)
      Taro.showToast({
        title: err && err.msg,
        icon: 'none'
      })
    })
  })
}
let resolves
/**
 * 简易封装网络请求
 * // NOTE 需要注意 RN 不支持 *StorageSync，此处用 async/await 解决
 * @param {*} options
 */
export default async function fetch(options) {
  const { url, payload, method = 'GET', dataType = 'json', showToast = true } = options
  const token = await getStorage('token') || ''
  const header = token ? { 'token': token, 'applicationId': applicationId } : { 'applicationId': applicationId }
  if (method === 'POST') {
    if (dataType === 'json'){
      header['content-type'] = 'application/json' // 默认值,
    }else{
      header['content-type'] = 'application/x-www-form-urlencoded'
    }
  }
  return new Promise((resolve, reject) => {
      Taro.request({
        url,
        method,
        data: payload,
        header
      }).then(async (res) => {
        const { code, data, msg } = res.data
        if (code === CODE_SUCCESS) { // 成功
          // return data || msg
          resolve(data || msg)
          resolves && resolves(data || msg)
        } else if (code === CODE_AUTH_TRY) { // 尝试登录
          wxLogin(options).then(() => {
            fetch(options)
            resolves = resolve
          })
        } else if (code === CODE_AUTH_EXPIRED) { // 授权登录
          dispatchAuthorized(false)
          console.log('not Auth')
        } else {
          if (showToast) {
            Taro.showToast({
              title: msg,
              icon: 'none'
            })
          }
        }
      }).catch((err) => {
        if (showToast) {
          Taro.showToast({
            title: err && err.msg,
            icon: 'none'
          })
        }
        reject({ message: err && err.msg, ...err })
      })
  })
}
