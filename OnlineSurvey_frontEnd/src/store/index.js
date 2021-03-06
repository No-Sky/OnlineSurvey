import { createStore } from 'vuex'

export default createStore({
  state: {
    isLogin: false,
  },
  //获取属性的状态
  getters: {
    //获取登录状态
    isLogin: state => state.isLogin,
  },
  //设置属性状态  
  mutations: {
    //保存登录状态
    userStatus(state, flag) {
      state.isLogin = flag
    },
  },
  //应用mmutations
  actions: {
    //获取登录状态
    userStatus ({commit}, flag) {
      commit('userStatus', flag)  
    }
  },
  modules: {
  }
})
