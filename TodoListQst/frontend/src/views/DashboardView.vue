<template>
  <div class="dashboard-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="user-profile">
        <div class="profile-image">
          <img src="https://picsum.photos/200/200?random=1" alt="User profile" />
        </div>
        <h3>{{ user.username || 'User' }}</h3>
        <p>{{ user.userEmail || 'user@example.com' }}</p>
      </div>

      <nav class="menu">
        <ul>
          <li :class="{ active: currentPage === 'todos' }">
            <a href="#" @click.prevent="navigateTo('todos')">
              <i class="fas fa-tasks"></i>
              <span>Todos</span>
            </a>
          </li>
          <li :class="{ active: currentPage === 'calendar' }">
            <a href="#" @click.prevent="navigateTo('calendar')">
              <i class="fas fa-calendar"></i>
              <span>Calendar</span>
            </a>
          </li>
          <li :class="{ active: currentPage === 'analytics' }">
            <a href="#" @click.prevent="navigateTo('analytics')">
              <i class="fas fa-chart-pie"></i>
              <span>Analytics</span>
            </a>
          </li>
          <li :class="{ active: currentPage === 'settings' }">
            <a href="#" @click.prevent="navigateTo('settings')">
              <i class="fas fa-cog"></i>
              <span>Settings</span>
            </a>
          </li>
        </ul>
      </nav>

      <div class="logout">
        <button @click="logout" :disabled="loading">
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
          <p>Welcome back, {{ user.username || 'User' }} <span class="current-date">{{ formattedDate }}</span></p>
        </div>

        <div class="header-actions">
          <div class="search-bar">
            <i class="fas fa-search"></i>
            <input
                type="text"
                v-model="searchQuery"
                placeholder="Search tasks..."
                @input="handleSearch"
            >
          </div>
          <button class="add-todo-btn" @click="openAddTodoModal" :disabled="loading">
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
          <select v-model="filterStatus" @change="debouncedApplyFilters">
            <option value="all">All</option>
            <option value="completed">Completed</option>
            <option value="pending">Pending</option>
          </select>
        </div>

        <div class="filter-group">
          <label>Priority:</label>
          <select v-model="filterPriority" @change="debouncedApplyFilters">
            <option value="all">All</option>
            <option value="1">High</option>
            <option value="2">Medium</option>
            <option value="3">Low</option>
          </select>
        </div>

        <div class="filter-group">
          <label>Date Range:</label>
          <input
              type="date"
              v-model="filterStartDate"
              @change="debouncedApplyFilters"
              :max="filterEndDate || undefined"
          >
          <input
              type="date"
              v-model="filterEndDate"
              @change="debouncedApplyFilters"
              :min="filterStartDate || undefined"
          >
        </div>

        <button class="filter-btn" @click="applyFilters" :disabled="loading">
          <i class="fas fa-filter"></i>
          <span>Apply Filters</span>
        </button>

        <button
            class="reset-filter-btn"
            @click="resetFilters"
            :disabled="isDefaultFilterState"
        >
          <i class="fas fa-sync-alt"></i>
          <span>Reset</span>
        </button>
      </div>

      <!-- 待办事项列表 -->
      <div class="todos-list">
        <div
            class="todo-item"
            v-for="todo in filteredTodos"
            :key="todo.id"
            :class="{
            'completed': todo.completed,
            'high-priority': todo.priority === 1,
            'overdue': isOverdue(todo) && !todo.completed
          }"
        >
          <div class="todo-checkbox">
            <input
                type="checkbox"
                :checked="todo.completed"
                @change="updateTodoStatus(todo.id, !todo.completed)"
                :disabled="loading"
            >
          </div>

          <div class="todo-content">
            <h3>{{ todo.title }}</h3>
            <p>{{ todo.description || 'No description' }}</p>
            <div class="todo-meta">
              <span class="due-date">
                <i class="fas fa-calendar"></i>
                {{ formatDate(todo.dueDate) || 'No due date' }}
                <span v-if="isOverdue(todo) && !todo.completed" class="overdue-badge">Overdue</span>
              </span>
              <span class="priority"
                    :class="{ 'high': todo.priority === 1, 'medium': todo.priority === 2, 'low': todo.priority === 3 }">
                <i class="fas fa-flag"></i>
                {{ getPriorityText(todo.priority) }}
              </span>
              <span v-if="todo.tags && todo.tags.length" class="tags">
                <i class="fas fa-tags"></i>
                <span v-for="tag in todo.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
              </span>
            </div>
          </div>

          <div class="todo-actions">
            <button
                class="edit-btn"
                @click="openEditTodoModal(todo)"
                :disabled="loading"
                aria-label="Edit task"
            >
              <i class="fas fa-edit"></i>
            </button>
            <button
                class="delete-btn"
                @click="prepareDeleteTodo(todo)"
                :disabled="loading"
                aria-label="Delete task"
            >
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="filteredTodos.length === 0 && !loading" class="empty-state">
          <div class="empty-icon">
            <i class="fas fa-clipboard-list"></i>
          </div>
          <h3>No tasks found</h3>
          <p>
            {{ searchQuery || hasActiveFilters ?
              'Try adjusting your search or filters.' :
              'Click the "Add Todo" button to create your first task.'
            }}
          </p>
          <button class="empty-btn" @click="openAddTodoModal" :disabled="loading">
            <i class="fas fa-plus"></i>
            Add Task
          </button>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading && todos.length === 0" class="list-loading">
          <i class="fas fa-spinner fa-spin"></i>
          <p>Loading your tasks...</p>
        </div>
      </div>
    </main>

    <!-- 添加/编辑待办事项模态框 -->
    <div v-if="showTodoModal" class="modal-backdrop" @click.self="closeTodoModal">
      <div class="modal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>{{ isEditing ? 'Edit Todo' : 'Add New Todo' }}</h3>
            <button class="close-btn" @click="closeTodoModal" :disabled="loading">
              <i class="fas fa-times"></i>
            </button>
          </div>

          <div class="modal-body">
            <form @submit.prevent="submitTodo">
              <div class="form-group">
                <label for="title">Title <span class="required">*</span></label>
                <input
                    type="text"
                    id="title"
                    v-model="todoForm.title"
                    required
                    placeholder="Task title"
                    :disabled="loading"
                    :class="{ 'invalid': formErrors.title }"
                >
                <p v-if="formErrors.title" class="error-message">{{ formErrors.title }}</p>
              </div>

              <div class="form-group">
                <label for="description">Description</label>
                <textarea
                    id="description"
                    v-model="todoForm.description"
                    placeholder="Task description"
                    :disabled="loading"
                    rows="3"
                ></textarea>
              </div>

              <div class="form-group">
                <label for="dueDate">Due Date</label>
                <input
                    type="date"
                    id="dueDate"
                    v-model="todoForm.dueDate"
                    :disabled="loading"
                    :min="minDueDate"
                >
              </div>

              <div class="form-group">
                <label for="priority">Priority <span class="required">*</span></label>
                <select
                    id="priority"
                    v-model="todoForm.priority"
                    :disabled="loading"
                    required
                >
                  <option value="1">High</option>
                  <option value="2">Medium</option>
                  <option value="3">Low</option>
                </select>
              </div>

              <div class="form-group">
                <label for="tags">Tags</label>
                <input
                    type="text"
                    id="tags"
                    v-model="tagInput"
                    placeholder="Add tags separated by commas"
                    :disabled="loading"
                >
                <div class="tags-preview">
                  <span
                      v-for="(tag, index) in todoForm.tags"
                      :key="index"
                      class="tag-item"
                  >
                    {{ tag }}
                    <button
                        type="button"
                        class="remove-tag"
                        @click="removeTag(index)"
                        :disabled="loading"
                    >
                      <i class="fas fa-times"></i>
                    </button>
                  </span>
                </div>
              </div>

              <div class="form-actions">
                <button
                    type="button"
                    class="cancel-btn"
                    @click="closeTodoModal"
                    :disabled="loading"
                >
                  Cancel
                </button>
                <button
                    type="submit"
                    class="save-btn"
                    :disabled="loading"
                >
                  <i v-if="loading" class="fas fa-spinner fa-spin"></i>
                  <span>{{ isEditing ? 'Update' : 'Create' }}</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 确认删除模态框 -->
    <div v-if="showDeleteConfirm" class="modal-backdrop" @click.self="cancelDelete">
      <div class="modal confirm-modal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>Confirm Delete</h3>
            <button class="close-btn" @click="cancelDelete" :disabled="loading">
              <i class="fas fa-times"></i>
            </button>
          </div>

          <div class="modal-body">
            <p>Are you sure you want to delete this task?</p>
            <p class="task-title">"{{ taskToDelete?.title }}"</p>
            <p class="delete-warning">This action cannot be undone.</p>

            <div class="form-actions">
              <button
                  type="button"
                  class="cancel-btn"
                  @click="cancelDelete"
                  :disabled="loading"
              >
                Cancel
              </button>
              <button
                  type="button"
                  class="delete-confirm-btn"
                  @click="confirmDelete"
                  :disabled="loading"
              >
                <i v-if="loading" class="fas fa-spinner fa-spin"></i>
                <span>Delete</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>

    <!-- 通知提示 -->
    <div
        v-if="showNotification"
        class="notification"
        :class="notificationType"
    >
      <i :class="notificationIcon"></i>
      <p>{{ notificationMessage }}</p>
      <button @click="showNotification = false">
        <i class="fas fa-times"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { debounce } from 'lodash';

