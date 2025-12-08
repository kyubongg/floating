// src/stores/auth.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "../api/axios";

/**
 * useAuthStore
 *
 * - 인증/인가 관련 전역 상태를 관리하는 Pinia 스토어
 * - 세션 기반(Spring Security + JSESSIONID) 로그인 구조에서
 *   사용자 정보(user), 로딩 상태, 에러 메시지 등을 관리한다.
 */
export const useAuthStore = defineStore("auth", () => {
  // ---------- state ----------
  const user = ref(null);          // 현재 로그인한 사용자 정보 { username: "..." }
  const initialized = ref(false);  // /api/me로 로그인 상태를 한 번이라도 확인했는지 여부
  const loading = ref(false);      // API 호출 중 로딩 상태
  const error = ref(null);         // 마지막 에러 메시지

  // ---------- getters ----------
  const isAuthenticated = computed(() => !!user.value);

  // ---------- actions ----------

  /**
   * 회원가입
   * - username, password를 받아 /signup API 호출
   * - 성공 시 단순히 완료만 알려주고, user 상태는 변경하지 않음
   */
  const signup = ({ username, password }) => {
    loading.value = true;
    error.value = null;

    // axios는 Promise를 반환하므로 then/catch/finally 체인 사용
    return api
      .post("/signup", { username, password })
      .then(() => {
        // 성공 시 별도 작업은 없음
      })
      .catch((e) => {
        // 서버에서 온 메시지가 있으면 사용, 없으면 기본 메시지
        error.value = e?.response?.data || "회원가입 실패";
        throw e; // 호출한 쪽(LoginView 등)에서 then/catch로 후속 처리할 수 있게 다시 던짐
      })
      .finally(() => {
        loading.value = false;
      });
  };

  /**
   * 로그인
   * - username/password로 /login 호출
   * - 성공 시 user 상태를 응답 값으로 세팅
   */
  const login = ({ username, password }) => {
    loading.value = true;
    error.value = null;

    const params = new URLSearchParams();
    params.append("username", username);
    params.append("password", password);

    return api
      .post("/login", params, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      })
      .then((res) => {
        // 백엔드에서 내려준 JSON: { username: "..." }
        user.value = res.data;
        initialized.value = true;
      })
      .catch((e) => {
        error.value = e?.response?.data || "로그인 실패";
        throw e;
      })
      .finally(() => {
        loading.value = false;
      });
  };

  /**
   * 로그아웃
   * - /logout 호출 후 user 상태를 null로 초기화
   */
  const logout = () => {
    loading.value = true;
    error.value = null;

    return api
      .post("/logout")
      .then(() => {
        user.value = null;
      })
      .catch((e) => {
        error.value = e?.response?.data || "로그아웃 실패";
        // 굳이 throw 하지 않아도 되지만, 필요하면 여기서도 throw 가능
      })
      .finally(() => {
        loading.value = false;
      });
  };

  /**
   * 세션 기반 로그인 상태 동기화
   * - 새로고침(F5) 이후에도 /api/me 를 통해 서버 세션에 로그인 상태가 남아 있는지 확인
   * - 로그인 상태라면 user를 세팅, 아니라면 null로 초기화
   */
  const fetchMe = () => {
    loading.value = true;
    error.value = null;

    return api
      .get("/api/me")
      .then((res) => {
        if (res.data && res.data.username) {
          user.value = res.data;
        } else {
          user.value = null;
        }
      })
      .catch(() => {
        // 401 혹은 기타 에러 시 로그인 안 된 것으로 처리
        user.value = null;
      })
      .finally(() => {
        loading.value = false;
        initialized.value = true; // 최소 한 번은 상태 확인 완료
      });
  };

  // 스토어에서 외부로 노출할 상태/메서드
  return {
    user,
    initialized,
    loading,
    error,
    isAuthenticated,
    signup,
    login,
    logout,
    fetchMe,
  };
});
