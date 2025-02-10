import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import ResumeView from '@/views/ResumeView.vue'
import ContactView from '@/views/ContactView.vue'
import ProjectsView from '@/views/ProjectsView.vue'
import NotFoundView from '@/views/NotFoundView.vue'
import CryptoTrackerView from '@/views/CryptoTrackerView.vue'
import ResumeGeneratorView from '@/views/ResumeGeneratorView.vue'
import CryptoHistoryView from '@/views/CryptoHistoryView.vue'
import LoginView from '@/views/LoginView.vue'
import RegistrationView from '@/views/RegistrationView.vue'
import DashBoardView from '@/views/DashBoardView.vue'

const publicPages = [
  "/crypto-tracker/login", "/crypto-tracker/register", 
  "/resume", "/", "/portfolio", "/contact", 
  "/crypto-tracker", "/crypto-tracker/:id/history", 
  "/resume-generator", "/404"
];
const loginPages = ["/crypto-tracker/login", "/crypto-tracker/register"]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/resume',
      name: 'resume',
      component: ResumeView
    },
    {
      path: '/portfolio',
      name: 'portfolio',
      component: ProjectsView
    },
    {
      path: '/contact',
      name: 'contact',
      component: ContactView
    },
    {
      path: '/crypto-tracker',
      name: 'crypto-tracker',
      component: CryptoTrackerView
    },
    {
      path: '/crypto-tracker/:id/history',
      name: 'crypto-history',
      component: CryptoHistoryView,
      props: true
    },
    {
      path: '/resume-generator',
      name: 'resume-generator',
      component: ResumeGeneratorView
    },
    {
      path: "/crypto-tracker/login",
      name: "login",
      component: LoginView
    },
    {
      path: "/crypto-tracker/register",
      name: "Registration",
      component: RegistrationView
    },
    {
      path: "/crypto-tracker/dashboard",
      name: "DashBoard",
      component: DashBoardView
    },
    {
      path: '/404',
      name: 'not-found',
      component: NotFoundView
    },
    {
      path: '/:catchAll(.*)',
      redirect: '/404'
    }
  ],
})


router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem("accessToken");

  // Check if the route exists in the defined routes
  const routeExists = router.getRoutes().some(route => route.path === to.path || route.path.includes(":catchAll"));

  if (!routeExists) {
    next("/404"); // Redirect to NotFoundView
  } else if (isAuthenticated && loginPages.includes(to.path)) {
    next("/crypto-tracker/dashboard"); // Redirect logged-in users away from login/register
  } 
  // ✅ Allow public access to crypto detail pages
  else if (
    !isAuthenticated &&
    !(publicPages.includes(to.path) || (to.path.startsWith("/crypto-tracker/") && to.path.includes("/history")))
  ) {
    next("/crypto-tracker/login"); // Redirect to login if trying to access a protected page
  } else {
    next(); // Allow navigation
  }
});


export default router
