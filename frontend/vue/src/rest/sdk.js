import md5 from './md5'
import store from '../store'
import JSEncrypt from 'jsencrypt/bin/jsencrypt'
import CryptoJS from 'crypto-js'
import {appid, secret, publickey} from './env'

export const gettimestamp = () => {
  var timezone = new Date().getTimezoneOffset();
  var timestamp = Date.parse(new Date());
  return timestamp / 1000 + timezone * 60;
}

export const getRestCredential = () => {
  var timestamp = gettimestamp();
  var str = appid + timestamp + secret;
  return {appid: appid, timestamp: timestamp, secret: md5.hex_md5(str)};
}

export const getRestToken = (url) => {
  const timestamp = gettimestamp();
  const token = url + timestamp;
  if (url.substr(0, 16) === "/auth/credential") {
    const encryptor = new JSEncrypt();
    encryptor.setPublicKey(publickey);
    const secret = encryptor.encrypt(md5.hex_md5(token));
    return {timestamp: timestamp, token: secret};
  } else {
      const public_key = store.state.rest.access_token_info.public_key;
      const encryptor = new JSEncrypt();
      encryptor.setPublicKey(public_key);
      const secret = encryptor.encrypt(md5.hex_md5(token));
      return {timestamp: timestamp, token: secret};
  }
}

// 加密
function encrypt_3des(plaintext, key) {
  try {
    //console.log("______encrypt_3des____" + plaintext + "_____" + key + "______");
    const DesKey = CryptoJS.enc.Utf8.parse(key);
    const decrypt = CryptoJS.TripleDES.encrypt(plaintext, DesKey, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
    //解析数据后转为Base64
    return decrypt.toString();
  } catch (errmsg) {
    console.log("______encrypt_3des____" + JSON.stringify(errmsg) + "_________");
    throw errmsg;
  }
}
// 解密
function decrypt_3des(ciphertext, key) {
  try {
    //console.log("______encrypt_3des____" + ciphertext + "_____" + key + "______");
    const DesKey = CryptoJS.enc.Utf8.parse(key);
    const decrypt = CryptoJS.TripleDES.decrypt(ciphertext, DesKey, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding });
    //解析数据后转为UTF-8
    //console.log("______decrypt_3des____" + decrypt.toString(CryptoJS.enc.Utf8) + "_________");
    return JSON.parse(decrypt.toString(CryptoJS.enc.Utf8));
  } catch (errmsg) {
    console.log("______decrypt_3des____" + JSON.stringify(errmsg) + "_________");
    throw errmsg;
  }
}

export const RestDecrypt = (cipher, public_key) => {
  var key = "";
  if (public_key === "") {
    key = md5.hex_md5(publickey);
  } else {
    key = md5.hex_md5(public_key);
  }
  return decrypt_3des(cipher, key);
}

export const RestEncrypt = (cipher, public_key) => {
  var key = "";
  if (public_key === "") {
    key = md5.hex_md5(publickey);
  } else {
    key = md5.hex_md5(public_key);
  }
  return encrypt_3des(cipher, key);
}
