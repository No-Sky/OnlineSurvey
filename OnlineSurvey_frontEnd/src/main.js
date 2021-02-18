import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import VueClipboard from 'vue3-clipboard'
import './plugins/axios'
import store from './store'

const app = createApp(App).use(store)
installElementPlus(app)
app.use(VueClipboard)
app.use(router)
app.mount('#app')