// 初始化路由
const router = useRouter();

// 配置axios基础URL和拦截器
axios.defaults.baseURL = 'http://localhost:8080/api/v1';

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
        router.push('/auth');
      }
      return Promise.reject(error);
    }
);

// 状态管理
const loading = ref(false);
const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref('success');
const showTodoModal = ref(false);
const showDeleteConfirm = ref(false);
const isEditing = ref(false);
const todos = ref([]);
const stats = ref({});
const user = ref({});
const searchQuery = ref('');
const currentPage = ref('todos');
const formErrors = ref({});
const taskToDelete = ref(null);

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
  completed: false,
  tags: []
});

const tagInput = ref('');

// 计算属性
const filteredTodos = computed(() => {
  return todos.value.filter(todo => {
    // 状态筛选
    const statusMatch = filterStatus.value === 'all' ||
        (filterStatus.value === 'completed' && todo.completed) ||
        (filterStatus.value === 'pending' && !todo.completed);

    // 优先级筛选
    const priorityMatch = filterPriority.value === 'all' ||
        parseInt(filterPriority.value) === todo.priority;

    // 日期范围筛选
    const dateMatch = (!filterStartDate.value ||
            new Date(todo.dueDate || '9999-12-31') >= new Date(filterStartDate.value)) &&
        (!filterEndDate.value ||
            new Date(todo.dueDate || '0000-01-01') <= new Date(filterEndDate.value));

    // 搜索筛选
    const searchMatch = !searchQuery.value ||
        todo.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        (todo.description && todo.description.toLowerCase().includes(searchQuery.value.toLowerCase()));

    return statusMatch && priorityMatch && dateMatch && searchMatch;
  });
});

