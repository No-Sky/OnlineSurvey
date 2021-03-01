import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import { VueClipboard } from '@soerenmartius/vue3-clipboard'
import store from './store'

const app = createApp(App)
installElementPlus(app)
app.use(store)
app.use(VueClipboard)
app.use(router)
app.mount('#app')