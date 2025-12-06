import StartView from "@/view/StartView.vue";
import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({

  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView
    }
  ]
})

export default router;