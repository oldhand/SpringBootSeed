
import { request } from '../rest/fetch';

export async function createVerify(name, username, contact, mobile, password) {
  const url = '/api/saass/createVerify';
  try {
    const json = await request(url, {}, { name, username, contact, mobile, password }, 'POST');
    console.log('______createVerify___' + JSON.stringify(json) + '______');
    return json;
  } catch (errorMsg) {
    console.log('______createVerify__errormsg__' + JSON.stringify(errorMsg) + '______');
    throw errorMsg;
  }
}
