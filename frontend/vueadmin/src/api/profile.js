import { request } from '../rest/fetch';

export async function getProfile(profileid) {
  const url = '/api/profile?id=' + profileid;
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

export async function modifypassword(profileid, password, uuid, verifycode) {
  const url = '/api/profile/modifypassword';
  try {
    return await request(url, {}, { profileid, password, uuid, verifycode }, 'PUT');
  } catch (errorMsg) {
    throw errorMsg;
  }
}

