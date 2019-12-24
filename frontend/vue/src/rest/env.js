/**
 * 配置编译环境和线上环境之间的切换
 *
 * baseUrl: 域名地址
 * imgBaseUrl: 图片所在域名地址
 *
 */

let baseUrl;
let appid;
let application;
let secret;
let publickey;

/**
 * 测试服务器配置
 */

baseUrl = 'http://127.0.0.1:8100';
appid = 'demo';
secret = '4c35458e913efbcf86ef621d22387b10';

publickey = "-----BEGIN PUBLIC KEY-----\n" +
    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI6dGvkKSHB6Q3TE6TKGFR4Nyt\n" +
    "6XH3gc7/LAzvW0aDNGZjkoA7jrMTBd/T0N/R4miBK7XNMI+4Z/gd8OgS0wShPwyq\n" +
    "Fwv8Q54goPr6dAXAQifzwK+eOs+Avu9rrVfT31i8wJeIzpk1aySoYB40ozasTdXg\n" +
    "Q2AHZH0AqU/Rne5GuQIDAQAB\n" +
    "-----END PUBLIC KEY-----";//默认公钥

export {
    baseUrl,
    appid,
    application,
    secret,
    publickey
}