// 检查是否有活跃的筛选条件
const hasActiveFilters = computed(() => {
  return filterStatus.value !== 'all' ||
      filterPriority.value !== 'all' ||
      filterStartDate.value ||
      filterEndDate.value;
});

// 检查是否是默认筛选状态
const isDefaultFilterState = computed(() => {
  return filterStatus.value === 'all' &&
      filterPriority.value === 'all' &&
      !filterStartDate.value &&
      !filterEndDate.value &&
      !searchQuery.value;
});

// 格式化当前日期
const formattedDate = computed(() => {
  return new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    month: 'long',
    day: 'numeric'
  });
});

// 最小截止日期（今天）
const minDueDate = computed(() => {
  return new Date().toISOString().split('T')[0];
});

// 通知图标
const notificationIcon = computed(() => {
  return notificationType.value === 'success'
      ? 'fas fa-check-circle'
      : 'fas fa-exclamation-circle';
});

// 生命周期钩子
onMounted(() => {
  initApp();
});

// 初始化应用
const initApp = async () => {
  try {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      user.value = JSON.parse(storedUser);
    } else {
      await router.push('/auth');
      return;
    }

    // 加载任务数据
    await Promise.all([
      fetchTodos(),
      fetchTodoStats()
    ]);
  } catch (error) {
    handleError(error, 'Failed to initialize application');
  }
};

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

