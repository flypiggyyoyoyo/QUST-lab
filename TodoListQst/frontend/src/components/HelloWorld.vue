<!-- src/components/HelloWorld.vue -->
<template>
  <div class="hello-container">
    <h1 v-if="!loading">{{ message }}</h1>
    <div v-else>加载中...</div>
    <div v-if="error" class="error-message">{{ error }}</div>
    <button @click="fetchHello">重新获取消息</button>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useHelloAPI } from '@/api/hello'

// 解构获取组合函数返回的状态和方法
const { message, loading, error, fetchHello } = useHelloAPI()

// 组件挂载时自动调用API
onMounted(() => {
  fetchHello()
})
</script>

<style scoped>
.error-message {
  color: red;
  margin-top: 10px;
}
</style>