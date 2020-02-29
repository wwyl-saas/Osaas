import Taro, {Component} from '@tarojs/taro'
import {View, Button} from '@tarojs/components'
import {connect} from '@tarojs/redux'
import {AtTextarea, AtImagePicker, AtCheckbox} from 'taro-ui'
import * as actions from '../../actions/users'
import {host} from '../../constants/api'
import LabelList from './labelList'
import './index.scss'

@connect(state => state.user, {...actions})
export default class Feedback extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '意见反馈',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    list: [],
    remark: '',
    type: '',
    files: [],
    showUploadBtn: true,
    upLoadImg: [],
    checkedList: ['list1'],
    checkboxOption: [{
      value: 'list1',
      label: '您的反馈将匿名发送',
      desc: ''
    }],
    noSubmit: false
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.props.dispatchFeedbackFeedbackType(
    ).then(res => {
      this.setState({
        list: res
      })
    })
  }

  componentDidHide() {
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  handleChange(event) {
    this.setState({
      remark: event.target.value
    })
  }

  setFatherUploadSrc = () => {
    let {upLoadImg} = this.state
    let str = upLoadImg.join(',');
    let item = {}
    item.evidence_url = str
    // this.props.dispatchFeedbackUpload({
    //   file: ''
    // }).then((res)=>{

    // })
  }

  onChange(v, doType, index) { // doType代表操作类型，移除图片和添加图片,index为移除图片时返回的图片下标
    if (doType === 'remove') {
      this.setState((prevState) => {
        let oldSendImg = prevState.upLoadImg
        oldSendImg.splice(oldSendImg[index], 1) // 删除已上传的图片地址
        return ({
          files: v,
          upLoadImg: oldSendImg
        })
      }, () => {
        const {files} = this.state
        // this.setFatherUploadSrc() // 设置删除数据图片地址
        if (files.length === 3) {  // 最多三张图片 隐藏添加图片按钮
          this.setState({
            showUploadBtn: false
          })
        } else if (files.length === 0) {
          this.setState({
            upLoadImg: []
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
          files: v
        })
      }, () => {
        const {files} = this.state
        if (files.length === 3) {  // 最多三张图片 隐藏添加图片按钮
          this.setState({
            showUploadBtn: false
          })
        } else {
          this.setState({
            showUploadBtn: true
          })
        }
        this.toUpload()
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

  toUpload = () => {
    const {files} = this.state
    if (files.length > 0) {
      const rootUrl = host // 服务器地址
      const cookieData = Taro.getStorageSync('token')  // 图片上传需要单独写入token
      this.uploadLoader({rootUrl, cookieData, path: files})
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
            let oldUpload = prevState.upLoadImg
            oldUpload.push(resultData.data)
            return ({
              upLoadImg: oldUpload
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
        } else {//若图片还没有传完，则继续调用函数
          data.i = i
          data.success = success
          data.fail = fail
          that.uploadLoader(data)
        }
      }
    })
  }

  handleChange2(value) {
    this.setState({
      checkedList: value
    })
  }

  submitClick() {
    let {upLoadImg, remark, type, noSubmit} = this.state
    if (noSubmit) return
    let str = upLoadImg.join(',')
    if (!type) {
      Taro.showToast({
        title: '请选择问题',
        icon: 'none'
      })
      return
    }
    if (!remark) {
      Taro.showToast({
        title: '请填写意见',
        icon: 'none'
      })
      return
    }
    // 禁用按钮
    this.setState({
      noSubmit: true
    })
    Taro.showLoading('正在提交,请稍等')
    this.props.dispatchFeedbackSubmit({
      anonymous: this.state.checkedList.length !== 0,
      pictureUrls: str, // 反馈图片ID,限制3个
      remark: remark,
      type: type
    }).then(() => {
      Taro.hideLoading()
      Taro.showToast({
        title: '提交成功',
        icon: 'success'
      }).then(() => {
        Taro.navigateBack({ delta: 1 })
      })
    })
  }

  labelClick = (item) => {
    this.setState({
      type: item
    })
  }

  render() {
    const {list, checkboxOption, checkedList, showUploadBtn, noSubmit} = this.state
    return (
      <View className='feedback'>
        <LabelList list={list} onLabelClick={this.labelClick.bind(this)}></LabelList>
        <View className='ipt'>
          <AtTextarea className='ipt-area'
            value={this.state.remark}
            onChange={this.handleChange.bind(this)}
            maxLength={200}
            placeholder='请留下您的宝贵意见'
          />
        </View>
        <View className='ipt'>
          <AtImagePicker
            length={3}
            count={3}
            files={this.state.files}
            onChange={this.onChange.bind(this)}
            onFail={this.onFail.bind(this)}
            onImageClick={this.onImageClick.bind(this)}
            showAddBtn={showUploadBtn}
          />
        </View>
        <View className='footer'>
          <AtCheckbox className='footer-checkbox'
            options={checkboxOption}
            selectedList={checkedList}
            onChange={this.handleChange2.bind(this)}
          />
          <Button className='submit' onClick={this.submitClick.bind(this)}>提交反馈</Button>
        </View>

      </View>
    )
  }
}
