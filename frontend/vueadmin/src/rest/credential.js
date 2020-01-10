import store from '../store'
import { execute } from './fetch'
import { getRestCredential, gettimestamp } from './sdk'

// Credential类 获得访问票据
var Credential = {
		gettokeninfo: () => {
			return store.state.rest.access_token_info;
		},
		settokeninfo: (info) => {
			store.commit('settoken', info);
			store.dispatch('settoken', info);
		},
        flush: async() => {
			try {
				Credential.settokeninfo({ 'access_token': '', 'public_key': '', 'access_timestamp': 0 });
				return await Credential.get();
			} catch (e) {
				return '';
			}
        },
		get: async() => {
        // Credential.settokeninfo({ 'access_token': '', 'public_key': '', 'access_timestamp': 0 });
				var timestamp = '';
				var credential_info;
				var access_token;
        const access_token_info = Credential.gettokeninfo();
        console.log('______getRestCredential_____' + JSON.stringify(access_token_info) + '______');
				access_token = access_token_info.access_token;
                var access_timestamp = access_token_info.access_timestamp;
                if (access_token !== '' && access_timestamp !== '') {
                        var timezone = new Date().getTimezoneOffset();
                        timestamp = Date.parse(new Date());
                        timestamp = timestamp / 1000 + timezone * 60;
                        if (timestamp - Number(access_timestamp) < 3000) {
                            return access_token;
                        }
				}
                credential_info = getRestCredential();
                // console.log("______getRestCredential_____" + JSON.stringify(credential_info) + "______");
                var url = '/auth/credential';
                var headers = {};
				try {
					var json = await execute(url, '', headers, credential_info, 'POST');
					if (json.token && json.token !== '' && json.publickey && json.publickey !== '') {
						access_token = json.token;
            const public_key = json.publickey;
						console.log('_____get_access_token____' + access_token + '______');
						Credential.settokeninfo({ 'access_token': access_token, 'public_key': public_key, 'access_timestamp': gettimestamp() });
						return access_token;
					} else {
						console.error('error: Credential.get failure');
						throw 'Credential.get failure';
					}
				} catch (errormsg) {
					throw errormsg;
				}
		}
}
export default Credential