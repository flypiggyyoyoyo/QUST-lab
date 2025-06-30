<template>
  <div id="app" style="font-family: Arial, sans-serif; padding: 2rem;">
    <h1>前后端分离示例</h1>
    <button @click="fetchHello" :disabled="loading">
      {{ loading ? '加载中...' : '获取后端 Hello' }}
    </button>
    <p v-if="error" style="color: red;">请求出错：{{ error }}</p>
    <p v-else-if="message">后端返回：<strong>{{ message }}</strong></p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'App',
  data() {
    return {
      message: '',
      error: '',
      loading: false
    }
  },
  methods: {
    async fetchHello() {
      this.loading = true
      this.error = ''
      this.message = ''
      try {
        // 如果前端开发服务器和后端在不同端口，axios 默认同源请求会被阻止。
        // 你已经在后端配置了允许跨域，所以这里直接请求即可。
        const res = await axios.get('http://localhost:8080/api/hello')
        this.message = res.data
      } catch (err) {
        this.error = err.response
            ? `${err.response.status} ${err.response.statusText}`
            : err.message
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style>
button {
  cursor: pointer;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background: #42b983;
  color: #fff;
}
button:disabled {
  background: #a5d6a7;
  cursor: not-allowed;
}
</style>
