<template>
  <div class="managerSettingStyle">
    <el-tabs tab-position="left" v-model="activeName">
      <el-tab-pane label="充电站管理" name="chargingStation">
        <!-- 标头 -->
        <div class="settingStyleTitle">充电站管理</div>

        <div style="background-color: white; padding:10px 0; border-radius: 8px;
         width: 98%; margin: 0 auto 10px;">
          <!-- 表格斑马纹 -->
          <div class="model_lg">
            <div class="btn-wrapper">
              <el-input style="width: 200px; margin: 0 8px" placeholder="请输入站点名称" v-model="station_name"></el-input>
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
              <!-- 批量导入 -->
              <el-upload :action="$baseUrl + '/user/import'" :headers="{ token: user.token}"
                         :show-file-list="false"
                         :on-success="handleImportStation"
                         style="color: #fff; margin-right: 10px">
                <el-button type="primary" icon="el-icon-upload2">批量导入</el-button>
              </el-upload>
              <!-- 批量导出 -->
              <el-button type="primary" icon="el-icon-download" style="color: #fff" @click="exportStationData">
                批量导出
              </el-button>
              <!-- 新增/删除 -->
              <el-button class="button_pr" type="primary" style="margin-right:0 !important" icon="el-icon-plus"
                         @click="handleAdd">新建站点
              </el-button>
              <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchStation">
                批量删除
              </el-button>
            </div>
          </div>
        </div>
        <!-- 充电站管理 -->
        <div class="infoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="chargingStation"
              :row-style="{height: '0'}"
              :stripe="Striped"
              style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
              box-shadow: none !important;"
              v-loading="loading"
              @selection-change="selectionChange"
              element-loading-text="正在加载数据中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0,0, 0.6)"
          >
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column label="ID" width="55" prop="id" align="center"></el-table-column>
            <el-table-column label="站点名称" prop="station_name" align="center"></el-table-column>
            <el-table-column label="充电桩数量" prop="ports_count" align="center"></el-table-column>
            <el-table-column label="详细地址" prop="station_address" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="create_time" align="center"></el-table-column>
            <el-table-column label="充电站状态" prop="station_status" align="center"></el-table-column>
            <el-table-column label="操作" align="center" width="165">
              <template slot-scope="scope" class="button-container">
                <span class="checkBtn"
                      @click="handleInspect(scope.$index, scope.row); $event.stopPropagation()">查看</span>
                <span class="editBtn" @click="editHandle(scope.$index, scope.row); $event.stopPropagation()">编辑</span>
                <span class="offlineBtn"
                      @click="handleStatus(scope.$index, scope.row); $event.stopPropagation()">下线</span>
                <span class="deleteBtn"
                      @click="deleteStation(scope.$index, scope.row.id);  $event.stopPropagation()">删除</span>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 20px 0;
            display: flex; align-items: flex-end; justify-content: center">
            <el-pagination
                :background="true"
                @current-change="currentChange"
                :current-page="pageNum"
                :page-size="pageSize"
                layout="total,prev, pager, next"
                :total="total">
            </el-pagination>
          </div>
        </div>
        <el-dialog :title="dialogInfoTitle" custom-class="stationDialog" :visible.sync="fromInfoVisible">
          <el-form :model="stationForm" label-width="80px"  :rules="rules" ref="formRef">
            <el-form-item label="电站缩略图：" prop="thumbList">
              <div class="img-wrapper">
                <el-upload
                    action="#"
                    list-type="picture-card"
                    :file-list="fileList"
                    :limit="limitCountImg"
                    :class="{disUnloadSty:noneBtnImg}"
                    :on-change="dealImgChange"
                    :auto-upload="false">
                  <i slot="default" class="el-icon-plus"></i>
                  <div slot="file" slot-scope="{file}">
                    <img
                        class="el-upload-list__item-thumbnail"
                        :src="file.url" alt=""
                    >
                    <span class="el-upload-list__item-actions">
        <span class="el-upload-list__item-preview"
              @click="handlePictureCardPreview(file)">
          <i class="el-icon-zoom-in"></i>
        </span>
        <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDealImgRemove(file)">
          <i class="el-icon-delete"></i>
        </span>
      </span>
                  </div>
                </el-upload>
                <el-dialog :visible.sync="dialogVisible">
                  <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
              </div>
            </el-form-item>
            <div class="formClass">
              <el-form-item label="站点名称：" prop="station_name">
                <el-input style="width: 200px" v-model="stationForm.station_name" placeholder="站点名称"></el-input>
              </el-form-item>
              <el-form-item label="电站状态：" prop="station_status">
                <template>
                  <el-select v-model="stationForm.station_status" placeholder="请选择">
                    <el-option
                        v-for="item in selection"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                  </el-select>
                </template>
              </el-form-item>
            </div>
            <div class="formClass">
            <el-form-item label="电桩位置：">
              <el-button @click="openMap(innerVisible = true)">地图定位</el-button>
              <Map ref="map" @close="$refs.map.close()" @updateLocation="updateLocation" />
            </el-form-item>
            <el-form-item label="电费设置：">
              <el-button>选择电价</el-button>
            </el-form-item>
            </div>
