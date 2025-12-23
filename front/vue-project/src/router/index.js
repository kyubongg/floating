// src/router/index.js
import { createRouter, createWebHistory } from "vue-router";

// 페이지 컴포넌트
import StartView from "../views/StartView.vue";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import SignupView from "../views/SignupView.vue";
import MyPageView from "../views/MyPageView.vue";
import EditProfileView from "../views/EditProfileView.vue";

// 인증 상태 관리 (Pinia)
import { useAuthStore } from "../stores/auth";
import WbtiMainView from "@/views/wbti/WbtiMainView2.vue";
import WbtiTestView from "@/views/wbti/WbtiTestView.vue";
import WbtiResultView from "@/views/wbti/WbtiResultView.vue";
import CalendarView from "@/views/CalendarView.vue";
import FindInfoView from "@/views/FindInfoView.vue";
import ResetPasswordView from "@/views/ResetPasswordView.vue";

// 라우터 인스턴스 생성
const router = createRouter({
  history: createWebHistory(),
  /**
   * SPA 라우트 정의
   * - 메인(Home)은 로그인 필요
   * - 로그인/회원가입 페이지는 누구나 접근 가능
   */
  routes: [
    {
      path: "/",
      name: "start",
      component: StartView,
    },
    {
      path: "/home",
      name: "home",
      component: HomeView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/signup",
      name: "signup",
      component: SignupView,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: MyPageView,
    },
    {
      path: "/edit-Profile",
      name: "editProfile",
      component: EditProfileView,
    },
    {
      path: "/wbti-test",
      name: "wbtiTest",
      component: WbtiTestView,
      meta: { requiresAuth: true, disallowHasWbti: true }, // 🔐 로그인 필요
    },
    {
      path: "/wbti-result",
      name: "wbtiResult",
      component: WbtiResultView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    },
    {
      path: "/calendar",
      name: "calendar",
      component: CalendarView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    },
    {
      path: "/findInfo",
      name: "findInfo",
      component: FindInfoView,
    },
    {
      path: "/resetPassword",
      name: "resetPassword",
      component: ResetPasswordView,
    },
  ],
});

/**
 * 전역 네비게이션 가드(beforeEach)
 *
 * 1. SPA가 처음 로딩되었을 때(auth.initialized = false),
 *    서버 세션을 확인하기 위해 /api/me 호출(fetchMe)
 *
 * 2. 로그인 필요한 페이지(requiresAuth)인데
 *    현재 로그인되어 있지 않다면 → /login 으로 리다이렉트
 *
 * 3. 이미 로그인한 상태에서 /login 또는 /signup 접근 시 → 메인(/)으로 이동
 *
 * 4. 그 외에는 정상 진행(next())
 */
router.beforeEach((to, from, next) => {
  const auth = useAuthStore();

  // 아직 로그인 여부 동기화를 하지 않았다면 fetchMe 실행
  if (!auth.initialized) {
    return auth.fetchMe().then(() => {
      proceedRouting(to, next, auth);
    });
  }

  // 이미 initialized 되었다면 바로 라우팅 진행
  proceedRouting(to, next, auth);
});

// 라우팅 처리 로직을 함수로 분리 (가독성 ↑)
// src/router/index.js 수정
function proceedRouting(to, next, auth) {
  const isAuthorized = auth.isAuthenticated;
  const hasWbti = auth.hasWbti === true; // 확실하게 true인지 체크

  // 1) 로그인하지 않은 유저가 보호된 페이지 접근 시
  if (to.meta.requiresAuth && !isAuthorized) {
    return next({ name: "login", query: { redirect: to.fullPath } });
  }

  // 2) 로그인한 유저가 다시 로그인/회원가입 페이지 접근 시 (이때만 홈으로)
  if (isAuthorized && (to.name === "login" || to.name === "signup")) {
    return next({ name: "home" });
  }

  // 3) WBTI 이미 한 유저가 테스트 페이지 접근 시
  if (to.meta.disallowHasWbti && hasWbti) {
    alert("이미 검사를 완료하셨습니다. 결과 페이지로 이동합니다.");
    return next({ name: 'wbtiResult' });
  }

  // 4) 이메일 검증을 거치지 않은 유저가 비밀번호 재설정 페이지 접근 시
  if (to.name === 'resetPassword') {
    if (!auth.verifiedEmail) {
      alert('이메일 인증이 필요합니다.');
      return next({ name: "findInfo" });
    }
  }

  // 5) 그 외엔 가려던 곳으로 보냄
  return next();
}

export default router;
