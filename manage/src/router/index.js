import Vue from 'vue'
import VueRouter from 'vue-router'
import MainView from "@/views/MainView";
import HomeView from "@/views/HomeView";
import UserManageView from "@/views/UserManageView";
import ChargeManageView from "@/views/ChargeManageView";
import OrderManageView from "@/views/OrderManageView";
import AdminstratorView from "@/views/AdminstratorView";
import noticeManageView from "@/views/NoticeManageView";

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
      { path:'home', name:'home',component:HomeView },
      { path:'usermanage',name:'usermanage',component: UserManageView },
      { path:'chargemanage', name: 'chargemanage', component:ChargeManageView },
      { path:'order', name: 'order', component:OrderManageView },
      { path:'notice', name: 'notice', component:noticeManageView },
      { path:'admininfo', name: 'admininfo', component:AdminstratorView},
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
