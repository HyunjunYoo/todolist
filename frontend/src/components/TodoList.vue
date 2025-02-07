<template>
  <div>
    <header>
      <h1>✨ To-Do List ✨</h1>
    </header>
    <TodoInput @add-todo="addTodo" @search-Todos="searchTodos" @resetSearch="resetSearch" />
    <TodoItemHeader @sort="updateSort" />
    <div class="todo-wrapper">
      <TodoItem
        v-for="todo in sortedTodos"
        :todo="todo"
        :key="todo.id"
        @updateTodoText="updateTodoText"
        @updateTodoStatus="updateTodoStatus"
        @deleteTodo="deleteTodo"
      />
    </div>
    <button v-show="isVisible" @click="scrollToTop" class="scroll-to-top">▲</button>
    <footer>
      <p>&copy; 2025 To-Do List App. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import '../assets/styles.css'
import { ref, computed, onMounted, onUnmounted } from 'vue'
import TodoInput from './TodoInput.vue'
import TodoItem from './TodoItem.vue'
import TodoItemHeader from './TodoItemHeader.vue'
import apiClient from '@/api/apiClient'

//  상태: 할 일 리스트
const todos = ref([])
const currentSortKey = ref(null) // 정령 항목
const sortDirection = ref('asc') // 정렬 상태
const isVisible = ref(false) // 맨위로

// 서버에서 Todo 목록 가져오기
const fetchTodos = async () => {
  try {
    const response = await apiClient.get('/todos')
    todos.value = response.data
    console.log('Todos 가져오기 성공:', todos.value)
  } catch (error) {
    console.error('Todos 가져오기 실패:', error)
  }
}

// 메서드: 할 일 추가
const addTodo = async (newTodo) => {
  try {
    const requestData = [newTodo]
    const response = await apiClient.post('/todos/add', requestData)
    console.log('Todo 추가 성공', response.data)
    fetchTodos()
  } catch (error) {
    console.error('Todo 추가 실패', error)
  }
}

// 할 일 수정
const updateTodoText = async (editTodo) => {
  try {
    const response = await apiClient.put('/todos/update', {
      id: editTodo.id,
      todoText: editTodo.todoText,
    })
    console.log('수정 성공', response.data)
    alert('수정되었습니다.')

    console.log('fetchTodos() 실행 전')
    fetchTodos()
    console.log('fetchTodos() 실행 완료')
  } catch (error) {
    console.error('수정 실패: ', error)
  }
}

// 할 일 삭제
const deleteTodo = async (todoId) => {
  const isConfirmed = confirm('정말 삭제하시겠습니까?')
  if (!isConfirmed) return
  try {
    const response = await apiClient.post('/todos/delete', { id: todoId })
    console.log('삭제 성공', response.data)
    fetchTodos()
  } catch (error) {
    console.error('삭제 실패:', error)
  }
}

// 할 일 조회
const searchTodos = async (searchData) => {
  console.log('검색 조건', searchData)
  try {
    const response = await apiClient.post('/todos/search', searchData)
    todos.value = response.data
    console.log('조회 성공', response.data)
  } catch (error) {
    console.error('조회 실패:', error)
  }
}

// 검색 초기화
const resetSearch = () => {
  fetchTodos()
}

// 상태 변경 로직
const updateTodoStatus = async (todo) => {
  try {
    const response = await apiClient.put('/todos/updateStatus', {
      id: todo.id,
      status: todo.status,
    })
    console.log('상태 저장 성공', response.data)
  } catch (error) {
    console.error('상태 저장 실패', error)
  }
}

// 사용자가 선택한 정렬 종류 및 오름차순, 내림차순 삽입입
const updateSort = (key) => {
  if (key === currentSortKey.value) {
    sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
  } else {
    currentSortKey.value = key
    sortDirection.value = 'asc'
  }
}

// 상태 정렬 로직
const statusOrder = { '시작 전': 0, '진행 중': 1, 완료: 2 }
const sortStatus = (a, b, key, direction) => {
  //a,b 할 일 객체/정렬기준(상태)/오름 내림차순
  return direction === 'asc' // 오름차순이면
    ? statusOrder[a[key]] - statusOrder[b[key]] // 각각의 상태를 배열의 순서대로 정렬하여 return
    : statusOrder[b[key]] - statusOrder[a[key]] // 정렬이 되는 이유는 sort함수의 원리리
}

// 날짜 정렬 로직
const sortDate = (a, b, key, direction) => {
  const dateA = new Date(a[key] || '1970-01-01')
  const dateB = new Date(b[key] || '1970-01-01')
  return direction === 'asc' ? dateA - dateB : dateB - dateA //날짜 계산 메서드 찾아보기
}

// 정렬된 todos를 계산하는 computed 속성
const sortedTodos = computed(() => {
  // 정렬 실행 전 todos를 반환
  if (!currentSortKey.value) return todos.value

  const key = currentSortKey.value
  const direction = sortDirection.value
  const sorted = [...todos.value]

  sorted.sort((a, b) => {
    // 상태 정렬
    if (key === 'status') return sortStatus(a, b, key, sortDirection.value)
    // 날짜 정렬
    if (key === 'startDate' || key === 'endDate') return sortDate(a, b, key, sortDirection.value)
    // 일반 텍스트 또는 숫자 정렬
    const valA = a[key]?.toString().toLowerCase() || ''
    const valB = b[key]?.toString().toLowerCase() || ''
    if (valA < valB) return direction === 'asc' ? -1 : 1
    if (valA > valB) return direction === 'asc' ? 1 : -1
    return 0
  })
  return sorted
})

const handleScroll = () => {
  isVisible.value = window.scrollY > 200 // 200px 이상 스크롤 시 버튼 표시
}
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
// 컴포넌트 로드 시 초기 데이터를 가져옴
fetchTodos()
</script>
