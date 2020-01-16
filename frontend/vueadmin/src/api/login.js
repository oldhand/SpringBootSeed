
import { request } from '../rest/fetch';

export async function getVerifyCode() {
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

export async function getInfo() {
  const url = '/api/profile/info';
  try {
    const json = await request(url);
    if (json.content.length > 0) {
      return json.content[0];
    }
    throw 'User not found';
  } catch (errorMsg) {
    throw errorMsg;
  }
}

export async function searchUser(username) {
  const url = '/api/profile?type=admin&username=' + username;
  try {
    const json = await request(url);
    if (json.content.length > 0) {
      return json.content[0];
    }
    throw 'User not found';
  } catch (errorMsg) {
    throw errorMsg;
  }
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
