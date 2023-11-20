import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css';
import ElementUI from 'element-ui';
import VueAMap from 'vue-amap';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/font_alimama/iconfont.css';
import request from "@/utils/request";


Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL
Vue.config.productionTip = false
export const eventBus = new Vue();
Vue.use(ElementUI,{size:'small'});
Vue.use(VueAMap);

VueAMap.initAMapApiLoader({
  key: '05c0d1b121be4c28eb4b2549b4de26cf',// 高德的key
  // 插件集合
  plugin: [
    "AMap.Geolocation", // 定位控件，用来获取和展示用户主机所在的经纬度位置
    "AMap.Geocoder", // 地理编码与逆地理编码服务，用于地址描述与坐标间的相互转换
    "AMap.Autocomplete",
    "AMap.PlaceSearch",
    "AMap.CitySearch",
  ],
  // 高德 sdk 版本，默认为 1.4.4
  v: '1.4.4'
});
//高德的安全密钥
window._AMapSecurityConfig = {
  securityJsCode:'da554a06cfb3b662721e24819689132a',
}
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')


