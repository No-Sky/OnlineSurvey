<template>
  <div>
    <Header />
    <div></div>
    <Index />
  </div>
</template>

<script>
// @ is an alias to /src
import Header from '@/components/Header.vue'
import Index from '@/components/Index.vue'

export default {
  name: 'Home',
  components: {
    Header, Index
  }
}
</script>
