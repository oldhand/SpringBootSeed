<template>
  <div class="forgetPassword">
    <el-form ref="forgetPasswordForm" :model="forgetPasswordForm" :rules="forgetPasswordRules" label-position="left" label-width="0px" class="forgetPassword-form">
      <h3 class="title">{{ $t('forgetPassword.title') }}</h3>
      <el-steps :active="3" :align-center="true" direction="horizontal" >
        <el-step :title="$t('forgetPassword.inputUsername')" />
        <el-step :title="$t('forgetPassword.smsVerification')" />
        <el-step :title="$t('forgetPassword.setNewPassword')" />
        <el-step :title="$t('forgetPassword.resetCompleted')" />
      </el-steps>
      <div style="width:60%; margin: 0 auto; margin-top: 30px;">
        <el-form-item prop="password" >
          <el-input v-model="forgetPasswordForm.newpassword" :placeholder="$t('forgetPassword.newpassword')" type="password" auto-complete="off" maxlength="20" >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>

        <el-form-item prop="confirmpassword" >
          <el-input v-model="forgetPasswordForm.confirmpassword" :placeholder="$t('forgetPassword.confirmpassword')" type="password" auto-complete="off" maxlength="20" >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>

        <el-form-item prop="verifycode" >
          <el-input v-model="forgetPasswordForm.verifycode" :placeholder="$t('forgetPassword.verifycode')" auto-complete="off" maxlength="4" style="width: 63%" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
          </el-input>
          <div class="forgetPassword-code">
            <img :src="codeUrl" @click="getCode">
          </div>
        </el-form-item>

        <div v-if="errorMsg != ''" class="error-msg">
          {{ errorMsg }}
        </div>

        <el-form-item style="width:100%;">
          <el-row style="margin:0px 0px 25px 0px;" type="flex" justify="center">
            <el-button size="medium" icon="el-icon-back" @click.native.prevent="handleBack">
              <span>{{ $t('forgetPassword.back') }}</span>
            </el-button>
            <el-button :loading="loading" size="medium" type="primary" icon="el-icon-right" @click.native.prevent="handleNext">
              <span v-if="!loading">{{ $t('forgetPassword.next') }}</span>
              <span v-else>{{ $t('forgetPassword.committing') }}...</span>
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
import { getProfile, modifypassword } from '@/api/profile'
import { getToken } from '@/api/sms'
import { getVerifyCode } from '@/api/login'
export default {
  name: 'ForgetPasswordSetNewPassword',
  data() {
    return {
      codeUrl: '',
      profileid: '',
      token: '',
      buttonName: this.$t('forgetPassword.sendSmsVerifyCode'),
      isDisabled: false,
      time: 120,
      forgetPasswordForm: {
        newpassword: '',
        confirmpassword: '',
        code: '',
        uuid: ''
      },
      forgetPasswordRules: {
        newpassword: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.newpasswordisrequired') }],
        confirmpassword: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.confirmpasswordisrequired') }],
        verifycode: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.verifycodeisrequired') }]
      },
      loading: false,
      errorMsg: ''
    }
  },
  created() {
    document.title = this.$t('pages.setNewPassword') + ' - ' + Config.webName;
    if (this.$route.query.token) {
      this.token = this.$route.query.token;
    }
    this.handlesearch();
  },
  methods: {
    getCode() {
      getVerifyCode().then(res => {
        this.codeUrl = res.img
        this.forgetPasswordForm.uuid = res.uuid
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    handlesearch() {
      const me = this;
      getToken(this.token).then(res => {
        me.profileid = res.parameter;
        this.getCode()
      }).catch((errorMsg) => {
        this.$alert(errorMsg + '，' + this.$t('forgetPassword.pleasereoperateforgetpassword'), this.$t('tip'), {
          confirmButtonText: this.$t('forgetPassword.OK'),
          type: 'error',
          callback: action => {
            this.$router.push({ path: '/forgetPassword' });
          }
        });
      });
    },
    handleBack() {
      this.$router.back(-1);
    },
    handleNext() {
      this.errorMsg = '';
      this.$refs.forgetPasswordForm.validate(valid => {
        if (valid) {
          this.loading = true
          const newpassword = this.forgetPasswordForm.newpassword;
          const confirmpassword = this.forgetPasswordForm.confirmpassword;
          if (newpassword === confirmpassword) {
            getProfile(this.profileid).then(res => {
              modifypassword(this.profileid, newpassword, this.forgetPasswordForm.uuid, this.forgetPasswordForm.verifycode).then(res => {
                this.loading = false
                if (res === 'ok') {
                  this.$router.push({ path: '/forgetPasswordResetCompleted' });
                }
              }).catch((errorMsg) => {
                this.loading = false
                this.errorMsg = errorMsg;
              });
            }).catch((errorMsg) => {
              this.loading = false
              this.errorMsg = errorMsg;
            });
          } else {
            const errorMsg = this.$t('forgetPassword.thetwopasswordsareinconsistent');
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
  .forgetPassword {
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

  .forgetPassword-form {
    border-radius: 6px;
    background: #ffffff;
    width: 585px;
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
  .forgetPassword-code {
    width: 35%;
    margin-right: 0px;
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
