// src/api/axios.js
import axios from "axios";

/**
 * Axios 인스턴스
 *
 * - 프론트(Vue)와 백엔드(Spring Boot) 간 HTTP 통신을 담당하는 공통 객체
 * - 모든 API 요청은 이 인스턴스를 통해 이루어진다.
 *
 * 설정 핵심:
 *   1) baseURL:
 *      - axios.get("/signup") → http://localhost:8080/signup 로 자동 변환됨
 *
 *   2) withCredentials:
 *      - 세션 기반 로그인(JSESSIONID)을 프론트에서도 사용하기 위해 필수
 *      - true 설정 시:
 *          - 요청(Request)에 JSESSIONID 쿠키가 자동 포함됨
 *          - 응답(Response) Set-Cookie로 온 JSESSIONID도 저장됨
 */
const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // 🔥 세션 인증을 위한 쿠키 포함 처리
});



export default api;
