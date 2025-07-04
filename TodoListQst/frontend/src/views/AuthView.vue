<template>
  <div class="auth-container">
    <div class="background"></div>

    <div class="auth-content">
      <GlassCard class="auth-card">
        <div class="brand-header">
          <div class="logo">
            <div class="logo-circle"></div>
            <span>FlyTODO</span>
          </div>
          <h1>Welcome to FlyTODO</h1>
        </div>

        <!-- 登录表单 -->
        <form v-if="isLogin" @submit.prevent="handleLogin">
          <div class="input-group">
            <label for="username">Users name or Email</label>
            <div class="input-wrapper">
              <input
                  type="text"
                  id="username"
                  v-model="loginForm.username"
                  placeholder="flypiggy"
              >
            </div>
          </div>

          <div class="input-group">
            <label for="password">Password</label>
            <div class="input-wrapper">
              <input
                  type="password"
                  id="password"
                  v-model="loginForm.password"
                  placeholder="**********"
              >
            </div>
          </div>

          <div class="divider"></div>

          <div class="slogan">
            <h3>Time is money</h3>
            <p>Turn your to-dos into to-dones, one step at a time.</p>
          </div>

          <button type="submit" class="signin-button" :disabled="loginLoading">
            {{ loginLoading ? 'Signing in...' : 'Sign in' }}
          </button>

          <div class="or-divider">
            <span>or</span>
          </div>

          <button
              class="google-signin-button"
              @click="handleSocialLogin('google')"
              :disabled="socialLoading"
          >
            <i v-if="!socialLoading" class="fab fa-google"></i>
            <i v-else class="fas fa-spinner fa-spin"></i>
            Sign in with Google
          </button>

          <div class="auth-footer">
            <p>New Lovebirds? <a href="#" @click.prevent="switchToRegister">Create Account</a></p>
          </div>
        </form>

        <!-- 注册表单 -->
        <form v-else @submit.prevent="handleRegister">
          <div class="input-group">
            <label for="username">userName</label>
            <div class="input-wrapper">
              <input
                  type="text"
                  id="username"
                  v-model="registerForm.username"
                  placeholder="flypiggy"
              >
            </div>
          </div>

          <div class="input-group">
            <label for="email">Email Address</label>
            <div class="input-wrapper">
              <input
                  type="email"
                  id="email"
                  v-model="registerForm.email"
                  placeholder="fly@example.com"
              >
            </div>
          </div>

          <div class="input-group">
            <label for="reg-password">Password</label>
            <div class="input-wrapper">
              <input
                  type="password"
                  id="reg-password"
                  v-model="registerForm.password"
                  placeholder="**********"
              >
            </div>
          </div>

          <div class="input-group">
            <label for="confirm-password">Confirm Password</label>
            <div class="input-wrapper">
              <input
                  type="password"
                  id="confirm-password"
                  v-model="registerForm.confirmPassword"
                  placeholder="**********"
              >
            </div>
          </div>

          <div class="terms">
            <label class="terms-checkbox">
              <input type="checkbox" v-model="registerForm.acceptTerms" />
              <span>I agree to the <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a></span>
            </label>
          </div>

          <button type="submit" class="signin-button" :disabled="registerLoading">
            {{ registerLoading ? 'Creating account...' : 'Create Account' }}
          </button>

          <div class="auth-footer">
            <p>Already have an account? <a href="#" @click.prevent="switchToLogin">Sign in</a></p>
          </div>
        </form>
      </GlassCard>

      <!-- 超时提示弹窗 -->
      <div v-if="showTimeout" class="timeout-modal">
        <div class="timeout-content">
          <i class="fas fa-exclamation-triangle timeout-icon"></i>
          <h3>Connection Timeout</h3>
          <p>Unable to connect to Google service. Please try again later.</p>
          <button class="timeout-button" @click="showTimeout = false">
            Back to Login
          </button>
        </div>
      </div>

      <!-- 错误提示弹窗 -->
      <div v-if="showError" class="error-modal">
        <div class="error-content">
          <i class="fas fa-exclamation-circle error-icon"></i>
          <h3>{{ errorTitle }}</h3>
          <p>{{ errorMessage }}</p>
          <button class="error-button" @click="showError = false">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount } from 'vue';
