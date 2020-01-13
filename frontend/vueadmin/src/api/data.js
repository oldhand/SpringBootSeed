import { request } from '../rest/fetch';

export function initData(url, params) {
  return request({
    url: url,
    method: 'get',
    params
  })
}


export async function initData(url, params) {
  try {
    return  await request(url);
    return json;
  } catch (errorMsg) {
    console.log('______initData____url:' + url + '____' + JSON.stringify(errorMsg) + '______');
    throw errorMsg;
  }
}
