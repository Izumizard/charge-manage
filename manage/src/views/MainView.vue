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
            <el-tabs
                v-model="activeTab"
                type="card"
                closable
                @tab-remove="removeTab"
                @tab-click="clickBtn"
            >
              <el-tab-pane
                  :key="index"
                  v-for="(item, index) in tabList"
                  :label="item.title"
                  :name="item.path"
              >
                {{ item.content }}
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

          <transition name="fade" mode="out-in">
            <keep-alive>
              <router-view v-if="$route.meta.keepAlive"></router-view>
            </keep-alive>
          </transition>
          <router-view v-if="!$route.meta.keepAlive"></router-view>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

import store from '../../src/store'
export default {
  name: "HomeView",
  data() {
    return {
      isCollapse: true, // 收缩
      asideWidth: "64px",
      collapseIcon: 'el-icon-s-unfold',


      activeTab: '',
    }
  },


  components: {},
  computed: {
    tabList() {
      return store.getters['getTabs']
    },
  },
  watch: {
    $route: function () {
      this.setActiveTab()
      this.addTab()
    },
  },
  created() {
  },
  mounted() {
    this.beforeRefresh()
    this.setActiveTab()
    this.addTab()
  },


  methods: {
    // 动态控制展开与收起和切换对应图标
    handleCollapse() {
      this.isCollapse = !this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
      this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
    },
    // 设置活跃的tab
    setActiveTab() {
      this.activeTab = this.$route.path
    },
    // 添加tab
    addTab() {
      const {path, meta} = this.$route
      const tab = {
        path,
        title: meta.title,
      }
      store.commit('addTab', tab)
    },
    // 点击tab
    clickBtn(tab) {
      const {name} = tab
      this.$router.push({path: name})
    },
    // 删除tab
    removeTab(target) {
      let active = this.activeTab;
      const tabs = this.tabList;

      // 查找目标标签页的索引
      const index = tabs.findIndex((tab) => tab.path === target);

      if (index !== -1) {
        // 移除目标标签页
        tabs.splice(index, 1);

        if (tabs.length === 0) {
          // 如果标签页列表为空，则跳转到首页
          this.$router.push('/home');
        } else if (active === target) {
          // 删除当前激活的标签页时，将活跃标签页切换到上一个或下一个标签页
          const nextTab = tabs[index] || tabs[index - 1];
          if (nextTab) {
            active = nextTab.path;
          } else {
            active = '/home'; // 如果没有上一个或下一个标签页，则将首页设为活跃标签页
          }
        }
      }

      // 更新当前激活的选项卡和选项卡列表
      this.activeTab = active;
      store.state.tabList = tabs.filter((tab) => tab.path !== target);
    },
    // 解决刷新数据丢失问题
    beforeRefresh() {
      window.addEventListener('beforeunload', () => {
        sessionStorage.setItem('tabsView', JSON.stringify(this.tabList))
      })
      let tabSession = sessionStorage.getItem('tabsView')
      if (tabSession) {
        let oldTabs = JSON.parse(tabSession)
        if (oldTabs.length > 0) {
          store.state.tabList = oldTabs
        }
      }
    },
  },
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

.el-tabs__item:hover .el-icon-close:before {
  display: inline-block !important;
}
.el-tabs__item .el-icon-close:before {
  display: none !important;
}
</style>