// 创建新待办事项 - 修复添加功能的核心逻辑
const createTodo = async () => {
  // 表单验证
  if (!validateForm()) return;

  loading.value = true;
  try {
    const requestData = {
      title: todoForm.title,
      description: todoForm.description,
      dueDate: todoForm.dueDate,
      priority: todoForm.priority,
      tags: todoForm.tags
    };

    // 确保API请求正确执行
    const response = await axios.post('/todos', requestData);

    // 验证响应并更新UI
    if (response.data.success) {
      showNotificationMessage('Task created successfully!', 'success');
      closeTodoModal();
      await fetchTodos();
      await fetchTodoStats();
    } else {
      throw new Error(response.data.message || 'Failed to create task');
    }
  } catch (error) {
    handleError(error, 'Failed to create todo');
  } finally {
    loading.value = false;
  }
};

// 更新待办事项
const updateTodo = async () => {
  if (!validateForm()) return;

  loading.value = true;
  try {
    const requestData = {
      title: todoForm.title,
      description: todoForm.description,
      dueDate: todoForm.dueDate,
      priority: todoForm.priority,
      tags: todoForm.tags
    };

    await axios.put(`/todos/${todoForm.id}`, requestData);
    showNotificationMessage('Task updated successfully!', 'success');
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
  loading.value = true;
  try {
    await axios.patch(`/todos/${id}/status`, { status });
    showNotificationMessage(`Task ${status ? 'completed' : 'marked as pending'}!`, 'success');
    fetchTodos();
    fetchTodoStats();
  } catch (error) {
    handleError(error, 'Failed to update status');
    loading.value = false;
  }
};

// 准备删除待办事项
const prepareDeleteTodo = (todo) => {
  taskToDelete.value = todo;
  showDeleteConfirm.value = true;
};

// 确认删除待办事项
const confirmDelete = async () => {
  if (!taskToDelete.value) return;

  loading.value = true;
  try {
    await axios.delete(`/todos/${taskToDelete.value.id}`);
    showNotificationMessage('Task deleted successfully!', 'success');
    showDeleteConfirm.value = false;
    fetchTodos();
    fetchTodoStats();
    taskToDelete.value = null;
  } catch (error) {
    handleError(error, 'Failed to delete todo');
    loading.value = false;
  }
};

// 取消删除
const cancelDelete = () => {
  showDeleteConfirm.value = false;
  taskToDelete.value = null;
};

// 打开添加待办事项模态框 - 确保模态框正确显示
const openAddTodoModal = () => {
  isEditing.value = false;
  resetForm();
  showTodoModal.value = true; // 关键: 确保模态框显示状态被正确设置

  // 调试用: 确认函数被调用
  console.log('Opening add todo modal');
};

// 打开编辑待办事项模态框
const openEditTodoModal = (todo) => {
  isEditing.value = true;
  resetForm();

  // 填充表单数据
  todoForm.id = todo.id;
  todoForm.title = todo.title;
  todoForm.description = todo.description;
  todoForm.dueDate = todo.dueDate ? new Date(todo.dueDate).toISOString().split('T')[0] : '';
  todoForm.priority = todo.priority;
  todoForm.completed = todo.completed;

  // 处理标签
  if (todo.tags) {
    todoForm.tags = [...todo.tags.map(tag => tag.name)];
  }

  showTodoModal.value = true;
};

// 关闭待办事项模态框
const closeTodoModal = () => {
  showTodoModal.value = false;
  resetForm();
};

// 重置表单
const resetForm = () => {
  todoForm.id = null;
  todoForm.title = '';
  todoForm.description = '';
  todoForm.dueDate = '';
  todoForm.priority = 2;
  todoForm.completed = false;
  todoForm.tags = [];
  tagInput.value = '';
  formErrors.value = {};
};

// 提交待办事项表单
const submitTodo = () => {
  if (isEditing.value) {
    updateTodo();
  } else {
    createTodo(); // 确保调用正确的创建函数
  }
};

// 添加标签
watch(tagInput, (newVal) => {
  if (newVal && newVal.includes(',')) {
    const tags = newVal.split(',').map(tag => tag.trim()).filter(Boolean);
    todoForm.tags = [...new Set([...todoForm.tags, ...tags])];
    tagInput.value = '';
  }
});

// 移除标签
const removeTag = (index) => {
  todoForm.tags.splice(index, 1);
};

// 应用筛选条件（防抖处理）
const debouncedApplyFilters = debounce(() => {
  // 筛选逻辑已在计算属性中实现
}, 300);

const applyFilters = () => {
  debouncedApplyFilters();
  showNotificationMessage('Filters applied!', 'info');
};

// 重置筛选条件
const resetFilters = () => {
  filterStatus.value = 'all';
  filterPriority.value = 'all';
  filterStartDate.value = '';
  filterEndDate.value = '';
  searchQuery.value = '';
  showNotificationMessage('Filters reset!', 'info');
};

// 处理搜索
const handleSearch = debounce(() => {
  // 搜索逻辑在计算属性中实现
}, 300);

// 表单验证 - 增强验证确保数据正确性
const validateForm = () => {
  const errors = {};

  if (!todoForm.title.trim()) {
    errors.title = 'Title is required';
  } else if (todoForm.title.length > 100) {
    errors.title = 'Title cannot exceed 100 characters';
  }

  if (!todoForm.priority) {
    errors.priority = 'Priority is required';
  }

  formErrors.value = errors;
  return Object.keys(errors).length === 0;
};

// 错误处理优化
const handleError = (error, defaultMessage) => {
  console.error(error);
  notificationMessage.value = error.response?.data?.message || defaultMessage;
  notificationType.value = 'error';
  showNotification.value = true;

  // 5秒后自动关闭错误提示
  setTimeout(() => {
    showNotification.value = false;
  }, 5000);
};

// 显示通知消息
const showNotificationMessage = (message, type = 'success') => {
  notificationMessage.value = message;
  notificationType.value = type;
  showNotification.value = true;

  // 3秒后自动关闭成功提示
  if (type === 'success' || type === 'info') {
    setTimeout(() => {
      showNotification.value = false;
    }, 3000);
  }
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

// 检查是否过期
const isOverdue = (todo) => {
  if (!todo.dueDate) return false;
  const dueDate = new Date(todo.dueDate);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return dueDate < today;
};

// 获取优先级文本
const getPriorityText = (priority) => {
  const texts = ['', 'High', 'Medium', 'Low'];
  return texts[priority] || 'Medium';
};

// 导航切换
const navigateTo = (page) => {
  currentPage.value = page;
};

// 退出登录
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  router.push('/auth');
};
</script>

<style scoped>
/* 全局样式设置 - 恢复并优化页面背景 */
:root {
  --primary-color: #4F46E5; /* 深紫色作为主色调 */
  --primary-light: #EEF2FF;
  --primary-dark: #4338CA;
  --success-color: #10B981;
  --success-light: #ECFDF5;
  --warning-color: #F59E0B;
  --warning-light: #FFFBEB;
  --error-color: #EF4444;
  --error-light: #FEE2E2;
  --info-color: #3B82F6;
  --info-light: #EFF6FF;
  --text-primary: #1F2937;
  --text-secondary: #6B7280;
  --text-tertiary: #9CA3AF;
  --bg-primary: #F3F4F6; /* 恢复页面背景色 */
  --bg-secondary: #FFFFFF;
  --border-color: #E5E7EB;
  --shadow-sm: 0 2px 5px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 10px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 8px 20px rgba(0, 0, 0, 0.12);
  --radius-sm: 6px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --transition: all 0.3s ease;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
}

/* 恢复页面背景 */
.dashboard-container {
  display: flex;
  min-height: 100vh;
  background-color: var(--bg-primary); /* 主背景色 */
  color: var(--text-primary);
}

/* 侧边栏样式 */
.sidebar {
  width: 260px;
  background-color: var(--bg-secondary); /* 侧边栏白色背景 */
  box-shadow: var(--shadow-sm);
  padding: 25px 0;
  display: flex;
  flex-direction: column;
  transition: var(--transition);
  z-index: 10;
}

.user-profile {
  padding: 0 25px 25px;
  border-bottom: 1px solid var(--border-color);
  text-align: center;
}

.profile-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 15px;
  border: 3px solid var(--primary-light);
  transition: var(--transition);
}

