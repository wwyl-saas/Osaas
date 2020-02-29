import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image } from '@tarojs/components'
import './index.scss'

export default class PayCode extends Component {
  static defaultProps = {
    allCode: {}
  }
  state = {

  }
  componentWillMount () {
  }
  swanClick = () => {
    this.props.onSwanClick()
  }

  render () {
    const { allCode } = this.props
    return (
        <View className='codeMask'>
            <View className='mask' onClick={this.swanClick.bind(this)}></View>
            <View className='msg'>
                <View className='topLine'></View>
                <View className='x-code'>
                    <View className='img-box'>
                      <Image className='code-img' src={allCode.minicode || allCode.qrcode} />
                    </View>
                    <Text className='x-name'>长按或者扫描二维码进入小程序</Text>
                </View>
                <View className='e-code'>
                    <Text className='e-name'>代付二维码</Text>
                    <Image className='code-img' src={allCode.qrcode} />
                    <Text className='e-name'>有效期至2019-11-12 13:00</Text>
                </View>
            </View>
        </View>
    )
  }
}
