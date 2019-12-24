import Vue from 'vue'
import Vuex from 'vuex'
import rest from './rest'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
	modules: {
		rest
	},
	getters
})

export default store
