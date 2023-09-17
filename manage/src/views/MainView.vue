<template>
  <div>
    <el-container>
      <!--   侧边栏  -->
      <el-aside :width="asideWidth" style="min-height: 100vh; background-color:#011635;border-radius: 12px">
        <div style="height:60px; line-height:60px; color : white; display:flex;align-items:center;
        justify-content: center;">
          <!--  logo    -->
          <img src="@/assets/logo1.png" alt="" style="width: 40px; height: 40px;" />
          <span class="logo-title" v-show="!isCollapse">C.M.System</span>
        </div>

        <el-menu :collapse="isCollapse" :collapse-transition="false" router background-color="#011635"
                 text-color="rgba(255,255,255,0.65)" active-text-color="#fff" style="border: none"
                 :default-active='$route.path'>
          <el-menu-item index="/home">
            <i class="el-icon-house"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

          <el-menu-item index="/usermanage">
            <i class="el-icon-user"></i>
            <span  slot="title">用户管理</span>
          </el-menu-item>

          <el-menu-item  index="/chargemanage">
            <i class="el-icon-monitor"></i>
            <span slot="title">充电桩管理</span>
          </el-menu-item>

          <el-menu-item index="/order">
            <i class="el-icon-wallet"></i>
            <span slot="title">订单管理</span>
          </el-menu-item>

          <el-submenu>
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/notice">通知信息</el-menu-item>
            <el-menu-item index="/admininfo">管理员信息</el-menu-item>
          </el-submenu>
        </el-menu>


      </el-aside>


      <el-container>
        <!--   头部区域  -->
        <el-header>
          <div class="box_bgd">
            <i :class="collapseIcon" style="font-size: 26px; align-items: center;" @click="handleCollapse"></i>
          </div>

          <div style="flex: 1; margin-top: 34px; display: flex; align-items: center; margin-left: 20px">
            <el-tabs class="custom-tabs" v-model="editableTabsValue" type="card" closable @tab-remove="removeTab">
              <el-tab-pane
                  v-for="(item, index) in editableTabs"
                  :key="item.name"
                  :label="item.title"
                  :name="item.name"
              >

              </el-tab-pane>
            </el-tabs>
          </div>


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

          <router-view></router-view>


        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

export default {
  name: "HomeView",
  data() {
    return {
      isCollapse: true, // 收缩
      asideWidth: "64px",
      collapseIcon: 'el-icon-s-unfold',


      editableTabsValue: '2',
      editableTabs: [{
        title: 'Tab 1',
        name: '1',
        content: 'Tab 1 content'
      }, {
        title: 'Tab 2',
        name: '2',
        content: 'Tab 2 content'
      }],
      tabIndex: 2
    }
  },
  methods: {
    // 动态控制展开与收起和切换对应图标
    handleCollapse() {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    },



    addTab(targetName) {
      let newTabName = ++this.tabIndex + '';
      this.editableTabs.push({
        title: 'New Tab',
        name: newTabName,
        content: 'New Tab content'
      });
      this.editableTabsValue = newTabName;
    },
    removeTab(targetName) {
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }

      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter(tab => tab.name !== targetName);
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
  transition: none !important;
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
  transition: none !important;
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
  transition: none !important;

}
.el-menu--inline .el-menu-item.is-active{
  padding-left: 45px !important;
  transition: none !important;
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
  border-radius: 8px !important;
}

.el-card__body, .el-main {
  padding: 15px !important;
}

<!--标签页-->
    .custom-tabs .el-tabs__item {
      border-radius: 8px !important;
    }
.custom-tabs .el-tabs__active-bar {
  border-radius: 8px !important;
}
.custom-tabs .el-tabs__item.is-active {
  border-bottom-left-radius: 8px !important;;
  border-bottom-right-radius: 8px !important;;
}
</style>