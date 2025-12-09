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
import WbtiMainView from "@/views/wbti/WbtiMainView.vue";
import WbtiTestView from "@/views/wbti/WbtiTestView.vue";
import WbtiResultView from "@/views/wbti/WbtiResultView.vue";

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
      path: "/wbti-main",
      name: "wbtiMain",
      component: WbtiMainView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    },
    {
      path: "/wbti-test",
      name: "wbtiTest",
      component: WbtiTestView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    },
    {
      path: "/wbti-result",
      name: "wbtiResult",
      component: WbtiResultView,
      meta: { requiresAuth: true }, // 🔐 로그인 필요
    }
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
function proceedRouting(to, next, auth) {
  // 1) 로그인 필요한 페이지인데 로그인 안 되어 있음 → /login
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return next({ name: "login", query: { redirect: to.fullPath } });
  }

  // 2) 로그인한 사용자가 다시 로그인/회원가입으로 가려는 경우 → 홈으로
  if ((to.name === "login" || to.name === "signup") && auth.isAuthenticated) {
    return next({ name: "home" });
  }

  // 3) 그 외 정상 라우팅
  return next();
}

export default router;
