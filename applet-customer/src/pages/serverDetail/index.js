import Taro, { Component } from '@tarojs/taro'
import {View, Text, ScrollView, Canvas, Image} from '@tarojs/components'
import { AtIcon, AtButton } from 'taro-ui'
import { connect } from '@tarojs/redux'
import {applicationId, API_GOODS_APPLET_CODE} from '../../constants/api'
import * as actions from '../../actions/product'
import { getWindowHeight } from '../../utils/style'
import withShare from '../../utils/withShare'
import DetailInfo from './detailInfo'
import Specs from './specs'
import Shop from './shop'
import Evaluate from './evaluate'
import Problem from './problem'
import Detail from './detail'
import './index.scss'

@connect(state => state.product, { ...actions })
@withShare({
  title: '商品详情',
  imageUrl: '',
  path: 'pages/serverDetail/index?'
})
export default class ServerDeatil extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '商品详情',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    evaluate: {
      title: '评价',
      list: []
    },
    goodsId: '',
    detail: {},
    isShowCode: false,
    butShow: false,
    tempFilePath: ''
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
    const { evaluate } = this.state
    const params = this.$router.params
    this.props.dispatchGoodsDetail({
      goodsId: params.goodsId
    }).then(data=>{
      if (!data) return
      evaluate.list = []
      if (data.lastComment) {
        evaluate.list.push(data.lastComment)
      }
      this.setState({
        detail: data,
        goodsId: params.goodsId,
        evaluate: evaluate
      })
      this.$setShareImageUrl = () => data.primaryPics[0]
      this.$setShareTitle = () => data.goodsName
      this.$setSharePath = () => `pages/serverDetail/index?goodsId=${params.goodsId}`
    })
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }
  gotoClick = () => {
    const params = this.$router.params
    const { detail } = this.state
    Taro.reLaunch({
      url: `/pages/appointment/index?goodsId=${params.goodsId}&goodsName=${detail.goodsName}&detail=true`
    })
  }

  shareClick = () => {
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
    const {goodsId, detail} = this.state
    // 创建canvas对象
    let ctx = Taro.createCanvasContext('cardCanvas')
    // 填充背景色
    let grd = ctx.createLinearGradient(0, 0, 1, 500)
    grd.addColorStop(0, '#FFF')
    grd.addColorStop(0.5, '#FFF')
    ctx.setFillStyle(grd)
    ctx.fillRect(0, 0, 400, 500)
    // 绘制商品图形
    let res = await Taro.downloadFile({
      url: detail.primaryPics && detail.primaryPics[0].replace('http', 'https') || ''
    })
    ctx.save()
    ctx.drawImage(res.tempFilePath, 0, 0, 320, 210)
    ctx.restore()

    // 绘制文字
    ctx.save()
    ctx.setFontSize(14)
    ctx.setFillStyle('#9d9063')
    // ctx.fillText(`【${detail.goodsName}】${detail.goodsDesc}`, 20, 240)
    this.drawTextOn(ctx, `【${detail.goodsName}】${detail.goodsDesc}`, 20, 210, 280)
    ctx.setFontSize(14)
    ctx.setFillStyle('#222')
    ctx.fillText('商店小程序二维码', 105, 310)
    ctx.restore()
    const token = Taro.getStorageSync('token')
    let minicode = `${API_GOODS_APPLET_CODE}?applicationId=${applicationId}&goodsId=${goodsId}&token=${token}`
    // 绘制小程序二维码图形
    let qrcode = await Taro.downloadFile({
      url: minicode || ''
    })
    ctx.drawImage(qrcode.tempFilePath, 110, 330, 100, 100)

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

  drawTextOn(mainCtx, t, x, y, w){
    let chr = t.split('')
    let temp = ''
    let row = []
    mainCtx.textBaseline = 'middle'
    for(let a = 0; a < chr.length; a++){
      if( mainCtx.measureText(temp).width < w ){
        ;
      } else {
        row.push(temp)
        temp = ""
      }
      temp += chr[a]
    }

    row.push(temp)

    for(let b = 0; b < row.length; b++){
      mainCtx.fillText(row[b],x,y+(b+1)*20)
    }
  }

  /**
   * saveCard() 保存图片到本地
   */
  async saveCard (e) {
    e.stopPropagation()
    // 将Canvas图片内容导出指定大小的图片
    let saveRes = await Taro.saveImageToPhotosAlbum({
      filePath: this.state.tempFilePath
    })
    if (saveRes.errMsg === 'saveImageToPhotosAlbum:ok') {
      Taro.showModal({
        title: '图片保存成功',
        content: '图片成功保存到相册了，快去发朋友圈吧~',
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
    const { detail, evaluate, isShowCode, goodsId, butShow, tempFilePath } = this.state
    return (
      <View className='serverDetail'>
            <ScrollView
              scrollY
              className='home__wrap'
              onScrollToLower={this.loadRecommend}
              style={{ height: getWindowHeight()}}
            >
                <DetailInfo info={detail}></DetailInfo>
                <Specs info={detail.goodsAttributes}></Specs>
                <Shop info={detail.merchantShops}></Shop>
                <Evaluate info={evaluate} commentNum={detail.commentNum} goodsId={goodsId}></Evaluate>
                <Detail info={detail.detailPics}></Detail>
                <Problem info={detail.goodsIssues}></Problem>
            </ScrollView>
            <View className='setFooter'>
              <View className='left'>
                  <AtButton className='operate' openType='contact' onContact={this.handleContact}>
                      <AtIcon value='message' size='20' color='#009900'></AtIcon>
                      <Text className='name'>客服</Text>
                  </AtButton>
                  <View className='operate' onClick={this.shareClick.bind(this)}>
                      <AtIcon value='external-link' size='20' color='#CC6600'></AtIcon>
                      <Text className='name'>分享</Text>
                  </View>
              </View>
              <View className='right' onClick={this.gotoClick.bind(this)}>
                  <Text>立即预约</Text>
              </View>
            </View>
          {isShowCode?(<View className='canvas-wrap' onClick={this.shareClick.bind(this)}>
            <Image className='can-img' src={tempFilePath} />
            {butShow?(<View onClick={this.saveCard.bind(this)} className='btn-save' type='primary' size='mini'>
              保存到相册分享
            </View>):null}
          </View> ):null}
        {!tempFilePath?(<Canvas
          id='card-canvas'
          className='card-canvas'
          style='width: 320px; height: 450px'
          canvasId='cardCanvas'
        >
        </Canvas>):null}
      </View>
    )
  }
}
