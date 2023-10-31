<template>
  <div id="app">
    <router-view v-if="show"/>
  </div>
</template>

<script>
export default {
  name: 'App',
  provide () {
    return{
      reload: this.reload
    }
  },
  data() {
    return {
      show: true
    }
  },

  methods:{
    reload(){
      this.show = false
      this.$nextTick(function(){
        this.show = true
      })
    }
  }
}
</script>