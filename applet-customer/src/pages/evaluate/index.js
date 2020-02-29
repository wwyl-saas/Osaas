import Taro, {Component} from '@tarojs/taro'
import {View, ScrollView} from '@tarojs/components'
import {AtRate, AtTextarea, AtImagePicker, AtCheckbox, AtButton} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '../../actions/product'
import {getWindowHeight} from '../../utils/style'
import {host} from '../../constants/api'
import './index.scss'

@connect(state => state.product, {...actions})
export default class Appointment extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '服务评价',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    files: {},
    showUploadBtn: true,
    remark: {},
    rate: {},
    checkboxOption: [{
      value: 'list1',
      label: '匿名',
      desc: ''
    }],
    checkedList: ['list1'],
    upLoadImg: {},
    orderDesc: {}
  }

  componentWillMount() {
    const params = this.$router.params
    this.props.dispatchOrderDesc({
      orderId: params.orderId
    }).then(data=>{
      this.setState({
        orderDesc: data || {}
      })
    })
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
  }

  componentDidHide() {
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  onChangeImg(listIndex, v, doType, index) { // doType代表操作类型，移除图片和添加图片,index为移除图片时返回的图片下标
    console.log(listIndex, v, doType, index)
    let {files, upLoadImg} = this.state
    files[listIndex] = v
    if (doType === 'remove') {
      this.setState((prevState) => {
        console.log(prevState)
        let oldSendImg = prevState.upLoadImg[listIndex]
        oldSendImg.splice(oldSendImg[index], 1) // 删除已上传的图片地址
        upLoadImg[listIndex] = oldSendImg
        return ({
          files: files,
          upLoadImg: upLoadImg
        })
      }, () => {
        // this.setFatherUploadSrc() // 设置删除数据图片地址
        if (files[listIndex].length === 3) {  // 最多三张图片 隐藏添加图片按钮
          this.setState({
            showUploadBtn: false
          })
        } else if (files[listIndex].length === 0) {
          upLoadImg[listIndex] = []
          this.setState({
            upLoadImg: upLoadImg
          })
        } else {
          this.setState({
            showUploadBtn: true
          })
        }
      })
    } else {
      this.setState(() => {
        return ({
          files: files
        })
      }, () => {
        if (files[listIndex].length === 3) {  // 最多三张图片 隐藏添加图片按钮
          this.setState({
            showUploadBtn: false
          })
        } else {
          this.setState({
            showUploadBtn: true
          })
        }
        this.toUpload(listIndex)
      })
    }
  }

  // 选择失败回调
  onFail(mes) {
    console.log(mes)
  }

  // 点击图片回调
  onImageClick(index, file) {
    console.log(index, file)
  }

  toUpload = (listIndex) => {
    const {files} = this.state
    if (files[listIndex].length > 0) {
      const rootUrl = host // 服务器地址
      const cookieData = Taro.getStorageSync('token')  // 图片上传需要单独写入token
      this.uploadLoader({rootUrl, cookieData, path: files[listIndex], listIndex: listIndex})
    } else {
      Taro.showToast({
        title: '请先点击+号选择图片',
        icon: 'none',
        duration: 2000
      })
    }
  }

  // 上传组件
  uploadLoader = (data) => {
    let {upLoadImg} = this.state
    let that = this
    let i = data.i ? data.i : 0 // 当前所上传的图片位置
    let success = data.success ? data.success : 0//上传成功的个数
    let fail = data.fail ? data.fail : 0;//上传失败的个数
    Taro.showLoading({
      title: `正在上传第${i + 1}张`
    })
    //发起上传
    Taro.uploadFile({
      url: data.rootUrl + '/api/v1/feedback/upload/img',
      header: {
        'content-type': 'multipart/form-data',
        'token': data.cookieData,
        'applicationId': 1
      },
      name: 'file',
      filePath: data.path[i].url,
      success: (resp) => {
        //图片上传成功，图片上传成功的变量+1
        let resultData = JSON.parse(resp.data)
        if (resultData.code === 0) {
          success++;
          this.setState((prevState) => {
            console.log(prevState)
            let oldUpload = prevState.upLoadImg[data.listIndex] || []
            oldUpload.push(resultData.data)
            upLoadImg[data.listIndex] = oldUpload
            return ({
              upLoadImg: upLoadImg
            })
          }, () => {
            // setSate会合并所有的setState操作，所以在这里等待图片传完之后再调用设置url方法
            /*
            * 该处十分重要
            **/
            // this.setFatherUploadSrc()// 设置数据图片地址字段
          })
        } else {
          fail++
        }
      },
      fail: () => {
        fail++ // 图片上传失败，图片上传失败的变量+1
      },
      complete: () => {
        Taro.hideLoading()
        i++ // 这个图片执行完上传后，开始上传下一张
        if (i == data.path.length) {   //当图片传完时，停止调用
          Taro.showToast({
            title: '上传成功',
            icon: 'success',
            duration: 2000
          })
          console.log('成功：' + success + " 失败：" + fail);
        } else {//若图片还没有传完，则继续调用函数
          data.i = i
          data.success = success
          data.fail = fail
          that.uploadLoader(data)
        }
      }
    })
  }

  handleChange(index, event) {
    let { remark } = this.state
    remark[index] = event.target.value
    this.setState({
      remark: remark
    })
  }

  rateChange(index, value) {
    let { rate } = this.state
    rate[index] = value
    this.setState({
      rate: rate
    })
  }

  checkChange(value) {
    this.setState({
      checkedList: value
    })
  }

  dispatchCompleteComment () {
    const { orderDesc, checkedList, rate, upLoadImg, remark } = this.state
    let queries = []
    let orderDescList = orderDesc.orderDescList || []
    orderDescList.forEach((item, index)=>{
      let obj = {
        "anonymousType": checkedList.length !== 0,
        "goodsId": item.id,
        "grade": (rate[index] || 0)*10,
        "pictureUrls": upLoadImg[index] && upLoadImg[index].join(',') || '',
        "remark": remark[index] || ''
      }
      queries.push(obj)
    })
    this.props.dispatchCompleteComment({
      "orderId": orderDesc.id,
      queries: queries
    }).then((data)=>{
      if (!data) return
      Taro.showToast({
        title: '发布成功',
        icon: 'success'
      })
      setTimeout(()=>{
        Taro.navigateBack({
          delta: 1
        })
      }, 1500)
    })
  }

  render() {
    const {files, showUploadBtn, orderDesc, remark, rate} = this.state
    return (
      <View className='evaluate'>
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{height: getWindowHeight()}}
        >
          <View className='eva-list'>
            {orderDesc.orderDescList && orderDesc.orderDescList.map((item, index) => (
              <View className='eva-item' key={'eva' + index}>
                <View className='product'>
                  <View className='img-box' style={{backgroundImage: 'url(' + item.briefPicUrl + ')'}}></View>
                  <View className='pro-info'>
                    <View className='name'>{item.name}</View>
                    <View className='star'>
                      <AtRate size='15' value={rate[index] || 0} onChange={this.rateChange.bind(this, index)} />
                    </View>
                  </View>
                </View>
                <View className='textarea'>
                  <AtTextarea
                    value={remark[index] || ''}
                    onChange={this.handleChange.bind(this, index)}
                    maxLength={200}
                    placeholder='您对我们的服务满意吗？满意的话请留下赞美之词，不满意的，请留下批评，我们会改正的搭～'
                  />
                </View>
                <View className='img-picker'>
                  <AtImagePicker
                    length={3}
                    count={3}
                    files={files[index]}
                    onChange={this.onChangeImg.bind(this, index)}
                    onFail={this.onFail.bind(this)}
                    onImageClick={this.onImageClick.bind(this)}
                    showAddBtn={showUploadBtn}
                  />
                </View>
              </View>
            ))}
          </View>
          <View className='submit-box'>
            <AtCheckbox
              options={this.state.checkboxOption}
              selectedList={this.state.checkedList}
              onChange={this.checkChange.bind(this)}
            />
            <AtButton type='primary' onClick={this.dispatchCompleteComment.bind(this)} size='normal'>发布</AtButton>
          </View>
        </ScrollView>
      </View>
    )
  }
}
