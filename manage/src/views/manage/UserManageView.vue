<template>
  <div>
  <div style="background-color: white; padding:10px 20px; border-radius: 8px;">
    <!-- 表格斑马纹 -->
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
      <!-- 批量导入/导出 -->
  <el-upload :action="$baseUrl + '/user/import'" :headers="{ token: user.token}"
             :show-file-list="false"
             :on-success="handleImport"
             style="color: #fff; margin-right: 10px">
    <el-button type="primary" icon="el-icon-upload2">批量导入</el-button>
  </el-upload>
      <el-button type="primary" icon="el-icon-download" style="color: #fff" @click="exportData">批量导出</el-button>
      <!-- 新增/删除 -->
    <el-button class="button_pr" type="primary"  style="margin-right:0px !important" icon="el-icon-plus" @click="handleAdd">新建用户</el-button>
    <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatch">批量删除</el-button>
    </div>
  </div>
    <div>

  <!-- 用户信息表格  -->
  <el-table
      :header-cell-style="{ backgroundColor: '#d3e0f3', color:'#666'}"
      :data="users"
      :stripe="Striped"
      style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
      box-shadow: none !important;"
      v-loading="loading"
      @cell-click="openDetails"
      @selection-change="handleSelectionChange"
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
        width="55"
        prop="id"
        align="center">
    </el-table-column>
    <el-table-column
        label="头像"
        prop="avatar"
        align="center">
      <template v-slot="scope">
        <el-image
            style="width: 50px; height: 50px;border-radius: 8px"
            v-if="scope.row.avatar" :src="scope.row.avatar"
        :preview-src-list="[scope.row.avatar]"
        @click="$event.stopPropagation()"></el-image>
      </template>
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
    <el-table-column align="center" width="180">
      <template slot="header" slot-scope="scope">
        <el-input
            v-model="username"
            size="medium"
            @input="load(1)"
            placeholder="输入关键字搜索">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </template>




      <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">
        <el-button
            size="small"
            plain
            class="editBtn"
            @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">
          编 辑</el-button>

        <el-button
            size="small"
            type="danger"
            plain
            slot="reference"
            class="deleteBtn"
            @click="handleDelete(scope.$index, scope.row.id);  $event.stopPropagation()">
          删 除</el-button>
      </template>
    </el-table-column>
  </el-table>
      <div style="margin: 20px 0;
            display: flex; align-items: flex-end; justify-content: center">
        <el-pagination
            :background="true"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total,prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
      <!-- 用户信息编辑弹窗 -->
      <el-dialog :title="dialogTitle" custom-class="userDialog" :visible.sync="fromVisible">
        <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
          <el-form-item label="用户名：" prop="username">
            <el-input v-model="form.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item label="姓名：" prop="name">
            <el-input v-model="form.name"  placeholder="姓名"></el-input>
          </el-form-item>
          <el-form-item label="电话：" prop="phone">
            <el-input v-model="form.phone"  placeholder="电话"></el-input>
          </el-form-item>
          <el-form-item label="邮箱：" prop="email">
            <el-input v-model="form.email" placeholder="邮箱"></el-input>
          </el-form-item>
          <el-form-item label="地址：" prop="address">
            <el-input type="textarea" v-model="form.address" placeholder="居住地址"></el-input>
          </el-form-item>
          <el-form-item label="角色：" prop="role">
            <el-select  :disabled="isRoleDisabled"
                v-model="form.role" placeholder="请选择">
              <el-option
                  v-for="item in roles"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="头像：">
            <el-upload :action="$baseUrl + '/file/upload'"
                       :on-success="handleAvatarSuccess"
                       :headers="{token : user.token}"
                       :file-list="form.avatar? [form.avatar] : []"
                       list-type="picture"
                       style="line-height: 34px; height: 34px">
              <el-button icon="el-icon-upload" type="primary" plain>
                {{btnText}}
              </el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="margin-top: 50px !important">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>


      <!-- 用户详情弹窗 -->
      <el-dialog v-if="showDialog" :visible.sync="showDialog"
                 custom-class="userManagerdialog"
                 title="用户详情" :show-close="false" :center="true">

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
          <el-form-item label="账户余额" style="color: rgb(0,188,18);">
            <template v-slot:label>
              <div class="label-cl">
                <i class="el-icon-money icon" style="margin-right: 5px;"></i>
                <span class="label-text">账户余额:</span>
              </div>
            </template>
            <span>￥{{showRow.amount}}</span>
          </el-form-item>
        </el-form>
        </div>

      </el-dialog>
        </div>
  </div>
</template>

<script>


import {deleteBatch, deleteUser, loadUser, save} from "@/api/manage/userManage";

