/**
 * 适当封装 Redux，简化调用
 */
/* eslint-disable import/prefer-default-export */
import fetch from './request'

export function createAction(options) {
  const { url, payload, method, dataType, fetchOptions, cb, type } = options
  return (dispatch) => {
    return fetch({ url, payload, method, dataType, ...fetchOptions }).then((res) => {
      dispatch({ type, payload: cb ? cb(res) : res })
      return res
    })
  }
}

export function setData(options) {
  const { payload, cb, type } = options
  return (dispatch) => {
    dispatch({ type, payload: cb ? cb() : payload })
  }
}

