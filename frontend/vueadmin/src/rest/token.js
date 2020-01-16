import Cookies from 'js-cookie'
import CryptoJS from 'crypto-js'
import md5 from './md5'

const AccessTokenKey = 'accesstoken';

export function getAccessToken() {
  const ciphertext = Cookies.get(AccessTokenKey);
  if (ciphertext !== undefined && ciphertext !== '') {
     return decrypt_3des(ciphertext);
  }
  return { 'access_token': '', 'public_key': '', 'access_timestamp': 0 };
}

export function setAccessToken(token) {
  const plaintext = JSON.stringify(token);
  const ciphertext = encrypt_3des(plaintext);
  return Cookies.set(AccessTokenKey, ciphertext);
}

export function removeAccessToken() {
  return Cookies.remove(AccessTokenKey)
}

// 加密
function encrypt_3des(plaintext) {
  try {
    const key = md5.hex_md5('springbootseed');
    const DesKey = CryptoJS.enc.Utf8.parse(key);
    const decrypt = CryptoJS.TripleDES.encrypt(plaintext, DesKey, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
    // 解析数据后转为Base64
    return decrypt.toString();
  } catch (errmsg) {
    throw errmsg;
  }
}
// 解密
function decrypt_3des(ciphertext) {
  try {
    const key = md5.hex_md5('springbootseed');
    const DesKey = CryptoJS.enc.Utf8.parse(key);
    const decrypt = CryptoJS.TripleDES.decrypt(ciphertext, DesKey, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
    // 解析数据后转为UTF-8
    return JSON.parse(decrypt.toString(CryptoJS.enc.Utf8));
  } catch (errmsg) {
    throw errmsg;
  }
}
