// pages/wallet/wallet.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isDeposit: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
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
        //3s关闭动画效果
        setTimeout(function () {
          getApp().globalData.isDeposit = true;
          wx.hideLoading();
          wx.navigateTo({
            url: '../index/index',
          });
        }, 3000)
        
        
      }
    })
  },

  recharge: function () {
    wx.navigateTo({
      url: '../pay/pay'
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
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