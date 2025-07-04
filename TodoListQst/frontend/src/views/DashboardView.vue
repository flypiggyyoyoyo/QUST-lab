<template>
  <div class="dashboard-container">
    <!-- Sidebar -->
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
        <button @click="logout" :disabled="loading" class="btn-logout">
          <i class="fas fa-sign-out-alt"></i>
          <span>Logout</span>
        </button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <!-- Header -->
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

      <!-- Stats Cards -->
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

      <!-- Filter Bar -->
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

      <!-- Todo List -->
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

        <!-- Empty State -->
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

        <!-- Loading State -->
        <div v-if="loading && todos.length === 0" class="list-loading">
          <i class="fas fa-spinner fa-spin"></i>
          <p>Loading your tasks...</p>
        </div>
      </div>
    </main>

    <!-- Add/Edit Todo Modal -->
    <div v-if="showTodoModal" class="modal-backdrop" @click.self="closeTodoModal">
      <div class="modal" :class="{ 'modal-enter': true }">
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

    <!-- Confirm Delete Modal -->
    <div v-if="showDeleteConfirm" class="modal-backdrop" @click.self="cancelDelete">
      <div class="modal confirm-modal" :class="{ 'modal-enter': true }">
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

    <!-- Loading Overlay -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <i class="fas fa-spinner fa-spin"></i>
      </div>
    </div>

    <!-- Notification -->
    <div
        v-if="showNotification"
        class="notification"
        :class="notificationType"
    >
      <i :class="notificationIcon"></i>
      <p>{{ notificationMessage }}</p>
      <button @click="showNotification = false" class="close-notification">
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

// Router setup
const router = useRouter();

// Axios configuration
axios.defaults.baseURL = 'http://localhost:8080/api/v1';

axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axios.interceptors.response.use(
    response => response,
    error => {
      if (error.response && error.response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        router.push('/auth');
      }
      return Promise.reject(error);
    }
);

// State management
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

// Filter conditions
const filterStatus = ref('all');
const filterPriority = ref('all');
const filterStartDate = ref('');
const filterEndDate = ref('');

// Todo form
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

// Computed properties
const filteredTodos = computed(() => {
  return todos.value.filter(todo => {
    const statusMatch = filterStatus.value === 'all' ||
        (filterStatus.value === 'completed' && todo.completed) ||
        (filterStatus.value === 'pending' && !todo.completed);

    const priorityMatch = filterPriority.value === 'all' ||
        parseInt(filterPriority.value) === todo.priority;

    const dateMatch = (!filterStartDate.value ||
            new Date(todo.dueDate || '9999-12-31') >= new Date(filterStartDate.value)) &&
        (!filterEndDate.value ||
            new Date(todo.dueDate || '0000-01-01') <= new Date(filterEndDate.value));

    const searchMatch = !searchQuery.value ||
        todo.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        (todo.description && todo.description.toLowerCase().includes(searchQuery.value.toLowerCase()));

    return statusMatch && priorityMatch && dateMatch && searchMatch;
  });
});

const hasActiveFilters = computed(() => {
  return filterStatus.value !== 'all' ||
      filterPriority.value !== 'all' ||
      filterStartDate.value ||
      filterEndDate.value;
});

const isDefaultFilterState = computed(() => {
  return filterStatus.value === 'all' &&
      filterPriority.value === 'all' &&
      !filterStartDate.value &&
      !filterEndDate.value &&
      !searchQuery.value;
});

const formattedDate = computed(() => {
  return new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    month: 'long',
    day: 'numeric'
  });
});

const minDueDate = computed(() => {
  return new Date().toISOString().split('T')[0];
});

const notificationIcon = computed(() => {
  return notificationType.value === 'success'
      ? 'fas fa-check-circle'
      : 'fas fa-exclamation-circle';
});

// Lifecycle hooks
onMounted(() => {
  initApp();
});

// Functions
const initApp = async () => {
  try {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      user.value = JSON.parse(storedUser);
    } else {
      await router.push('/auth');
      return;
    }
    await Promise.all([fetchTodos(), fetchTodoStats()]);
  } catch (error) {
    handleError(error, 'Failed to initialize application');
  }
};

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

