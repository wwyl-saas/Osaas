import Taro, { Component } from '@tarojs/taro'
import { View, Text, Form, Button } from '@tarojs/components'
import './index.scss'

export default class SetFooter extends Component {
  static defaultProps = {
    setData: {},
    changeData: {}
  }

  confirmClick (orderId, e) {
    console.log('e', e)
    console.log('formId', e.detail.formId)
    const formId = e.detail.formId
    this.props.onDispatchPayConfirm && this.props.onDispatchPayConfirm(orderId, formId)
  }

  cancelClick  () {
    this.props.onCancelClick && this.props.onCancelClick()
  }

  render () {
    const { setData, changeData } = this.props
    return (
      <View className='setFooter'>
        <View className='footer_tip'>
          共
          <Text className='coupon'>{setData.totalNum}</Text>
          件商品，实付
          <Text className='coupon'>¥{changeData.payAmount || setData.payAmount}</Text>
        </View>
        <View className='foot-row'>
          <View className='cancel' onClick={this.cancelClick.bind(this)}>
            <Text>放弃支付</Text>
          </View>
          <Form className='submit' reportSubmit='true' onSubmit={this.confirmClick.bind(this, setData.orderId)}>
            <Button className='btn' formType='submit'>
              <Text>确认付款</Text>
            </Button>
          </Form>
        </View>
      </View>
    )
  }
}
