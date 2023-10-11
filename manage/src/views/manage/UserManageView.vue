<template>
  <div>
  <div style="background-color: white; padding:10px 20px; border-radius: 8px;">
    <div class="model_mg">
      <el-tooltip :content="'表格斑马纹'" placement="top">
        <el-switch
          class="switch_cl"
          v-model="Striped"
          active-color="#1677ff"
          inactive-color="#bfbfbf"
          :active-value="true"
          :inactive-value="false"
          active-text="开"
          inactive-text="关"
          @change="changeStriped"
        >
        </el-switch>
      </el-tooltip>
    <el-button class="button_pr" type="primary"  icon="el-icon-plus">新 建</el-button>
    <el-button type="danger" icon="el-icon-delete" style="color: #fff">删 除</el-button>
    </div>
  </div>
    <div>
  <el-table
      :data="users"
      :stripe="Striped"
      style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
      box-shadow: none !important;"
      max-height="500"
      v-loading="loading"
      @cell-click="openDetails"
      element-loading-text="正在加载用户数据"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0,0, 0.6)"
      >
    <el-table-column
        type="selection"
        width="55"
        align="center"
    >
    </el-table-column>

    <el-table-column
        label="ID"
        prop="id"
        align="center">
    </el-table-column>
    <el-table-column
        label="头像"
        prop="avatar"
        align="center">
    </el-table-column>
    <el-table-column
        label="用户名"
        prop="username"
        align="center">
    </el-table-column>
    <el-table-column
        label="姓名"
        prop="name"
        align="center">
    </el-table-column>
    <el-table-column
        label="电子邮箱"
        prop="email"
        align="center">
    </el-table-column>
    <el-table-column
        label="地址"
        prop="address"
        align="center"
        show-overflow-tooltip
    >
    </el-table-column>
    <el-table-column
        label="角色权限"
        prop="role"
        align="center"
    >
    </el-table-column>
    <el-table-column>
      <template slot="header" slot-scope="scope">
        <el-input
            v-model="search"
            size="medium"
            placeholder="输入关键字搜索">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </template>




      <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center ">
        <el-button
            size="small"
            plain
            class="editBtn"
            @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">
          编 辑</el-button>

        <el-popconfirm
            icon="el-icon-info"
            icon-color="red"
            title="确定删除这条用户信息？"
        >
        <el-button
            size="small"
            type="danger"
            plain
            slot="reference"
            class="deleteBtn"
            @click="handleDelete(scope.$index, scope.row);  $event.stopPropagation()">
          删 除</el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>


      <!-- 弹窗 -->
      <el-dialog v-if="showDialog" :visible.sync="showDialog"
                 title="用户详情" :show-close="false" :center="true" class="dialog_cl">

          <div style="display: flex; justify-content: center; align-items: center; margin-top: 5px;">
            <img :src="showRow.avatar" style="width: 100px; height: 100px;">
          </div>
        <div style="padding-left: 20px;">
        <el-form :model="showRow" label-width="100px" style="margin-top: 20px !important;">
          <el-form-item label="用户名">
            <template v-slot:label>
              <div class="label-cl">
                <i class="el-icon-user icon" style="margin-right: 20px;"></i>
                <span class="label-text">用户名:</span>
              </div>
            </template>
            {{showRow.username}}
          </el-form-item>
          <el-form-item label="用户电话">
            <template v-slot:label>
              <div class="label-cl">
                <i class="el-icon-phonenumber icon" style="margin-right: 5px;"></i>
                <span class="label-text">用户电话:</span>
              </div>
            </template>
            {{showRow.phone}}
          </el-form-item>
          <el-form :model="showRow" label-width="100px">
            <el-form-item label="用户密码">
              <template v-slot:label>
                <div class="label-cl">
                  <i class="el-icon-lock icon" style="margin-right: 5px;"></i>
                  <span class="label-text">用户密码:</span>
                </div>
              </template>
              <div class="password-container">
                {{ showPassword ? showRow.password : "******" }}
                <i class="el-icon-view password-icon"  :class="{ active: showPassword }"
                   @click="clickSeePassword"></i>
              </div>
              <el-popover
                  v-model="popoverVisible"
                  title="请输入超级管理员密码"
                  width="200"
                  trigger="click"
              >
                <el-input
                    v-model="adminPassword"
                    type="password"
                    placeholder="请输入超级管理员密码"
                    clearable
                ></el-input>
                <div class="popover-footer">
                  <el-button type="primary" @click="confirmAdminPassword">确认</el-button>
                </div>
              </el-popover>
            </el-form-item>
          </el-form>
          <el-form-item label="账户余额" style="color: rgb(0,188,18);">
            <template v-slot:label>
              <div class="label-cl">
                <i class="el-icon-money icon" style="margin-right: 5px;"></i>
                <span class="label-text">账户余额:</span>
              </div>
            </template>
            <span>￥{{showRow.balance}}</span>
          </el-form-item>
        </el-form>
        </div>

      </el-dialog>

        </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "UserManageView",

  data() {
    return {
      showRow: {},
      users:[],
      search: '',
      loading: false,
      Striped: false,
      showDialog: false, // 弹窗的显示与隐藏
      adminPassword: "", // 超级管理员密码
      popoverVisible: false, // 弹框是否可见
    }
  },



  mounted() {
    request.get('/user/selectAll').then(res => {
      console.log(res.data)
      this.users = res.data
    })
    // 在页面加载时从本地存储中读取选择状态
    const selection = localStorage.getItem('changeStriped');
    if (selection !== null) {
      this.Striped = JSON.parse(selection);
    }
  },

  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },

    changeStriped() {
      this.isStriped = !this.isStriped;
      // 在选择switch时将选择状态保存到本地存储
      localStorage.setItem('changeStriped', JSON.stringify(this.Striped));
    },


    openDetails (row, column, cell, event){
      if (column.type === 'selection' || column.property === 'role') {
        event.stopPropagation(); // 阻止事件冒泡，不触发弹出窗口
      } else {
        // 处理其他单元格点击事件，弹出窗口等逻辑
        this.showRow = row;
        this.showDialog = true;
      }
    },
    clickSeePassword() {
      if (this.showPassword) {
        this.showPassword = false;
        this.adminPassword = ""; // 关闭密码显示时清空超级管理员密码
      } else {
        // 如果未输入超级管理员密码，则弹出确认框
        if (!this.adminPassword) {
          this.popoverVisible = true;
        } else if (this.adminPassword === "admin123") {
          this.showPassword = true;
        }
      }
    },
    confirmAdminPassword() {
      if (this.adminPassword === "admin123") {
        this.showPassword = true;
      }
      this.popoverVisible = false;
    },
  },
};


