import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from "@/views/MainView";
import HomeView from "@/views/HomeView";
import UserManageView from "@/views/UserManageView";
import ChargeManageView from "@/views/ChargeManageView";
import OrderManageView from "@/views/OrderManageView";
import AdminstratorView from "@/views/AdminstratorView";
import NoticeManageView from "@/views/NoticeManageView";

Vue.use(VueRouter)

// 修改 Router.prototype.push 方法
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: "/",
    redirect:"home",
    name: "main",
    component: MainView,
    children : [
      { path:'home', name:'home', component:HomeView, meta:{ keepAlive:true, comp:HomeView, title:'首页'} },
      { path:'usermanage',name:'usermanage',component: UserManageView, meta:{ keepAlive:true, comp:UserManageView, title:'用户管理'}},
      { path:'chargemanage', name: 'chargemanage', component:ChargeManageView, meta:{ keepAlive:true, comp:ChargeManageView, title:'充电管理'}},
      { path:'order', name: 'order', component:OrderManageView, meta:{ keepAlive:true, comp:OrderManageView, title:'订单管理'}},
      { path:'notice', name: 'notice', component:NoticeManageView, meta:{ keepAlive:true, comp:NoticeManageView, title:'通知信息'}},
      { path:'admininfo', name: 'admininfo', component:AdminstratorView, meta:{ keepAlive:true, comp:AdminstratorView, title:'管理员信息'}},
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
