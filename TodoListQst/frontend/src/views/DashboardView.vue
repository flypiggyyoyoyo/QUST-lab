<template>
  <div class="dashboard-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="user-profile">
        <div class="profile-image">
          <img src="https://picsum.photos/200/200?random=1" alt="User profile" />
        </div>
        <h3>{{ user.fullname || 'User' }}</h3>
        <p>{{ user.email || 'user@example.com' }}</p>
      </div>

      <nav class="menu">
        <ul>
          <li class="active">
            <a href="#" @click.prevent="navigateTo('todos')">
              <i class="fas fa-tasks"></i>
              <span>Todos</span>
            </a>
          </li>
          <li>
            <a href="#" @click.prevent="navigateTo('calendar')">
              <i class="fas fa-calendar"></i>
              <span>Calendar</span>
            </a>
          </li>
          <li>
            <a href="#" @click.prevent="navigateTo('analytics')">
              <i class="fas fa-chart-pie"></i>
              <span>Analytics</span>
            </a>
          </li>
          <li>
            <a href="#" @click.prevent="navigateTo('settings')">
              <i class="fas fa-cog"></i>
              <span>Settings</span>
            </a>
          </li>
        </ul>
      </nav>

      <div class="logout">
        <button @click="logout">
          <i class="fas fa-sign-out-alt"></i>
          <span>Logout</span>
        </button>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航栏 -->
      <header class="header">
        <div class="header-title">
          <h1>My Todo List</h1>
          <p>Welcome back, {{ user.fullname || 'User' }}</p>
        </div>

        <div class="header-actions">
          <button class="add-todo-btn" @click="openAddTodoModal">
            <i class="fas fa-plus"></i>
            <span>Add Todo</span>
          </button>
        </div>
      </header>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="card" :class="{'card-completed': stats.completedPercentage > 70}">
          <div class="card-icon">
            <i class="fas fa-check-circle"></i>
          </div>
          <div class="card-content">
            <h3>{{ stats.completedCount || 0 }}</h3>
            <p>Completed Tasks</p>
          </div>
          <div class="card-progress">
            <div class="progress-bar" :style="{ width: stats.completedPercentage + '%' }"></div>
          </div>
        </div>

        <div class="card" :class="{'card-pending': stats.pendingPercentage > 70}">
          <div class="card-icon">
            <i class="fas fa-clock"></i>
          </div>
          <div class="card-content">
            <h3>{{ stats.pendingCount || 0 }}</h3>
            <p>Pending Tasks</p>
          </div>
          <div class="card-progress">
            <div class="progress-bar" :style="{ width: stats.pendingPercentage + '%' }"></div>
          </div>
        </div>

        <div class="card" :class="{'card-important': stats.highPriorityCount > 0}">
          <div class="card-icon">
            <i class="fas fa-exclamation-circle"></i>
          </div>
          <div class="card-content">
            <h3>{{ stats.highPriorityCount || 0 }}</h3>
            <p>High Priority</p>
          </div>
          <div class="card-progress">
            <div class="progress-bar" :style="{ width: stats.highPriorityPercentage + '%' }"></div>
          </div>
        </div>
      </div>

      <!-- 筛选工具栏 -->
      <div class="filter-bar">
        <div class="filter-group">
          <label>Filter by:</label>
          <select v-model="filterStatus">
            <option value="all">All</option>
            <option value="completed">Completed</option>
            <option value="pending">Pending</option>
          </select>
        </div>

        <div class="filter-group">
          <label>Priority:</label>
          <select v-model="filterPriority">
            <option value="all">All</option>
            <option value="1">High</option>
            <option value="2">Medium</option>
            <option value="3">Low</option>
          </select>
        </div>

        <div class="filter-group">
          <label>Date Range:</label>
          <input type="date" v-model="filterStartDate" placeholder="Start Date">
          <input type="date" v-model="filterEndDate" placeholder="End Date">
        </div>

        <button class="filter-btn" @click="applyFilters">
          <i class="fas fa-filter"></i>
          <span>Apply Filters</span>
        </button>
      </div>

      <!-- 待办事项列表 -->
      <div class="todos-list">
        <div class="todo-item"
             v-for="todo in filteredTodos"
             :key="todo.id"
             :class="{ 'completed': todo.completed, 'high-priority': todo.priority === 1 }">
          <div class="todo-checkbox">
            <input type="checkbox"
                   :checked="todo.completed"
                   @change="updateTodoStatus(todo.id, !todo.completed)">
          </div>

          <div class="todo-content">
            <h3>{{ todo.title }}</h3>
            <p>{{ todo.description || 'No description' }}</p>
            <div class="todo-meta">
              <span class="due-date">
                <i class="fas fa-calendar"></i>
                {{ formatDate(todo.dueDate) || 'No due date' }}
              </span>
              <span class="priority"
                    :class="{ 'high': todo.priority === 1, 'medium': todo.priority === 2, 'low': todo.priority === 3 }">
                <i class="fas fa-flag"></i>
                {{ getPriorityText(todo.priority) }}
              </span>
            </div>
          </div>

          <div class="todo-actions">
            <button class="edit-btn" @click="openEditTodoModal(todo)">
              <i class="fas fa-edit"></i>
            </button>
            <button class="delete-btn" @click="deleteTodo(todo.id)">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="todos.length === 0" class="empty-state">
          <div class="empty-icon">
            <i class="fas fa-clipboard-list"></i>
          </div>
          <h3>No tasks found</h3>
          <p>Click the "Add Todo" button to create your first task.</p>
          <button class="empty-btn" @click="openAddTodoModal">
            <i class="fas fa-plus"></i>
            Add Task
          </button>
        </div>
      </div>
    </main>

    <!-- 添加/编辑待办事项模态框 -->
    <div v-if="showTodoModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEditing ? 'Edit Todo' : 'Add New Todo' }}</h3>
          <button class="close-btn" @click="closeTodoModal">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="submitTodo">
            <div class="form-group">
              <label for="title">Title</label>
              <input type="text"
                     id="title"
                     v-model="todoForm.title"
                     required
                     placeholder="Task title">
            </div>

            <div class="form-group">
              <label for="description">Description</label>
              <textarea id="description"
                        v-model="todoForm.description"
                        placeholder="Task description"></textarea>
            </div>

            <div class="form-group">
              <label for="dueDate">Due Date</label>
              <input type="date"
                     id="dueDate"
                     v-model="todoForm.dueDate">
            </div>

            <div class="form-group">
              <label for="priority">Priority</label>
              <select id="priority" v-model="todoForm.priority">
                <option value="1">High</option>
                <option value="2">Medium</option>
                <option value="3">Low</option>
              </select>
            </div>

            <div class="form-actions">
              <button type="button" class="cancel-btn" @click="closeTodoModal">
                Cancel
              </button>
              <button type="submit" class="save-btn">
                {{ isEditing ? 'Update' : 'Create' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="showError" class="error-notification">
      <div class="error-content">
        <i class="fas fa-exclamation-circle"></i>
        <p>{{ errorMessage }}</p>
        <button @click="showError = false">
          <i class="fas fa-times"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import axios from 'axios';

// 配置axios基础URL和拦截器
axios.defaults.baseURL = 'http://localhost:8080/api/v1'; // 根据实际后端地址修改

// 请求拦截器，添加token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器，处理401未授权
axios.interceptors.response.use(
    response => response,
    error => {
      if (error.response && error.response.status === 401) {
        // 未授权，跳转到登录页
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/'; // 跳转到登录页
      }
      return Promise.reject(error);
    }
);

// 状态管理
const loading = ref(false);
const showError = ref(false);
const errorMessage = ref('');
const showTodoModal = ref(false);
const isEditing = ref(false);
const todos = ref([]);
const stats = ref({});
const user = ref(JSON.parse(localStorage.getItem('user')) || {});

// 筛选条件
const filterStatus = ref('all');
const filterPriority = ref('all');
const filterStartDate = ref('');
const filterEndDate = ref('');

// 待办事项表单
const todoForm = reactive({
  id: null,
  title: '',
  description: '',
  dueDate: '',
  priority: 2,
  completed: false
});

// 计算属性：筛选后的待办事项
const filteredTodos = computed(() => {
  return todos.value.filter(todo => {
    const statusMatch = filterStatus.value === 'all' ||
        (filterStatus.value === 'completed' && todo.completed) ||
        (filterStatus.value === 'pending' && !todo.completed);

    const priorityMatch = filterPriority.value === 'all' ||
        parseInt(filterPriority.value) === todo.priority;

    const dateMatch = (!filterStartDate.value || new Date(todo.dueDate) >= new Date(filterStartDate.value)) &&
        (!filterEndDate.value || new Date(todo.dueDate) <= new Date(filterEndDate.value));

    return statusMatch && priorityMatch && dateMatch;
  });
});

// 生命周期钩子
onMounted(() => {
  fetchTodos();
  fetchTodoStats();
});

// 获取所有待办事项
const fetchTodos = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/todos');
    todos.value = response.data.data;
  } catch (error) {
    handleError(error, 'Failed to fetch todos');
  } finally {
    loading.value = false;
  }
};

