import Taro, {Component} from '@tarojs/taro'
import {View, Text, Button, Image, Form} from '@tarojs/components'
import './index.scss'

export default class ProjectList extends Component {
  static defaultProps = {
    list: []
  }
  state = {}

  cancelClick(orderItem) {
    this.props.onCancel && this.props.onCancel(orderItem.id)
  }

  payClick (orderItem, e) {
    console.log('formId', e.currentTarget.formId)
    const formId = e.currentTarget.formId
    this.props.onPay && this.props.onPay(orderItem.id, formId)
  }

  goToHref (orderItem) {
    Taro.navigateTo({
      url: `/pages/evaluate/index?orderId=${orderItem.id}`
    })
  }

  detailClick (orderItem) {
    Taro.navigateTo({
      url: `/pages/orderDetail/index?orderId=${orderItem.id}`
    })
  }

  render() {
    const {list} = this.props
    return (
      <View>
        {list && list.map((orderItem, orderIndex) => (
          <View
            className='payList'
            key={'order' + orderIndex}
          >
            <View className='order'>
              <View className='orderNum'>
                <Text className='line'></Text>
                <Text className='name'>订单编号</Text>
                <Text className='num'>{orderItem.orderNo}</Text>
              </View>
              <View className={'status' + ' ' + (orderItem.status === '代付款' ? 'red' : '')}>
                {orderItem.status}
              </View>
            </View>
            <View className='goods' onClick={this.detailClick.bind(this, orderItem)}>
              {orderItem.orderDescList && orderItem.orderDescList.map((item, index) => (
                <View key={'good' + index} className='pro-item'>
                  <View className='left'>
                    <Image className='left-img' src={item.briefPicUrl} />
                  </View>
                  <View className='right'>
                    <View className='name'>
                      <Text>{item.name}</Text>
                    </View>
                    <View className='des'>
                      <Text>{item.goodsBrief}</Text>
                    </View>
                    <View className='num'>
                      <Text>x {item.goodsNum}</Text>
                    </View>
                  </View>
                </View>
              ))}
            </View>
            <View className='operation'>
              <Text>共{orderItem.totalNum}件商品，合计:￥{orderItem.payAmount}</Text>
              <View className='btns'>
                {orderItem.status === '待支付' && <Button onClick={this.cancelClick.bind(this, orderItem)} className='cancleBtn'>
                  取消订单
                </Button>}
                {orderItem.status === '待支付' && <Form className='right submit' reportSubmit='true' onSubmit={this.payClick.bind(this, orderItem)}>
                  <Button formType='submit' className='sureBtn'>
                    去付款
                  </Button>
                </Form>}
                {orderItem.status === '已支付' && <Button onClick={this.goToHref.bind(this, orderItem)} className='sureBtn'>
                  评价
                </Button>}
              </View>
            </View>
          </View>
        ))}
      </View>
    )
  }
}
