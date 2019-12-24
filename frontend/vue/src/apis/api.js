
import {supplierid} from '@/rest/env'
import credential from '@/rest/credential';
import base64 from '@/rest/base64'
import {request} from "../rest/fetch";

export const getverifycode = async() => {
  let url = "/auth/verifycode";
  var headers = {};
  let access_token = await credential.get();
  try {
    var json = await request(url, access_token, headers);
    console.log("______verifycode____" + JSON.stringify(json) + "______");
     return json;
  } catch (errormsg) {
    if (errormsg === "AccessToken check failed" || errormsg === "Rest data decryption failure") {
      try {
        access_token = await Credential.flush();
        json = await request(url, access_token, headers);
        console.log("______verifycode____" + JSON.stringify(json) + "______");
        return json;
      } catch (errmsg) {
        throw errmsg;
      }
    } else {
      throw errormsg;
    }
  }

};
