import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import VueClipboard from 'vue3-clipboard'

const app = createApp(App)
installElementPlus(app)
app.use(VueClipboard)
app.use(router)
app.mount('#app')