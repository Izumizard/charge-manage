<template>
  <div class="managerSettingStyle">
    <el-tabs tab-position="left" v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="充值记录" name="rechargeRecords">
        <!-- 标头 -->
        <div class="settingStyleTitle">充值记录</div>

        <div style="background-color: white; padding:10px 20px; border-radius: 8px;">
          <!-- 表格斑马纹 -->
          <div class="model_mg">
            <div class="btn-wrapper">
              <el-input style="width: 200px; margin: 0 8px" placeholder="请输入用户名" v-model="user_username"></el-input>
              <el-button type="primary" @click="load(1)">查询</el-button>
              <el-button type="info" @click="reset()">重置</el-button>
            </div>
            <div class="model_mt">
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
            <!-- 批量导出 -->
            <el-button type="primary" icon="el-icon-download" style="color: #fff" @click="exportRechargeRecordsData">
              批量导出</el-button>
            <!-- 批量删除 -->
            <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchRechargeRecords">
              批量删除</el-button>
            </div>
          </div>
        </div>
        <!-- 充值记录 -->
        <div class="recordsInfoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="rechargeRecords"
              :row-style="{height: '0'}"
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
            <el-table-column label="用户名" prop="user_username" align="center"></el-table-column>
            <el-table-column label="用户头像" prop="user_avatar" align="center">
              <template v-slot="scope">
                <el-image style="width: 50px; height: 50px;border-radius: 8px"
                    v-if="scope.row.avatar" :src="scope.row.avatar"
                    :preview-src-list="[scope.row.avatar]"
                    @click="$event.stopPropagation()"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="充值金额" prop="recharge" align="center"></el-table-column>
            <el-table-column label="订单号" prop="number" align="center" width="178"></el-table-column>
            <el-table-column label="支付方式" prop="type" align="center"></el-table-column>
            <el-table-column label="充值状态" prop="status" align="center"></el-table-column>
            <el-table-column label="充值时间" prop="recharge_time" align="center" width="158"></el-table-column>
            <el-table-column label="操作" align="center" width="180">
              <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">

                <el-button size="small" type="danger" plain slot="reference" class="deleteBtn"
                    @click="deleteRechargeRecord(scope.$index, scope.row.id);  $event.stopPropagation()">
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
      </el-tab-pane>

      <el-tab-pane label="交易记录" name="ordersInfo" >

        <!-- 标头 -->
        <div class="settingStyleTitle">交易记录</div>
        <div style="background-color: white; padding:10px 20px; border-radius: 8px;">
          <!-- 表格斑马纹 -->
          <div class="model_mg">
            <div class="btn-wrapper">
              <el-input style="width: 200px; margin: 0 8px" placeholder="请输入用户名" v-model="user_username"></el-input>
              <el-button type="primary" @click="load(1)">查询</el-button>
              <el-button type="info" @click="reset()">重置</el-button>
            </div>
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
            <!-- 批量导出 -->
            <el-button type="primary" icon="el-icon-download" style="color: #fff" @click="exportOrdersData">
              批量导出</el-button>
            <!-- 批量删除 -->
            <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchOrders">
              批量删除</el-button>
          </div>
        </div>
        <!-- 交易记录  -->
        <div class="ordersInfoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="ordersRecords"
              :row-style="{height: '0'}"
              :stripe="Striped"
              style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
      box-shadow: none !important;"
              v-loading="loading"
              @selection-change="handleSelectionChange"
              element-loading-text="正在加载数据中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0,0, 0.6)"
          >
            <el-table-column  fixed type="selection" width="55" align="center"></el-table-column>

            <el-table-column  fixed label="ID" width="55" prop="id" align="center"></el-table-column>
            <el-table-column  fixed label="用户名" prop="username" align="center"></el-table-column>
            <el-table-column  fixed label="用户头像" prop="avatar" align="center">
              <template v-slot="scope">
                <el-image style="width: 50px; height: 50px;border-radius: 8px"
                          v-if="scope.row.avatar" :src="scope.row.avatar"
                          :preview-src-list="[scope.row.avatar]"
                          @click="$event.stopPropagation()"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="订单号" prop="order_no" align="center" width="188"></el-table-column>
            <el-table-column label="站点名称" prop="station_name" align="center" width="178"></el-table-column>
            <el-table-column label="电桩编号" prop="port_number" align="center"></el-table-column>
            <el-table-column label="预约时段" align="center" width="380">
              <template slot-scope="scope">
                {{ scope.row.start_time }} - {{scope.row.end_time}}
              </template>
            </el-table-column>
            <el-table-column label="订单状态" prop="status" align="center"></el-table-column>
            <el-table-column label="订单金额" align="center">
              <template slot-scope="scope">
                {{ scope.row.price }} 元
              </template>
            </el-table-column>
            <el-table-column label="结束时间" prop="update_time" align="center" width="158"></el-table-column>
            <el-table-column fixed="right" label="操作" align="center">
              <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">

                <el-button size="small" type="danger" plain slot="reference" class="deleteBtn"
                           @click="deleteRechargeRecord(scope.$index, scope.row.id);  $event.stopPropagation()">
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
                :total="allNum">
            </el-pagination>
        </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  deleteBatchRechargeRecords,
  deleteRechargeRecord,
  loadOrders,
  loadRechargeRecords
} from "@/api/manage/orderManage";

