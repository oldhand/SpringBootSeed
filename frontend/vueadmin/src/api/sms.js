
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

