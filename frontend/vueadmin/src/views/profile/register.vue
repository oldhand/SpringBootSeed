<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" label-position="right" label-width="0px" class="register-form">
      <h3 class="title">{{ $t('register.title') }}</h3>
      <el-steps :active="1" :align-center="true" direction="horizontal" >
        <el-step :title="$t('register.fillAccountInformation')" />
        <el-step :title="$t('register.smsVerification')" />
        <el-step :title="$t('register.fillServiceInformation')" />
        <el-step :title="$t('register.UploadQualificationInformation')" />
        <el-step :title="$t('register.registerCompleted')" />
      </el-steps>
      <div style="width:70%; margin: 0 auto; margin-top: 30px;">
        <el-form-item :label="$t('register.name')+':'" prop="name" label-width="100px" required="true">
          <el-input v-model="registerForm.name" :placeholder="$t('register.placeholder_name')" type="text" auto-complete="off">
            <svg-icon slot="prefix" icon-class="saas" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('register.username')+':'" prop="username" label-width="100px" required="true">
          <el-input v-model="registerForm.username" :placeholder="$t('register.placeholder_username')" type="text" auto-complete="off">
            <svg-icon slot="prefix" icon-class="users" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('register.contact')+':'" prop="contact" label-width="100px" required="true">
          <el-input v-model="registerForm.contact" :placeholder="$t('register.placeholder_contact')" type="text" auto-complete="off">
            <svg-icon slot="prefix" icon-class="users" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('register.mobile')+':'" prop="mobile" label-width="100px" required="true">
          <el-input v-model="registerForm.mobile" :placeholder="$t('register.placeholder_mobile')" type="text" auto-complete="off" >
            <svg-icon slot="prefix" icon-class="mobile" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('register.password')+':'" prop="password" label-width="100px" required="true">
          <el-input v-model="registerForm.password" :placeholder="$t('register.placeholder_password')" type="password" auto-complete="off" maxlength="20" >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>

        <el-form-item :label="$t('register.confirmpassword')+':'" prop="confirmpassword" label-width="100px" required="true">
          <el-input v-model="registerForm.confirmpassword" :placeholder="$t('register.placeholder_confirmpassword')" type="password" auto-complete="off" maxlength="20" >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('register.verifycode')+':'" prop="verifycode" label-width="100px" required="true">
          <el-input v-model="registerForm.verifycode" :placeholder="$t('register.placeholder_verifycode')" auto-complete="off" maxlength="4" style="width: 63%" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCode">
          </div>
        </el-form-item>

        <div v-if="errorMsg != ''" class="error-msg">
          {{ errorMsg }}
        </div>

        <el-form-item style="width:100%;">
          <el-row style="margin:0px 0px 25px 0px;" type="flex" justify="center">
            <el-button size="medium" icon="el-icon-back" @click.native.prevent="handleBack">
              <span>{{ $t('register.back') }}</span>
            </el-button>
            <el-button :loading="loading" size="medium" type="primary" icon="el-icon-right" @click.native.prevent="handleNext">
              <span v-if="!loading">{{ $t('register.next') }}</span>
              <span v-else>{{ $t('register.committing') }}...</span>
            </el-button>
          </el-row>
        </el-form-item>
      </div>

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
  import { getVerifyCode, verifyCode } from '@/api/login'
  import { createVerify } from '@/api/saas'
  export default {
    name: 'Register',
    data() {
      return {
        codeUrl: '',
        registerForm: {
          name: 'demo',
          username: 'admin',
          contact: '管理员',
          mobile: '15111122026',
          password: '123qwe',
          confirmpassword: '123qwe',
          verifycode: '',
          uuid: ''
        },
        registerRules: {
          name: [{ required: true, trigger: 'blur', message: this.$t('register.nameisrequired') }],
          username: [{ required: true, trigger: 'blur', message: this.$t('register.usernameisrequired') }],
          contact: [{ required: true, trigger: 'blur', message: this.$t('register.contactisrequired') }],
          mobile: [{ required: true, trigger: 'blur', message: this.$t('register.mobileisrequired') },
                   { required: true, pattern: /^[1][3,4,5,7,8][0-9]{9}$/, message: this.$t('register.mobileverifyrule'), trigger: 'blur' }],
          password: [{ required: true, trigger: 'blur', message: this.$t('register.passwordisrequired') }],
          confirmpassword: [{ required: true, trigger: 'blur', message: this.$t('register.confirmpasswordisrequired') }],
          verifycode: [{ required: true, trigger: 'blur', message: this.$t('register.verifycodeisrequired') }]
        },
        loading: false,
        errorMsg: ''
      }
    },
    created() {
      document.title = this.$t('pages.register') + ' - ' + Config.webName;
      this.getCode()
    },
    methods: {
      getCode() {
        getVerifyCode().then(res => {
          this.codeUrl = res.img
          this.registerForm.uuid = res.uuid
        }).catch((errorMsg) => {
          this.errorMsg = errorMsg;
        });
      },
      handleBack() {
        this.$router.back(-1);
      },
      handleNext() {
        this.errorMsg = '';
        this.$refs.registerForm.validate(valid => {
          if (valid) {
            const name = this.registerForm.name;
            const username = this.registerForm.username;
            const contact = this.registerForm.contact;
            const mobile = this.registerForm.mobile;
            const verifycode = this.registerForm.verifycode;
            const password = this.registerForm.password;
            const confirmpassword = this.registerForm.confirmpassword;
            const uuid = this.registerForm.uuid;
            this.loading = true
            if (password === confirmpassword) {
              verifyCode(uuid, verifycode).then(res => {
                if (res === 'ok') {
                  createVerify(name, username, contact, mobile, password).then(res => {
                    this.loading = false
                    // this.$router.push({ path: '/registerSmsVerification?profileid=' + res.id });
                  }).catch((errorMsg) => {
                    this.errorMsg = errorMsg;
                    this.loading = false
                  });
                }
              }).catch((errorMsg) => {
                this.errorMsg = errorMsg;
                this.loading = false
                this.getCode()
              })
            } else {
              const errorMsg = this.$t('register.thetwopasswordsareinconsistent');
              this.loading = false
              this.$message(errorMsg);
              this.errorMsg = errorMsg;
            }
          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  .register {
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

  .register-form {
    border-radius: 6px;
    background: #ffffff;
    width: 585px;
    margin-bottom: 100px;
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
</style>
