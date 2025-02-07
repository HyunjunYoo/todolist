<template>
  <div class="todo-list">
    <div class="todo-item">
      <div class="todo-status">
        <button :class="getStatusClass(newTodo.status)" @click="updateTodoStatus">
          {{ newTodo.status }}
        </button>
      </div>

      <!-- 할 일 텍스트 -->
      <div class="todo-text">
        <span v-if="!isEditing">{{ newTodo.todoText }}</span>
        <!-- <span v-if="!isEditing">{{ Todo.todoText }}</span> -->
        <input v-else v-model="editableTodoText" class="editTodoText" />
      </div>

      <!-- 수정/완료 버튼 -->
      <div>
        <button @click="toggleEditMode" class="editButton">
          {{ isEditing ? '☑️' : '✏️' }}
        </button>
      </div>

      <!-- 카테고리리 -->
      <div class="todo-category">
        <span>{{ newTodo.category }}</span>
      </div>

      <!-- 시작일 -->
      <div class="todo-start-date">
        <span>{{ newTodo.startDate }}</span>
      </div>

      <!-- 마감일 -->
      <div class="todo-deadline">
        <span>{{ newTodo.endDate }}</span>
      </div>

      <!-- <div>
        <span>{{ newTodo.createDate }}</span>
      </div> -->

      <!-- 삭제 버튼 -->
      <div class="todo-remove">
        <button @click="emit('deleteTodo', newTodo.id)" class="editButton">🗑️</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  todo: {
    type: Object,
    required: true,
  },
})

// update시 즉각적으로 데이터 반영이 되지 않는 문제 발생
// todo는 props.todo의 복사본이 아니라, 주소를 감싸는 ref가 된다. => 내부 변경 감지X
// todo.value는 처음 props.todo를 받을 때 한 번만 설정되고,
// 부모가 props.todo를 변경해도 todo.value는 그대로 남아 있음 => 즉시 반영X
// props.todo.text를 직접 참조함으로써, 부모의 데이터가 변경될 때 Vue가 자동으로 업데이트
// ref를 담는 순간 연결성을 잃어버림

// const todo = ref(props.todo)
const newTodo = computed(() => props.todo)
const emit = defineEmits(['deleteTodo', 'updateTodoStatus', 'updateTodoText'])

const isEditing = ref(false)
const editableTodoText = ref(props.todo.todoText)

// 수정 모드 토글
const toggleEditMode = () => {
  if (isEditing.value) {
    if (editableTodoText.value === null || editableTodoText.value === '') {
      alert('할 일을 작성해주십시오.')
      return
    }

    const editData = {
      id: props.todo.id,
      todoText: editableTodoText.value,
    }
    emit('updateTodoText', editData)
  }
  isEditing.value = !isEditing.value
}

const updateTodoStatus = () => {
  const statusCycle = ['시작 전', '진행 중', '완료']
  const currentStatus = newTodo.value.status
  newTodo.value.status = statusCycle[(statusCycle.indexOf(currentStatus) + 1) % statusCycle.length]

  emit('updateTodoStatus', newTodo.value)
}

function getStatusClass(status) {
  switch (status) {
    case '시작 전':
      return 'status-red'
    case '진행 중':
      return 'status-orange'
    case '완료':
      return 'status-green'
    default:
      return ''
  }
}
</script>
