import { request } from '../rest/fetch';

export async function initData(url, params) {
  try {
    return await request(url);
  } catch (errorMsg) {
    console.log('______initData____url:' + url + '____' + JSON.stringify(errorMsg) + '______');
    throw errorMsg;
  }
}
