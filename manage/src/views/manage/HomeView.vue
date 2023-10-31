<template>
  <div>

  <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding:10px 20px; border-radius: 8px; margin-bottom: 20px;
  background-color: white">
    <i class="el-icon-broadcast" style="margin-right: 5px"></i> 本系统仅供内部测试使用，不做商业用途。
  </div>
  <div class="carousel_cl" style="padding: 10px 20px; margin-bottom: 20px;">
    <el-carousel indicator-position="outside" :height="banH +'px'">
      <el-carousel-item v-for="item in 4" :key="item">

      </el-carousel-item>
    </el-carousel>
  </div>
  </div>
</template>

<script>
export default {
  name: "HomeView",
  data(){
    return{
      banH:300,
    }
  },

  methods: {

    setbanH() {

      this.banH = 250

    }

  },

  mounted() {

    this.setbanH()

    window.addEventListener('resize', () => {

      this.setbanH()

    }, false)

  },


}
</script>

<style scoped>
.carousel_cl{
  width: 512px;
  height: 400px;
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