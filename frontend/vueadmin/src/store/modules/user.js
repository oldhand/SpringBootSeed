import { login, getInfo, logout } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    user: {},
    roles: [],
    // 第一次加载菜单时用到
    loadMenus: false
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_LOAD_MENUS: (state, loadMenus) => {
      state.loadMenus = loadMenus
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const id = userInfo.id
      const password = userInfo.password
      const verifycode = userInfo.verifycode
      const uuid = userInfo.uuid
      const rememberMe = userInfo.rememberMe
      return new Promise((resolve, reject) => {
        login(id, password, verifycode, uuid).then(res => {
          setToken(res.id, rememberMe)
          commit('SET_TOKEN', res.id)
          setUserInfo(res, commit)
          // 第一次加载菜单时用到， 具体见 src 目录下的 permission.js
          commit('SET_LOAD_MENUS', true)
          resolve(res)
        }).catch(error => {
          console.log('______Login__error__' + JSON.stringify(error) + '______');
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        console.log('_____getInfo________');
        getInfo().then(res => {
          setUserInfo(res, commit)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit }) {
      console.log('_____LogOut________');
      return new Promise((resolve, reject) => {
        logout().then(res => {
          logOut(commit)
          resolve()
        }).catch(error => {
          logOut(commit)
          reject(error)
        })
      })
    },

    updateLoadMenus({ commit }) {
      console.log('____updateLoadMenus________');
      return new Promise((resolve, reject) => {
        commit('SET_LOAD_MENUS', false)
      })
    }
  }
}

export const logOut = (commit) => {
  commit('SET_TOKEN', '')
  commit('SET_ROLES', [])
  removeToken()
}

export const setUserInfo = (res, commit) => {
  // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
  // if (res.roles.length === 0) {
  //   commit('SET_ROLES', ['ROLE_SYSTEM_DEFAULT'])
  // } else {
  //   commit('SET_ROLES', res.roles)
  // }
  commit('SET_ROLES', ['ROLE_SYSTEM_DEFAULT'])
  commit('SET_USER', res)
}

export default user
