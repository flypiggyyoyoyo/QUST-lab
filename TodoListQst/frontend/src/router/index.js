// router/index.js
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/auth', // 登录/注册页面路径
        name: 'Auth',
        // 确保组件路径正确（检查文件是否存在于 src/views 下）
        component: () => import('@/views/AuthView.vue'),
        meta: { guestOnly: true }
    },
    {
        path: '/dashboard', // 仪表盘路径
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/', // 根路径重定向到登录页
        redirect: '/auth'
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 临时注释路由守卫（先排除守卫干扰）
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = localStorage.getItem('token');
//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/auth');
//   } else if (to.meta.guestOnly && isAuthenticated) {
//     next('/dashboard');
//   } else {
//     next();
//   }
// });

export default router;