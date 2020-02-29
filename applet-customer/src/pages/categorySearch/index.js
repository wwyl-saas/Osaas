import Taro, { Component, Config } from '@tarojs/taro'
import { View, Text, ScrollView, Button } from '@tarojs/components'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import { AtSearchBar } from 'taro-ui'
import { getWindowHeight } from '../../utils/style'
import ClassList from '../serverClass/classList'
import './index.scss'

@connect(state => state.product, { ...actions })
export default class CategoryList extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '商品分类',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    hasMore: true,
    keyWords: '',
    selected: -1,
    productList: []
  }

  componentWillMount () { }

  componentDidMount () {
    const params = this.$router.params
    this.setState({
      keyWords: params.name
    })
    this.props.dispatchCategorySearch({
      keyWords: params.name
    }).then(data => {
      this.setState({
        productList: data
      })
    })
  }

  componentWillUnmount () { }

  componentDidShow () {
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }
  onChange (keyWords) {
    this.setState({
        keyWords: keyWords
    })
  }
  onActionClick () {
    this.props.dispatchCategorySearch({
      keyWords: this.state.keyWords
    }).then(data => {
      this.setState({
        productList: data
      })
    })
  }

  render () {
    const { keyWords, selected, productList } = this.state
    return (
      <View className='settlement'>
            <View className='header-search'>
                    <AtSearchBar
                        value={keyWords}
                        placeholder={'商品搜索'}
                        onChange={this.onChange.bind(this)}
                        onActionClick={this.onActionClick.bind(this)}
                    />
            </View>
            <ScrollView
                scrollY
                className='home__wrap'
                onScrollToLower={this.loadRecommend}
                style={{ height: getWindowHeight()}}
                >
                <ClassList selected={selected} list={productList} ></ClassList>
            </ScrollView>
      </View>
    )
  }
}
