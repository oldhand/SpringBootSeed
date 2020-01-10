'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://localhost:8181"',
  baseUrl: '"http://localhost:8181"',
  appid: '"demo"',
  secret: '"4c35458e913efbcf86ef621d22387b10"',
  publickey: '"-----BEGIN PUBLIC KEY-----\\n" + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI6dGvkKSHB6Q3TE6TKGFR4Nyt\\n" + "6XH3gc7/LAzvW0aDNGZjkoA7jrMTBd/T0N/R4miBK7XNMI+4Z/gd8OgS0wShPwyq\\n" + "Fwv8Q54goPr6dAXAQifzwK+eOs+Avu9rrVfT31i8wJeIzpk1aySoYB40ozasTdXg\\n" + "Q2AHZH0AqU/Rne5GuQIDAQAB\\n" + "-----END PUBLIC KEY-----"' //默认公钥
});
