
import { request } from '../rest/fetch';

export async function getverifycode() {
  const url = '/api/profile/verifycode';
  try {
    const json = await request(url);
    // console.log('______verifycode____' + JSON.stringify(json) + '______');
    return json;
  } catch (errorMsg) {
    // console.log('______verifycode__errormsg__' + JSON.stringify(errorMsg) + '______');
    throw errorMsg;
  }
}

export async function login(id, password, verifycode, uuid) {
  const url = '/api/profile/login';
  try {
    return await request(url, {}, { id, password, verifycode, uuid }, 'POST');
  } catch (errorMsg) {
    throw errorMsg;
  }
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
