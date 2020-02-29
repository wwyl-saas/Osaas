// NOTE H5 端使用 devServer 实现跨域，需要修改 package.json 的运行命令，加入环境变量
const isH5 = process.env.CLIENT_ENV === 'h5'
const HOST = '"https://test-c.wanwuyoulian.com"'
const HOST_SOCKET = '"test-c.wanwuyoulian.com"'
// const HOST = '"http://localhost:8080"'
// const HOST_SOCKET = '"localhost:8080"'
const APPID = 1

module.exports = {
  env: {
    NODE_ENV: '"development"'
  },
  defineConstants: {
    HOST: isH5 ? '"/api"' : HOST,
    HOST_SOCKET: isH5 ? '"/api-m"' : HOST_SOCKET,
    APPID: APPID
  },
  weapp: {},
  h5: {
    devServer: {
      proxy: {
        '/api/': {
          target: JSON.parse(HOST),
          pathRewrite: {
            '^/api/': '/'
          },
          changeOrigin: true
        },
        '/api-m/': {
          target: JSON.parse(HOST_SOCKET),
          pathRewrite: {
            '^/api-m/': '/'
          },
          changeOrigin: true
        }
      }
    }
  }
}