<!--              <el-form-item label="服务费用：" prop="station_name">-->
<!--                <el-input style="width: 200px" v-model="stationForm.station_name" placeholder="服务费用"></el-input>-->
<!--                <span style="margin-left: 5px">元/度</span>-->
<!--              </el-form-item>-->
            <div class="formClass">
              <el-form-item label="站点经度：" prop="station_longitude">
                <el-input style="width: 200px" :disabled="true" v-model="stationForm.station_longitude" placeholder="站点经度"></el-input>
              </el-form-item>
              <el-form-item label="站点纬度：" prop="station_latitude">
                <el-input style="width: 65%" :disabled="true" v-model="stationForm.station_latitude" placeholder="站点纬度"></el-input>
              </el-form-item>
            </div>
            <el-form-item label="停车费用：" prop="station_name">
              <el-input style="width: 65%" v-model="stationForm.parking_fee" placeholder="停车费用"></el-input>
            </el-form-item>
            <el-form-item label="开放时间：" prop="station_name">
              <el-input style="width: 60%" v-model="stationForm.open_time" placeholder="开放时间"></el-input>
            </el-form-item>
            <el-form-item label="详细地址：" prop="station_address">
              <el-input type="textarea" style="width: 86%" v-model="stationForm.station_address"
                        placeholder="详细地址"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="fromInfoVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </el-dialog>

      </el-tab-pane>

      <el-tab-pane label="充电桩管理" name="chargingPort">

        <!-- 标头 -->
        <div class="settingStyleTitle">充电桩管理</div>
        <div style="background-color: white; padding:10px 0; border-radius: 8px;
         width: 98%; margin: 0 auto 10px;">
          <!-- 表格斑马纹 -->
          <div class="model_lg">
            <div class="btn-wrapper">
              <el-input style="width: 200px; margin: 0" placeholder="请输入关联站点名称"></el-input>
              <el-select filterable style="width: 145px; margin: 0 10px" v-model="value" placeholder="请选择电桩状态">
                <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
              <el-button type="primary">查询</el-button>
              <el-button type="info">重置</el-button>
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
              <!-- 批量导入 -->
              <el-upload :action="$baseUrl + '/user/import'" :headers="{ token: user.token}"
                         :show-file-list="false"
                         :on-success="handleImport"
                         style="color: #fff; margin-right: 10px">
                <el-button type="primary" icon="el-icon-upload2">批量导入</el-button>
              </el-upload>
              <!-- 批量导出 -->
              <el-button type="primary" icon="el-icon-download" style="color: #fff" @click="exportRechargeRecordsData">
                批量导出
              </el-button>
              <!-- 新增/删除 -->
              <el-button class="button_pr" type="primary" style="margin-right:0 !important" icon="el-icon-plus"
                         @click="handleAdd">新建电桩
              </el-button>
              <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchRechargeRecords">
                批量删除
              </el-button>
            </div>
          </div>
        </div>
        <!-- 充电桩管理  -->
        <div class="portInfoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="chargingPorts"
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
            <el-table-column label="电桩编码" prop="port_number" align="center"></el-table-column>
            <el-table-column label="电桩类型" prop="port_type" align="center"></el-table-column>
            <el-table-column label="关联电站" prop="port_type" align="center"></el-table-column>
            <el-table-column label="电桩功率" align="center">
              <template slot-scope="scope">
                {{ scope.row.port_power }} W / {{ scope.row.port_voltage }} V
              </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="p_create_time" align="center"></el-table-column>
            <el-table-column label="电桩状态" prop="port_type" align="center"></el-table-column>
            <el-table-column label="操作" align="center" width="185">
              <template slot-scope="scope" class="button-container">
                <span class="checkBtn" @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">查看</span>
                <span class="editBtn" @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">编辑</span>
                <span class="offlineBtn"
                      @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">下线</span>
                <span class="deleteBtn"
                      @click="deleteRechargeRecord(scope.$index, scope.row.id);  $event.stopPropagation()">删除</span>
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
    </el-tabs>
  </div>
