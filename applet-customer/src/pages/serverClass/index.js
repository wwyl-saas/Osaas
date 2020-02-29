import Taro, { Component } from '@tarojs/taro'
import { View, ScrollView } from '@tarojs/components'
import { AtSearchBar } from 'taro-ui'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import { getWindowHeight } from '../../utils/style'
import SelectClass from './selectClass'
import ClassList from './classList'
import './index.scss'

@connect(state => state.product, { ...actions })
export default class ServerClass extends Component {

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
    loading: true,
    hasMore: true,
    value: '',
    selected: 0,
    scrollTop: 0
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
    this.dispatchCategoryList()
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchCategoryList () {
    Taro.showLoading({title: '加载中...', mask: true})
    this.props.dispatchCategoryList().then(()=>{
      this.setState({
        loading: false
      })
      Taro.hideLoading()
    })
  }

  onChange (value) {
    this.setState({
      value: value
    })
  }

  onActionClick () {
    const { value } = this.state
    Taro.navigateTo({
      url: `/pages/categorySearch/index?name=${value}&selected={selected}`,
      events: {
      },
      success: function(res) {
        console.log(res)
      }
    })
  }

  selectClick = (index) => {
    const { selected } = this.state
    if (selected !== index) {
      const scrollTop = Math.random()
      console.log(scrollTop)
      this.setState({
        scrollTop: scrollTop
      })
    }
    this.setState({
        selected: index
    })
  }

  render () {
    const { categoryList } = this.props
    const { selected, scrollTop, loading } = this.state
    console.log(scrollTop)
    return (
      <View className='settlement'>
            <View className='header-search'>
                    <AtSearchBar
                      value={this.state.value}
                      placeholder='商品搜索'
                      onChange={this.onChange.bind(this)}
                      onActionClick={this.onActionClick.bind(this)}
                    />
                    <SelectClass
                      list={categoryList}
                      selected={selected}
                      onSelectClick={this.selectClick.bind(this)}
                    >
                    </SelectClass>
            </View>
            <ScrollView
              scrollY
              enableBackToTop
              scrollTop={scrollTop}
              className='home__wrap'
              onScrollToLower={this.loadRecommend}
              style={{ height: getWindowHeight()}}
            >
                <ClassList selected={selected} list={categoryList} ></ClassList>
            {!loading?<View className='noData'>
              {categoryList.length === 0 ? (
                <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
            </View>:null}
            </ScrollView>
      </View>
    )
  }
}
