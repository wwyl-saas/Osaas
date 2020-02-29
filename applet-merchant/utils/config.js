const server = {
  pro: {
    url: 'https://b.wanwuyoulian.com ',
  },
  test: {
    url: 'https://test-b.wanwuyoulian.com',
  },
  dev: {
    url: 'http://localhost:8081',
  }
}
const server1 = {
  pro: {
    url: 'wss://b.wanwuyoulian.com ',
  },
  test: {
    url: 'wss://test-b.wanwuyoulian.com',
  },
  dev: {
    url: 'ws://localhost:8081',
  }
}
export default {
  sendLink() {
    let app = getApp()
    let env = app.build.env
    return server[env].url
  },
  socketLink() {
    let app = getApp()
    let env = app.build.env
    return server1[env].url
  }
}