</template>

<script>
import {deleteBatchRechargeRecords, deleteRechargeRecord, loadRechargeRecords} from "@/api/manage/orderManage";
import Map from '@/components/MapContainer.vue';
import {loadStation} from "@/api/manage/chargeManage";
export default {
  components: {
    Map
  },
  data() {
    return {
      activeName: 'chargingStation',
      selection: [{
        value: '正常',
        label: '正常'
      }, {
        value: '下线',
        label: '下线'
      }, {
        value: '维护中',
        label: '维护中'
      }],
      chargingStation: [],
      chargingPorts:[],
      station_name: '',
      dialogInfoTitle: '',
      stationForm: {
        address: '',
        province: '',
        city: '',
        latitude: 0, // 纬度
        longitude: 0, // 经度
        district: ''
      },
      fileList: [],
      innerVisible: false,
      showBtnDealImg: true,
      noneBtnImg: false,
      limitCountImg: 3,
      fromInfoVisible: false,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      user_username: '',
      loading: false,
      Striped: false,
      user: JSON.parse(localStorage.getItem('manage_config') || "{}")
    }
  },

  methods: {
    changeStriped() {
      this.isStriped = !this.isStriped;
      // 在选择switch时将选择状态保存到本地存储
      localStorage.setItem('changeStriped', JSON.stringify(this.Striped));
    },
    openMap() {
      this.$refs.map.open()
      if (this.form.longitude === 'undefined' && this.form.latitude === 'undefined') {
        this.$refs.map.open()
      } else {
        this.$refs.map.open(this.form)
      }
    },
    updateLocation(orgAddr, lon, lat) {
      this.stationForm = {
        station_address: orgAddr,
        station_latitude: lat, // 纬度
        station_longitude: lon, // 经度
      }
    },
    //查看站点信息
    handleInspect(index, row) {
      this.stationForm = JSON.parse(JSON.stringify(row));
      this.fileList = [];
      if (row.thumbnailList && row.thumbnailList.length > 0) {
        this.fileList = row.thumbnailList.map(url => ({ url: url }));
      }
      this.fromInfoVisible = true;
    },
    //编辑站点信息
    editHandle() {

    },
    //下线站点
    handleStatus() {

    },
    //删除充电站信息
    deleteStation(index, id) {
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
          .catch(() => {
          })
    },
    //批量删除充电站信息
    deleteBatchStation() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
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
          .catch(() => {
          })
    },
    dealImgChange(file, fileList) {
      this.noneBtnImg = fileList.length >= this.limitCountImg;
    },
    handleDealImgRemove(file, fileList) {
      this.noneBtnImg = fileList.length >= this.limitCountImg;
    },
    selectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    currentChange(pageNum) {
      this.load(pageNum)
    },
    exportStationData() {  //批量导出站点数据
      if (!this.ids.length) { //没有选择行的时候，全部导出，或者根据我的搜索条件导出
        window.open(this.$baseUrl + '/rechargeRecords/export?token=' + this.user.token + "&user_username=" +
            this.user_username)
      } else {   //  [1, 2, 3] => '1,2,3'
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/rechargeRecords/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    handleImportStation() {

    },
    load(pageNum) {
      this.chargingPorts = []
      if (pageNum) this.pageNum = pageNum
      loadStation(this.pageNum, this.pageSize, this.station_name)
          .then(res => {
            this.chargingStation = res.data.records
            this.total = res.data.total
            this.loading = false
          })
    },
    reset() {
      this.station_name= ''
      this.load()
    },
  },
  created() {
    const tab = this.$route.query.tab;
    if (tab) {
      this.activeName = tab;
    }
    this.load()
  },

  mounted() {
    // 在页面加载时从本地存储中读取选择状态
    const selection = localStorage.getItem('changeStriped');
    if (selection !== null) {
      this.Striped = JSON.parse(selection);
    }
  },
  computed: {
    thumbnailListLength() {
      return this.fileList.length;
    },
  },

  watch: {
    '$route.query.tab'(tab) {
      if (tab) {
        this.activeName = tab;
      }
    },
    thumbnailListLength(newValue) {
      this.noneBtnImg = newValue === this.limitCountImg;
    },
  }
}


</script>

<style scoped>
.managerSettingStyle {
  background-color: #fff;
  width: 100%;
  height: calc(100vh - 100px);
  border-radius: 12px;
  padding: 12px;
}

.el-tabs {
  border-left: 0;
  height: 100%;
}

/deep/ .el-tabs__nav {
  width: 150px;
  height: 100%;
  overflow: hidden;
  position: relative;
}

.checkBtn, .editBtn, .deleteBtn, .offlineBtn {
  cursor: pointer;
  transition: color 0.3s;
}

.checkBtn, .editBtn, .offlineBtn {
  color: #0A83F7;
}

.deleteBtn {
  color: #F7100A;
}

.checkBtn {
  margin-right: 10px;
}

.checkBtn:hover, .editBtn:hover, .deleteBtn:hover, .offlineBtn:hover {
  color: #cb99c9;
}

/deep/ .el-button--info:hover {
  background-color: rgba(76, 40, 130, 0.4);
}

/deep/ .el-tabs__nav .el-tabs__item {
  font-size: 16px;
  text-align: left !important;
  line-height: 40px;
  padding-left: 20px;
  transition: transform 0.3s ease, opacity 0.3s ease;
}

/deep/ .el-tabs__nav .el-tabs__item.is-active {
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

.infoStyle {
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 98%;
  margin: 0 auto;
}

.portInfoStyle {
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 98%;
  margin: 0 auto;
}

.img-wrapper {
  display: flex;
  justify-content: center;
}

::v-deep.disUnloadSty .el-upload--picture-card {
  display: none; /* 上传按钮隐藏 */
}
.dialog-footer{
  display: flex;
  justify-content: center;
}
.stationDialog {
  width: 100% !important;
  height: 100% !important;
}

/deep/ .el-form-item__label {
  white-space: nowrap;
}

.el-form-item {
  margin-bottom: 80px;
}

.userSettingForm .el-form-item {
  line-height: 50px;
  max-width: 460px;
}

.el-button {
  border-radius: 12px !important;
}

/deep/ .el-input__inner,
/deep/ .el-textarea__inner {
  border-radius: 8px !important;
  overflow: hidden;
}

.btn-wrapper {
  display: flex;
  align-items: center;
}

.model_mt {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.model_lg {
  display: flex;
  justify-content: space-between;
}

/deep/ .formClass {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.formClass > * {
  flex: 1;
}

.switch_cl {
  overflow: hidden !important;
  border-radius: 100px !important;
  margin-right: 10px !important;
  transition: padding-inline-start 0.2s ease-in-out, padding-inline-end 0.3s ease-in-out;
}


.switch_cl .el-switch__label {
  position: absolute;
  display: none;
  color: #fff !important;
}

.el-switch__core {
  background-color: rgba(166, 166, 166, 1);
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

.el-switch__label * {
  font-size: 1px !important;
}

</style>
