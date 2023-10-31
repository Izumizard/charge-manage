<template>
  <transition
      name="animate__animated animate__bounce"
      enter-active-class="animate__rollIn"
      leave-active-class="animate__rollOut"
      appear
  >
  <div>
    <el-container>
      <!--   侧边栏  -->
      <transition
          name="animate__animated animate__bounce"
          enter-active-class="animate__slideInUp"
          leave-active-class="animate__slideOutDown"
          appear
      >
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
            <span slot="title">充电站管理</span>
          </el-menu-item>

          <el-menu-item index="/orders">
            <i class="el-icon-wallet"></i>
            <span slot="title">订单管理</span>
          </el-menu-item>

          <el-submenu  index="">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/notice">通知信息</el-menu-item>
            <el-menu-item index="/admininfo">管理员信息</el-menu-item>
            <el-menu-item index="/logs">系统日志</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      </transition>

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

        <transition
            name="animate__animated animate__bounce"
            enter-active-class="animate__slideInUp"
            leave-active-class="animate__slideOutDown"
            appear>
          <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: flex-end;">
            <el-dropdown placement="bottom">
              <div style="display: flex; align-items: center; cursor: default">
                <img :src="user.avatar || 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'" alt="" style="width: 40px; height: 40px; margin: 0 5px;
                border-radius: 12px;
                overflow: hidden">
                <span style="margin-left: 5px !important">{{user.username || "管理员" }}</span>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="myInfoClick"><i class="el-icon-admin"></i>个人信息</el-dropdown-item>
                <el-dropdown-item @click.native="ChangePwdClick" ><i class="el-icon-edit" ></i>修改密码</el-dropdown-item>
                <el-dropdown-item  @click.native="logout"><i class="el-icon-out"></i><span>退出登录</span></el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </transition>
        </el-header>
        <!--  主体区域  -->
        <el-main>

          <!--  悬浮设置按钮 -->
          <div class="settingBtnStyle">
            <button class="settingBtn" @click="gotoSetting">
              <i class="el-icon-setting" style="font-size: 24px; color: #fff"></i>
            </button>
          </div>


            <keep-alive>
              <router-view v-if="$route.meta.keepAlive" @update:user="updateUser"></router-view>
            </keep-alive>


        </el-main>

      </el-container>
    </el-container>
  </div>
</transition>
</template>

<script>
import 'animate.css';
import store from '../../src/store'
window.onload = function() {
  const settingBtnStyle = document.querySelector('.settingBtnStyle');
  const settingBtn = document.querySelector('.settingBtn');

  let isDragging = false;
  let startY, currentY, offsetY;

  if (settingBtnStyle) {
    settingBtnStyle.classList.add('hidden'); // 隐藏按钮
    settingBtnStyle.addEventListener('mouseenter', () => {
      settingBtnStyle.classList.remove('hidden'); // 鼠标进入按钮时移除隐藏样式
    });

    settingBtnStyle.addEventListener('mouseleave', () => {
      if (!isDragging) {
        settingBtnStyle.classList.add('hidden'); // 鼠标移出按钮时添加隐藏样式
      }
    });
    settingBtn.addEventListener('mousedown', (e) => {
      isDragging = true;

      startY = e.clientY;
      offsetY = settingBtnStyle.offsetTop;
    });
    window.addEventListener('mousemove', (e) => {
      if (!isDragging) {
        return;
      }

      currentY = e.clientY;
      const distanceY = currentY - startY;
      settingBtnStyle.style.top = `${offsetY + distanceY}px`;
    });

  }


  window.addEventListener('mouseup', () => {
    isDragging = false;
  });
}

export default {
  name: "MainView",
  inject: ['reload'],
  data() {
    return {
      isCollapse: true, // 收缩
      asideWidth: "64px",
      collapseIcon: 'el-icon-s-unfold',
      user:JSON.parse(localStorage.getItem('manage_config') || '{}'),
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
    updateUser(user){      //获取子组件传过来的数据，更新当前页面的数据
      this.user = JSON.parse(JSON.stringify(user))    //让父级的对象跟子级的对象毫无关联
    },
    myInfoClick(){
      this.$router.push({path: '/admininfo', query:{ tab:'myInfo'}});
    },
    ChangePwdClick() {
      this.$router.push({ path: '/admininfo', query: { tab: 'changePwd' } });
    },
    gotoSetting(){
      this.reload();
    },
    //退出登录并清理token和用户数据
    logout(){
      localStorage.clear()
      sessionStorage.clear()
      this.$message({
        message:"您已成功退出管理系统！",
        type:"success"
      })
      setTimeout( () =>{
        this.$router.push('/login')
      },3000)
    },
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
    async removeTab(target) {
      const tabs = this.tabList;
      const index = tabs.findIndex((tab) => tab.path === target);

      if (index !== -1) {
        tabs.splice(index, 1);

        if (tabs.length === 0) {
          // 如果标签页列表为空，则跳转到首页
          await this.$router.push('/home');
        } else if (target === this.activeTab) {
          // 删除当前激活的标签页时，将活跃标签页切换到上一个标签页
          const prevTab = tabs[Math.max(index - 1, 0)];
          this.activeTab = prevTab.path;
          await this.$router.push(prevTab.path);
        }

        store.state.tabList = tabs.filter((tab) => tab.path !== target);
      }

      // 返回一个空的 Promise，以便遗留下的事件处理程序能够正常执行
      return Promise.resolve();
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
.settingBtnStyle {
  position: fixed;
  top: 50%;
  right: 10px;
  bottom: 20px;
  z-index: 999;
  justify-content: flex-end;
  align-items: center;
  transition: transform 0.6s ease-in-out; /* 增加过渡效果的持续时间 */
}
.settingBtn {
  display: block;
  width: 42px;
  height: 42px;
  border: none;
  background-color: #409EFF;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  transition: all 0.3s ease-in-out;
}
.settingBtn:hover {
  transform: scale(1.1);
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.2);
}
.hidden {
  transform: translateX(75%); /* 隐藏在侧边 */
}


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
  animation: bounce;
  animation-duration: 2s;

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
