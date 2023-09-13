<template>
  <div>
  <div style="background-color: white; padding:10px 20px; border-radius: 5px;">
    <div class="model_mg">
      <el-tooltip :content="'表格斑马纹'" placement="top">
        <el-switch
          class="switch_cl"
          v-model="stripechange"
          active-color="#1677ff"
          inactive-color="#bfbfbf"
          :active-value="true"
          :inactive-value="false"
          active-text="开"
          inactive-text="关"
          @change="stripeChange"
        >
        </el-switch>
      </el-tooltip>
    <el-button class="button_pr"type="primary"  icon="el-icon-plus">新 建</el-button>
    <el-button type="danger" icon="el-icon-delete" style="color: #fff">删 除</el-button>
    </div>
  </div>
    <div>
  <el-table
      :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
      stripe:false
      style="width: 100%;margin-top: 15px;" max-height="250"
      v-loading="loading"
      element-loading-text="正在加载用户数据"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0,0, 0.8)"
      >
    <el-table-column
        type="selection"
        width="55"
        align="center"
    >
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
        label="电话"
        prop="phone"
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
        align="center">
    </el-table-column>
    <el-table-column
        label="头像"
        prop="avatar"
    align="center">
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
      <template slot-scope="scope"class=".button-container">
        <el-button
            size="small"
            plain
            @click="handleEdit(scope.$index, scope.row)">编 辑</el-button>
        <span style="margin-right: 20px;"></span>
        <el-button
            size="small"
            type="danger"
            plain
            style="margin-left: 0"
            @click="handleDelete(scope.$index, scope.row)">删 除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </div>
</div>
</template>

<script>
export default {
  name: "UserManageView",

  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '刘小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      search: '',
      loading: false,
      StripeChange:true
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },

  // stripeChage() {
  //     this.stripeChage() = ! this.strpie()
  // }


  },
}
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
  font-size: 5px !important;
}



.button_pr{
  background-color: rgba(22,119,255,1) !important;
  color: #fff !important;
  margin-right: 15px !important;
}
.button_pr:hover{
  background-color: rgba(22, 119, 255, 0.8) !important;
}
</style>