// 获取待办事项统计数据
const fetchTodoStats = async () => {
  try {
    const response = await axios.get('/todos/stats');
    stats.value = response.data.data;
  } catch (error) {
    handleError(error, 'Failed to fetch stats');
  }
};

// 创建新待办事项
const createTodo = async () => {
  loading.value = true;
  try {
    const requestData = {
      title: todoForm.title,
      description: todoForm.description,
      dueDate: todoForm.dueDate,
      priority: todoForm.priority
    };

    await axios.post('/todos', requestData);
    closeTodoModal();
    fetchTodos();
    fetchTodoStats();
  } catch (error) {
    handleError(error, 'Failed to create todo');
  } finally {
    loading.value = false;
  }
};

// 更新待办事项
const updateTodo = async () => {
  loading.value = true;
  try {
    const requestData = {
      title: todoForm.title,
      description: todoForm.description,
      dueDate: todoForm.dueDate,
      priority: todoForm.priority
    };

    await axios.put(`/todos/${todoForm.id}`, requestData);
    closeTodoModal();
    fetchTodos();
    fetchTodoStats();
  } catch (error) {
    handleError(error, 'Failed to update todo');
  } finally {
    loading.value = false;
  }
};

// 更新待办事项状态
const updateTodoStatus = async (id, status) => {
  try {
    await axios.patch(`/todos/${id}/status`, { status });
    fetchTodos();
    fetchTodoStats();
  } catch (error) {
    handleError(error, 'Failed to update status');
  }
};

