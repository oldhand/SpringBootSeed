import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '../layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/

export const constantRouterMap = [
  {
    path: '/login',
    meta: { title: 'Login', noCache: true },
    component: () => import('@/views/profile/login'),
    hidden: true
  },
	{
    path: '/register',
    meta: { title: 'Register', noCache: true },
    component: () => import('@/views/profile/register'),
    hidden: true
	},
  {
    path: '/forgetPassword',
    meta: { title: 'ForgetPassword', noCache: true },
    component: () => import('@/views/profile/forgetPassword'),
    hidden: true
  },
  {
    path: '/forgetPasswordSmsVerification',
    meta: { title: 'SmsVerification', noCache: true },
    component: () => import('@/views/profile/forgetPasswordSmsVerification'),
    hidden: true
  },
  {
    path: '/forgetPasswordSetNewPassword',
    meta: { title: 'SetNewPassword', noCache: true },
    component: () => import('@/views/profile/forgetPasswordSetNewPassword'),
    hidden: true
  },
  {
    path: '/forgetPasswordResetCompleted',
    meta: { title: 'SetNewPassword', noCache: true, allowBack: true },
    component: () => import('@/views/profile/forgetPasswordResetCompleted'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/features/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/features/401'),
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/features/redirect')
      }
    ]
  },
  {
    path: '/',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/home/index'),
        name: 'home',
        meta: { title: 'Home', icon: 'index', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'center',
        component: () => import('@/views/settings/users/center'),
        name: 'usercenter',
        meta: { title: 'UserCenter', icon: 'user' }
      }
    ]
  }
 // { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
