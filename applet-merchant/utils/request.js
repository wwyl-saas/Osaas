const util = require('../utils/util')

export default {
  ajax(options) {
    var user_token = wx.getStorageSync('token')
    
    var _header = {
      'token': user_token
    }
   
    if (options.method === "POST") {
      if (options.dataType === 'json'){
        _header['content-type'] = 'application/json'
      }else{
        _header['content-type'] = 'application/x-www-form-urlencoded' // 默认值,
      }
    }

    //请求参数
    let _wx_option = {
      url: options.url,
      method: options.method || 'GET',
      header: _header,
      data: options.data || {},
      // 数据格式的转换也在这进行处理
      success: function (res) {
        if (res.statusCode == 200) {
          if (res.data.code==0){
            if (options.url.indexOf('/api/auth/logout') > -1) {
              _header['token'] = ''
              wx.removeStorageSync('token')
            }
            typeof options.success == 'function' && options.success.call(this, res.data)
          }else{
            util.mytoast(res.data.msg, 'none')
            typeof options.fail == 'function' && options.fail.call(this, res.data)
            //400错误返回到登录页
            if (parseInt(res.data.code / 1000) == 4) {
              wx.removeStorageSync('token')
              wx.redirectTo({
                url: '/pages/login/login'
              })
            }
          }
        } else {
          util.mytoast('服务状态异常', 'none')
          console.log(res)
        }
      },
      fail: function (err) {
        util.mytoast('网络状态异常', 'none')
        console.log(err)
      },
      complete: function(){
        typeof options.complete == 'function' && options.complete.call(this)
      }
    }
    wx.request(_wx_option);
  }
}