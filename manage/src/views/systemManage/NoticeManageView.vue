<template>
  <div class="managerSettingStyle">
    <el-tabs tab-position="left" v-model="activeName">
      <el-tab-pane label="轮播图管理" name="swiperInfo">
        <!-- 标头 -->
        <div class="settingStyleTitle">轮播图管理</div>

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
            <el-button class="button_pr" type="primary"  style="margin-right:0px !important" icon="el-icon-plus" @click="handleAdd">新增图片</el-button>
            <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchSwiper">批量删除</el-button>
          </div>
        </div>
          <!-- 轮播图管理 -->
          <div class="swiperInfoStyle">
            <el-table
                :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
                :data="swiperData"
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
              <el-table-column label="标题" prop="topic" align="center"></el-table-column>
              <el-table-column label="轮播图" prop="banner" align="center">
                <template v-slot="scope">
                  <el-image
                      style="width: 85px; height: 85px;border-radius: 8px"
                      v-if="scope.row.banner" :src="scope.row.banner"
                      :preview-src-list="[scope.row.banner]"
                      @click="$event.stopPropagation()"></el-image>
                </template></el-table-column>
              <el-table-column label="是否展示" prop="status" align="center">
                <template v-slot="scope">
                  <el-tooltip :content="'是否展示'" placement="top">
                  <el-switch v-model="scope.row.status"
                             :active-value="true"
                             :inactive-value="false"
                             active-text="是"
                             inactive-text="否"
                             @change="updateDisplay(scope.row)"></el-switch>
                  </el-tooltip>
                </template>
              </el-table-column>
              <el-table-column label="描述" prop="description" align="center" show-overflow-tooltip></el-table-column>

              <el-table-column label="操作" align="center" width="180">
                <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">
                  <el-button size="small" plain class="editBtn"
                             @click="handleEdit(scope.$index, scope.row); $event.stopPropagation()">
                    编 辑</el-button>

                  <el-button size="small" type="danger" plain slot="reference" class="deleteBtn"
                      @click="handleDeleteSwiper(scope.$index, scope.row.id);  $event.stopPropagation()">
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
            <!-- 图片信息新增/编辑弹窗 -->
            <el-dialog :title="dialogTitle" custom-class="userDialog" :visible.sync="fromVisible">
              <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
                <el-form-item label="标题：" prop="topic">
                  <el-input v-model="form.topic" placeholder="标题"></el-input>
                </el-form-item>
                <el-form-item label="是否展示：" prop="status">
                  <template v-slot="scope">
                    <el-tooltip :content="'是否展示'" placement="top">
                    <el-switch v-model="form.status"
                               :active-value="true"
                               :inactive-value="false"
                               active-text="是"
                               inactive-text="否"
                               @change="updateDisplay(scope.row)">

                    </el-switch>
                    </el-tooltip>
                  </template>
                </el-form-item>
                <el-form-item label="描述：" prop="description">
                  <el-input type="textarea" v-model="form.description" placeholder="描述"></el-input>
                </el-form-item>
                <el-form-item label="轮播图：" prop="banner">
                  <el-input type="textarea" v-model="form.banner" placeholder="轮播图"
                  style="margin-bottom: 20px"></el-input>
                  <el-upload :action="$baseUrl + '/file/upload'"
                             :on-success="handleBannerSuccess"
                             :headers="{token : user.token}"
                             :file-list="form.banner? [form.banner] : []"
                             list-type="picture"
                             style="line-height: 34px; height: 34px">
                    <el-button icon="el-icon-upload" type="primary" plain>
                      {{ btnText }}
                    </el-button>
                  </el-upload>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer" style="margin-top: 50px !important">
                <el-button @click="fromVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveSwiper">确 定</el-button>
              </div>
            </el-dialog>


          </div>
      </el-tab-pane>

      <el-tab-pane label="通知管理" name="noticeInfo" >

        <!-- 标头 -->
        <div class="settingStyleTitle">通知管理</div>

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
            <el-button class="button_pr" type="primary"  style="margin-right:0 !important" icon="el-icon-plus" @click="handleAddNotice">新增通知</el-button>
            <el-button type="danger" icon="el-icon-delete" style="color: #fff" @click="deleteBatchNotice">批量删除</el-button>
          </div>
        </div>
        <!-- 通知管理  -->
        <div class="noticeInfoStyle">
          <el-table
              :header-cell-style="{ backgroundColor: '#c3cde6', color:'#666'}"
              :data="noticeData"
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

            <el-table-column label="ID" width="55" prop="id" align="center"></el-table-column>
            <el-table-column label="标题" prop="title" align="center"></el-table-column>
            <el-table-column label="内容" prop="content" align="center"></el-table-column>
            <el-table-column label="发布人" prop="user" align="center"></el-table-column>
            <el-table-column label="发布时间" prop="time" align="center"></el-table-column>
            <el-table-column label="是否公开" prop="open" align="center">
              <template v-slot="scope">
                <el-tooltip :content="'是否公开'" placement="top">
                  <el-switch v-model="scope.row.open"
                             :active-value="true"
                             :inactive-value="false"
                             active-text="是"
                             inactive-text="否"
                             @change="update(scope.row)"></el-switch>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column align="center" width="180">
              <template slot="header" slot-scope="scope">
                <el-input v-model="title" size="medium" @input="load(null,1)" placeholder="输入关键字搜索">
                  <i slot="prefix" class="el-input__icon el-icon-search"></i>
                </el-input>
              </template>
              <template slot-scope="scope" class="button-container" style=" display: flex; align-items: center">
                <el-button size="small" plain class="editBtn" @click="handleEditNotice(scope.$index, scope.row); $event.stopPropagation()">
                  编 辑</el-button>
                <el-button size="small" type="danger" plain slot="reference" class="deleteBtn" @click="handleDelete(scope.$index, scope.row.id);
      $event.stopPropagation()">
                  删 除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 20px 0;
            display: flex; align-items: flex-end; justify-content: center">
            <el-pagination
                :background="true"
                @current-change="handleCurrent"
                :current-page="pageNumOne"
                :page-size="pageSizeOne"
                layout="total,prev, pager, next"
                :total="total">
            </el-pagination>
          </div>
          <!-- 图片信息新增/编辑弹窗 -->
          <el-dialog :title="noticeDialogTitle" custom-class="userDialog" :visible.sync="noticeFromVisible">
            <el-form :model="noticeForm" label-width="80px" style="padding-right: 20px" :rules="rule" ref="noticeFormRef">
              <el-form-item label="标题：" prop="title">
                <el-input v-model="noticeForm.title" placeholder="标题"></el-input>
              </el-form-item>
              <el-form-item label="内容：" prop="content">
                <el-input type="textarea" v-model="noticeForm.content" placeholder="内容"></el-input>
              </el-form-item>
              <el-form-item label="是否展示：" prop="open">
                <template v-slot="scope">
                  <el-tooltip :content="'是否公开'" placement="top">
                    <el-switch v-model="noticeForm.open"
                               :active-value="true"
                               :inactive-value="false"
                               active-text="是"
                               inactive-text="否"
                               @change="update(scope.row)">

                    </el-switch>
                  </el-tooltip>
                </template>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" style="margin-top: 50px !important">
              <el-button @click="noticeFromVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </div>
          </el-dialog>
        </div>

      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {eventBus} from "@/main";
