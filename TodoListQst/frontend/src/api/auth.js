import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/api', // 代理前缀
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

export default {
    /**
     * 用户注册
     * @param {Object} userData - 用户注册数据
     * @returns {Promise} - 注册结果
     */
    async register(userData) {
        try {
            const response = await apiClient.post('/v1/user/register', userData);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.message || '注册失败，请稍后再试');
        }
    },

    /**
     * 用户登录
     * @param {Object} credentials - 登录凭证
     * @returns {Promise} - 登录结果
     */
    async login(credentials) {
        try {
            const response = await apiClient.post('/v1/user/login', credentials);
            return response.data;
        } catch (error) {
            throw new Error(error.response?.data?.message || '登录失败，请检查用户名和密码');
        }
    }
};