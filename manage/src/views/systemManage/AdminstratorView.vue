<template>
  <div class="userSettingStyle">
    <el-tabs tab-position="left" v-model="activeName">
      <el-tab-pane label="我的信息" name="myInfo">
        <!-- 标头 -->
        <div class="settingStyleTitle">我的信息</div>
        <!-- 用户信息 -->
        <div style="display: flex">
          <div class="userImageStyle">
            <div class="userImageSpan">用户头像</div>
            <div style="height: 160px; margin-bottom: 12px">
              <el-image
                  :model="user"
                  :src="user.avatar ||'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'"
                  style="width: 160px; height: 160px; border-radius: 8px; margin-bottom: 35px"
              ></el-image>
              <el-button icon="el-icon-upload2" type="primary" plain @click="openUpload">
                更换头像
              </el-button>
              <!-- 上传弹窗 -->
              <el-dialog
                  :visible.sync="uploadDialog"
                  title="更换头像"
                  width="560px"
                  :close-on-click-modal="false"
                  @open="openDialog"
                  @close="handleClose"
                  v-loading="loading"
              >
                <div class="container">
                  <!-- 左侧裁剪区 -->
                  <div class="left">
                    <!-- 大图显示区 -->
                    <div class="big-image-preview">
                      <img :src="imgUrl || this.user.avatar" alt="大图" class="big-image" ref="imgRef"/>
                    </div>
                    <div class="tool">
                      <i class="el-icon-refresh-left" @click="rotateImage(-45)"></i>
                      <i class="el-icon-circle-plus-outline" @click="zoomImage(0.2)"></i>
                      <i class="el-icon-remove-outline" @click="zoomImage(-0.2)"></i>
                      <i class="el-icon-refresh-right" @click="rotateImage(45)"></i>
                    </div>
                  </div>
                  <!-- 右侧预览区 -->
                  <div class="right">
                    <!-- 小图预览区域 -->
                    <div class="right-top">
                      <div>预览</div>
                      <div
                          class="image-view"
                          :style="{ width: '100px', height: '100px', 'border-radius': shape === 'default' ? '10px' : '50%' }"></div>
                      <div>100 x 100</div>
                      <div
                          class="image-view"
                          :style="{ width: '50px', height: '50px', 'border-radius': shape === 'default' ? '10px' : '50%' }"></div>
                      <div>50 x 50</div>
                    </div>
                    <!-- 按钮 -->
                    <div class="right-bottom">
                      <div>
                        <el-button type="primary" size="small" @click="chooseImage">上传</el-button>
                        <el-button type="primary" size="small" @click="uploadImage">确定</el-button>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 只用input来实现上传，但是不显示input -->
                <input
                    v-show="false"
                    ref="fileRef"
                    type="file"
                    @change="getImageInfo"/>
              </el-dialog>
            </div>


          </div>
          <!-- 用户设置表单 -->
          <div class="userSettingForm">
            <el-form :model="user" label-width="80px" class="settingform" style="margin-top: 30px">
              <el-form-item label="用户名：" prop="username">
                <span style="display: flex; justify-content: center; font-size: 18px">{{ user.username }}</span>
              </el-form-item>
              <el-form-item label="姓名：" prop="name">
                <el-input v-model="user.name" placeholder="姓名"></el-input>
              </el-form-item>
              <el-form-item label="电话：" prop="phone">
                <el-input v-model="user.phone" placeholder="电话"></el-input>
              </el-form-item>
              <el-form-item label="邮箱：" prop="email">
                <el-input v-model="user.email" placeholder="邮箱"></el-input>
              </el-form-item>
              <el-form-item label="地址：" prop="address">
                <el-input type="textarea" v-model="user.address" placeholder="居住地址"></el-input>
              </el-form-item>
              <div style="display: flex; justify-content: center;margin-top: 30px">
                <el-button type="primary" icon="el-icon-check"
                           style="margin-right: 580px"
                           @click="update">保 存
                </el-button>
              </div>
            </el-form>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="修改密码" name="changePwd">

        <!-- 标头 -->
        <div class="settingStyleTitle">修改密码</div>

        <!-- 修改密码  -->
        <div class="pwdChangeStyle">
          <el-form :model="user" label-width="80px" class="pwdSettingForm" :rules="rules" ref="changePwdRef">
            <el-form-item label="当前密码：" prop="password" style="margin-top:30px; width: 350px">
              <el-input show-password placeholder="当前密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码：" prop="newPwd" style="margin-top:10px; width: 350px">
              <el-input show-password v-model="user.newPwd" placeholder="新密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码：" prop="confirmPwd" style="margin-top:10px; width: 350px">
              <el-input show-password v-model="user.confirmPwd" placeholder="确认密码"></el-input>
            </el-form-item>
            <div style="margin-top: 30px;margin-bottom: 20px">
              <el-button type="primary" icon="el-icon-check"
                         style=""
                         @click="confirm">确 认
              </el-button>
            </div>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {updateUser, uploadAvatar} from "@/api/systemManage/adminstrator";
import 'cropperjs/dist/cropper.css';
import Cropper from 'cropperjs';

