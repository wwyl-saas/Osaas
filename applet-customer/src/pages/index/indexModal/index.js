import Taro, {Component} from '@tarojs/taro'
import {View, Image} from '@tarojs/components'
import {connect} from '@tarojs/redux'
import * as actions from '../../../actions/product'
import './index.scss'
import fukuanma from '../../../assets/images/fukuanma.png'

@connect(state => state.product, {...actions})
export default class IndexModal extends Component {
  static defaultProps = {
    code: '',
    radarImg: ''
  }

  state = {}

  componentDidMount() {
  }

  cancelClick () {
    this.props.onCloseModal && this.props.onCloseModal()
  }

  newClick = () => {
    this.props.onPayBarcode && this.props.onPayBarcode(true)
  }

  render() {
    const {radarImg, code} = this.props
    return (
      <View className='index-modal'>
        <View className='model-mask'></View>
        <View className='modal-msg'>
          <View className='msg-t'>
            <View className='t-box'>
              <Image className='t-img' src={fukuanma} />
            </View>
          </View>
          <View className='msg-m'>
            <View className='name'>我的付款码</View>
            <View className='code'>
              <Image className='code-img' src={radarImg} />
            </View>
            <View className='num'>{code}</View>
          </View>
          <View className='msg-btn'>
            <View className='cancel' onClick={this.cancelClick.bind(this)}>取消</View>
            <View className='newBtn' onClick={this.newClick.bind(this)}>刷新</View>
          </View>
        </View>
      </View>
    )
  }
}
