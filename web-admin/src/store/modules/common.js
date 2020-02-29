import { getOrderStatus } from '@/api/order'

const state = {
  orderStatus: []
}

const mutations = {
  SET_ORDERSTATUS: (state, status) => {
    state.orderStatus = status
  }
}

const actions = {
  initOrderStatus({ commit, state }) {
    return new Promise((resolve, reject) => {
      getOrderStatus().then(response => {
        const { data } = response
        if (!data) {
          reject('获取订单状态失败')
        }
        commit('SET_ORDERSTATUS', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
