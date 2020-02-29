const util = require('../utils/util')
const {
  socketLink
} = require('../utils/config.js').default
class SocketApi {

  constructor() {
    this.connectStatus = 0 // websocket 连接状态 0：未连接，1：已连接
    this.heartListen = null // 存活心跳
    this.holdMessage = null // 定时通信
    this.watcherList = [] // 订阅者
  }

  /* 初始化连接 */
  connectSocket() {
    const socketUrl = socketLink()
    const token = wx.getStorageSync('token')
    const url = `${socketUrl}/websocket/settle?token=${token}`
    // websocket连接
    wx.connectSocket({
      url: url,
      success: res => {
        console.log('连接成功', res)
        console.log(this)
        // 设置连接状态
        this.connectStatus = 1
        // 心跳
        this.setHeartListen() 
        this.setHoldMessage()
      },
      fail: err => {
        console.error('连接失败')
      }
    })
    // 监听webSocket错误
    wx.onSocketError(res => {
      console.log('监听到 WebSocket 打开错误，请检查！')
      // 修改连接状态
      this.connectStatus = 0
    })
    // 监听WebSocket关闭
    wx.onSocketClose(res => {
      console.log('监听到 WebSocket 已关闭！')
      this.connectStatus = 0
    })
    // websocket打开
    wx.onSocketOpen(res => {
      console.log('监听到 WebSocket 连接已打开！')
    })
    // 收到websocket消息
    wx.onSocketMessage(res => {
      this.getSocketMsg(JSON.parse(res.data)) // 收到的消息为字符串，需处理一下
    })

  }

  sendSocketMessage(msg) {
    if (this.connectStatus === 1) {
      wx.sendSocketMessage({
        data: msg
      })
    } else {
      console.log("连接已经关闭")
    }
  }

  setHoldMessage() {
    clearInterval(this.holdMessage)
    this.holdMessage = setInterval(() => {
      if (this.connectStatus === 1) {
        this.sendSocketMessage("echo")
      }
    }, 30000)
  }


  setHeartListen() {
    clearInterval(this.heartListen)
    this.heartListen = setInterval(() => {
      if (this.connectStatus === 0) {
        console.log('监听到没心跳了，抢救一下')
        this.reconnect()
      }
    }, 2000)
  }


  /* 重连 */
  reconnect() {
    if (this.connectStatus === 0){
      console.log('尝试重连')
      wx.closeSocket({
        success: res => {
          console.log('先关闭socket成功')
        }
      })
      this.connectSocket()
    }
  }

  /* 关闭websocket */
  closeSocket() {
    wx.closeSocket({
      success: res => {
        console.log('关闭socket成功')
      }
    })
    clearInterval(this.heartListen)
    clearInterval(this.holdMessage)
    this.connectStatus = 0
  }

  /* 添加watcher */
  addWatcher(fn) {
    this.watcherList.push(fn)
    return this.watcherList.length - 1 // 返回添加位置的下标，Page unload的时候方便删除List成员
  }

  /* 删除watcher */
  delWatcher(index) {
    if(index&&index!='undefined'){
      this.watcherList.splice(index, 1)
      console.log('销毁watcher', this.watcherList)
    }
  }

  /* 收到消息 */
  getSocketMsg(data) {
    console.log('收到消息', data)
    if (data.stage >= 0) {
      // 给每个订阅者发消息
      const list = this.watcherList
      for (let i = 0; i < list.length; i++) {
        list[i](data)
      }
      // 其他返回类型
    }
  }
}

export default new SocketApi