// 删除待办事项
const deleteTodo = async (id) => {
  if (confirm('Are you sure you want to delete this task?')) {
    loading.value = true;
    try {
      await axios.delete(`/todos/${id}`);
      fetchTodos();
      fetchTodoStats();
    } catch (error) {
      handleError(error, 'Failed to delete todo');
    } finally {
      loading.value = false;
    }
  }
};

// 打开添加待办事项模态框
const openAddTodoModal = () => {
  isEditing.value = false;
  todoForm.id = null;
  todoForm.title = '';
  todoForm.description = '';
  todoForm.dueDate = '';
  todoForm.priority = 2;
  todoForm.completed = false;
  showTodoModal.value = true;
};

// 打开编辑待办事项模态框
const openEditTodoModal = (todo) => {
  isEditing.value = true;
  todoForm.id = todo.id;
  todoForm.title = todo.title;
  todoForm.description = todo.description;
  todoForm.dueDate = todo.dueDate;
  todoForm.priority = todo.priority;
  todoForm.completed = todo.completed;
  showTodoModal.value = true;
};

// 关闭待办事项模态框
const closeTodoModal = () => {
  showTodoModal.value = false;
};

// 提交待办事项表单
const submitTodo = () => {
  if (isEditing.value) {
    updateTodo();
  } else {
    createTodo();
  }
};

