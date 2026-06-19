import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'article', meta: { name: '旅游信息' }, component: () => import('@/views/manager/Article.vue'), },
        { path: 'routes', meta: { name: '路线信息' }, component: () => import('@/views/manager/Routes.vue'), },
        { path: 'travels', meta: { name: '游记信息' }, component: () => import('@/views/manager/Travels.vue'), },
        { path: 'user', meta: { name: '用户管理' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'comment', meta: { name: '评论管理' }, component: () => import('@/views/manager/Comment.vue'), },
        { path: 'collect', meta: { name: '收藏管理' }, component: () => import('@/views/manager/Collect.vue'), },
        { path: 'tourism', meta: { name: '商品信息' }, component: () => import('@/views/manager/Tourism.vue'), },
        { path: 'orders', meta: { name: '订单信息' }, component: () => import('@/views/manager/Orders.vue'), },
        { path: 'myFeedback', meta: { name: '反馈信息' }, component: () => import('@/views/manager/MyFeedback.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  },
        { path: 'password', component: () => import('@/views/front/Password.vue'),  },
        { path: 'addTravel', component: () => import('@/views/front/addTravel.vue'),  },
        { path: 'travel', component: () => import('@/views/front/Travel.vue'),  },
        { path: 'travelDetail', component: () => import('@/views/front/TravelDetail.vue'),  },
        { path: 'article', component: () => import('@/views/front/Article.vue'),  },
        { path: 'articleDetail', component: () => import('@/views/front/ArticleDetail.vue'),  },
        { path: 'collect', component: () => import('@/views/front/Collect.vue'),  },
        { path: 'routes', component: () => import('@/views/front/Routes.vue'),  },
        { path: 'routesDetail', component: () => import('@/views/front/RoutesDetail.vue'),  },
        { path: 'tourism', component: () => import('@/views/front/Tourism.vue'),  },
        { path: 'tourismDetail', component: () => import('@/views/front/TourismDetail.vue'),  },
        { path: 'orders', component: () => import('@/views/front/Orders.vue'),  },
        { path: 'search', component: () => import('@/views/front/Search.vue'),  },
        { path: 'feedback', component: () => import('@/views/front/Feedback.vue'),  },
        { path: 'myFeedback', component: () => import('@/views/front/MyFeedback.vue'),  },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/adminLogin', component: () => import('@/views/AdminLogin.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

const adminOnlyPaths = ['/manager']
const userOnlyPaths = [
  '/front/person',
  '/front/password',
  '/front/addTravel',
  '/front/collect',
  '/front/orders',
  '/front/feedback',
  '/front/myFeedback'
]

router.beforeEach((to) => {
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
  if (adminOnlyPaths.some(path => to.path.startsWith(path)) && user.role !== 'ADMIN') {
    return '/adminLogin'
  }
  if (userOnlyPaths.some(path => to.path.startsWith(path)) && user.role !== 'USER') {
    return '/login'
  }
  return true
})

export default router
