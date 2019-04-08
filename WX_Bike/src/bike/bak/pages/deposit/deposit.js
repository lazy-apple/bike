// pages/deposit/deposit.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },
  
  deposit: function () {
    var that = this;
    wx.showModal({
      title: '提示',
      content: '是否要充值押金？',
      confirmText: '确认',
      success: function (res) {
        //模拟加载的动画
        wx.showLoading({
          title: '充值中...',
        })
        var phoneNum = getApp().globalData.phoneNum;
        wx.request({
          url: "http://localhost:8888/deposit",
          header: { 'content-type': 'application/x-www-form-urlencoded' },
          data: {
            phoneNum: phoneNum,
            deposit: 299,
            status: 2
          },
          method: 'POST',
          success: function (res) {
            wx.hideLoading();
            wx.navigateTo({
              url: '../identify/identify',
            });
          }
        })
      }
    })
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    //页面加载后记录log
    //打印log
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})