<template>
  <div class="todo-list">
    <div class="todo-header-row">
      <div
        v-for="(header, index) in headers"
        :key="index"
        :class="headerClasses[index]"
        @click="handleSort(index)"
      >
        {{ header }}
        <button class="todo-header-sort-button">↕️</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const emit = defineEmits(['sort', 'resetSort'])

const headers = ref(['구분', '할 일', '카테고리', '시작일', '마감일'])
const headerClasses = ref([
  'todo-status-header',
  'todo-text-header',
  'todo-category-header',
  'todo-start-date-header',
  'todo-deadline-header',
])

// emit 관련 예시 (필요시 사용)
const handleSort = (index) => {
  const sortKey = ['status', 'todoText', 'category', 'startDate', 'deadline']
  const key = sortKey[index]
  emit('sort', key) // 정렬 키를 이벤트로 전달
}
</script>
