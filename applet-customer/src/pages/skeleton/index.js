import Taro, { Component } from '@tarojs/taro'
import { View } from '@tarojs/components'
import './index.scss'

// 约定.skeleton样式类为骨架屏查找绘制节点的根节点
// 约定.skeleton-square样式类，表示绘制当前节点的骨架节点样式为方形（如商品卡片）
// 约定.skeleton-circular样式类，表示绘制当前节点的骨架节点为圆形（如logo）
// 约定.skeleton-cylinder样式类，表示绘制当前节点的骨架节点为长条形（如搜索框）
// 约定.skeleton-light与.skeleton-dark为块元素背景骨架样式
class Skeleton extends Component {
  static defaultProps = {
    selector: 'skeleton',
    backgroundColor: '#fff',
    lightColor: 'white',
    darkColor: '#2f3333',
  }

  state = {
    lights: [],
    darks: [],
    squares: [],
    circulars: [],
    cylinders: [],
  }

  componentDidMount() {
    const { selector } = this.props

    Promise.all([
      this.selectAll(`.${selector} >>> .${selector}-light`),
      this.selectAll(`.${selector} >>> .${selector}-dark`),
      this.selectAll(`.${selector} >>> .${selector}-square`),
      this.selectAll(`.${selector} >>> .${selector}-circular`),
      this.selectAll(`.${selector} >>> .${selector}-cylinder`),
    ]).then(([lights, darks, squares, circulars, cylinders]) =>
      this.setState({
        lights,
        darks,
        squares,
        circulars,
        cylinders,
      }),
    )
  }

  selectAll = selector =>
    new Promise(resolve =>
      Taro.createSelectorQuery()
        .selectAll(selector)
        .boundingClientRect()
        .exec(res => resolve(res[0])),
    )

  createStyle = ({ width, height, top, left }) => ({
    width: `${width}px`,
    height: `${height}px`,
    top: `${top}px`,
    left: `${left}px`,
  })

  createCylinderStyle = rect => ({
    ...this.createStyle(rect),
    'border-radius': `${rect.height / 2}px`,
  })

  render() {
    const { backgroundColor, lightColor, darkColor } = this.props
    const { lights, darks, circulars, squares, cylinders } = this.state

    const skeletonStyle = { backgroundColor }

    return (
      <View
        className='skeleton'
        style={skeletonStyle}
      >
        {darks.map(dark => (
          <View
            key={`${dark.top}-${dark.left}`}
            className='item dark'
            style={{ ...this.createStyle(dark), backgroundColor: darkColor }}
          />
        ))}
        {lights.map(light => (
          <View
            key={`${light.top}-${light.left}`}
            className='item light'
            style={{ ...this.createStyle(light), backgroundColor: lightColor }}
          />
        ))}
        {squares.map(square => (
          <View
            key={`${square.top}-${square.left}`}
            className='item square'
            style={this.createStyle(square)}
          />
        ))}
        {circulars.map(circular => (
          <View
            key={`${circular.top}-${circular.left}`}
            className='item circular'
            style={this.createStyle(circular)}
          />
        ))}
        {cylinders.map(cylinder => (
          <View
            key={`${cylinder.top}-${cylinder.left}`}
            className='item cylinder'
            style={this.createCylinderStyle(cylinder)}
          />
        ))}
      </View>
    )
  }
}

export default  Skeleton
