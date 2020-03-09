<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-position="left" label-width="0px" class="login-form">
      <div class="select-lang">
        <select-lang />
      </div>
      <h3 class="title">SpringBootSeed <br>{{ $t('login.title') }}</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" :placeholder="$t('login.username')" type="text" auto-complete="off">
          <svg-icon slot="prefix" icon-class="users" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" :placeholder="$t('login.password')" type="password" auto-complete="off" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item v-if="needverifycode" prop="code" >
        <el-input v-model="loginForm.code" :placeholder="$t('login.verifycode')" auto-complete="off" maxlength="4" style="width: 63%" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode">
        </div>
      </el-form-item>

      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">{{ $t('login.rememberMe') }}</el-checkbox>

      <el-row style="margin:0px 0px 25px 0px;" type="flex" justify="space-between">
        <el-link type="primary" @click="doforgetPassword()">{{ $t('login.forgetPassword') }}</el-link>
        <el-link type="primary" @click="doRegister()">{{ $t('login.register') }}</el-link>
      </el-row>

      <div v-if="errorMsg != ''" class="error-msg">
        {{ errorMsg }}
      </div>

      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
          <span v-if="!loading">{{ $t('login.login') }}</span>
          <span v-else>{{ $t('login.logining') }}...</span>
        </el-button>
      </el-form-item>

    </el-form>
    <!--  底部  -->
    <div v-if="$store.state.settings.showFooter" id="el-login-footer">
      <span v-html="$store.state.settings.footerTxt"/>
      <span> ⋅ </span>
      <a href="http://www.beian.miit.gov.cn" target="_blank">{{ $store.state.settings.caseNumber }}</a>
    </div>
  </div>
</template>

<script>
import Config from '@/config'
import selectLang from '@/components/lang/select-lang';
import { getVerifyCode, searchUser } from '@/api/login'
import Cookies from 'js-cookie'
export default {
  name: 'Login',
  components: {
    selectLang
  },
  data() {
    return {
      codeUrl: '',
      cookiePass: '',
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: this.$t('login.usernameisrequired') }],
        password: [{ required: true, trigger: 'blur', message: this.$t('login.passwordisrequired') }],
        code: [{ required: true, trigger: 'change', message: this.$t('login.verifycodeisrequired') }]
      },
      loading: false,
      needverifycode: false,
      redirect: undefined,
      errorMsg: ''
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    document.title = this.$t('pages.login') + ' - ' + Config.webName;
    this.getCookie()
  },
  methods: {
    getCode() {
      getVerifyCode().then(res => {
        this.codeUrl = res.img
        this.loginForm.uuid = res.uuid
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    getCookie() {
      const username = Cookies.get('username')
      let password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.needverifycode = Cookies.get('needverifycode')
      if (this.needverifycode) {
        this.getCode()
      }
      // 保存cookie里面的加密后的密码
      this.cookiePass = password === undefined ? '' : password
      password = password === undefined ? this.loginForm.password : password
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: ''
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        let user
        if (this.needverifycode) {
          user = {
            id: this.loginForm.username,
            password: this.loginForm.password,
            rememberMe: this.loginForm.rememberMe,
            verifycode: this.loginForm.code,
            uuid: this.loginForm.uuid
          }
        } else {
          user = {
            id: this.loginForm.username,
            password: this.loginForm.password,
            rememberMe: this.loginForm.rememberMe,
            verifycode: '',
            uuid: ''
          }
        }
        if (valid) {
          this.loading = true
          searchUser(user.id).then(res => {
            user.id = res.id;
            if (user.rememberMe) {
              Cookies.set('username', this.loginForm.username, { expires: Config.passCookieExpires })
              Cookies.set('password', user.password, { expires: Config.passCookieExpires })
              Cookies.set('rememberMe', user.rememberMe, { expires: Config.passCookieExpires })
            } else {
              Cookies.remove('username')
              Cookies.remove('password')
              Cookies.remove('rememberMe')
            }
            this.$store.dispatch('Login', user).then((res) => {
              this.loading = false
              Cookies.remove('needverifycode')
              this.$router.push({ path: this.redirect || '/' })
            }).catch((errorMsg) => {
              this.errorMsg = errorMsg;
              this.loading = false
              Cookies.set('needverifycode', true, { expires: Config.passCookieExpires })
              this.needverifycode = true;
              this.getCode()
            })
          }).catch((errorMsg) => {
            this.errorMsg = errorMsg;
            this.loading = false
          });
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    doforgetPassword() {
      this.$router.push({ path: '/forgetPassword' });
    },
    doRegister() {
      this.$router.push({ path: '/register' });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image:url(../../assets/images/bg.jpg);
  }
  .title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
    line-height: 25px;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 385px;
    margin-bottom: 150px;
    padding: 25px 25px 5px 25px;
    box-shadow: 4px 5px 10px rgba(0,0,0,.4);
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
    .input-icon{
      height: 39px;width: 14px;margin-left: 2px;
    }
  }
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  .login-code {
    width: 33%;
    display: inline-block;
    height: 38px;
    float: right;
    img{
      cursor: pointer;
      vertical-align:middle
    }
  }
  .error-msg {
    font-size: 13px;
    margin:0px 0px 25px 0px;
    color: red;
  }
  .select-lang {
    position: absolute;
    overflow: auto;
    margin-left: 300px;
  }
</style>
