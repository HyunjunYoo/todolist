<template>
  <div class="form-container">
    <div class="input-container">
      <input
        v-model="newTodo"
        @keyup.enter="submitTodo"
        maxlength="30"
        placeholder="할 일을 입력하세요"
      />
      <select v-model="selectedCategory">
        <option v-for="category in categories" :key="category.value" :value="category.value">
          {{ category.icon }} {{ category.label }}
        </option>
      </select>
      <input type="date" v-model="selectedStartDate" />
      <input type="date" v-model="selectedEndDate" />
      <button @click="submitTodo">추가</button>
    </div>

    <!-- 할 일 검색 -->
    <div class="search-container">
      <select v-model="searchStatus">
        <option value="">전체</option>
        <option value="시작 전">시작 전</option>
        <option value="진행 중">진행 중</option>
        <option value="완료">완료</option>
      </select>

      <input
        v-model="searchTodoText"
        @keyup.enter="searchTodos"
        placeholder="검색어를 입력하세요."
      />

      <select v-model="searchCategory">
        <option value="">전체</option>
        <option v-for="category in categories" :key="category.value" :value="category.value">
          {{ category.icon }} {{ category.label }}
        </option>
      </select>

      <input type="date" v-model="searchStartDate" />
      <div class="tooltip-text">시작일을 기준으로 검색</div>
      <button @click="searchTodos">검색</button>
      <button @click="resetSearch">↻ 초기화</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 상태 및 변수 정의
const getToday = () => new Date().toISOString().slice(0, 10)

const newTodo = ref('')
const selectedStatus = ref('시작 전')
const selectedStartDate = ref(getToday())
const selectedEndDate = ref('')
const selectedCategory = ref('개인')
const searchTodoText = ref('')
const searchCategory = ref('')
const searchStatus = ref('')
const searchStartDate = ref('')
const categories = ref([
  { value: '개인', label: '개인', icon: '😃' },
  { value: '업무', label: '업무', icon: '💼' },
  { value: '학습', label: '학습', icon: '📚' },
  { value: '건강', label: '건강', icon: '🏃' },
  { value: '가사', label: '가사', icon: '🏠' },
  { value: '기타', label: '기타', icon: '🎸' },
])

const emit = defineEmits(['add-todo', 'search-Todos', 'resetSearch'])

// Todo 추가
const submitTodo = () => {
  if (!newTodo.value.trim()) return
  if (selectedEndDate.value && selectedEndDate.value < selectedStartDate.value) {
    alert('마감일은 시작일보다 빠를 수 없습니다.')
    return
  }

  const todoData = {
    status: selectedStatus.value,
    todoText: newTodo.value.trim(),
    category: selectedCategory.value,
    startDate: selectedStartDate.value || '없음',
    endDate: selectedEndDate.value || '없음',
  }

  emit('add-todo', todoData)
  resetTodoFields()
}

// 할 일 필드 초기화
const resetTodoFields = () => {
  newTodo.value = ''
  selectedCategory.value = '개인'
  selectedStartDate.value = getToday()
  selectedEndDate.value = ''
}

// 검색 기능
const searchTodos = () => {
  const searchData = {
    status: searchStatus.value || null,
    todoText: searchTodoText.value.trim() || null,
    category: searchCategory.value || null,
    startDate: searchStartDate.value || null,
  }
  emit('search-Todos', searchData)
}

// 검색 초기화
const resetSearch = () => {
  emit('resetSearch')
  searchTodoText.value = ''
  searchCategory.value = ''
  searchStatus.value = ''
  searchStartDate.value = ''
}
</script>
