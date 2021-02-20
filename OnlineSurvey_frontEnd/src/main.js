import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import VueClipboard from 'vue3-clipboard'
// import axiosPlugins from './plugins/axios'
import axios from 'axios'
import store from './store'

const app = createApp(App).use(store)
installElementPlus(app)
app.config.globalProperties.$axios = axios
app.use(VueClipboard)
app.use(router)
app.mount('#app')