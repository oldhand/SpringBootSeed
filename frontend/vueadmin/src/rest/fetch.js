import axios from 'axios';
import credential from './credential';
import base64 from './base64'
import { getAccessToken } from './token'
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
            const result = await axios.get(url, { headers: headers }).catch(function(errorMsg) {
              if (errorMsg.response) {
                if (errorMsg.response.status === 400) {
                  throw errorMsg.response.data.message;
                }
              }
              throw errorMsg;
            });
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = getAccessToken().public_key;
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
             console.log('______POST___body____' + JSON.stringify(data) + '______');
            if (access_token === '') {
                encryptdata = RestEncrypt(JSON.stringify(data), process.env.publickey);
            } else {
                const public_key = getAccessToken().public_key;
                encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            }
            headers['Content-Type'] = 'application/json; charset=UTF-8';
            const result = await axios.post(url, encryptdata, { headers: headers }).catch(function(errorMsg) {
              if (errorMsg.response) {
                if (errorMsg.response.status === 400) {
                  throw errorMsg.response.data.message;
                }
              }
              throw errorMsg;
            });
            if (result.status === 200 || result.status === 201) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = getAccessToken().public_key;
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
            const public_key = getAccessToken().public_key;
            const encryptdata = RestEncrypt(JSON.stringify(data), public_key);
            headers['Content-Type'] = 'application/json; charset=UTF-8';
            const result = await axios.put(url, encryptdata, { headers: headers }).catch(function(errorMsg) {
              if (errorMsg.response) {
                if (errorMsg.response.status === 400) {
                  throw errorMsg.response.data.message;
                }
              }
              throw errorMsg;
            });
            if (result.status === 200 || result.status === 201) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = getAccessToken().public_key;
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
            const result = await axios.delete(url, { headers: headers }).catch(function(errorMsg) {
              if (errorMsg.response) {
                if (errorMsg.response.status === 400) {
                  throw errorMsg.response.data.message;
                }
              }
              throw errorMsg;
            });
            if (result.status === 200) {
                const cipher = result.data;
                if (cipher && cipher !== '' && base64.isbase64(cipher)) {
                    if (access_token === '') {
                        const decryptbody = RestDecrypt(cipher, '')
                        return decryptbody;
                    } else {
                        const public_key = getAccessToken().public_key;
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
    } catch (errMsg) {
        if (errMsg.message) {
          if (errMsg.message === 'Network Error') {
            throw 'Unable to connect to server';
          } else {
            throw errMsg.message;
          }
        } else {
          throw errMsg;
        }
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
