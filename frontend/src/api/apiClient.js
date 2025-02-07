import axios from 'axios' // HTTP 요청 라이브러리

const apiClient = axios.create({
  // 커스텀 인스턴스 생성
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 5000,
})

export default apiClient