</script>


<style>


.el-input--medium{
  font-size: 14px !important;
}
.el-input--medium .el-input__inner {
  height: 28px !important;
}
.el-input__prefix{
  position: absolute;
  transform: translateY(-15%);
}
.model_mg{
  display: flex;
  align-items: center;
  justify-content: flex-end !important;
}
.switch_cl{
  overflow: hidden !important;
  border-radius: 100px !important;
  margin-right: 20px !important;
  transition:padding-inline-start 0.2s ease-in-out,padding-inline-end 0.3s ease-in-out;
}


.switch_cl .el-switch__label {
  position: absolute;
  display: none;

  color: #fff !important;
}
.el-switch__core{
  background-color: rgba(166, 166, 166, 1) ;
}
.switch_cl .el-switch__label--left {
  z-index: 9;
  left: 21px !important;
}
.switch_cl .el-switch__label--right {
  z-index: 9;
  left: -3px !important;
}
.switch_cl .el-switch__label.is-active {
  display: block;
}
.el-switch__label *{
  font-size: 1px !important;
}

.button-container{
  justify-content: space-between;
}
.editBtn {
  margin-right: 10px!important;
}

.deleteBtn {
  margin-left: 10px !important;
}
@media (max-width:800px) {
  .button-container {
    flex-direction: column;
  }
  .editBtn,
  .deleteBtn {
    margin:0 !important;
  }
}
.button_pr{
  background-color: rgba(22,119,255,1) !important;
  color: #fff !important;
  margin-right: 15px !important;
}
.button_pr:hover{
  background-color: rgba(22, 119, 255, 0.8) !important;
}

.el-dialog{
  width:20% !important;
  height:60% !important;
  border-radius: 12px !important;
}


.label-cl {
  display: flex;
  align-items: center;
}
.label-text {
  margin-left: 5px;
}
.password-container {
  position: relative;
}
.password-icon {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  color: #c0c4cc;
  cursor: pointer;
}
</style>