.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-profile h3 {
  font-size: 18px;
  margin-bottom: 5px;
  color: var(--text-primary);
}

.user-profile p {
  font-size: 14px;
  color: var(--text-secondary);
}

.menu {
  flex: 1;
  padding: 20px 0;
}

.menu ul {
  list-style: none;
}

.menu li {
  margin-bottom: 8px;
}

.menu a {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 25px;
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 15px;
  transition: var(--transition);
  border-left: 3px solid transparent;
}

.menu a:hover {
  background-color: var(--bg-primary);
  color: var(--primary-color);
}

.menu li.active a {
  background-color: var(--primary-light);
  color: var(--primary-color);
  border-left-color: var(--primary-color);
  font-weight: 500;
}

.menu i {
  width: 20px;
  text-align: center;
}

.logout {
  padding: 0 25px 20px;
}

.logout button {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background-color: transparent;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-size: 15px;
  cursor: pointer;
  transition: var(--transition);
}

.logout button:hover {
  background-color: var(--error-light);
  color: var(--error-color);
  border-color: var(--error-color);
}

/* 主内容区样式 - 增强背景对比 */
.main-content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
  background-color: var(--bg-primary); /* 确保主内容区有背景色 */
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
}

.header-title h1 {
  font-size: 28px;
  margin-bottom: 8px;
  color: var(--text-primary);
  position: relative;
  padding-bottom: 10px;
}

