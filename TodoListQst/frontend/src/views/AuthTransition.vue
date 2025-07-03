<!-- src/views/AuthTransition.vue -->
<template>
  <transition :name="transitionName" mode="out-in">
    <slot></slot>
  </transition>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  isLogin: {
    type: Boolean,
    required: true
  }
});

const transitionName = computed(() => {
  return props.isLogin ? 'slide-right' : 'slide-left';
});
</script>

<style scoped>
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.slide-left-enter-from,
.slide-right-leave-to {
  opacity: 0;
  transform: translateX(50px);
}

.slide-left-leave-to,
.slide-right-enter-from {
  opacity: 0;
  transform: translateX(-50px);
}
</style>