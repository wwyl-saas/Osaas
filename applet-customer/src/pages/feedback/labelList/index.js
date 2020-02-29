import Taro, {Component} from '@tarojs/taro'
import {View, Text} from '@tarojs/components'
import './index.scss'

export default class LabelList extends Component {
  static defaultProps = {
    list: []
  }

  state = {
    selectValue: ''
  }

  labelClick = (item) => {
    this.setState({
      selectValue: item
    })
    this.props.onLabelClick(item)
  }

  render() {
    const {list} = this.props
    const {selectValue} = this.state
    return (
      <View className='lable-list'>
        {list && list.map((item, index) => (
          <Text
            className={'label-item ' + (selectValue === item ? 'active' : '')}
            key={'label' + index}
            onClick={this.labelClick.bind(this, item)}
          >
            {item}
          </Text>
        ))}
      </View>
    )
  }
}