import {
  deleteBatchNotice,
  deleteBatchSwiper, deleteNotice,
  deleteSwiper, loadNotice,
  loadSwiper, saveNotice,
  saveSwiper, updateOpen,
  updateSwiperStatus
} from "@/api/systemManage/noticeManage";

export default {
  data() {
    return{
      activeName: 'swiperInfo',
      swiperData: [],
      noticeData:[],
      pageNumOne:1,
      pageSizeOne:10,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      banner: '',
      title:'',
      loading: false,
      Striped: false,
      form: '',
      noticeForm: '',
      btnText: '',
      dialogTitle: '',
      noticeDialogTitle: '',
      fromVisible: false,
      noticeFromVisible: false,
      user:JSON.parse(localStorage.getItem('manage_config') || "{}"),
      rules: {
        banner:  { required: true, message: '请上传图片或输入图片网址', trigger:'blur' },
      },
      rule: {
        title: { required: true, message: '请输入标题', trigger: 'blur' },
        content: { required: true, message: '请输入通知内容', trigger: 'blur' },
      }
    }
  },

  methods: {
    changeStriped() {
      this.isStriped = !this.isStriped;
      // 在选择switch时将选择状态保存到本地存储
      localStorage.setItem('changeStriped', JSON.stringify(this.Striped));
    },
    //轮播图
    handleSelectionChange(rows){
      this.ids = rows.map(v => v.id)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAdd(){       //新增数据
      this.form = {}   //新增数据的时候清空数据
      this.dialogTitle = '新增图片' //修改dialog标题
      this.btnText = '上传图片'
      this.fromVisible = true //打开弹窗
    },
    deleteBatchSwiper() {
      if (!this.ids || this.ids.length === 0) {
        this.$message.warning('未选择数据！');
        return;
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deleteBatchSwiper(this.ids).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功！')
            this.loading = false
            eventBus.$emit('updateBanner');
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      })
          .catch(() =>{})
    },
    updateDisplay(row) {
      const status = !!row.status; // 取反status的值
      updateSwiperStatus(row.id, status)
         .then(response => {
            this.status = status;
            eventBus.$emit('updateBanner');
          }).catch({ })
    },
    handleBannerSuccess(response, file, fileList){
      this.form.banner = response.data
    },
    handleEdit(index, row) {  // 编辑数据
      this.dialogTitle = '编辑轮播图' //修改dialog标题
      this.btnText = '更改图片'
      this.form = JSON.parse(JSON.stringify(row))  //给form对象赋值 注意要深拷贝数据
      this.fromVisible = true//打开弹窗
    },
    handleDeleteSwiper(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            deleteSwiper(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    eventBus.$emit('updateBanner');
                    this.load()
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() =>{})
    },
    saveSwiper() {  // 保存按钮触发的逻辑 它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          saveSwiper(this.form)
            .then(res => {
            if (res.code === '200') {
              eventBus.$emit('updateBanner');
              this.$message.success('保存成功')
              this.load(1)
              this.loading = false
              this.fromVisible = false
            } else {
              this.$message.error('保存失败')
            }
          })
        }
      })
    },
    load(pageNum, pageNumOne){
      if (pageNum) this.pageNum = pageNum
        loadSwiper(this.pageNum, this.pageSize, this.banner)
        .then(res => {
        this.noticeData = []
        this.swiperData = res.data.records
        this.total = res.data.total
        this.loading = false
      })
      if (pageNumOne) this.pageNumOne = pageNumOne
      loadNotice(this.pageNumOne, this.pageSizeOne, this.title)
          .then(res =>{
            this.noticeData = res.data.records
            this.total = res.data.total
            this.loading = false
          })
    },

    //系统通知
    handleSelection(rows){
      this.ids = rows.map(v => v.id)
    },
    handleCurrent(pageNumOne) {
      this.load(pageNumOne)
    },
    handleAddNotice() {       //新增数据
      this.noticeForm = {}   //新增数据的时候清空数据
      this.noticeDialogTitle = '新增通知' //修改dialog标题
      this.noticeFromVisible = true //打开弹窗
    },
    handleEditNotice(index, row) {
      this.noticeDialogTitle = '编辑通知' //修改dialog标题
      this.noticeForm = JSON.parse(JSON.stringify(row))  //给form对象赋值 注意要深拷贝数据
      this.noticeFromVisible = true//打开弹窗
    },
    handleDelete(index, id) {
      this.$confirm('确认删除这条信息？', '确认删除', {type: 'warning'})
          .then(() => {
            deleteNotice(id)
                .then(res => {
                  if (res.code === '200') {
                    this.$message.success('操作成功！')
                    // eventBus.$emit('updateBanner');
                    this.load()
                  } else {
                    this.$message.error(res.msg)
                  }
                })
          })
          .catch(() =>{})
    },
    update(row) {
      const open =  !!row.open
      updateOpen(row.id, open)
          .then(response =>{
            this.open = open
          }).catch({ })
    },
    save() {  // 保存按钮触发的逻辑 它会触发新增或者更新
      this.$refs.noticeFormRef.validate((valid) => {
        if (valid) {
          saveNotice(this.noticeForm)
              .then(res => {
                if (res.code === '200') {
                  // eventBus.$emit('updateBanner');
                  this.$message.success('保存成功')
                  this.load(1)
                  this.loading = false
                  this.noticeFromVisible = false
                } else {
                  this.$message.error('保存失败')
                }
              })
        }
      })
    },
    deleteBatchNotice() {
      if (!this.ids || this.ids.length === 0) {
        this.$message.warning('未选择数据！');
        return;
      }
      this.$confirm('确认删除这些信息？', '确认删除', {type: 'warning'}).then(() => {
        deleteBatchNotice(this.ids).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功！')
            this.loading = false
            //eventBus.$emit('updateBanner');
            this.load(null,1)
          } else {
            this.$message.error(res.msg)
          }
        })
      })
          .catch(() =>{})
    },
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
.swiperInfoStyle{
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 98%;
  margin: 0 auto;
}
.noticeInfoStyle {
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

</style>