const fetchTodoStats = async () => {
  try {
    const response = await axios.get('/todos/stats');
    stats.value = response.data.data;
  } catch (error) {
    handleError(error, 'Failed to fetch stats');
  }
};

const createTodo = async () => {
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
    const response = await axios.post('/todos', requestData);
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

const prepareDeleteTodo = (todo) => {
  taskToDelete.value = todo;
  showDeleteConfirm.value = true;
};

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

const cancelDelete = () => {
  showDeleteConfirm.value = false;
  taskToDelete.value = null;
};

const openAddTodoModal = () => {
  isEditing.value = false;
  resetForm();
  showTodoModal.value = true;
};

const openEditTodoModal = (todo) => {
  isEditing.value = true;
  resetForm();
  todoForm.id = todo.id;
  todoForm.title = todo.title;
  todoForm.description = todo.description;
  todoForm.dueDate = todo.dueDate ? new Date(todo.dueDate).toISOString().split('T')[0] : '';
  todoForm.priority = todo.priority;
  todoForm.completed = todo.completed;
  if (todo.tags) {
    todoForm.tags = [...todo.tags.map(tag => tag.name)];
  }
  showTodoModal.value = true;
};

const closeTodoModal = () => {
  showTodoModal.value = false;
  resetForm();
};

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

const submitTodo = () => {
  if (isEditing.value) {
    updateTodo();
  } else {
    createTodo();
  }
};

watch(tagInput, (newVal) => {
  if (newVal && newVal.includes(',')) {
    const tags = newVal.split(',').map(tag => tag.trim()).filter(Boolean);
    todoForm.tags = [...new Set([...todoForm.tags, ...tags])];
    tagInput.value = '';
  }
});

const removeTag = (index) => {
  todoForm.tags.splice(index, 1);
};

const debouncedApplyFilters = debounce(() => {}, 300);

const applyFilters = () => {
  debouncedApplyFilters();
  showNotificationMessage('Filters applied!', 'info');
};

const resetFilters = () => {
  filterStatus.value = 'all';
  filterPriority.value = 'all';
  filterStartDate.value = '';
  filterEndDate.value = '';
  searchQuery.value = '';
  showNotificationMessage('Filters reset!', 'info');
};

const handleSearch = debounce(() => {}, 300);

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

const handleError = (error, defaultMessage) => {
  console.error(error);
  notificationMessage.value = error.response?.data?.message || defaultMessage;
  notificationType.value = 'error';
  showNotification.value = true;
  setTimeout(() => {
    showNotification.value = false;
  }, 5000);
};

const showNotificationMessage = (message, type = 'success') => {
  notificationMessage.value = message;
  notificationType.value = type;
  showNotification.value = true;
  if (type === 'success' || type === 'info') {
    setTimeout(() => {
      showNotification.value = false;
    }, 3000);
  }
};

const formatDate = (dateString) => {
  if (!dateString) return null;
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const isOverdue = (todo) => {
  if (!todo.dueDate) return false;
  const dueDate = new Date(todo.dueDate);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return dueDate < today;
};

const getPriorityText = (priority) => {
  const texts = ['', 'High', 'Medium', 'Low'];
  return texts[priority] || 'Medium';
};

const navigateTo = (page) => {
  currentPage.value = page;
};

const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  router.push('/auth');
};
</script>

<style scoped>
/* Global Styles */
:root {
  --primary-color: #5B6EF7; /* Modern purple-blue */
  --primary-light: #E6E9FF;
  --primary-dark: #4A5DE8;
  --success-color: #28A745;
  --success-light: #E8F5E9;
  --warning-color: #FF9800;
  --warning-light: #FFF3E0;
  --error-color: #DC3545;
  --error-light: #FDECEA;
  --info-color: #17A2B8;
  --info-light: #E3F2FD;
  --text-primary: #2D3748;
  --text-secondary: #718096;
  --text-tertiary: #A0AEC0;
  --bg-primary: #F7FAFC;
  --bg-secondary: #FFFFFF;
  --border-color: #E2E8F0;
  --shadow-sm: 0 2px 5px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 10px 25px rgba(0, 0, 0, 0.1);
  --radius-sm: 6px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --transition: all 0.3s ease;
  --sidebar-width: 260px;
  --header-height: 80px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* Dashboard Container */
.dashboard-container {
  display: flex;
  min-height: 100vh;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  overflow: hidden;
}

/* Sidebar */
.sidebar {
  width: var(--sidebar-width);
  background: linear-gradient(180deg, var(--primary-color), var(--primary-dark));
  color: white;
  padding: 24px;
  display: flex;
  flex-direction: column;
  transition: var(--transition);
  box-shadow: var(--shadow-md);
  z-index: 10;
}

.user-profile {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.profile-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 15px;
  border: 3px solid rgba(255, 255, 255, 0.2);
  box-shadow: var(--shadow-md);
  transition: var(--transition);
}

.profile-image:hover {
  transform: scale(1.05);
}

.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-profile h3 {
  font-size: 18px;
  margin-bottom: 5px;
  font-weight: 600;
}

.user-profile p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.menu ul {
  list-style: none;
  margin-bottom: 30px;
}

.menu li {
  margin-bottom: 8px;
}

.menu a {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 15px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 15px;
  border-radius: var(--radius-sm);
  transition: var(--transition);
}

.menu a:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
}

.menu li.active a {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 500;
}

.logout {
  margin-top: auto;
}

.btn-logout {
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  font-size: 15px;
  cursor: pointer;
  transition: var(--transition);
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-logout:hover {
  background-color: rgba(255, 255, 255, 0.15);
}

.btn-logout:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Main Content */
.main-content {
  flex: 1;
  padding: 25px;
  background-color: var(--bg-primary);
  overflow-y: auto;
  height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border-color);
}

.header-title h1 {
  font-size: 26px;
  color: var(--primary-color);
  margin-bottom: 5px;
  font-weight: 600;
}

.header-title p {
  font-size: 15px;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.current-date {
  color: var(--text-tertiary);
  font-size: 14px;
  padding-left: 8px;
  border-left: 1px solid var(--border-color);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-bar {
  position: relative;
  width: 300px;
}

.search-bar input {
  width: 100%;
  padding: 12px 35px 12px 15px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 14px;
  transition: var(--transition);
  background-color: var(--bg-secondary);
}

.search-bar input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(91, 110, 247, 0.1);
}

.search-bar i {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-tertiary);
  font-size: 15px;
}

.add-todo-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  font-size: 14px;
}

