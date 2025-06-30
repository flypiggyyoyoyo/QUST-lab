// src/api/hello.js
import axios from 'axios'
const service = axios.create({
    baseURL: '/api', // 与Vite代理路径一致
    timeout: 5000
})

export function getHello() {
    return service.get('/hello') // 实际请求路径：/api/hello
}