// 应用筛选条件
const applyFilters = () => {
  // 筛选逻辑已在计算属性中实现，这里只需触发更新
  fetchTodos();
};

// 处理错误
const handleError = (error, defaultMessage) => {
  console.error(error);
  errorMessage.value = error.response?.data?.message || defaultMessage;
  showError.value = true;
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return null;
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

// 获取优先级文本
const getPriorityText = (priority) => {
  const texts = ['', 'High', 'Medium', 'Low'];
  return texts[priority] || 'Medium';
};

// 导航切换
const navigateTo = (page) => {
  // 这里可以实现路由切换逻辑
  console.log(`Navigate to ${page}`);
};

// 退出登录
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = '/'; // 跳转到登录页
};
</script>

<style scoped>
/* 整体布局 */
.dashboard-container {
  display: flex;
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 侧边栏 */
.sidebar {
  width: 280px;
  background: linear-gradient(135deg, #4a3e7e 0%, #6a5acd 100%);
  color: white;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  height: 100vh;
  position: sticky;
  top: 0;
}

.user-profile {
  text-align: center;
  margin-bottom: 40px;
}

.profile-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-profile h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 5px;
}

.user-profile p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
}

.menu ul {
  list-style: none;
  padding: 0;
}

.menu li {
  margin-bottom: 15px;
}

.menu a {
  display: flex;
  align-items: center;
  gap: 15px;
  color: white;
  text-decoration: none;
  padding: 12px 15px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.menu a:hover,
.menu li.active a {
  background-color: rgba(255, 255, 255, 0.1);
}

.menu i {
  font-size: 18px;
  width: 20px;
  text-align: center;
}

.logout {
  margin-top: auto;
}

.logout button {
  width: 100%;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 12px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout button:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 主内容区 */
.main-content {
  flex: 1;
  padding: 30px;
  overflow-x: hidden;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header-title h1 {
  font-size: 28px;
  font-weight: 700;
  color: #4a3e7e;
  margin-bottom: 5px;
}

.header-title p {
  font-size: 16px;
  color: #6c757d;
}

.header-actions .add-todo-btn {
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.3);
}

.header-actions .add-todo-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 142, 83, 0.4);
}

/* 统计卡片 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  background-color: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 50px;
  height: 50px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 0 0 0 50%;
}

.card-icon {
  font-size: 36px;
  color: #4a3e7e;
  margin-bottom: 15px;
}

.card-content h3 {
  font-size: 24px;
  font-weight: 700;
  color: #4a3e7e;
  margin-bottom: 5px;
}

.card-content p {
  font-size: 14px;
  color: #6c757d;
}

.card-progress {
  margin-top: 15px;
  height: 8px;
  background-color: #f1f1f1;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  transition: width 0.5s ease;
}

.card-completed .progress-bar {
  background: linear-gradient(90deg, #2ecc71 0%, #27ae60 100%);
}

.card-pending .progress-bar {
  background: linear-gradient(90deg, #f39c12 0%, #e67e22 100%);
}

.card-important .progress-bar {
  background: linear-gradient(90deg, #e74c3c 0%, #c0392b 100%);
}

/* 筛选工具栏 */
.filter-bar {
  background-color: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: flex-end;
}

.filter-group {
  flex: 1;
  min-width: 200px;
}

.filter-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #4a3e7e;
  font-weight: 500;
}

