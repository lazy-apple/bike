function getZoneName(lat, lng) {
  var qqMapWX = require('../libs/qqmap-wx-jssdk.js');

  // 实例化API核心类
  var qqMap = new qqMapWX({
    key: 'EBMBZ-N5FWU-JYOVP-B3LKB-63JCQ-XBFHT' // 必填
  });

  // 调用接口
  
  qqMap.reverseGeocoder({
    location: {
      latitude: lat,
      longitude: lng
    },
    success: function (res) {
      return res;
    }
  });
}

module.exports = {
  getZoneName
}