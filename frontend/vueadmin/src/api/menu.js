import { request } from '../rest/fetch';

export async function buildMenus() {
  const url = '/api/tabs/buildmemus';
  try {
    const json = await request(url);
    console.log('______buildmemus____' + JSON.stringify(json) + '______');
    return json;
  } catch (errorMsg) {
    // console.log('______buildmemus__errormsg__' + JSON.stringify(errorMsg) + '______');
    throw errorMsg;
  }
}

// 获取所有的菜单树
export function getMenusTree() {
  return request({
    url: 'api/menus/tree',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/menus',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/menus/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/menus',
    method: 'put',
    data
  })
}

export function downloadMenu(params) {
  return request({
    url: 'api/menus/download',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
