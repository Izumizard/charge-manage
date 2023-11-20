import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from "@/views/MainView";
import HomeView from "@/views/manage/HomeView";
import UserManageView from "@/views/manage/UserManageView";
import ChargeManageView from "@/views/manage/ChargeManageView";
import OrderManageView from "@/views/manage/OrderManageView";
import AdminstratorView from "@/views/systemManage/AdminstratorView";
import NoticeManageView from "@/views/systemManage/NoticeManageView";
import { MessageBox } from 'element-ui';
import 'element-ui/lib/theme-chalk/message-box.css'
import login from "@/views/Login";
import LogsView from "@/views/systemManage/LogsView";



Vue.use(VueRouter)

// 修改 Router.prototype.push 方法
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: "/",
    redirect:'/home',
    name: "main",
    component: MainView,
    children : [
      {
        path:'home',
        name:'home',
        component:HomeView,
        meta:{
          keepAlive:true,
          comp:HomeView,
          title:'首页'
        }
      },
      {
        path:'usermanage',
        name:'usermanage',
        component: UserManageView,
        meta: {
          keepAlive:true,
          comp:UserManageView,
          title:'用户管理'
          }
        },
      {
        path:'chargemanage',
        name: 'chargemanage',
        component:ChargeManageView,
        meta:{
          keepAlive:true,
          comp:ChargeManageView,
          title:'站点管理'
          }
        },
      {
        path:'orders',
        name: 'orders',
        component:OrderManageView,
        meta:{
          keepAlive:true,
          comp:OrderManageView,
          title:'订单管理'
          }
        },
      {
        path:'notice',
        name: 'notice',
        component:NoticeManageView,
        meta:{
          keepAlive:true,
          comp:NoticeManageView,
          title:'通知信息'
          }
        },
      {
        path:'admininfo',
        name: 'admininfo',
        component:AdminstratorView,
        meta:{
          keepAlive:true,
          comp:AdminstratorView,
          title:'管理员信息'
          }
        },
      {
        path:'logs',
        name: 'logs',
        component:LogsView,
        meta:{
          keepAlive:true,
          comp:LogsView,
          title:'系统日志'
        }
      },
    ]
  },
  {
    path:"/login", name:'login', component: login,
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


/**MessageBox css自定义样式
 *动态创建style标签，将自定义样式添加到页面中
 **/
const styleNode = document.createElement('style');
styleNode.setAttribute('type', 'text/css');  // 设置style标签的type属性为text/css
styleNode.innerHTML = `
  .login-message .el-button{
    border-radius:14px !important;
  }
  .el-message-box{
    border-radius: 14px !important;
    width: 240px !important;
}
  `;
document.head.appendChild(styleNode); // 将style标签添加到head标签中

// 路由导航守卫
router.beforeEach((to,form,next)=>{
  /**
   * to 将要访问的路径
   *  from 代表从哪个路径跳转来的
   *  next 函数 表示放行
   */
  let token = localStorage.getItem('manage_config');
  if (to.path !== '/login' && !token) {
    MessageBox.alert('请登录！', '提示', {
      confirmButtonText: '确定',
      type: 'error',
      showClose:false,
      customClass:"login-message",
      callback: () => {
        next('/login');
      },
    });
  } else {
    next();
  }
})


export default router
