
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
    var putjson = await request(url, headers, json, "PUT");
    console.log("____put__generator____" + JSON.stringify(putjson) + "______");
    return json;
  } catch (errormsg) {
    console.log("________query__generator______errormsg__" + JSON.stringify(errormsg) + "______");
  }
};

export const jobs = async() => {
  let url = "/api/jobs?endTime=2020-01-01 00:00:00&startTime=2010-01-01 00:00:00";
  var headers = {};
  try {
    var json = await request(url, headers);
    console.log("____query__jobs____" + JSON.stringify(json) + "______");
    return json;
  } catch (errormsg) {
    console.log("________query__jobs______errormsg__" + JSON.stringify(errormsg) + "______");
  }
};
