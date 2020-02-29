import Taro, { Component } from '@tarojs/taro'
import {View, Text, Image, ScrollView} from '@tarojs/components'
import {AtRate} from "taro-ui";
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import { getWindowHeight } from '../../utils/style'

import './index.scss'

@connect(state => state.product, { ...actions })
export default class Evaluate extends Component {

  config = {
    navigationBarTitleText: '全部评价',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    loadMore: false,
    evaluate: {
      title: '评价列表',
      list: []
    },
    pageIndex: 0,
    pageSize: 20,
    selectBtn1: [
      {name: '有图', id: 1}
    ],
    selectBtn2: [
      {name: '全部', id: ''},
      {name: '好评', id: 0},
      {name: '中评', id: 1},
      {name: '差评', id: 2}
    ],
    havePicture: '',
    type: '',
  }

  componentDidShow () {
    this.dispatchGoodsComments()
  }

  loadRecommend = () => {
    if (this.state.loadMore) {
      this.dispatchGoodsComments()
    }
  }

  dispatchGoodsComments () {
    Taro.showLoading({title: '加载中...', mask: true})
    const { evaluate, pageIndex, pageSize, havePicture, type } = this.state
    const params = this.$router.params
    this.props.dispatchGoodsComments({
      goodsId: params.goodsId,
      havePicture: havePicture, // 0无图 1有图
      pageIndex: pageIndex + 1,
      pageSize: pageSize,
      type: type // 0好评 1中评 2差评
    }).then(res=>{
      Taro.hideLoading()
      evaluate.list = evaluate.list.concat(res.results)
      this.setState({
        pageIndex: res.pageIndex,
        pageSize: res.pageSize,
        loading: res.pageIndex < res.totalPage,
        loadMore: res.pageIndex < res.totalPage,
        evaluate: evaluate
      })
    })
  }

  btnClick1 (id) {
    this.setState({
      havePicture: id,
      pageIndex: 0,
      pageSize: 20,
      loading: true,
      loadMore: false,
      evaluate: {
        title: '评价列表',
        list: []
      },
    }, ()=>{
      this.dispatchGoodsComments()
    })
  }

  btnClick2 (id) {
    this.setState({
      type: id,
      pageIndex: 0,
      pageSize: 20,
      loading: true,
      loadMore: false,
      evaluate: {
        title: '评价列表',
        list: []
      },
    }, ()=>{
      this.dispatchGoodsComments()
    })
  }

  render () {
    const { loadMore, evaluate, loading, selectBtn1, selectBtn2, havePicture, type } = this.state
    let info  = evaluate
    return (
      <View className='evaluate'>
        <ScrollView
          scrollY
          className='home__wrap'
          enableBackToTop
          onScrollToLower={this.loadRecommend}
          style={{ height: getWindowHeight()}}
        >
          <View className='typeBtns'>

            {selectBtn2.map((itemBtn, indexBtn) => (
              <Text
                key={'btn2'+indexBtn}
                className={'typeBtn' + ' ' + (itemBtn.id===type?'active':'')}
                onClick={this.btnClick2.bind(this, itemBtn.id)}
              >
                {itemBtn.name}
              </Text>
            ))}
            {selectBtn1.map((itemBtn, indexBtn) => (
              <Text
                key={'btn1'+indexBtn}
                className={'typeBtn' + ' ' + (itemBtn.id===havePicture?'active':'')}
                onClick={this.btnClick1.bind(this, itemBtn.id)}
              >
                {itemBtn.name}
              </Text>
            ))}
          </View>

          {info.list && info.list.map((item,index)=>(
            <View key={'evaluate'+index} className={'evaluate-item'+' '+(!loadMore&&index>10000?'hide-item':'')}>
              <View className='info-top'>
                <View className='top-left'>
                  <Image className='avatar' src={item.avatar} />
                  <View className='left-r'>
                    <Text>{item.customerName}</Text>
                    <Text className='time'>{item.createTime}</Text>
                  </View>
                </View>
                <View className='top-right'>
                  <AtRate value={item.grade/10} size='13' />
                  {item.grade/10}分
                </View>
              </View>
              <View className='content-box'>
                <Text>{item.remark}</Text>
              </View>
              <View className='pictureList'>
                {item.pictureList && item.pictureList.map((item2,index2)=>(
                  <Image className='picture' key={'img'+index2} src={item2}></Image>
                ))}
              </View>
              <View className='userinfo'>
                  <Text>— 此次服务由{item.merchantUserTitle}{item.merchantUserName}提供 —</Text>
              </View>
            </View>
          ))}
          {!loading?<View className='noData'>
              {info.list && info.list.length===0? (
                <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
            </View>:null}
        </ScrollView>
      </View>
    )
  }
}
