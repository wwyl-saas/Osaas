import Taro, {Component} from '@tarojs/taro'
import {ScrollView, View} from '@tarojs/components'
import {connect} from '@tarojs/redux'
import * as actions from '../../actions/product'
import {getWindowHeight} from '../../utils/style'
import PayList from './payList'
import './index.scss'

@connect(state => state.product, {...actions})
export default class ServiceRecords extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '订单列表',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    hasMore: true,
    pageIndex: 1,
    pageSize: 10,
    records: [],
    totalPage: 0,
  }

  componentWillMount() {
  }

  componentDidMount() {

  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.dispatchOrderHistory()
  }

  componentDidHide() {
  }

  hadReachBottom(){
    console.log('onReachBottom')
    Taro.showLoading({title: '加载中...', mask: false})
    const {pageIndex,pageSize,totalPage,records} = this.state
    this.props.dispatchOrderHistory({
      pageIndex: pageIndex+1,
      pageSize: pageSize,
      status: this.$router.params.status
    }).then((data) => {
      let list=records.concat(data.results)
      if(pageIndex<totalPage){
        this.setState({
          hasMore: true,
          records: list,
          pageIndex: data.pageIndex,
          pageSize: data.pageSize,
          totalPage: data.totalPage,
        }, () => {
          Taro.hideLoading()
        })
      }else{
        this.setState({
          hasMore: false,
          loading: false,
          records: list,
          pageIndex: data.pageIndex,
          pageSize: data.pageSize,
          totalPage: data.totalPage,
        }, () => {
          Taro.hideLoading()
        })
      }
    })
  }

  loadRecommend = () => {
    if (this.state.hasMore) {
      this.hadReachBottom()
    }
  }

  dispatchOrderHistory() {
    Taro.showLoading({title: '加载中...', mask: true})
    const {pageIndex,pageSize} = this.state
    this.props.dispatchOrderHistory({
      pageIndex: pageIndex,
      pageSize: pageSize,
      status: this.$router.params.status
    }).then((data) => {
      if(data.totalPage<=1){
        this.setState({
          hasMore: false,
          loading: false,
          records: data.results,
          pageIndex: data.pageIndex,
          pageSize: data.pageSize,
          totalPage: data.totalPage,
        }, () => {
          Taro.hideLoading()
        })
      }else{
        this.setState({
          hasMore: true,
          records: data.results,
          pageIndex: data.pageIndex,
          pageSize: data.pageSize,
          totalPage: data.totalPage,
        }, () => {
          Taro.hideLoading()
        })
      }
    })
  }

  cancelClick(orderId) {
    this.props.dispatchOrderClose({
      orderId: orderId
    }).then((data) => {
      if (!data) return
      Taro.showToast({
        title: '取消成功',
        icon: 'success'
      })
      this.dispatchOrderHistory()
    })
  }

  payClick(orderId, formId) {
    const self = this
    this.props.dispatchPayConfirm({
      "formId": formId,
      "orderId": orderId
    }).then(data => {
      // "nonceStr": "string",
      // "packageValue": "string",
      // "pay": true,
      // "payId": 0,
      // "paySign": "string",
      // "signType": "string",
      // "timeStamp": "string",
      // "type": "余额支付"
      if (data.pay) {
        Taro.showToast({
          title: '余额支付完成',
          icon: 'success'
        }).then(()=>{
          this.dispatchOrderHistory()
        })
        return
      }
      Taro.requestPayment({
        timeStamp: data.timeStamp,
        nonceStr: data.nonceStr,
        package: data.packageValue,
        signType: data.signType || 'MD5',
        paySign: data.paySign,
        success(res) {
          console.log(res)
          Taro.showToast({
            title: '支付完成',
            icon: 'success'
          })
          self.dispatchOrderHistory()
        },
        fail(res) {
          console.log(res)
          Taro.showToast({
            title: '支付失败',
            icon: 'none'
          })
        }
      })
    })
  }

  render() {
    const {records, loading} = this.state
    return (
      <View className='serviceRecords'>
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{height: getWindowHeight()}}
        >
          <PayList
            list={records}
            onCancel={this.cancelClick.bind(this)}
            onPay={this.payClick.bind(this)}
          >

          </PayList>
            {!loading?<View className='noData'>
              {records.length === 0 ? (
                <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
            </View>:null}
        </ScrollView>
      </View>
    )
  }
}
