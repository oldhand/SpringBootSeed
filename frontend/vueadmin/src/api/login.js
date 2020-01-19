
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
    return await request(url);
  } catch (errorMsg) {
    throw errorMsg;
  }
}

function isMobileAvailable(input) {
  const myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
  if (myreg.test(input)) {
    return true;
  }
  return false;
}

function getRegMobile(input) {
  const myreg = /^[+][8][6][1][3,4,5,7,8][0-9]{9}$/;
  if (myreg.test(input)) {
    return input.substring(3, input.length);
  } else {
    const nextreg = /^[8][6][1][3,4,5,7,8][0-9]{9}$/;
    if (nextreg.test(input)) {
      return input.substring(2, input.length);
    }
  }
  return input;
}

export async function searchUser(username) {
  let url;
  if (isMobileAvailable(username)) {
    url = '/api/profile?type=admin&regioncode=86&mobile=' + username;
  } else {
    const mobile = getRegMobile(username);
    if (mobile !== username) {
      url = '/api/profile?type=admin&regioncode=86&mobile=' + mobile;
    } else {
      url = '/api/profile?type=admin&username=' + username;
    }
  }
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

export async function logout() {
  const url = '/api/profile/logout';
  try {
    return await request(url, {}, {}, 'POST');
  } catch (errorMsg) {
    throw errorMsg;
  }
}
