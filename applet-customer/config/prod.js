const isH5 = process.env.CLIENT_ENV === 'h5'

const HOST = '"https://www.wanwuyoulian.com"'
const HOST_SOCKET = '"www.wanwuyoulian.com"'
const APPID = 1

// XXX 搭了个 proxy 用于演示 prod 环境的 H5
const HOST_H5 = '"https://www.wanwuyoulian.com"'
const HOST_M_H5 = '"https://www.wanwuyoulian.com"'

module.exports = {
  env: {
    NODE_ENV: '"production"'
  },
  defineConstants: {
    HOST: isH5 ? HOST_H5 : HOST,
    HOST_SOCKET: isH5 ? HOST_M_H5 : HOST_SOCKET,
    APPID: APPID
  },
  weapp: {},
  h5: {
    publicPath: '/customer'
    /**
     * 如果h5端编译后体积过大，可以使用webpack-bundle-analyzer插件对打包体积进行分析。
     * 参考代码如下：
     * webpackChain (chain) {
     *   chain.plugin('analyzer')
     *     .use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin, [])
     * }
     */
  }
}