export default {
  data() {
    return{
      activeName: 'rechargeRecords',
      rechargeRecords: [],
      ordersRecords: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      allNum: 0,
      user_username: '',
      username: '',
      loading: false,
      Striped: false,
      user:JSON.parse(localStorage.getItem('manage_config') || "{}")
    }
  },

  methods: {
    handleTabClick(tab) {
      if (tab.name !== this.activeTab) {
        switch (this.activeTab) {
          case "rechargeRecords":
            this.reset();
            break;
          case "ordersInfo":
            this.reset();
            break;
        }
        this.activeTab = tab.name;
      }
    },
    changeStriped() {
      this.isStriped = !this.isStriped;
      // 在选择switch时将选择状态保存到本地存储
      localStorage.setItem('changeStriped', JSON.stringify(this.Striped));
    },
    handleSelectionChange(rows){
      this.ids = rows.map(v => v.id)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    exportRechargeRecordsData() {  //批量导出数据
      if(!this.ids.length){ //没有选择行的时候，全部导出，或者根据我的搜索条件导出
        window.open(this.$baseUrl + '/rechargeRecords/export?token=' + this.user.token + "&user_username=" +
            this.user_username)
      } else{   //  [1, 2, 3] => '1,2,3'
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/rechargeRecords/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    exportOrdersData() {
      if(!this.ids.length){ //没有选择行的时候，全部导出，或者根据我的搜索条件导出
        window.open(this.$baseUrl + '/orders/export?token=' + this.user.token + "&username=" +
            this.username)
      } else{   //  [1, 2, 3] => '1,2,3'
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/orders/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    deleteBatchRechargeRecords() {
      if (!this.ids || this.ids.length === 0) {
        this.$message.warning('未选择数据！');
        return;
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deleteBatchRechargeRecords(this.ids).then(res => {
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

    deleteRechargeRecord(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            deleteRechargeRecord(id)
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
      loadRechargeRecords(this.pageNum, this.pageSize, this.user_username)
          .then(res => {
            this.rechargeRecords = res.data.records
            this.total = res.data.total
            this.loading = false
          })
      loadOrders(this.pageNum, this.pageSize, this.user_username)
          .then(res => {
            this.ordersRecords = res.data.records
            this.allNum = res.data.total
            this.loading = false
          })
    },
    reset(){
      this.user_username = ''
      this.load()
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

}


</script>

<style scoped>
.managerSettingStyle {
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
.recordsInfoStyle{
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 98%;
  margin: 0 auto;
}
.ordersInfoStyle {
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 98%;
  margin: 0 auto;
}


/deep/.el-form-item__label {
  white-space: nowrap;
}
.el-input--small .el-input__inner {
  height: 32px;
  line-height: 150px;
  width: 300px;
}
.el-form-item {
  margin-bottom: 80px;
}
.userSettingForm .el-form-item {
  line-height: 50px;
  max-width: 460px;
}

.el-button{
  border-radius: 12px !important;
}
/deep/.el-input__inner,
/deep/.el-textarea__inner {
  border-radius: 8px !important;
  overflow: hidden;
}

.model_mg {
  display: flex;
}

.btn-wrapper {
  margin-right: auto;
}

.model_mt {
  margin-left: auto;
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

</style>