.add-todo-btn:hover {
  background-color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.add-todo-btn:active {
  transform: translateY(0);
}

.add-todo-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* Stats Cards */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 25px;
}

.card {
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
  padding: 20px;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  position: relative;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-color: var(--primary-color);
}

.card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-3px);
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
  font-size: 26px;
  color: var(--primary-color);
  margin-bottom: 12px;
}

.card-completed .card-icon {
  color: var(--success-color);
}

.card-pending .card-icon {
  color: var(--warning-color);
}

.card-important .card-icon {
  color: var(--error-color);
}

.card-content h3 {
  font-size: 28px;
  margin-bottom: 5px;
  font-weight: 600;
  color: var(--text-primary);
}

.card-content p {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 15px;
}

.card-progress {
  height: 6px;
  background-color: var(--border-color);
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background-color: var(--primary-color);
  transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1);
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

/* Filter Bar */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 25px;
  flex-wrap: wrap;
  padding: 15px 20px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-md);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-size: 14px;
  color: var(--text-secondary);
  white-space: nowrap;
}

.filter-group select,
.filter-group input {
  padding: 9px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  font-size: 14px;
  transition: var(--transition);
  background-color: var(--bg-secondary);
}

.filter-group select:focus,
.filter-group input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.filter-group input[type="date"] {
  width: 140px;
}

