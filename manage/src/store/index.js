import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        tabList:[]
    },
    mutations: {
        addTab: (state, tab) => {
            // 如果tab已经存在，不添加新的tabs
            if (state.tabList.some(item => item.path === tab.path)) return
            state.tabList.push(tab)
        }
    },
    getters: {
        // 获取tbsList
        getTabs: (state) => {
            return state.tabList
        }
    },
    actions: {
    },
    modules: {
    }
})