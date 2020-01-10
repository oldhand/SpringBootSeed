
const rest = {
  state: {
    access_token_info: {}
  },
  mutations: {
    settoken(state, info) {
      state.access_token_info = info;
    }
  },
  actions: {
    settoken({ commit }, info) {
      commit('settoken', info);
    }
  }
}
export default rest