export default {
  name: "UserManageView",

  data() {
    return {
      showRow: {},
      users:[],
      username: '',
      pageNum:1,
      pageSize:10,
      total: 0,
      form:{role: ''},
      roles:[{
        label: '用户', value: '用户'
      }, {
        label: '管理员', value: '管理员'
      }, {
        label: '超级管理员', value: '超级管理员'
      }],
      dialogTitle: '',
      btnText:'',
      loading: true,
      Striped: false,
      fromVisible:false,
      showDialog: false, // 弹窗的显示与隐藏
      user:JSON.parse(localStorage.getItem('manage_config') || "{}"),
      rules:{
        username: [
          { required: true, message: '请输入账号', trigger:'blur' },
        ]
      },
      ids:[]
    }
  },
  computed: {
    isRoleDisabled() {
      if(this.user.role === '超级管理员') {
        return false;
      } else if(this.user.role === '管理员'){
        return this.form.role !== '用户';
      }
    }
  },

created() {
    this.load()

},

mounted() {
    // 在页面加载时从本地存储中读取选择状态
    const selection = localStorage.getItem('changeStriped');
    if (selection !== null) {
      this.Striped = JSON.parse(selection);
    }
  },

  methods: {
    exportData() {  //批量导出数据
      if(!this.ids.length){ //没有选择行的时候，全部导出，或者根据我的搜索条件导出
        window.open(this.$baseUrl + '/user/export?token=' + this.user.token + "&username=" +
        this.username)
      } else{   //  [1, 2, 3] => '1,2,3'
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/user/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    handleImport(res, file ,fileList) {
      if(res.code === '200') {
        this.$message.success("导入成功！")
        this.loading = false
        this.load(1)
      } else {
        this.$message.error(res.msg)
      }
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
      }
      this.$confirm('确认删除用户信息？', '确认删除', {type: 'warning'}).then(() => {
            if (this.user.role !== '超级管理员') {
              this.$message.error('删除失败！权限不足！')
              return;
            }
            deleteBatch(this.ids).then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.loading = false
                    this.load(1)
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() =>{})
    },
    handleSelectionChange (rows) {    //当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
    },
    handleEdit(index, row) {  // 编辑数据
      this.dialogTitle = '编辑用户' //修改dialog标题
      this.btnText = '更改头像'
      this.form = JSON.parse(JSON.stringify(row))  //给form对象赋值 注意要深拷贝数据
      this.fromVisible = true//打开弹窗
    },
    handleDelete(index, id) {
      this.$confirm('确认删除这条用户信息？', '确认删除', {type: 'warning'})
          .then(() => {
            if (this.user.role !== '超级管理员') {
              this.$message.error('删除失败！权限不足！')
              return;
            }
            deleteUser(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.load()
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() =>{})
    },
    handleAdd(){       //新增数据
      this.form = {}   //新增数据的时候清空数据
      this.dialogTitle = '新增用户' //修改dialog标题
      this.btnText = '上传头像'
      this.fromVisible = true //打开弹窗
    },
    save() {  // 保存按钮触发的逻辑 它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          if (this.user.role === '超级管理员') {
            if (this.form.role === '超级管理员' && this.user.role === '管理员') {
              this.$message.error('权限不足！')
            } else {
              save(this.form).then(res => {
                if (res.code === '200') {
                  this.$message.success('保存成功')
                  this.load(1)
                  this.loading = false
                  this.fromVisible = false
                } else {
                  this.$message.error('保存失败')
                }
              })
            }
          } else if (this.user.role === '管理员') {
            if (this.form.role === '超级管理员' || this.form.role === '管理员') {
              this.$message.error('权限不足！');
            } else {
              save(this.form).then(res => {
                if (res.code === '200') {
                  this.$message.success('保存成功')
                  this.load(1)
                  this.loading = false
                  this.fromVisible = false
                } else {
                  this.$message.error('保存失败')
                }
              })
            }
          } else {
            this.$message.error('权限不足！')
          }
        }
      });
    },
    handleAvatarSuccess(response, file, fileList){
      this.form.avatar = response.data
    },
    load(pageNum){
      if (pageNum) this.pageNum = pageNum
      loadUser(this.pageNum, this.pageSize, this.username, this.name)
        .then(res => {
        this.users = res.data.records
        this.total = res.data.total
        this.loading = false
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
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
.userDialog{
  height: 75% !important;
  width: 25% !important;
  border-radius: 12px !important;
}
.userManagerdialog{
  width:20% !important;
  height:60% !important;
  border-radius: 12px !important;
}

/deep/.el-input--medium .el-input__inner {
  border-radius: 12px;
}
/deep/.el-button{
  border-radius: 10px !important;
  overflow: hidden !important;
}
.label-cl {
  display: flex;
  align-items: center;
}
.label-text {
  margin-left: 5px;
}

</style>
