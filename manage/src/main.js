import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/font_alimama/iconfont.css';
import request from "@/utils/request";


Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL
Vue.config.productionTip = false
export const eventBus = new Vue();
Vue.use(ElementUI,{size:'small'});
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')


