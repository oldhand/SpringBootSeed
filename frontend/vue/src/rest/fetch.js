import axios from 'axios';
import store from '../store'
import {baseUrl} from './env'
import base64 from './base64'
import {getRestToken, RestDecrypt, RestEncrypt} from './sdk'

axios.defaults.retry = 3;
axios.defaults.retryDelay = 1000;
axios.defaults.timeout = 30000;

export const request = async(url, access_token = '', headers = {}, data = {}, type = 'GET') => {

    type = type.toUpperCase();
    console.log("url : " + baseUrl + url);
    const info = getRestToken(url);
    headers.token = info.token;
    headers.timestamp = info.timestamp;
    if (access_token !== '') {
        headers.accesstoken = access_token;
    }
    url = baseUrl + url;
    try {
        if (type === "GET") {
            const result = await axios.get(url, {headers: headers});
            //console.log("______result____" + JSON.stringify(result) + "_________");
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '') {
                     //console.log('access_token: ' + access_token);
                     //console.log('cipher: ' + cipher);
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    }
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === "POST") {
            const public_key = store.state.rest.access_token_info.public_key;
            const encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            headers["Content-Type"] = "application/json; charset=UTF-8";
            const result = await axios.post(url, encryptdata, {headers: headers});
            //console.log("______result____" + JSON.stringify(result) + "_________");
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '') {
                    //console.log('access_token: ' + access_token);
                    //console.log('cipher: ' + cipher);
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    }
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === "PUT") {
            const public_key = store.state.rest.access_token_info.public_key;
            const encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            headers["Content-Type"] = "application/json; charset=UTF-8";
            const result = await axios.put(url, encryptdata, {headers: headers});
            // console.log("______result____" + JSON.stringify(result) + "_________");
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '') {
                    // console.log('access_token: ' + access_token);
                    // console.log('cipher: ' + cipher);
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    }
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else if (type === "DELETE") {
            const result = await axios.delete(url, {headers: headers});
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '') {
                    // console.log('access_token: ' + access_token);
                    // console.log('cipher: ' + cipher);
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        //console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    } else {
                        const public_key = store.state.rest.access_token_info.public_key;
                        const decryptbody = RestDecrypt(cipher, public_key)
                        console.log('resData: ' + JSON.stringify(decryptbody));
                        return decryptbody;
                    }
                } else {
                    throw 'reponse body is empty';
                }
            }
        } else {
            throw 'Wrong request type';
        }
    } catch (errmsg) {
        console.log("______await_axios____" + JSON.stringify(errmsg) + "_________");
        throw errmsg;
    }
}
