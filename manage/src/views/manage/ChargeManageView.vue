<template>
  <div class="managerSettingStyle">
    <el-tabs tab-position="left" v-model="activeName" @tab-click="handleTabClick">
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
              <el-button type="info" @click="reset">重置</el-button>
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
              <el-upload :action="$baseUrl + '/station/import'" :headers="{ token: user.token}"
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
                         @click="addStation">新建站点
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
                      @click="handleStatus(scope.$index, scope.row.id, scope.row.station_status); $event.stopPropagation()">{{ scope.row.station_status
                === '正常' ? '下线' : '上线' }}</span>
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
          <el-form :model="stationForm" label-width="80px" :rules="stationRules" ref="formRef">
            <el-form-item v-if="showThumbnail" label="电站缩略图：" prop="thumbList">
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
            <el-form-item label=" 电桩位置：">
              <el-button @click="openMap(innerVisible = true)">地图定位</el-button>
              <Map ref="map" @close="$refs.map.close()" @updateLocation="updateLocation" />
            </el-form-item>
            </div>
            <div class="formClass">
              <el-form-item label="站点经度：" prop="station_longitude">
                <el-input style="width: 200px" :disabled="true" v-model="stationForm.station_longitude" placeholder="站点经度"></el-input>
              </el-form-item>
              <el-form-item label=" 站点纬度：" prop="station_latitude">
                <el-input style="width: 65%" :disabled="true" v-model="stationForm.station_latitude" placeholder="站点纬度"></el-input>
              </el-form-item>
            </div>
            <el-form-item label=" 停车费用：" prop="parking_fee">
              <el-input style="width: 65%" v-model="stationForm.parking_fee" placeholder="停车费用"></el-input>
            </el-form-item>
            <el-form-item label=" 开放时间：" prop="open_time">
              <el-input style="width: 60%" v-model="stationForm.open_time" placeholder="开放时间"></el-input>
            </el-form-item>
            <el-form-item label=" 详细地址：" prop="station_address">
              <el-input type="textarea" style="width: 86%" v-model="stationForm.station_address"
                        placeholder="详细地址"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" v-if="showButtons">
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
              <el-input style="width: 200px; margin: 0" v-model="station_name" placeholder="请输入关联站点名称"></el-input>
              <el-select filterable clearable style="width: 145px; margin: 0 10px" v-model="port_status" placeholder="请选择电桩状态">
                <el-option
                    v-for="item in portSearch"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
              <el-button type="primary" @click="load(1)">查询</el-button>
              <el-button type="info" @click="resetData">重置</el-button>
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
              <!-- 新增/删除 -->
              <el-button class="button_pr" type="primary" style="margin-right:0 !important" icon="el-icon-plus"
                         @click="handleAdd">新建电桩
              </el-button>
              <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchPorts">
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
            <el-table-column label="关联电站" prop="station_name" align="center"></el-table-column>
            <el-table-column label="电桩功率" align="center">
              <template slot-scope="scope">
                {{ scope.row.port_power }} W / {{ scope.row.port_voltage }} V
              </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="p_create_time" align="center"></el-table-column>
            <el-table-column label="电桩状态" prop="port_status" align="center"></el-table-column>
            <el-table-column label="操作" align="center" width="185">
              <template slot-scope="scope" class="button-container">
                <span class="checkBtn" @click="checkPort(scope.$index, scope.row); $event.stopPropagation()">查看</span>
                <span class="editBtn" @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">编辑</span>
                <span class="offlineBtn"
                      @click="handleOffline(scope.$index, scope.row.id, scope.row.port_status); $event.stopPropagation()">
                  {{ scope.row.port_status === '维修中' ? '上线' : '下线' }}
                </span>
                <span class="deleteBtn"
                      @click="deletePort(scope.$index, scope.row.id);  $event.stopPropagation()">删除</span>
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


        <!-- 电桩弹窗 -->
        <el-dialog :title="dialogInfoTitle" :visible.sync="portFormVisible">
          <el-form ref="portFormRef" :model="portData" label-width="100px" :rules="portRules">
            <el-form-item label="电桩编码：" prop="port_number">
              <el-input style="width: 200px" v-model="portData.port_number" placeholder="电桩编码"></el-input>
            </el-form-item>
            <el-form-item label="所属站点：" prop="station_name">
            <template>
              <el-select id="selectStation"   v-model="portData.station_id" filterable style="width: 300px"  placeholder="请选择">
                <el-option
                    v-for="item in stationList"
                    :key="item.id"
                    :label="item.station_name"
                    :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="电桩类型" prop="port_type">
            <el-select v-model="portData.port_type" filterable style="width: 200px"  placeholder="请选择">
              <el-option label="直流" value="直流"></el-option>
              <el-option label="交流" value="交流"></el-option>
            </el-select>
          </el-form-item>
           <el-form-item label="电桩功率">
             <el-input style="width: 100px" v-model="portData.port_power" placeholder="W"></el-input> W
             <el-input style="margin-left: 20px; width: 100px" v-model="portData.port_voltage" placeholder="V"></el-input> V
           </el-form-item>
            <el-form-item label="电桩状态" prop="port_status">
              <el-select v-model="portData.port_status" filterable style="width: 200px"  placeholder="请选择">
                <el-option label="空闲中" value="空闲中"></el-option>
                <el-option label="维修中" value="维修中"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" v-if="showFooter">
            <el-button @click="portFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="savePort">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="电价管理" name="ePrice">
        <!-- 标头 -->
        <div class="settingStyleTitle">电价管理</div>

        <div style="background-color: white; padding:10px 0; border-radius: 8px;
         width: 98%; margin: 0 auto 10px;">
          <!-- 表格斑马纹 -->
          <div class="model_lg">
            <div class="btn-wrapper">
              <el-input style="width: 200px; margin: 0 8px" placeholder="请输入站点名称" v-model="station_name"></el-input>
              <el-button type="primary" @click="load(1)">查询</el-button>
              <el-button type="info" @click="reset">重置</el-button>
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
              <!-- 新增/删除 -->
              <el-button class="button_pr" type="primary" style="margin-right:0 !important" icon="el-icon-plus"
                         @click="addRules">新建规则
              </el-button>
              <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchPrices">
                批量删除
              </el-button>
            </div>
          </div>
        </div>
        <!-- 电价管理 -->
        <div class="infoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="priceData"
              :stripe="Striped"
              style="width: 100%;margin-top: 15px; font-size: 14px !important;border-radius: 8px;
              box-shadow: none !important;"
              v-loading="loading"
              @selection-change="selectionChangeByPrices"
              element-loading-text="正在加载数据中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0,0, 0.6)"
          >
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column label="ID" width="55" prop="id" align="center"></el-table-column>
            <el-table-column label="站点名称" prop="station_name" align="center"></el-table-column>
            <el-table-column label="开始时间" prop="start_time" align="center"></el-table-column>
            <el-table-column label="结束时间" prop="end_time" align="center"></el-table-column>
            <el-table-column label="电价类型" prop="price_type" align="center">
              <template v-slot="scope">
              <el-tag type="success" size="medium" v-if="scope.row.price_type === '峰'">{{scope.row.price_type}}</el-tag>
              <el-tag type="info" size="medium" v-if="scope.row.price_type === '平'">{{scope.row.price_type}}</el-tag>
              <el-tag type="warning" size="medium" v-if="scope.row.price_type === '谷'">{{scope.row.price_type}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="费用" align="center">
              <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <p>服务费: {{ scope.row.service_fee }} 元</p>
                <p>电价: {{ scope.row.e_price }} 元</p>
                <div slot="reference">
                  <p>{{ scope.row.t_price }} 元</p>
                </div>
              </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="165">
              <template slot-scope="scope" class="button-container">
                <span class="checkBtn"
                      @click="checkForm(scope.$index, scope.row); $event.stopPropagation()">查看</span>
                <span class="editBtn" @click="editForm(scope.$index, scope.row); $event.stopPropagation()">编辑</span>
                <span class="deleteBtn"
                      @click="deletePrice(scope.$index, scope.row.id);  $event.stopPropagation()">删除</span>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 20px 0;
            display: flex; align-items: flex-end; justify-content: center">
            <el-pagination
                :background="true"
                @current-change="currentChangePage"
                :current-page="pageNum"
                :page-size="pageSize"
                layout="total,prev, pager, next"
                :total="totalNum">
            </el-pagination>
          </div>
        </div>

        <!-- 电价弹窗 -->
        <el-dialog :title="dialogInfoTitle" :visible.sync="priceFormVisible">
          <el-form ref="priceFormRef" :model="formData" label-width="100px" :rules="priceRules">
            <el-form-item label="所属站点：" prop="station_name">
              <template>
                <el-select id="select"   v-model="formData.station_id" filterable style="width: 300px"  placeholder="请选择">
                  <el-option
                      v-for="item in stationList"
                      :key="item.id"
                      :label="item.station_name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </template>
            </el-form-item>
            <el-form-item label="起始时间：" prop="start_time">
              <el-time-picker  v-model="formData.start_time" placeholder="选择开始时间" value-format="HH:mm:ss"></el-time-picker>
            </el-form-item>
            <el-form-item label="终止时间：" prop="end_time">
              <el-time-picker v-model="formData.end_time" placeholder="选择结束时间" value-format="HH:mm:ss"></el-time-picker>
            </el-form-item>
            <el-form-item label="电价类型：" prop="price_type">
              <el-select v-model="formData.price_type" placeholder="请选择">
                <el-option label="谷" value="谷"></el-option>
                <el-option label="平" value="平"></el-option>
                <el-option label="峰" value="峰"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="服务费：" prop="service_fee">
              <el-input v-model="formData.service_fee" style="width: 32%"></el-input> 元
            </el-form-item>

            <el-form-item label="电价：" prop="e_price">
              <el-input v-model="formData.e_price" style="width: 32%"></el-input> 元
            </el-form-item>
            <el-form-item v-if="showTypes">
              <el-button type="primary" @click="savePrice">保存</el-button>
              <el-button @click="priceFormVisible = false">取 消</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </el-tab-pane>

      <el-tab-pane label="缩略图管理" name="thumbList">
        <!-- 标头 -->
        <div class="settingStyleTitle">缩略图管理</div>
        <div style="background-color: white; padding:10px 0; border-radius: 8px;
         width: 98%; margin: 0 auto 10px;">
          <!-- 表格斑马纹 -->
          <div class="model_lg">
            <div class="btn-wrapper">
              <el-select filterable clearable style="width: 250px; margin: 0 10px" v-model="station_name" placeholder="请选择站点名称">
                <el-option
                    v-for="item in stationList"
                    :key="item.id"
                    :label="item.station_name"
                    :value="item.station_name">
                </el-option>
              </el-select>
              <el-button type="primary" @click="load(1)">查询</el-button>
              <el-button type="info" @click="reset">重置</el-button>
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
              <!-- 新增/删除 -->
              <el-button class="button_pr" type="primary" style="margin-right:0 !important" icon="el-icon-plus"
                         @click="addPic">新增缩略图
              </el-button>
              <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchPictures">
                批量删除
              </el-button>
            </div>
          </div>
        </div>
        <!-- 缩略图管理  -->
        <div class="portInfoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="picData"
              :row-style="{height: '0'}"
              :stripe="Striped"
              style="width: 100%;margin-top: 15px; font-size: 14px !important; cursor: pointer; border-radius: 8px;
      box-shadow: none !important;"
              v-loading="loading"
              @selection-change="handleSelection"
              element-loading-text="正在加载数据中..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0,0, 0.6)"
          >
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column label="ID" width="350" prop="id" align="center"></el-table-column>
            <el-table-column label="关联电站" width="400" prop="station_name" align="center"></el-table-column>
            <el-table-column label="缩略图" width="345" prop="thumbnail" align="center">
              <template v-slot="scope">
                <el-image style="width: 100px; height: 100px;border-radius: 8px"
                          v-if="scope.row.thumbnail" :src="scope.row.thumbnail"
                          :preview-src-list="[scope.row.thumbnail]"
                          @click="$event.stopPropagation()"></el-image>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="185">
              <template slot-scope="scope" class="button-container">
                <span class="editBtn" @click="editPic(scope.$index, scope.row); $event.stopPropagation()">编辑</span>
                <span class="deleteBtn"
                      @click="deletePic(scope.$index, scope.row.id);  $event.stopPropagation()">删除</span>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 20px 0;
            display: flex; align-items: flex-end; justify-content: center">
            <el-pagination
                :background="true"
                @current-change="handleChange"
                :current-page="pageNum"
                :page-size="pageSize"
                layout="total,prev, pager, next"
                :total="totalData">
            </el-pagination>
          </div>
        </div>
        <!-- 缩略图弹窗 -->
        <el-dialog :title="dialogInfoTitle" :visible.sync="picFromVisible">
          <el-form :model="picForm" label-width="80px" style="padding-right: 20px" :rules="picRules" ref="picFormRef">
            <el-form-item label="关联电站：" prop="station_name">
              <template>
                  <el-select id="selectid"   v-model="picForm.station_id" filterable style="width: 300px"  placeholder="请选择">
                    <el-option
                        v-for="item in stationList"
                        :key="item.id"
                        :label="item.station_name"
                        :value="item.id">
                    </el-option>
                  </el-select>
              </template>
            </el-form-item>
            <el-form-item label="缩略图：" prop="thumbnail">
              <el-input type="textarea" v-model="picForm.thumbnail" placeholder="缩略图"
                        style="margin-bottom: 20px"></el-input>
              <el-upload :action="$baseUrl + '/file/upload'"
                         :on-success="handlePicSuccess"
                         :headers="{token : user.token}"
                         :file-list="picForm.thumbnail? [picForm.thumbnail] : []"
                         list-type="picture"
                         style="line-height: 34px; height: 34px">
                <el-button icon="el-icon-upload" type="primary" plain>
                  {{ textBtn }}
                </el-button>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" style="margin-top: 50px !important">
            <el-button @click="picFromVisible = false">取 消</el-button>
            <el-button type="primary" @click="savePicture">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Map from '@/components/MapContainer.vue';
import {
  deletePicturesBatch,
  deletePort,
  deletePortBatch, deletePricesBatch,
  deleteStation,
  deleteStationBatch,
  loadOfStation, loadPicture,
  loadPort,
  loadPrices,
  loadStation, pictureDelete, priceDelete, savePicture, savePort, savePrice, saveStation, updatePortStatus,
  updateStationStatus
} from "@/api/manage/chargeManage";

export default {
  components: {
    Map
  },
  data() {
    const checkPrice = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入价格'));
        } else if (!/^\d+(\.\d{1,2})?$/.test(value)) {
          callback(new Error('请输入正确的价格（最多两位小数）'));
        } else {
          callback();
        }

    }
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
      portSearch:[{
        value: '空闲中',
        label: '空闲中'
      }, {
        value: '已预约',
        label: '已预约'
      }, {
        value: '充电中',
        label: '充电中'
      }, {
        value: '维护中',
        label: '维护中'
      }],
      stationRules:{
        station_name:[{
          required:true, message:'请输入站点名称', trigger:'blur'
        }],
        station_status:[{
          required:true, message:'请选择电站状态', trigger:'blur'
        }]

      },
      portRules:{
        port_number: [{
          required:true, message:'请输入电桩编号', trigger:'blur'
        }, {
          pattern: /^(CD)[0-9]{5}$/,
          message: '电桩编号格式不正确，请输入类似于CD12345的格式',
          trigger: 'blur'
        }],
      },
      picRules:{
        thumbnail:  [{
          required: true, message: '请上传图片或输入图片网址', trigger:'blur'
        }],
      },
      priceRules: {
        price_type: [{
          required:true, message:'请选择电价类型', trigger:'change'
        }],
        start_time:[{
          required:true, message:'请选择起始时间', trigger:'change'
        }],
        end_time:[{
          required:true, message:'请选择终止时间', trigger:'change'
        }],
        service_fee: [{
          required:true,validator:checkPrice,trigger:'blur'
        }],
        e_price: [{
          required:true,validator:checkPrice,trigger:'blur'
        }]
      },
      value: '',
      chargingStation: [],
      chargingPorts:[],
      priceData:[],
      picData:[],
      station_name: '',
      port_status: '',
      dialogInfoTitle: '',
      formData: {},
      portData: {},
      picForm:{},
      stationForm: {
        address: '',
        province: '',
        city: '',
        latitude: 0, // 纬度
        longitude: 0, // 经度
        district: ''
      },
      stationList: [],
      fileList: [],
      innerVisible: false,
      priceFormVisible: false,
      portFormVisible: false,
      picFromVisible: false,
      showBtnDealImg: true,
      noneBtnImg: false,
      limitCountImg: 3,
      fromInfoVisible: false,
      pageNum: 1,
      pageSize: 5,
      total: 0,
      allNum: 0,
      totalNum: 0,
      totalData:0,
      user_username: '',
      loading: false,
      Striped: false,
      showButtons: true,
      showTypes: true,
      showFooter: true,
      showThumbnail:false,
      textBtn: '',
      user: JSON.parse(localStorage.getItem('manage_config') || "{}")
    }
  },

  methods: {
    handleTabClick(tab) {
      if (tab.name !== this.activeTab) {
        switch (this.activeTab) {
          case "chargingStation":
            this.reset();
            break;
          case "chargingPort":
            this.reset();
            break;
          case "ePrice":
            this.reset();
            break;
          case "thumbList":
            this.reset()
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

    //保存缩略图
    savePicture() {
      this.$refs.picFormRef.validate((valid) => {
        if (valid) {
          savePicture(this.picForm).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)
              this.loading = false
              this.picFromVisible = false
            } else {
              this.$message.error('保存失败')
            }
          })
        }
      })
    },

    //删除缩略图
    deletePic(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            pictureDelete(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.load(1)
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() => {
          })
    },

    //批量删除缩略图
    deleteBatchPictures() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deletePicturesBatch(this.ids).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功！')
            this.loading = false
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
          })
    },

    //编辑缩略图
    editPic(index, row) {
      this.picForm = JSON.parse(JSON.stringify(row))
      this.picFromVisible = true
      this.dialogInfoTitle = '新增缩略图'
      this.textBtn = '更改图片'
    },

    //新增缩略图
    addPic() {
      this.picForm = {}
      this.picFromVisible = true
      this.dialogInfoTitle = '新增缩略图'
      this.textBtn = '上传图片'
    },

    //删除电桩
    deletePort(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            deletePort(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.load(1)
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() => {
          })
    },

    //批量删除电桩
    deleteBatchPorts() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deletePortBatch(this.ids).then(res => {
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

    //保存电桩信息
    savePort() {
      this.$refs.portFormRef.validate((valid) => {
        if (valid) {
          // 设置创建时间为当前时间
          this.portData.p_create_time = new Date().toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai', hour12: false }).replace(/\//g, '-');
          savePort(this.portData).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              this.load(1);
              this.loading = false;
              this.portFormVisible = false;
            } else {
              this.$message.error('保存失败');
            }
          });
        }
      });
    },

    //下线电桩
    handleOffline(index, id, port_status) {
      let newStatus = '';
      let confirmMsg = '';
      let actionMsg = '';
      if (port_status === '空闲中') {
        newStatus = '维修中';
        confirmMsg = '确定下线该电桩？';
        actionMsg = '下线电桩';
      } else if (port_status === '使用中' || port_status === '已预约') {
        this.$message.warning('当前电桩正在使用，无法下线');
        return;
      } else if (port_status === '维修中') {
        newStatus = '空闲中';
        confirmMsg = '确定上线该电桩';
        actionMsg = '上线电桩';
      }
      this.$confirm(confirmMsg, actionMsg, { type: 'warning' })
          .then(() => {
            updatePortStatus(id, newStatus)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！');
                    this.load(1);
                  } else {
                    this.$message.error(res.msg);
                  }
                });
          })
          .catch(() => {
          });
    },

    //新增电桩
    handleAdd() {
      this.dialogInfoTitle = '新增电桩'
      this.portData = {}
      this.portFormVisible = true
      this.showFooter = true
    },

    //编辑电桩信息
    handleEdit(index, row) {
      this.dialogInfoTitle = '编辑电桩'
      this.portFormVisible = true
      this.portData = JSON.parse(JSON.stringify(row))
      this.showFooter = true
    },

    //查看电桩信息
    checkPort(index, row) {
      this.dialogInfoTitle = '查看电桩'
      this.portFormVisible = true
      this.portData = JSON.parse(JSON.stringify(row))
      this.showFooter = false
    },

    //添加电价信息
    addRules() {
      this.formData = {}
      this.dialogInfoTitle = '新增电价规则'
      this.priceFormVisible = true
      this.showTypes = true
    },

    // 查看电价信息
    checkForm(index, row) {
      this.dialogInfoTitle = '查看电价'
      this.priceFormVisible = true
      this.formData = JSON.parse(JSON.stringify(row))
      this.showTypes = false
    },

    //编辑电价信息
    editForm(index, row) {
      this.dialogInfoTitle = '编辑电价'
      this.priceFormVisible = true
      this.formData = JSON.parse(JSON.stringify(row))
      this.showTypes = true
    },

    //删除电价信息
    deletePrice(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            priceDelete(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.load(1)
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() => {
          })
    },

    //批量删除电价信息
    deleteBatchPrices() {
      if (!this.ids.length) {
        this.$message.warning('未选择数据！')
        return
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deletePricesBatch(this.ids).then(res => {
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

    //保存电价信息
    savePrice() {
      this.$refs.priceFormRef.validate((valid) => {
        if (valid) {
          savePrice(this.formData).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
              this.load(1)
              this.loading = false
              this.priceFormVisible = false
            } else {
              this.$message.error('保存失败')
            }
          })
        }
      })
    },

    // 添加站点信息
    addStation() {
      this.stationForm = {}
      this.dialogInfoTitle = '新增站点'
      this.fromInfoVisible = true
      this.showButtons = true
      this.showThumbnail = false
    },

    //保存站点信息
    save() {
      this.$refs.formRef.validate((valid) => {
            if (valid) {
              this.stationForm.create_time = new Date().toLocaleString
              ('zh-CN', { timeZone: 'Asia/Shanghai', hour12: false, year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' }).replace(/\//g, '-')
              const { station_name, station_address, station_longitude, station_latitude, station_status,
                open_time, parking_fee, create_time } = this.stationForm;
              saveStation(this.stationForm).then(res => {
                if (res.code === '200') {
                  this.$message.success('保存成功')
                  this.load(1)
                  this.loading = false
                  this.fromInfoVisible = false
                } else {
                  this.$message.error('保存失败')
                }
              })
            }
      })
    },

    //查看站点信息
    handleInspect(index, row) {
      this.showButtons = false
      this.showThumbnail = true
      this.dialogInfoTitle = '查看站点'
      this.stationForm = JSON.parse(JSON.stringify(row))
      this.fileList = []
      if (row.thumbnailList && row.thumbnailList.length > 0) {
        this.fileList = row.thumbnailList.map(url => ({ url: url }));
      }
      this.fromInfoVisible = true
    },

    //编辑站点信息
    editHandle(index, row) {
      this.showButtons = true
      this.showThumbnail = false
      this.dialogInfoTitle = '查看站点'
      this.stationForm = JSON.parse(JSON.stringify(row))
      this.fileList = [];
      if (row.thumbnailList && row.thumbnailList.length > 0) {
        this.fileList = row.thumbnailList.map(url => ({ url: url }));
      }
      this.fromInfoVisible = true
    },

    //下线站点
    handleStatus(index, id, station_status) {
      const newStatus = station_status === '正常' ? '下线' : '正常'
      this.$confirm(`确定${newStatus === '下线' ? '下线' : '上线'}该站点`,
          `${newStatus === '下线' ? '下线' : '上线'}站点`, { type: 'warning' })
          .then(() => {
            updateStationStatus(id, newStatus)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！');
                    this.load(1);
                  } else {
                    this.$message.error(res.msg);
                  }
                });
          })
          .catch(() => {
          });
    },

    //删除充电站信息
    deleteStation(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            deleteStation(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    this.load(1)
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
        deleteStationBatch(this.ids).then(res => {
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
    handlePicSuccess(response, file, fileList){
      this.picForm.thumbnail = response.data
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
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    handleSelection(rows) {
      this.ids = rows.map(v => v.id)
    },
    selectionChangeByPrices(rows) {
      this.ids = rows.map(v => v.id)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleChange(pageNum) {
      this.load(pageNum)
    },
    currentChangePage(pageNum) {
      this.load(pageNum)
    },
    currentChange(pageNum) {
      this.load(pageNum)
    },
    exportStationData() {  //批量导出站点数据
      if (!this.ids.length) { //没有选择行的时候，全部导出，或者根据我的搜索条件导出
        window.open(this.$baseUrl + '/station/export?token=' + this.user.token + "&station_name=" +
            this.station_name)
      } else {   //  [1, 2, 3] => '1,2,3'
        let idStr = this.ids.join(',')
        window.open(this.$baseUrl + '/station/export?token=' + this.user.token + '&ids=' + idStr)
      }
    },
    handleImportStation(res, file ,fileList) {
      if(res.code === '200') {
        this.$message.success("导入成功！")
        this.loading = false
        this.load(1)
      } else {
        this.$message.error(res.msg)
      }
    },
    resetData() {
      this.port_status = ''
      this.station_name = ''
      this.load(1)
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      loadStation(this.pageNum, this.pageSize, this.station_name)
          .then(res => {
            this.chargingStation = res.data.records
            this.total = res.data.total
            this.loading = false
          }).catch(() => {
      })
      loadPort(this.pageNum, this.pageSize, this.station_name, this.port_status)
          .then(res => {
            this.chargingPorts = []
            this.chargingPorts = res.data.records
            this.allNum = res.data.total
            this.loading = false
          }).catch(() => {
      })
      loadPrices(this.pageNum, this.pageSize, this.station_name)
          .then(res => {
            this.priceData = []
            this.priceData = res.data.records
            this.totalNum = res.data.total
            this.loading = false
          }).catch(() => {
      })
      loadPicture(this.pageNum, this.pageSize, this.station_name)
          .then(res => {
            this.picData = []
            this.picData = res.data.records
            this.totalData = res.data.total
            this.loading = false
          }).catch(() => {
      })
    },
    reset() {
      this.station_name= ''
      this.load(1)
    },
    //加载站点名称
    loadInfoOfStation() {
      loadOfStation(this.stationList).then(res =>{
        if(res.code==="200"){
          this.stationList=res.data
          return true
        }
      })
    }
  },
  created() {
    this.load()
    this.loadInfoOfStation()
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
/deep/ .el-input__prefix{
  margin-top: 5px;
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
