<template>
<div class="userSettingStyle">
  <el-tabs tab-position="left" v-model="activeName">
    <el-tab-pane label="我的信息" name="myInfo">
      <!-- 标头 -->
      <div class="settingStyleTitle">我的信息</div>
      <!-- 用户信息 -->
      <div style = "display: flex">
        <div class="userImageStyle">
          <div class="userImageSpan">用户头像</div>
          <div style="height: 160px; margin-bottom: 12px">
            <el-image
              style="width: 150px; height: 150px; border-radius: 12px"
              src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
              fit="fill"
            ></el-image>
          </div>
          <el-upload action="" style="line-height: 34px; height: 34px;margin-top: 150px">
            <el-button icon="el-icon-upload2" type="primary" plain
                       >更换头像</el-button>
          </el-upload>
        </div>
        <!-- 用户设置表单 -->
        <div class="userSettingForm">
        <el-form :model="user" label-width="80px" >
          <el-form-item label="用户名：" prop="username">
            <span>{{user.username}}</span>
          </el-form-item>
          <el-form-item label="姓名：" prop="name">
            <el-input v-model="user.name"></el-input>
          </el-form-item>
            <el-form-item label="电话：" prop="phone">
              <el-input v-model="user.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱：" prop="email">
              <el-input v-model="user.email"></el-input>
            </el-form-item>
            <el-form-item label="地址：" prop="address">
              <el-input v-model="user.address"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-check">提交</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-tab-pane>

    <el-tab-pane label="修改密码" name="changePwd">
      <!-- 标头 -->
      <div class="settingStyleTitle">修改密码</div>
    </el-tab-pane>
    <el-tab-pane label="账号绑定" name="accountBind">
      <!-- 标头 -->
      <div class="settingStyleTitle">账号绑定</div>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
export default {
  data() {

    return{
      activeName:'myInfo',
      user:JSON.parse(localStorage.getItem('manage_config') || "{}")
    }
  },
  mounted(){
    this.showtabs();//跳转到指定tab
  },
  methods: {
    //获取当前点击下标
    showtabs() {
      // 比较结果activeName的值
      if (this.$route.query.activeName != null) {
        this.activeName = this.$route.query.activeName;
      }
    }
  }
};

</script>

<style scoped>
.userSettingStyle {
  background-color:#fff;
  width: 100%;
  height: calc(100vh - 100px);
  border-radius: 12px;
  padding: 12px;
}
.el-tabs {
  border-left:0;
  height: 100%;
}
/deep/ .el-tabs__nav {
  width: 150px;
  height: 100%;
  overflow: hidden;
  position: relative;
}
/deep/ .el-tabs__nav .el-tabs__item {
  font-size: 16px;
  text-align: left !important;
  line-height: 40px;
  padding-left: 20px;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

/deep/ .el-tabs__nav .el-tabs__item.is-active{
  font-weight: 500;
  background-color: #e6f7ff;
  padding-left: 20px !important;
}
/deep/ .el-tabs__nav .el-tabs__active-bar {
  height: 40px !important;
}
/deep/ .el-tabs__nav .el-tabs__item:nth-child(2) {
  padding-left: 20px !important;
}
.settingStyleTitle {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 20px;
  line-height: 28px;
  text-align: left;
}
.userSettingForm{
  text-align: left;
  margin-left: 10px;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 100%;
}
.userSettingForm .el-form-item {
  line-height: 50px;
  max-width: 460px;
}
.userImageStyle {
  width: 300px !important;
  text-align: center;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  height: 650px;
}
.userImageSpan {
  line-height: 32px;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 500;
}
.el-image{
  margin-top: 120px;
}
.el-button{
  border-radius: 12px !important;
}

</style>