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
        <!-- 新增/删除 -->
        <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatch">批量删除</el-button>
      </div>
    </div>
    <div>

      <!-- 日志信息表格  -->
      <el-table
          :header-cell-style="{ backgroundColor: '#c4d8e2', color:'#666'}"
          :data="tableData"
          :stripe="Striped"
          style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
      box-shadow: none !important;"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          element-loading-text="正在加载数据中..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0,0, 0.6)"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>

        <el-table-column label="ID" width="55" prop="id" align="center"></el-table-column>
        <el-table-column label="操作模块" prop="operation" align="center"></el-table-column>
        <el-table-column label="操作类型" prop="type" align="center">
          <template v-slot="scope">
            <el-tag type="priamry" v-if="scope.row.type === '新增' ">{{scope.row.type}}</el-tag>
            <el-tag type="priamry" v-if="scope.row.type === '查询' ">{{scope.row.type}}</el-tag>
            <el-tag type="info" v-if="scope.row.type === '修改' ">{{scope.row.type}}</el-tag>
            <el-tag type="danger" v-if="scope.row.type === '删除' ">{{scope.row.type}}</el-tag>
            <el-tag type="danger" v-if="scope.row.type === '批量删除' ">{{scope.row.type}}</el-tag>
            <el-tag type="success" v-if="scope.row.type === '登录' ">{{scope.row.type}}</el-tag>
            <el-tag type="success" v-if="scope.row.type === '注册' ">{{scope.row.type}}</el-tag>
            <el-tag type="success" v-if="scope.row.type === '充值' ">{{scope.row.type}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作人IP" prop="ip" align="center"></el-table-column>
        <el-table-column label="操作人" prop="user" align="center"></el-table-column>
        <el-table-column label="操作时间" prop="time" align="center"></el-table-column>
        <el-table-column align="center" width="180">
          <template slot="header" slot-scope="scope">
            <el-input v-model="operation" size="medium" @input="load(1)" placeholder="输入关键字搜索">
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </template>




          <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">

            <el-button size="small" type="danger" plain slot="reference" class="deleteBtn"
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
    </div>
  </div>
</template>

<script>
import {deleteBatch, deleteLogs, loadLogs} from "@/api/systemManage/logsManage";
export default {
  name: "LogsView",
  data() {
    return {
      tableData:[],
      operation:'',
      pageNum:1,
      pageSize:10,
      total: 0,
      loading: true,
      Striped: false,
      fromVisible:false,
      user:JSON.parse(localStorage.getItem('manage_config') || "{}"),
      ids:[]
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
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
      }
      this.$confirm('确认删除信息？', '确认删除', {type: 'warning'}).then(() => {
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

    handleDelete(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            if (this.user.role !== '超级管理员') {
              this.$message.error('删除失败！权限不足！')
              return;
            }
            deleteLogs(id)
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

    load(pageNum){
      if (pageNum) this.pageNum = pageNum
      loadLogs(this.pageNum, this.pageSize, this.operation).then(res => {
        this.tableData = res.data.records
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
