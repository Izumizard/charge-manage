<template>
  <div>
    <el-dialog
        class="container"
        append-to-body
        title="查看地图"
        :visible.sync="visible"
        :before-close="handleClose"
        :close-on-click-modal="false"
        top="0vh"
        width="100%"
    >
      <div>
        <el-amap-search-box id="search" class="search-box" :search-option="searchOption" :on-search-result="onSearchResult" />
        <el-amap class="amap-box" :zoom="amap.zoom" :center="amap.center" :plugin="amap.plugin" :events="amap.events">
          <el-amap-marker v-for="(marker, index) in amap.markers" :key="'marker' + index" :position="marker.position" />
          <el-amap-text
              v-for="(marker, index) in amap.markers"
              :key="'text' + index"
              :text="marker.text"
              :offset="marker.offset"
              :position="marker.position"
          />
        </el-amap>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sure">确 定</el-button>
        <el-button @click="handleClose">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
function getFormData() {
  return {
    lat: '39.909186',
    lon: '116.39746',
    orgAddr: '区政府',
    province: '',
    city: '',
    district: ''
  }
}
import { AMapManager, lazyAMapApiLoaderInstance } from 'vue-amap'
const amapManager = new AMapManager()
export default {
  name: 'Map',
  data() {
    const vm = this
    return {
      visible: false,
      amapManager,
      // form对象
      dataForm: getFormData(),
      // 地图搜索对象
      searchOption: {
        city: '全国',
        citylimit: true
      },
      // 地图对象
      amap: {
        zoom: 16,
        center: [116.319802, 39.98294],
        events: {
          // 点击获取地址的数据
          click(e) {
            const { lng, lat } = e.lnglat
            vm.dataForm.lon = lng
            vm.dataForm.lat = lat
            // 这里通过高德 SDK 完成。
            var geocoder = new AMap.Geocoder({
              radius: 1000,
              extensions: 'all'
            })
            geocoder.getAddress([lng, lat], function (status, result) {
              if (status === 'complete' && result.info === 'OK') {
                if (result && result.regeocode) {
                  // console.log('点击获取地址的数据', result)
                  vm.dataForm.orgAddr = result.regeocode.formattedAddress
                  vm.dataForm.province = result.regeocode.addressComponent.province
                  vm.dataForm.city = result.regeocode.addressComponent.city
                  vm.dataForm.district = result.regeocode.addressComponent.district
                  vm.dataForm.lat = lat ? lat.toString() : ''
                  vm.dataForm.lon = lng ? lng.toString() : ''
                  vm.amap.markers = []
                  const obj = {
                    position: [lng, lat],
                    text: result.regeocode.formattedAddress,
                    offset: [0, 30]
                  }
                  vm.amap.markers.push(obj)
                }
              }
            })
          }
        },
        plugin: ['ToolBar'],
        markers: [
          {
            position: [116.319802, 39.98294],
            text: '中关村',
            offset: [0, 30]
          }
        ]
      }
    }
  },
  methods: {
    // 地图搜索回调
    onSearchResult(pois) {
      const vm = this
      vm.amap.markers = []
      let latSum = 0
      let lngSum = 0
      if (pois.length > 0) {
        pois.forEach((poi, index) => {
          const { lng, lat } = poi
          if (index === 0) {
            lngSum = lng
            latSum = lat
            const obj = {
              position: [poi.lng, poi.lat],
              text: poi.name,
              offset: [0, 30]
            }
            vm.amap.markers.push(obj)
            console.log('地图搜索回调', poi)
            vm.dataForm.orgAddr = poi.name
            vm.dataForm.lat = poi.lat ? poi.lat.toString() : ''
            vm.dataForm.lon = poi.lng ? poi.lng.toString() : ''
          }
        })
        vm.amap.center = [lngSum, latSum]
      }
    },
    // 打开弹窗
    open(data) {
      const vm = this
      vm.dataForm = getFormData()
      if (data) {
        if (data.longitude == 'undefined' && data.latitude == 'undefined') {
          vm.amap.markers = [
            {
              position: [116.319802, 39.98294],
              text: '区政府',
              offset: [0, 30]
            }
          ]
          vm.amap.center = [116.319802, 39.98294]
        } else {
          //通过已有的数据进行回显标记
          vm.amap.markers = [
            {
              position: [data.longitude, data.latitude],
              text: data.address,
              offset: [0, 30]
            }
          ]
          vm.amap.center = [data.longitude, data.latitude]
        }
      }
      vm.visible = true
    },
    // 关闭弹窗
    handleClose() {
      const vm = this
      vm.visible = false
      vm.dataForm = getFormData()
      vm.amap.markers = []
      const obj = {
        position: [vm.dataForm.lon, vm.dataForm.lat],
        text: vm.dataForm.orgAddr,
        offset: [0, 30]
      }
      vm.amap.markers.push(obj)
      vm.amap.center = [vm.dataForm.lon, vm.dataForm.lat]
    },
    // 提交方法
    sure() {
      const vm = this
      this.$emit(
          'updateLocation',
          vm.dataForm.orgAddr,
          vm.dataForm.lon,
          vm.dataForm.lat,
          vm.dataForm.province,
          vm.dataForm.city,
          vm.dataForm.district
      )
      vm.handleClose()
    }
  }
}
</script>

<style scoped>
.container {
  width: 50% !important;
  height: 72%;
  margin: auto;
  overflow: hidden;
}
.amap-box {
  height: 45vh;
}
.search-box {
  position: absolute;
  z-index: 5;
  width: 70%;
  left: 13%;
  top: 15px;
  height: 30px;
}
/deep/.el-vue-search-box-container .search-box-wrapper{
  float: left;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  border-radius: 0 7px 7px 0;
  outline: none;
  position: relative;
}
/deep/.el-vue-search-box-container .search-box-wrapper .search-btn{
  position: absolute;
  right: 0;
  top: 1px;
  width: 20%;
  border-radius: 0 7px 7px 0;
  border: none;
  outline: none;
  background: #38a28a;
  color: #fff;
}
.tip-box {
  text-align: center;
  width: 100%;
  position: absolute;
  top: 35px;
  padding: 10px 0;
  background-color: #fff;
  opacity: 0.8;
}
</style>