import axios from 'axios';
import GlassCard from '@/components/GlassCard.vue';
import { useRouter } from 'vue-router'; // 新增：引入路由钩子

// 配置axios基础URL
axios.defaults.baseURL = 'http://localhost:8080/api/v1/user'; // 根据实际后端地址修改

// 创建路由实例
const router = useRouter();

// 登录/注册状态
const isLogin = ref(true);

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
});

// 注册表单数据
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  acceptTerms: false
});

// 加载状态
const loginLoading = ref(false);
const registerLoading = ref(false);
const socialLoading = ref(false);

// 错误提示
const showError = ref(false);
const errorTitle = ref('');
const errorMessage = ref('');

// 超时提示
const showTimeout = ref(false);
let timeoutTimer = null;

// 切换到注册页面
const switchToRegister = () => {
  isLogin.value = false;
};

// 切换到登录页面
const switchToLogin = () => {
  isLogin.value = true;
};

const handleLogin = async () => {
  loginLoading.value = true;

  try {
    // 发送登录请求
    const response = await axios.post('/login', {
      username: loginForm.username,
      password: loginForm.password
    });

    // 登录成功，存储token和用户信息
    // 注意：根据后端LoginResponse类调整字段名
    localStorage.setItem('token', response.data.accessToken); // 直接使用accessToken字段
    localStorage.setItem('user', JSON.stringify({
      id: response.data.userId,           // 映射后端的userId到前端的id
      username: response.data.username,  // 直接使用username字段
      email: response.data.userEmail     // 映射后端的userEmail到前端的email
    }));

    // 登录成功后跳转到仪表盘
    await router.push('/dashboard');
    console.log('登录成功，跳转至仪表盘');

  } catch (error) {
    // 处理登录失败
    console.error('登录失败:', error);

    if (error.response) {
      errorTitle.value = 'Login Failed';
      errorMessage.value = error.response.data.message || '登录失败，请检查用户名和密码';
    } else {
      errorTitle.value = 'Network Error';
      errorMessage.value = '无法连接到服务器，请稍后再试';
    }

    showError.value = true;
  } finally {
    loginLoading.value = false;
  }
};

// 处理注册
const handleRegister = async () => {
  // 前端验证
  if (registerForm.password !== registerForm.confirmPassword) {
    errorTitle.value = 'Validation Error';
    errorMessage.value = "Passwords don't match";
    showError.value = true;
    return;
  }

  if (!registerForm.acceptTerms) {
    errorTitle.value = 'Validation Error';
    errorMessage.value = 'Please accept the terms and conditions';
    showError.value = true;
    return;
  }

  registerLoading.value = true;

  try {
    // 发送注册请求
    const response = await axios.post('/register', {
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    });

    // 注册成功，提示用户并切换到登录页
    alert('注册成功，请登录');
    switchToLogin();

  } catch (error) {
    // 处理注册失败
    console.error('注册失败:', error);

    if (error.response) {
      // 服务器返回错误状态码
      errorTitle.value = 'Registration Failed';
      errorMessage.value = error.response.data.message || '注册失败，请稍后再试';
    } else {
      // 网络错误或其他错误
      errorTitle.value = 'Network Error';
      errorMessage.value = '无法连接到服务器，请稍后再试';
    }

    showError.value = true;
  } finally {
    registerLoading.value = false;
  }
};

// 处理社交登录（保持原有模拟逻辑）
const handleSocialLogin = (platform) => {
  socialLoading.value = true;

  // 模拟2秒后超时
  timeoutTimer = setTimeout(() => {
    socialLoading.value = false;
    showTimeout.value = true;
  }, 2000);
};

// 清理定时器
onBeforeUnmount(() => {
  if (timeoutTimer) clearTimeout(timeoutTimer);
});
</script>

