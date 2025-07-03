import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue'),
        meta: { guestOnly: true }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/RegisterView.vue'),
        meta: { guestOnly: true }
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/',
        redirect: '/login'
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 路由守卫：控制访问权限
router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('token');

    // 如果需要登录但用户未登录，重定向到登录页
    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/login');
    }
    // 如果是游客页面但用户已登录，重定向到仪表盘
    else if (to.meta.guestOnly && isAuthenticated) {
        next('/dashboard');
    }
    // 其他情况正常访问
    else {
        next();
    }
});

export default router;