export default {
  props: {
    // 形状
    shape: {
      type: String,
      default: 'round',
      validator: (val) => {
        return ['default', 'round'].includes(val);
      }
    },
    //原始头像
    originImgUrl: {
      type: String,
      default: ''
    }
  },
  data() {
    const validatePwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPwd) {
        callback(new Error('两次密码输入不同,请检查后重试！'))
      } else if (value === this.user.password) {
        callback(new Error('新密码不能与当前密码一致'));
      } else {
        callback()
      }
    }
    const validateNewPwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认新密码'))
      } else if (value === this.user.password) {
        callback(new Error('新密码不能与当前密码一致'));
      } else {
        callback();
      }
    }
    return {
      activeName: 'myInfo',
      uploadDialog: false,
      cropper: '',
      fileName: '' || "avatar.png",
      fileType: '' || "image/png",
      fileRef: null,
      imgUrl: this.originImgUrl,
      loading: false,
      user: JSON.parse(localStorage.getItem('manage_config') || "{}"),

      rules: {
        password: [
          {
            required: true, message: '请输入当前密码', trigger: 'blur'
          },
        ],
        newPwd: [
          {
            validator: validateNewPwd, required: true, trigger: 'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
        confirmPwd: [
          {
            validator: validatePwd, required: true, trigger: 'blur'
          },
          {
            min: 6, max: 20, message: '密码长度太短', trigger: 'blur'
          }
        ],
      }
    }
  },

  methods: {
    openDialog() {
      this.cropImage()
      this.loading = false
    },
    handleClose() {
      if (this.cropper && typeof this.cropper.destroy === "function") {
        this.cropper.destroy();
      }
      this.uploadDialog = false
    },
    openUpload() {
      this.uploadDialog = true
      this.loading = true
    },
    rotateImage(angle) {
      this.cropper.rotate(angle)
    },
    zoomImage(num) {
      this.cropper.zoom(num)
    },
    chooseImage() {
      // 当input的type属性值为file时，点击方法可以进行选取文件
      this.$refs.fileRef.click()
    },

    // 预览头像
    uploadImage() {
      let cas = this.cropper.getCroppedCanvas()
      cas.toBlob((blob) => {
        this.updateBlob = blob
        this.user.avatar = URL.createObjectURL(blob)
      }, this.fileType)
      this.uploadDialog = false
    },
    getImageInfo(e) {
      // 上传的文件
      let file = e.target.files[0]
      this.fileName = file.name.toString();
      this.fileType = file.type.toString();
      // 获取 window 的 URL 工具
      let URL = window.URL || window.webkitURL
      // 通过 file 生成目标 url
      this.imgUrl = URL.createObjectURL(file)
      this.$nextTick(() => {
        // 判定裁剪对象是否存在
        // 存在销毁重新创建（这里不替换图片，图片不一样大时会变形），不存在创建对象
        if (this.cropper) {
          this.cropper.destroy()
          this.cropImage()
        } else {
          this.cropImage()
        }
      });
    },
    cropImage() {
      if (this.$refs.imgRef) {
        this.cropper = new Cropper(this.$refs.imgRef, {
          // 宽高比
          aspectRatio: 1,
          viewMode: 1,
          // 预览
          preview: '.image-view',
          // cropBoxResizable: false,
          background: false,
          crop(event) {
          }
        });
      }
    },
    confirm() {
      this.$refs.changePwdRef.validate((valid) => {
        if (valid) {
          this.user.password = this.user.newPwd
          //保存当前的用户信息到数据库
          updateUser(this.user).then(res => {
            if (res.code === '200') {
              //成功更新
              this.$message.success('密码已更改！')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    //封装的更新用户信息方法
    updateUserInfo() {
      updateUser(this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('资料已更新！')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('manage_config', JSON.stringify(this.user))
          // 触发父级更新
          this.$emit('update:user', this.user)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    update() {
      //保存当前的用户信息到数据库
      if (this.updateBlob) {
        const file = new File([this.updateBlob], this.fileName, {type: this.fileType})
        let formData = new FormData()
        formData.append("file", file)
        uploadAvatar(formData).then((res) => {
          this.user.avatar = res.data
          this.updateUserInfo()
        })
      } else if (!this.updateBlob) { // 如果没有更新头像，则直接更新用户信息
        this.updateUserInfo()
      }
    },
  },

  created() {
    const tab = this.$route.query.tab;
    if (tab) {
      this.activeName = tab;
    }
  },

  watch: {
    '$route.query.tab'(tab) {
      if (tab) {
        this.activeName = tab;
      }
    }
  },
}
</script>

<style scoped>
@import '../../assets/css/cropperjs.css';

.userSettingStyle {
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

.userSettingForm {
  text-align: left;
  margin-left: 10px;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 100%;
}

.pwdChangeStyle {
  text-align: left;
  border: 1px solid #ebeef5;
  padding: 16px;
  border-radius: 12px;
  width: 80%;
  margin: 0 auto;
}

.pwdSettingForm {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

/deep/ .el-form-item__label {
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

.el-image {
  margin-top: 70px;
}

.el-button {
  border-radius: 12px !important;
}

/deep/ .el-input__inner,
/deep/ .el-textarea__inner {
  border-radius: 8px !important;
  overflow: hidden;
}
</style>
