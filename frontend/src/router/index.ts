import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import ResumeView from '@/views/ResumeView.vue'
import ContactView from '@/views/ContactView.vue'
import ProjectsView from '@/views/ProjectsView.vue'
import NotFoundView from '@/views/NotFoundView.vue'
import CryptoTrackerView from '@/views/CryptoTrackerView.vue'
import ResumeGeneratorView from '@/views/ResumeGeneratorView.vue'
import CryptoHistoryView from '@/views/CryptoHistoryView.vue'

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
      path: '/:catchAll(.*)',
      name: 'not-found',
      component: NotFoundView
    }
  ],
})

export default router
