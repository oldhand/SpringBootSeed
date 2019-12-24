
import {supplierid} from '@/rest/env'
import {request} from "../rest/fetch";

export const getverifycode = async() => {
  let url = "/auth/verifycode";
  var headers = {};
  try {
    var json = await request(url, headers);
    console.log("______verifycode____" + JSON.stringify(json) + "______");
    return json;
  } catch (errormsg) {
    console.log("______verifycode__errormsg__" + JSON.stringify(errormsg) + "______");
  }
};
