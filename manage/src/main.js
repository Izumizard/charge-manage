import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/font_alimama/iconfont.css';
import axios from 'axios'
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(ElementUI,{size:'small'});
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
