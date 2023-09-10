<template>
  <div>
    <el-container>
      <!--   侧边栏  -->
      <el-aside :width="asideWidth" style="min-height: 100vh; background-color:#011635;">
       <div style="height:60px; line-height:60px; color : white; display:flex;align-items:center;justify-content: center;">
         <!--  logo    -->
         <img src="@/assets/logo1.png" alt="" style="width: 40px; height: 40px;" />
         <span class="logo-title" v-show="!isCollapse">C.M.System</span>
       </div>

        <el-menu :collapse="isCollapse" :collapse-transition="false" router background-color="#011635" text-color="rgba(255,255,255,0.65)" active-text-color="#fff" style="border: none" :default-active='$route.path'>
          <el-menu-item index="/">
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

            <el-menu-item index="">
              <i class="el-icon-user"></i>
              <span  slot="title">用户管理</span>
            </el-menu-item>

            <el-menu-item  index="">
              <i class="el-icon-monitor"></i>
              <span slot="title">充电桩管理</span>
            </el-menu-item>

          <el-menu-item index="">
            <i class="el-icon-wallet"></i>
             <span slot="title">订单管理</span>
          </el-menu-item>

          <el-submenu>
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统设置</span>
            </template>
            <el-menu-item>通知信息</el-menu-item>
            <el-menu-item index="/">管理员信息</el-menu-item>
          </el-submenu>
        </el-menu>


      </el-aside>


      <el-container>
        <!--   头部区域  -->
        <el-header>
          <div class="box_bgd">
            <i :class="collapseIcon" style="font-size: 26px" @click="handleCollapse"></i>
          </div>
          <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px; margin-top:8px;">
            <el-breadcrumb-item :to="{ path: '/main' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path: '/user' }">用户管理</el-breadcrumb-item>
          </el-breadcrumb>

          <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: flex-end;">
            <el-dropdown placement="bottom">
              <div style="display: flex; align-items: center; cursor: default">
              <img src="@/assets/img1.png" alt="" style="width: 40px; height: 40px; margin: 0 5px">
              <span style="margin-left: 5px !important">管理员</span>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item><i class="el-icon-admin" ></i>个人信息</el-dropdown-item>
                <el-dropdown-item><i class="el-icon-edit"></i>修改密码</el-dropdown-item>
                <el-dropdown-item @click=""><i class="el-icon-out"></i>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <!--  主体区域  -->
        <el-main>
        <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding:10px 20px; border-radius: 5px; margin-bottom: 20px;">
          <i class="el-icon-guangbo" style="margin-right: 5px"></i> 本系统仅供内部测试使用，不做商业用途。
        </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

export default {
  name:"HomeView",
  data(){
    return {
      isCollapse: true, // 收缩
      asideWidth: "64px",
      collapseIcon:'el-icon-s-unfold'
      }
  },
  methods: {
    // 动态控制展开与收起和切换对应图标
    handleCollapse() {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    }
  }
}


</script>

<style>
.el-menu--inline{
  background-color: #041322 !important;
}
.el-menu--inline .el-menu-item{
  background-color: #041322 !important;
  padding-left: 49px !important;
}
.el-menu-item:hover,.el-submenu__title:hover,.el-submenu__title:hover i,.el-menu-item:hover i {
  color:#fff !important;
}
.el-menu-item.is-active {
  background-color: #1890ff !important;
  border-radius:8px !important;
  width: calc(100% - 8px);
  margin-left: 4px;
}
.el-menu-item.is-active i, .el-menu-item.is-active .el-tooltip {
  margin-left: -4px;
}
.el-menu-item {
  height: 40px !important;
  line-height: 40px !important;
}
.el-submenu__title{
  height: 40px !important;
  line-height: 40px !important;
}
.box_bgd {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  width: 40px;
  margin-top: 8px;

}
.el-submenu .el-menu-item {
  min-width: 0 !important;

}
.el-menu--inline .el-menu-item.is-active{
  padding-left: 45px !important;
}
.el-aside {
  transition: width .4s;
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
}
.box_bgd:hover {
  color:#cb99c9 !important;
}
.logo-title{
  margin-left: 18px !important;
  font-size: 20px !important;
  transition: all .4s;  /* 0.4s */
}

.el-header {
  box-shadow: 2px 0 6px rgba(0,21,41,.35);
  display: flex;
  align-items: center;
}
</style>