.header-title h1::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 50px;
  height: 3px;
  background-color: var(--primary-color);
  border-radius: 3px;
}

.header-title p {
  font-size: 15px;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  gap: 10px;
}

.current-date {
  color: var(--primary-color);
  font-weight: 500;
  font-size: 14px;
  padding: 3px 10px;
  background-color: var(--primary-light);
  border-radius: 12px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-bar {
  position: relative;
  margin-right: 15px;
  flex: 1;
  max-width: 300px;
}

.search-bar input {
  width: 100%;
  height: 44px;
  padding: 0 15px 0 40px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: var(--transition);
  box-shadow: var(--shadow-sm);
}

.search-bar input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

.search-bar i {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-tertiary);
  transition: var(--transition);
}

.search-bar input:focus + i {
  color: var(--primary-color);
}

/* 添加按钮样式优化 - 增强可见性 */
.add-todo-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  box-shadow: 0 4px 6px -1px rgba(79, 70, 229, 0.2);
}

.add-todo-btn:hover {
  background-color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 6px 8px -1px rgba(79, 70, 229, 0.3);
}

.add-todo-btn:active {
  transform: translateY(0);
}

/* 统计卡片样式 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 20px;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  position: relative;
  overflow: hidden;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background-color: var(--primary-color);
}

.card-completed::before {
  background-color: var(--success-color);
}

.card-pending::before {
  background-color: var(--warning-color);
}

.card-important::before {
  background-color: var(--error-color);
}

.card-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
  background-color: var(--primary-light);
  color: var(--primary-color);
}

.card-completed .card-icon {
  background-color: var(--success-light);
  color: var(--success-color);
}

.card-pending .card-icon {
  background-color: var(--warning-light);
  color: var(--warning-color);
}

.card-important .card-icon {
  background-color: var(--error-light);
  color: var(--error-color);
}

.card-icon i {
  font-size: 20px;
}

.card-content h3 {
  font-size: 28px;
  margin-bottom: 5px;
  color: var(--text-primary);
}

.card-content p {
  font-size: 14px;
  color: var(--text-secondary);
}

.card-progress {
  height: 6px;
  background-color: var(--border-color);
  border-radius: 3px;
  margin-top: 15px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background-color: var(--primary-color);
  transition: width 1s ease;
}

.card-completed .progress-bar {
  background-color: var(--success-color);
}

.card-pending .progress-bar {
  background-color: var(--warning-color);
}

.card-important .progress-bar {
  background-color: var(--error-color);
}

/* 筛选栏样式 */
.filter-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 15px 20px;
  margin-bottom: 30px;
  box-shadow: var(--shadow-sm);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-group label {
  font-size: 14px;
  color: var(--text-secondary);
  white-space: nowrap;
}