<style scoped>
/* 保持原有样式不变 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  position: relative;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f8f9fa;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f9f7fe 0%, #e8e6f7 100%);
  z-index: -1;
}

.auth-content {
  width: 100%;
  max-width: 450px;
  z-index: 2;
  position: relative;
}

.auth-card {
  padding: 40px 35px;
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(50, 50, 93, 0.1), 0 5px 15px rgba(0, 0, 0, 0.07);
}

.brand-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
}

.logo-circle {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.3);
}

.logo span {
  font-size: 28px;
  font-weight: 700;
  color: #4a3e7e;
  letter-spacing: 0.5px;
}

h1 {
  font-size: 24px;
  font-weight: 500;
  color: #6c757d;
  margin-bottom: 8px;
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #4a3e7e;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

input {
  width: 100%;
  height: 50px;
  padding: 0 20px;
  background: white;
  border: 1px solid #e0e0e5;
  border-radius: 10px;
  color: #4a3e7e;
  font-size: 16px;
  outline: none;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
}

input:focus {
  border-color: #a777e3;
  box-shadow: 0 0 0 3px rgba(167, 119, 227, 0.15);
}

.divider {
  height: 1px;
  background: #e0e0e5;
  margin: 30px 0;
}

.slogan {
  text-align: center;
  margin-bottom: 30px;
}

.slogan h3 {
  font-size: 18px;
  font-weight: 600;
  color: #4a3e7e;
  margin-bottom: 12px;
}

.slogan p {
  font-size: 14px;
  color: #6c757d;
  line-height: 1.6;
}

.signin-button {
  width: 100%;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  color: white;
  border: none;
  padding: 16px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.3);
  margin-bottom: 20px;
}

.signin-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 142, 83, 0.4);
}

.or-divider {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.or-divider::before,
.or-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e0e0e5;
}

.or-divider span {
  padding: 0 15px;
  color: #6c757d;
  font-size: 14px;
  font-weight: 500;
}

.google-signin-button {
  width: 100%;
  background: white;
  color: #4a3e7e;
  border: 1px solid #e0e0e5;
  padding: 16px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 25px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.03);
}

.google-signin-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
  border-color: #a777e3;
}

.google-signin-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.auth-footer {
  text-align: center;
  margin-top: 10px;
}

.auth-footer p {
  color: #6c757d;
  font-size: 15px;
}

.auth-footer a {
  color: #ff6b9d;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.auth-footer a:hover {
  color: #e55a8a;
  text-decoration: underline;
}

/* 条款同意 */
.terms {
  margin: 20px 0;
}

.terms-checkbox {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 14px;
  color: #6c757d;
  cursor: pointer;
}

.terms-checkbox input {
  width: auto;
  height: auto;
  margin-top: 4px;
}

.terms-checkbox a {
  color: #ff6b9d;
  text-decoration: none;
}

.terms-checkbox a:hover {
  text-decoration: underline;
}

/* 超时提示弹窗 */
.timeout-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  animation: fadeIn 0.3s ease;
}

.timeout-content {
  background: white;
  border-radius: 16px;
  padding: 40px 30px;
  text-align: center;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
}

.timeout-icon {
  font-size: 48px;
  color: #ff6b6b;
  margin-bottom: 20px;
}

.timeout-content h3 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #4a3e7e;
}

.timeout-content p {
  color: #6c757d;
  margin-bottom: 25px;
  line-height: 1.6;
}

.timeout-button {
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.timeout-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.4);
}

/* 错误提示弹窗 */
.error-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  animation: fadeIn 0.3s ease;
}

.error-content {
  background: white;
  border-radius: 16px;
  padding: 40px 30px;
  text-align: center;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
}

.error-icon {
  font-size: 48px;
  color: #ff6b6b;
  margin-bottom: 20px;
}

.error-content h3 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #4a3e7e;
}

.error-content p {
  color: #6c757d;
  margin-bottom: 25px;
  line-height: 1.6;
}

.error-button {
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.error-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.4);
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 按钮加载动画 */
.fa-spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>