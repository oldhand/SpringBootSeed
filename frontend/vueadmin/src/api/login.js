
import { request } from '../rest/fetch';

export async function getverifycode() {
  const url = '/api/profile/verifycode';
  const headers = {};
  try {
    const json = await request(url, headers);
    console.log('______verifycode____' + JSON.stringify(json) + '______');
    return json;
  } catch (errorMsg) {
    console.log('______verifycode__errormsg__' + JSON.stringify(errorMsg) + '______');
  }
}

export function login(username, password, code, uuid) {
  return request({
    url: 'auth/login',
    method: 'post',
    data: {
      username,
      password,
      code,
      uuid
    }
  })
}

export function getInfo() {
  return request({
    url: 'auth/info',
    method: 'get'
  })
}

export function getCodeImg() {
  return request({
    url: 'auth/code',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'delete'
  })
}
