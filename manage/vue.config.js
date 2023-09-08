const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave:false,
  devServer:{
    port: 5400
  },
  chainWebpack:config => {
    config.plugin('html')
        .tap(args => {
          args[0].title = "充电桩后台管理系统"
          return args;
        })
  }
})
