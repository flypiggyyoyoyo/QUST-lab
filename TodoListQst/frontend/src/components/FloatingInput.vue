<!-- src/components/FloatingInput.vue -->
<template>
  <div class="floating-input" :class="{ 'has-value': modelValue }">
    <div class="input-icon">
      <i :class="'fas fa-' + icon"></i>
    </div>
    <input
        :type="type"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :required="required"
    />
    <label>{{ label }}</label>
    <div class="underline"></div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  },
  required: {
    type: Boolean,
    default: false
  }
});

defineEmits(['update:modelValue']);
</script>

<style scoped>
.floating-input {
  position: relative;
  margin-bottom: 15px;
}

.input-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: rgba(255, 255, 255, 0.7);
  z-index: 2;
  transition: color 0.3s;
}

input {
  width: 100%;
  height: 50px;
  padding: 15px 20px 0 45px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 16px;
  outline: none;
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
}

input:focus {
  border-color: rgba(167, 119, 227, 0.5);
  background: rgba(255, 255, 255, 0.08);
}

input:focus ~ .input-icon {
  color: #a777e3;
}

label {
  position: absolute;
  top: 16px;
  left: 45px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 16px;
  pointer-events: none;
  transition: all 0.3s ease;
}

.floating-input.has-value label,
input:focus ~ label {
  top: 8px;
  left: 45px;
  font-size: 12px;
  color: #a777e3;
}

.underline {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(to right, #6e8efb, #a777e3);
  transition: width 0.4s ease;
}

input:focus ~ .underline {
  width: 100%;
}
</style>