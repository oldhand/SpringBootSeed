import axios from 'axios';
import store from '../store'
import credential from './credential';
import base64 from './base64'
import { getRestToken, RestDecrypt, RestEncrypt } from './sdk'

axios.defaults.retry = 3;
axios.defaults.retryDelay = 1000;
axios.defaults.timeout = 30000;

export const execute = async(url, access_token = '', headers = {}, data = {}, type = 'GET') => {
    type = type.toUpperCase();
    console.log('url : [' + type + ']' + process.env.baseUrl + url);
    const info = getRestToken(url);
    headers.token = info.token;

    headers.timestamp = info.timestamp;
    if (access_token !== '') {
        headers.accesstoken = access_token;
    }
    url = process.env.baseUrl + url;
    try {
        if (type === 'GET') {
            console.log('______GET____' + JSON.stringify(url) + '____' + JSON.stringify(headers) + '___');
            const result = await axios.get(url, { headers: headers });
            console.log('______GET____' + JSON.stringify(result) + '______');
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        return decryptbody;
                    }
                } else if (cipher && cipher !== '') {
                    return cipher;
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === 'POST') {
            let encryptdata
            if (access_token === '') {
                encryptdata = RestEncrypt(JSON.stringify(data), process.env.publickey);
            } else {
                const public_key = store.state.rest.access_token_info.public_key;
                encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            }
            headers['Content-Type'] = 'application/json; charset=UTF-8';
            const result = await axios.post(url, encryptdata, { headers: headers });
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        return decryptbody;
                    }
                } else if (cipher && cipher !== '') {
                    return cipher;
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === 'PUT') {
            const public_key = store.state.rest.access_token_info.public_key;
            const encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            headers['Content-Type'] = 'application/json; charset=UTF-8';
            const result = await axios.put(url, encryptdata, { headers: headers });
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        return decryptbody;
                    }
                } else if (cipher && cipher !== '') {
                    return cipher;
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === 'DELETE') {
            const result = await axios.delete(url, { headers: headers });
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        return decryptbody;
                    }
                } else if (cipher && cipher !== '') {
                    return cipher;
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else {
             throw 'Wrong request type';
        }
    } catch (errmsg) {
        console.log('______await_axios____' + JSON.stringify(errmsg) + '_________');
        throw errmsg;
    }
}

export const request = async(url, headers = {}, data = {}, type = 'GET') => {
    let access_token = await credential.get();
    try {
        var json = await execute(url, access_token, headers, data, type);
        return json;
    } catch (errormsg) {
        if (errormsg === 'AccessToken check failed' || errormsg === 'Rest data decryption failure') {
            try {
                access_token = await Credential.flush();
                json = await execute(url, access_token, headers, data, type);
                return json;
            } catch (errmsg) {
                throw errmsg;
            }
        } else {
            throw errormsg;
        }
    }
}