.filter-group select,
.filter-group input {
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 14px;
  color: var(--text-primary);
  transition: var(--transition);
}

.filter-group select:focus,
.filter-group input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

.filter-group input[type="date"] {
  width: 140px;
}

.filter-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 14px;
  cursor: pointer;
  transition: var(--transition);
}

.filter-btn:hover {
  background-color: var(--primary-dark);
}

.reset-filter-btn {
  background: transparent;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
  padding: 10px 18px;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 8px;
}

.reset-filter-btn:hover {
  background-color: var(--bg-primary);
  color: var(--primary-color);
  border-color: var(--primary-light);
}

/* 待办事项列表样式 */
.todos-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.todo-item {
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 15px 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  border-left: 4px solid transparent;
}

.todo-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.todo-item.completed {
  background-color: var(--success-light);
  border-left-color: var(--success-color);
}

.todo-item.completed .todo-content h3 {
  text-decoration: line-through;
  color: var(--text-secondary);
}

.todo-item.high-priority {
  border-left-color: var(--error-color);
}

.todo-item.overdue {
  border-left-color: var(--error-color);
  background-color: var(--error-light);
}

.todo-checkbox input {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid var(--border-color);
  cursor: pointer;
  transition: var(--transition);
}

.todo-checkbox input:checked {
  background-color: var(--success-color);
  border-color: var(--success-color);
  accent-color: var(--success-color);
}

.todo-content {
  flex: 1;
}

.todo-content h3 {
  font-size: 16px;
  margin-bottom: 5px;
  color: var(--text-primary);
  transition: var(--transition);
}

.todo-content p {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.todo-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  font-size: 13px;
}

.due-date {
  display: flex;
  align-items: center;
  gap: 5px;
  color: var(--text-secondary);
}

.overdue-badge {
  background-color: var(--error-color);
  color: white;
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 4px;
  margin-left: 8px;
}

.priority {
  display: flex;
  align-items: center;
  gap: 5px;
}

.priority.high {
  color: var(--error-color);
}

.priority.medium {
  color: var(--warning-color);
}

.priority.low {
  color: var(--success-color);
}

.tags {
  display: flex;
  align-items: center;
  gap: 5px;
  flex-wrap: wrap;
}

.tag {
  background-color: var(--primary-light);
  color: var(--primary-color);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.todo-actions {
  display: flex;
  gap: 10px;
}

.edit-btn, .delete-btn {
  background: transparent;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
}

.edit-btn {
  color: var(--primary-color);
}

.edit-btn:hover {
  background-color: var(--primary-light);
}

.delete-btn {
  color: var(--text-secondary);
}

.delete-btn:hover {
  color: var(--error-color);
  background-color: var(--error-light);
}

/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 50px 20px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px dashed var(--border-color);
}

.empty-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: var(--primary-light);
  color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.empty-icon i {
  font-size: 40px;
}

.empty-state h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.empty-state p {
  font-size: 14px;
  color: var(--text-secondary);
  max-width: 400px;
  margin: 0 auto 25px;
}

.empty-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 14px;
  cursor: pointer;
  transition: var(--transition);
}

.empty-btn:hover {
  background-color: var(--primary-dark);
}

/* 列表加载状态 */
.list-loading {
  text-align: center;
  padding: 60px 20px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.list-loading i {
  font-size: 32px;
  margin-bottom: 15px;
  color: var(--primary-color);
  animation: spin 1s linear infinite;
}

.list-loading p {
  font-size: 16px;
  color: var(--text-secondary);
}

/* 模态框样式 - 确保正确显示 */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  opacity: 1;
  visibility: visible;
  transition: var(--transition);
}

.modal {
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  width: 100%;
  max-width: 550px;
  max-height: 90vh;
  overflow-y: auto;
  transform: translateY(0);
  transition: var(--transition);
}

.modal-content {
  padding: 0;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  font-size: 20px;
  color: var(--text-primary);
}

