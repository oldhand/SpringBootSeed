
import { request } from '../rest/fetch';

export async function search(regioncode, mobile) {
  const url = '/api/smslog/search';
  try {
    return await request(url, {}, { regioncode, mobile }, 'POST');
  } catch (errorMsg) {
    throw errorMsg;
  }
}

export async function send(regioncode, mobile, template) {
  const url = '/api/smslog/send';
  try {
    return await request(url, {}, { regioncode, mobile, template }, 'POST');
  } catch (errorMsg) {
    throw errorMsg;
  }
}

export async function verify(uuid, verifycode, parameter) {
  const url = '/api/smslog/verify';
  try {
    return await request(url, {}, { uuid, verifycode, parameter }, 'POST');
  } catch (errorMsg) {
    throw errorMsg;
  }
}

export async function getToken(token) {
  const url = '/api/smslog/' + token;
  try {
    return await request(url);
  } catch (errorMsg) {
    throw errorMsg;
  }
}

