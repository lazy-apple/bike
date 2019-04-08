//app.js
App({
  onLaunch: function () {

    // 登录,然后获取到用户的唯一身份ID，用于以后记log
    wx.login({
      success: res => {
        //根据你的微信小程序的密钥到后台获取ID
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        if (res.code) {
          var appid = "wx5b603fddb2c1bca9";
          var secret = "ca6b9becf4ff51692f01fa86065ad35c";
          var code = res.code;
          //发起网络请求
          wx.request({
            url: 'https://api.weixin.qq.com/sns/jscode2session?appid=' + appid + '&secret=' + secret + '&js_code=' + code + '&grant_type=authorization_code',
            success: function (r) {
              //获取到每个用户的对立id
              //console.log(r.data.openid)
              //把openid保存到本地
              wx.setStorageSync('openid', r.data.openid)
            }
          })
        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    })
  },
  globalData: {
    openid: "",
    status: 0,
    balance: 0, //余额
    userInfo: null
  }
})