.close-btn {
  background: transparent;
  border: none;
  color: var(--text-tertiary);
  font-size: 20px;
  cursor: pointer;
  transition: var(--transition);
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.close-btn:hover {
  color: var(--error-color);
  background-color: var(--error-light);
}

.modal-body {
  padding: 25px;
}

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--text-primary);
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 14px;
  color: var(--text-primary);
  transition: var(--transition);
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

.form-group textarea {
  resize: vertical;
}

.form-group input.invalid {
  border-color: var(--error-color);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 30px;
}

.cancel-btn {
  padding: 10px 20px;
  background-color: transparent;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: var(--transition);
}

.cancel-btn:hover {
  background-color: var(--bg-primary);
}

.save-btn {
  padding: 10px 20px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 14px;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 8px;
}

.save-btn:hover {
  background-color: var(--primary-dark);
}

/* 标签样式 */
.tags-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.tag-item {
  background-color: var(--primary-light);
  color: var(--primary-color);
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.remove-tag {
  background: transparent;
  border: none;
  color: var(--text-tertiary);
  cursor: pointer;
  font-size: 12px;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  padding: 0;
}

.remove-tag:hover {
  background-color: rgba(0, 0, 0, 0.1);
  color: var(--primary-color);
}

/* 确认删除模态框 */
.confirm-modal .modal-content {
  max-width: 400px;
}

.task-title {
  font-weight: 600;
  color: var(--text-primary);
  margin: 10px 0;
  padding: 10px;
  background-color: var(--bg-primary);
  border-radius: var(--radius-md);
}

.delete-warning {
  color: var(--error-color);
  font-size: 14px;
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.delete-confirm-btn {
  background-color: var(--error-color);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: var(--radius-md);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 8px;
}

.delete-confirm-btn:hover {
  background-color: #d32f2f;
}

/* 通知样式 */
.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-lg);
  display: flex;
  align-items: center;
  gap: 15px;
  z-index: 1000;
  animation: slideIn 0.3s ease-out forwards;
  background-color: white;
  max-width: 350px;
  border-left: 4px solid transparent;
}

.notification.success {
  border-left-color: var(--success-color);
}

.notification.error {
  border-left-color: var(--error-color);
}

.notification.info {
  border-left-color: var(--info-color);
}

.notification i {
  font-size: 20px;
  flex-shrink: 0;
}

.notification.success i {
  color: var(--success-color);
}

.notification.error i {
  color: var(--error-color);
}

.notification.info i {
  color: var(--info-color);
}

.notification p {
  margin: 0;
  font-size: 14px;
  flex: 1;
  line-height: 1.4;
}

.notification button {
  background: transparent;
  border: none;
  color: var(--text-tertiary);
  cursor: pointer;
  font-size: 16px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: var(--transition);
  flex-shrink: 0;
}

.notification button:hover {
  background-color: var(--bg-primary);
  color: var(--text-secondary);
}

/* 加载遮罩 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 5px solid var(--primary-light);
  border-top-color: var(--primary-color);
  animation: spin 1s linear infinite;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading-spinner i {
  color: var(--primary-color);
  font-size: 24px;
}

/* 工具类样式 */
.required {
  color: var(--error-color);
}

.error-message {
  color: var(--error-color);
  font-size: 12px;
  margin: 5px 0 0 0;
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 动画 */
@keyframes slideIn {
  from { transform: translateX(100%); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 响应式优化 */
@media (max-width: 1024px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .stats-cards .card:last-child {
    grid-column: span 2;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
    padding: 15px 20px;
  }

  .menu ul {
    display: flex;
    overflow-x: auto;
    padding-bottom: 10px;
  }

  .menu li {
    margin-bottom: 0;
    margin-right: 10px;
    flex-shrink: 0;
  }

  .user-profile {
    margin-bottom: 15px;
    display: flex;
    align-items: center;
    gap: 15px;
  }

  .profile-image {
    width: 50px;
    height: 50px;
    margin: 0;
  }

  .stats-cards {
    grid-template-columns: 1fr;
  }

  .stats-cards .card:last-child {
    grid-column: span 1;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .header-actions {
    width: 100%;
  }

  .search-bar {
    max-width: none;
  }

  .modal {
    width: 95%;
  }
}
</style>