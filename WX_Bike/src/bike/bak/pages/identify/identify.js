// pages/identify/identify.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
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
  
  },

  formSubmit: function (e) {
    //获取全局变量的数据
    var globalData = getApp().globalData;
    var phoneNum = globalData.phoneNum;
    var name = e.detail.value.name
    var idNum = e.detail.value.idNum
    wx.request({
      url: "http://localhost:8888/identify",
      header: { 'content-type': 'application/x-www-form-urlencoded' },
      data: {
        phoneNum: phoneNum,
        name: name,
        idNum: idNum,
        status: 3
      },
      method: 'POST',
      success: function (res) {
        wx.hideLoading();
        //更新全局变量中的status属性
        globalData.status = 3
        wx.navigateTo({
          url: '../index/index',
        });
      }
    })
  }


})