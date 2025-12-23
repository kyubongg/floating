// src/stores/auth.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "../api/axios";
import { useWbtiStore } from "./wbti";

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

  const wbtiStore = useWbtiStore();

  // ---------- getters ----------
  const isAuthenticated = computed(() => !!user.value);
  const hasWbti = computed(() => {
    return !!(user.value && user.value.wbtiCode);
  });

  const userName = computed(() => user.value?.name || '게스트');
  const userWbtiCode = computed(() => user.value?.wbtiCode || '미정');
  const userGender = computed(() => {
    if (user.value?.gender === 'M') {
      return '남';
    }
    else {
      return '여';
    }
  })
  const verifiedEmail = ref('');

  const currentScore = computed(() => user.value?.petScore || '0');
  const maxScoreByLevel = 100;
  const scorePercentage = computed(() => {
    return (currentScore.value % maxScoreByLevel);
  });
  const level = computed(() => {
    return Math.floor(currentScore.value / 100) + 1;
  });

  // ---------- actions ----------

  /**
   * 회원가입
   * - username, password를 받아 /signup API 호출
   * - 성공 시 단순히 완료만 알려주고, user 상태는 변경하지 않음
   */
  const signup = (userData) => {
    loading.value = true;
    error.value = null;

    // axios는 Promise를 반환하므로 then/catch/finally 체인 사용
    return api
      .post("/user/signup", {
        id: userData.id,
        pw: userData.password,
        email: userData.email,
        name: userData.name,
        birth: userData.birth,
        gender: userData.gender,
        height: userData.height,
        weight: userData.weight
      })
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
  const login = async ({ id, pw }) => {
    loading.value = true;
    error.value = null;

    const requestBody = {
      id: id,
      pw: pw,
    }

    try {
      const res = await api.post("/user/signin", requestBody, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      const accessToken = res.data.accessToken;
      localStorage.setItem("accessToken", accessToken);
      await fetchMe();

      return res;
    } catch (e) {
      error.value = e?.response?.data.message || "로그인 실패";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 로그아웃
   * - /logout 호출 후 user 상태를 null로 초기화
   */
  const logout = async () => {
    loading.value = true;

    try {
      localStorage.removeItem("accessToken");
      user.value = null;
      wbtiStore.userCondition = {
        experiences: [],
        quitReason: '',
        bodyConditions: [],
        availableTime: '',
        economy: '',
        purpose: '',
      };
      wbtiStore.resultScores = {
        socialType: 0,
        motivationType: 0,
        executionType: 0,
        activityType: 0,
      };
      
      error.value = null;
    }
    catch (e) {
      error.value = "로그아웃 중 오류가 발생했습니다.";
    }
    finally {
      loading.value = false;
    }
  };

  /**
   * 세션 기반 로그인 상태 동기화
   * - 새로고침(F5) 이후에도 로컬 저장소를 통해 Access Token 존재 여부 확인
   * - 로그인 상태라면 user를 세팅, 아니라면 null로 초기화
   */
  const fetchMe = () => {
    loading.value = true;
    error.value = null;

    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      user.value = null;
      initialized.value = true;
      loading.value = false;
      return Promise.resolve();
    }

    return api
      .get("/user/detail")
      .then((res) => {
        if (res.data && res.data.id) {
          user.value = res.data;
        } else {
          user.value = null;
        }
      })
      .catch((e) => {
        localStorage.removeItem("accessToken");
        user.value = null;
      })
      .finally(() => {
        loading.value = false;
        initialized.value = true;
      })
  };

  /**
   * 회원 정보 수정
   */
  const editProfile = async (password, formData) => {
    loading.value = true;
    error.value = null;

    const requestBody = {
      pw: password,
      email: formData.value.email,
      name: formData.value.name,
      birth: formData.value.birth,
      gender: formData.value.gender,
      height: formData.value.height,
      weight: formData.value.weight
    }
    try {
      const res = await api.put("/user/", requestBody, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      await fetchMe();

      return res;
    } catch (e) {
      error.value = e?.response?.data.message || "회원 정보 수정 실패";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 회원 탈퇴
   */
  const withdraw = async (password) => {
    loading.value = true;
    error.value = null;

    try {
      const res = await api.delete("/user/", {
        data: { pw: password },
        headers: {
          "Content-Type": "application/json",
        },
      });

      localStorage.removeItem("accessToken");
      user.value = null;

      return res;
    } catch (e) {
      error.value = e?.response?.data.message || "회원 탈퇴 실패";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 이메일 인증 코드 전송 
   */
  const sendVerificationCode = async (name, email) => {
    loading.value = true;
    error.value = null;

    try {
      const res = await api.post("/user/send-code", {
        name: name,
        email: email,
      },
      { withCredentials: true });
      return res;
    } catch (e) {
      error.value = e?.response?.data?.message || "인증 코드 발송 실패";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 이메일 인증 코드 검증
   */
  const verifyCode = async (email, code) => {
    loading.value = true;
    error.value = null;

    const requestBody = {
      email: email,
      code: code,
    }

    try {
      const res = await api.post("/user/verify-code", requestBody,
      { withCredentials: true });

      verifiedEmail.value = email;
      return res;
    } catch (e) {
      error.value = e?.response?.data?.message || "인증 번호가 일치하지 않습니다.";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  /**
  * 비밀번호 재설정
  */
  const resetPassword = async (newPassword, verifiedEmail) => {
    loading.value = true;
    error.value = null;

    const requestBody = {
      newPw: newPassword,
      email: verifiedEmail,
    }

    try {
      console.log(requestBody);
      const res = await api.put("/user/password", requestBody,
      { withCredentials: true });
      return res;
    } catch (e) {
      error.value = e?.response?.data?.message || "비밀번호 변경 실패";
      throw e;
    } finally {
      loading.value = false;
    }
  };

  // 스토어에서 외부로 노출할 상태/메서드
  return {
    user,
    initialized,
    loading,
    error,
    isAuthenticated,
    hasWbti,
    userName,
    userWbtiCode,
    userGender,
    verifiedEmail,
    currentScore,
    maxScoreByLevel,
    scorePercentage,
    level,
    signup,
    login,
    logout,
    fetchMe,
    editProfile,
    withdraw,
    sendVerificationCode,
    verifyCode,
    resetPassword,
  };
});
