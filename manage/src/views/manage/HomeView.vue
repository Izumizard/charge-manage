<template>
  <div>
  <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding:10px 20px; border-radius: 8px; margin-bottom: 20px;
  background-color: white">
    <i class="el-icon-broadcast" style="margin-right: 5px"></i> 本系统仅供内部测试使用，不做商业用途。
  </div>
  <div class="carousel_cl" style="padding: 10px 20px; margin-bottom: 20px;">
    <el-carousel indicator-position="outside" :height="banH +'px'" >
      <el-carousel-item v-for="item in items" :key="item">
        <img :src="item" alt="banner">
      </el-carousel-item>
    </el-carousel>
  </div>
    <div style="display: flex">
    <el-card style="width: 31%">
      <div style="margin-bottom: 15px; font-size: 24px; font-weight: bold">系统公告</div>
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item v-for="(item, index) in notices" :key="item.id"  :name="index + ''">
          <template slot="title">
            <h4>{{ item.title }}</h4>
            <p style="margin-left:5px">-</p>
            <p style="margin-left: 5px; font-weight: 350">{{item.time}}</p>
          </template>
          <p>{{ item.content }}</p>
        </el-collapse-item>
      </el-collapse>
    </el-card></div>
  </div>
</template>

<script>
import {eventBus} from "@/main";
import {notice, swiper} from "@/api/manage/home";

export default {
  name: "HomeView",
  data(){
    return{
      items:[],
      banH:300,
      notices: [],
      activeName: '0'
    }
  },
  created() {
    this.loadNotice()
    eventBus.$on('updateBanner', () => {
      swiper().then(response => {
        this.items =  response.data.filter(item => item.status).map(item =>item.banner)
      })
    })
  },
  methods: {
    setbanH() {
      this.banH = 250
    },
    loadNotice() {
      notice().then(res => {
        this.notices = res.data
      })
    }
  },

  mounted() {

    this.setbanH()

    window.addEventListener('resize', () => {

      this.setbanH()

    }, false)
    swiper().then(response => {
      this.items =  response.data.filter(item => item.status).map(item =>item.banner)
    })
  },


}
</script>

<style scoped>
.carousel_cl{
  width: 512px;
  height: 300px;
}
::v-deep .el-carousel__container {
  border-radius: 8px;
  overflow:hidden;
  transform: translateY(0);
}

::v-deep .el-carousel__item {
  border-radius: 8px;

}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

</style>
