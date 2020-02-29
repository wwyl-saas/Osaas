import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, Canvas, Button } from '@tarojs/components'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/users'
import {applicationId, API_APPLET_CODE, API_ANORHER_PAY_QRCODE} from '../../constants/api'
import './index.scss'
import zflc from '../../assets/images/zflc.png'
import fukuanma from '../../assets/images/fukuanma.png'

@connect(state => state.user, { ...actions })
export default class PayOther extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '代人支付',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    textList: [
      '1.代人支付二维码只能使用一次即过期；',
      '2.代人支付二维码有效期为一小时；',
      '3.请妥善保存您的二维码，不公开传播，本店概不担负您因代人支付二维码流失导致的损失。',
      '4.代人支付时请保证您的余额充足，否则会扣款失败。生成支付二维码'
    ],
    allCode: {
      minicode: '',
      qrcode: ''
    },
    isShowCode: false,
    butShow: false,
    tempFilePath: ''
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
  }
  componentDidHide () { }

  swanClick = () => {
    this.setState({
      isShowCode: !this.state.isShowCode,
      butShow: false
    }, () => {
      this.drawImage()
    })
  }

  /**
   * drawImage() 定义绘制图片的方法
   */
  async drawImage () {
    const token = Taro.getStorageSync('token')
    const { allCode } = this.state
    allCode.minicode = `${API_APPLET_CODE}?applicationId=${applicationId}`
    allCode.qrcode = `${API_ANORHER_PAY_QRCODE}?applicationId=${applicationId}&token=${token}`
    this.setState({
      allCode: allCode
    })
    // 创建canvas对象
    let ctx = Taro.createCanvasContext('cardCanvas')
    // 填充背景色
    let grd = ctx.createLinearGradient(0, 0, 1, 500)
    grd.addColorStop(0, '#FFF')
    grd.addColorStop(0.5, '#FFF')
    ctx.setFillStyle(grd)
    ctx.fillRect(0, 0, 400, 500)
    // 绘制小程序二维码图形
    let res = await Taro.downloadFile({
      url: allCode.minicode || ''
    })
    ctx.save()
    ctx.beginPath()
    // ctx.arc(160, 86, 66, 0, Math.PI * 2, false)
    // ctx.arc(160, 88, 66, 0, Math.PI * 2)
    // ctx.closePath()
    // ctx.clip()
    // ctx.stroke()
    ctx.drawImage(res.tempFilePath, 100, 20, 120, 120)
    ctx.restore()

    // 绘制文字
    ctx.save()
    ctx.setFontSize(14)
    ctx.setFillStyle('#757474')
    ctx.fillText('长按或者扫描二维码进入小程序', 60, 160)
    ctx.restore()

    // 绘制二维码
    let qrcode = await Taro.downloadFile({
      url: allCode.qrcode || ''
    })
    ctx.drawImage(qrcode.tempFilePath, 70, 220, 180, 180)
    ctx.restore()
    // 绘制文字
    ctx.save()
    ctx.setFontSize(18)
    ctx.setFillStyle('#242424')
    ctx.fillText('代付二维码', 118, 420)
    // 将以上绘画操作进行渲染
    ctx.draw(true, () => {
      this.canvasToTempFilePath()
    })
  }

  async canvasToTempFilePath () {
    let res = await Taro.canvasToTempFilePath({
      x: 0,
      y: 0,
      width: 400,
      height: 500,
      destWidth: 360,
      destHeight: 450,
      canvasId: 'cardCanvas',
      fileType: 'png'
    })
    this.setState({
      tempFilePath: res.tempFilePath,
      butShow: true
    })
  }

  /**
   * saveCard() 保存图片到本地
   */
  async saveCard (e) {
    e.stopPropagation()
    let saveRes = await Taro.saveImageToPhotosAlbum({
      filePath: this.state.tempFilePath
    })
    if (saveRes.errMsg === 'saveImageToPhotosAlbum:ok') {
      Taro.showModal({
        title: '图片保存成功',
        content: '图片成功保存到相册了，分享给您的朋友吧~',
        showCancel: false,
        confirmText: '确认'
      })
    } else {
      Taro.showModal({
        title: '图片保存失败',
        content: '请重新尝试!',
        showCancel: false,
        confirmText: '确认'
      })
    }
  }

  render () {
    const { textList, isShowCode, butShow, tempFilePath } = this.state
    return (
      <View className='payOther'>
          <View className='pay-title'>代人支付流程</View>
          <View className='tipImg'>
            <Image className='img' src={zflc} />
          </View>
          <View className='step'>
            <Text className='title'>支付说明</Text>
            {textList.map((item,index)=>(
              <Text key={'text'+index} className='msg'>{item}</Text>
            ))}
          </View>
          <View className='swan' onClick={this.swanClick.bind(this)}>
            <View className='img-box'>
                <Image className='img' src={fukuanma} />
            </View>
            <Text className='img-name'>点击生成支付二维码</Text>
          </View>
          {isShowCode?(<View className='canvas-wrap' onClick={this.swanClick.bind(this)}>
            <Image className='can-img' src={tempFilePath} />
            {butShow?(<View
              onClick={this.saveCard.bind(this)}
              className='btn-save'
            >
              保存到相册
            </View>):null}
          </View> ):null}
          <View className='canvas-box'>
            {!tempFilePath?(<Canvas
              id='card-canvas'
              className='card-canvas'
              style='width: 320px; height: 450px'
              canvasId='cardCanvas'
            >
            </Canvas>):null}
          </View>
      </View>
    )
  }
}