.filter-btn, .reset-filter-btn {
  padding: 9px 18px;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: var(--transition);
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.filter-btn {
  background-color: var(--primary-color);
  color: white;
}

.filter-btn:hover {
  background-color: var(--primary-dark);
}

.filter-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.reset-filter-btn {
  background-color: var(--bg-secondary);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.reset-filter-btn:hover {
  background-color: var(--primary-light);
  color: var(--primary-color);
  border-color: var(--primary-light);
}

.reset-filter-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Todo List */
.todos-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.todo-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  border: 1px solid var(--border-color);
  position: relative;
  overflow: hidden;
}

.todo-item:hover {
  box-shadow: var(--shadow-md);
  transform: translateX(3px);
}

.todo-item.completed {
  border-left: 4px solid var(--success-color);
  opacity: 0.9;
}

.todo-item.high-priority {
  border-left: 4px solid var(--error-color);
}

.todo-item.overdue {
  border-left: 4px solid var(--error-color);
}

.todo-item.completed .todo-content h3 {
  text-decoration: line-through;
  color: var(--text-tertiary);
}

.todo-checkbox {
  margin-right: 15px;
  min-width: 20px;
}

.todo-checkbox input {
  width: 18px;
  height: 18px;
  accent-color: var(--primary-color);
  cursor: pointer;
}

.todo-content {
  flex: 1;
}

.todo-content h3 {
  font-size: 16px;
  margin-bottom: 6px;
  color: var(--text-primary);
  transition: var(--transition);
  font-weight: 600;
}

.todo-content p {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 10px;
  line-height: 1.4;
}

.todo-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  font-size: 13px;
  color: var(--text-tertiary);
}

.due-date {
  display: flex;
  align-items: center;
  gap: 5px;
}

.overdue-badge {
  background-color: var(--error-light);
  color: var(--error-color);
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 12px;
  margin-left: 5px;
  font-weight: 500;
}

.priority {
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: 500;
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
  gap: 8px;
}

.tag {
  background-color: var(--primary-light);
  color: var(--primary-color);
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 12px;
  margin-left: 3px;
}

.todo-actions {
  display: flex;
  align-items: center;
}

.edit-btn, .delete-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  transition: var(--transition);
  padding: 5px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-btn {
  color: var(--primary-color);
}

.edit-btn:hover {
  background-color: var(--primary-light);
}

.delete-btn {
  color: var(--error-color);
}

.delete-btn:hover {
  background-color: var(--error-light);
}

.edit-btn:disabled, .delete-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 40px 20px;
  background-color: var(--bg-secondary);
  border-radius: var(--radius-md);
  border: 1px dashed var(--border-color);
  margin-top: 20px;
}

.empty-icon {
  font-size: 48px;
  color: var(--text-tertiary);
  margin-bottom: 15px;
}

.empty-state h3 {
  font-size: 18px;
  color: var(--text-primary);
  margin-bottom: 10px;
}

.empty-state p {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 20px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.empty-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: var(--transition);
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin: 0 auto;
}

.empty-btn:hover {
  background-color: var(--primary-dark);
}

.empty-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* List Loading */
.list-loading {
  text-align: center;
  padding: 50px 20px;
  color: var(--text-secondary);
}

.list-loading i {
  font-size: 32px;
  margin-bottom: 15px;
  color: var(--primary-color);
}

/* Modal */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
  opacity: 0;
  visibility: hidden;
  transition: var(--transition);
}

.modal-backdrop:not([v-if=false]) {
  opacity: 1;
  visibility: visible;
}

.modal {
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  transform: translateY(20px) scale(0.98);
  transition: var(--transition);
  opacity: 0;
}

.modal-enter {
  transform: translateY(0) scale(1);
  opacity: 1;
}

.confirm-modal {
  width: 400px;
}

.modal-content {
  padding: 0;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 20px;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  font-size: 18px;
  color: var(--text-primary);
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: var(--text-tertiary);
  transition: var(--transition);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background-color: var(--primary-light);
  color: var(--primary-color);
}

.close-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: var(--text-primary);
  margin-bottom: 8px;
  font-weight: 500;
}

.required {
  color: var(--error-color);
}