.filter-group select,
.filter-group input {
  width: 100%;
  height: 40px;
  padding: 0 15px;
  background: white;
  border: 1px solid #e0e0e5;
  border-radius: 8px;
  color: #4a3e7e;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.filter-group select:focus,
.filter-group input:focus {
  border-color: #a777e3;
  box-shadow: 0 0 0 3px rgba(167, 119, 227, 0.15);
}

.filter-btn {
  background: linear-gradient(135deg, #4a3e7e 0%, #6a5acd 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(74, 62, 126, 0.3);
}

/* 待办事项列表 */
.todos-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.todo-item {
  background-color: white;
  border-radius: 12px;
  padding: 15px 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.todo-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.todo-item.completed {
  opacity: 0.7;
  text-decoration: line-through;
}

.todo-item.high-priority::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: #e74c3c;
}

.todo-checkbox {
  min-width: 20px;
}

.todo-checkbox input {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.todo-content {
  flex: 1;
}

.todo-content h3 {
  font-size: 18px;
  font-weight: 600;
  color: #4a3e7e;
  margin-bottom: 5px;
}

.todo-content p {
  font-size: 14px;
  color: #6c757d;
  margin-bottom: 10px;
}

.todo-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #888;
}

.todo-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.todo-meta .priority.high {
  color: #e74c3c;
}

.todo-meta .priority.medium {
  color: #f39c12;
}

.todo-meta .priority.low {
  color: #2ecc71;
}

.todo-actions {
  display: flex;
  gap: 10px;
}

.todo-actions button {
  background: transparent;
  border: none;
  color: #888;
  cursor: pointer;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.todo-actions button:hover {
  background-color: #f1f1f1;
}

.todo-actions .edit-btn:hover {
  color: #4a3e7e;
}

.todo-actions .delete-btn:hover {
  color: #e74c3c;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px 20px;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  font-size: 64px;
  color: #a777e3;
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 24px;
  font-weight: 600;
  color: #4a3e7e;
  margin-bottom: 10px;
}

.empty-state p {
  font-size: 16px;
  color: #6c757d;
  margin-bottom: 20px;
}

.empty-btn {
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8e53 100%);
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 4px 15px rgba(255, 142, 83, 0.3);
}

.empty-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 142, 83, 0.4);
}

/* 模态框 */
.modal {
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

.modal-content {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.2);
  animation: slideUp 0.3s ease;
}

.modal-header {
  padding: 20px 30px;
  border-bottom: 1px solid #e0e0e5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #4a3e7e;
}

.modal-header .close-btn {
  background: transparent;
  border: none;
  color: #888;
  cursor: pointer;
  font-size: 18px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.modal-header .close-btn:hover {
  background-color: #f1f1f1;
  color: #4a3e7e;
}

.modal-body {
  padding: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #4a3e7e;
  font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 12px 15px;
  background: white;
  border: 1px solid #e0e0e5;
  border-radius: 8px;
  color: #4a3e7e;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  border-color: #a777e3;
  box-shadow: 0 0 0 3px rgba(167, 119, 227, 0.15);
}

.form-group textarea {
  min-height: 100px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.cancel-btn {
  background: #f1f1f1;
  color: #4a3e7e;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #e0e0e0;
}

.save-btn {
  background: linear-gradient(135deg, #4a3e7e 0%, #6a5acd 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(74, 62, 126, 0.3);
}

/* 加载中状态 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4a3e7e 0%, #6a5acd 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  animation: spin 1s linear infinite;
  box-shadow: 0 0 20px rgba(74, 62, 126, 0.3);
}

/* 错误提示 */
.error-notification {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #e74c3c;
  color: white;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 10px 25px rgba(231, 76, 60, 0.2);
  display: flex;
  align-items: center;
  gap: 15px;
  z-index: 100;
  animation: fadeIn 0.3s ease;
}

.error-content {
  display: flex;
  align-items: center;
  gap: 15px;
  flex: 1;
}

.error-content i {
  font-size: 20px;
}

.error-content p {
  margin: 0;
  font-size: 14px;
}

.error-notification button {
  background: transparent;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 16px;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
