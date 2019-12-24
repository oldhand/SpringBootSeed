
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

export const generator = async() => {
  let url = "/api/generator/config";
  var headers = {};
  try {
    var json = await request(url, headers);
    console.log("____query__generator____" + JSON.stringify(json) + "______");
    return json;
  } catch (errormsg) {
    console.log("________query__generator______errormsg__" + JSON.stringify(errormsg) + "______");
  }
};
