const { sendLink } = require('../utils/config.js').default
const util = require('../utils/util')
class ApiCommon {
  constructor() {
  }

//微信上传
upload(url, filePath, name, data, success){
  var user_token = wx.getStorageSync('token')
  wx.uploadFile({
    url: sendLink() + url,
    filePath: filePath,
    name: name,
    formData: data,
    header:{
      'token': user_token
    },
    success(res) {
      if (res.statusCode == 200) {
        let data = JSON.parse(res.data);
        if (data.code == 0) {
          typeof success == 'function' && success.call(this, data)
        } else {
          util.mytoast(res.data.msg, 'none')
        }
      } else {
        util.mytoast('网络状态异常', 'none')
        console.log(res)
      }
    },
    fail: function (err) {
      util.mytoast('网络状态异常', 'none')
      console.log(err)
    }
  })
}

  //上传图片
  uploadImage(filePath,success) {
    this.upload('/api/upload/img', filePath, 'file', {}, success)
  }
}

export default new ApiCommon