.form-group input, .form-group textarea, .form-group select {
  width: 100%;
  padding: 11px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  font-size: 15px;
  transition: var(--transition);
}

.form-group input:focus, .form-group textarea:focus, .form-group select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(91, 110, 247, 0.1);
}

.form-group textarea {
  resize: vertical;
}

.form-group input.invalid {
  border-color: var(--error-color);
}

.error-message {
  color: var(--error-color);
  font-size: 13px;
  margin-top: 5px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 25px;
}

.save-btn, .delete-confirm-btn, .cancel-btn {
  padding: 10px 20px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: var(--transition);
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.save-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
}

.save-btn:hover {
  background-color: var(--primary-dark);
}

.save-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.delete-confirm-btn {
  background-color: var(--error-color);
  color: white;
  border: none;
}

.delete-confirm-btn:hover {
  background-color: #C82333;
}

.delete-confirm-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.cancel-btn {
  background-color: white;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.cancel-btn:hover {
  background-color: var(--bg-primary);
}

.cancel-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.tags-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.tag-item {
  background-color: var(--primary-light);
  color: var(--primary-color);
  padding: 4px 10px;
  border-radius: 15px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.remove-tag {
  background: none;
  border: none;
  color: var(--primary-color);
  cursor: pointer;
  font-size: 12px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.remove-tag:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.task-title {
  font-weight: 600;
  color: var(--text-primary);
  margin: 10px 0;
  padding: 10px;
  background-color: var(--bg-primary);
  border-radius: var(--radius-sm);
  border-left: 3px solid var(--error-color);
}

.delete-warning {
  color: var(--error-color);
  font-size: 13px;
  margin-top: 15px;
  padding: 10px;
  background-color: var(--error-light);
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Notification */
.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 14px 20px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  display: flex;
  align-items: center;
  gap: 12px;
  z-index: 1000;
  max-width: 350px;
  transform: translateX(calc(100% + 20px));
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
}

.notification:not([v-if=false]) {
  transform: translateX(0);
}

.notification.success {
  background-color: var(--success-light);
  color: var(--success-color);
  border-color: var(--success-color);
}

.notification.error {
  background-color: var(--error-light);
  color: var(--error-color);
  border-color: var(--error-color);
}

.notification.info {
  background-color: var(--info-light);
  color: var(--info-color);
  border-color: var(--info-color);
}

.notification i {
  font-size: 18px;
}

.notification p {
  flex: 1;
  font-size: 14px;
  line-height: 1.4;
}

.close-notification {
  background: none;
  border: none;
  color: inherit;
  cursor: pointer;
  font-size: 16px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
}

.close-notification:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

/* Loading Overlay */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
  opacity: 0;
  visibility: hidden;
  transition: var(--transition);
}

.loading-overlay:not([v-if=false]) {
  opacity: 1;
  visibility: visible;
}

.loading-spinner {
  font-size: 32px;
  color: var(--primary-color);
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Responsive Design */
@media (max-width: 992px) {
  .search-bar {
    width: 220px;
  }

  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .sidebar {
  var(--sidebar-width): 80px;
    padding: 15px 10px;
  }

  .user-profile h3, .user-profile p, .menu span, .logout span {
    display: none;
  }

  .menu a {
    justify-content: center;
    padding: 12px;
  }

  .btn-logout {
    justify-content: center;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .search-bar {
    width: 100%;
  }

  .stats-cards {
    grid-template-columns: 1fr;
  }

  .filter-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-group {
    width: 100%;
  }

  .filter-group select,
  .filter-group input[type="date"] {
    width: 100%;
  }

  .todo-meta {
    flex-wrap: wrap;
    gap: 10px;
  }
}

@media (max-width: 576px) {
  .main-content {
    padding: 15px;
  }

  .add-todo-btn span {
    display: none;
  }

  .add-todo-btn {
    padding: 10px;
  }

  .todo-item {
    padding: 12px 15px;
  }

  .form-actions {
    flex-direction: column;
  }

  .save-btn, .cancel-btn, .delete-confirm-btn {
    width: 100%;
  }
}
</style>