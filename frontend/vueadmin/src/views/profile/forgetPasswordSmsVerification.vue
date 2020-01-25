<template>
  <div class="forgetPassword">
    <el-form ref="forgetPasswordForm" :model="forgetPasswordForm" :rules="forgetPasswordRules" label-position="left" label-width="0px" class="forgetPassword-form">
      <h3 class="title">{{ $t('forgetPassword.title') }}</h3>
      <el-steps :active="2" :align-center="true" direction="horizontal" >
        <el-step :title="$t('forgetPassword.inputUsername')" />
        <el-step :title="$t('forgetPassword.smsVerification')" />
        <el-step :title="$t('forgetPassword.setNewPassword')" />
        <el-step :title="$t('forgetPassword.resetCompleted')" />
      </el-steps>
      <div style="width:60%; margin: 0 auto; margin-top: 30px;">
        <el-form-item prop="mobile">
          <el-input :value="'+' + forgetPasswordForm.regioncode + forgetPasswordForm.mobile" type="text" readonly auto-complete="off" >
            <svg-icon slot="prefix" icon-class="mobile" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item prop="smsverifycode" >
          <el-input v-model="forgetPasswordForm.smsverifycode" :placeholder="$t('forgetPassword.smsVerifycode')" auto-complete="off" maxlength="6" style="width: 60%" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
          </el-input>
          <div class="forgetPassword-code">
            <el-button :disabled="isDisabled" style="width:100%" size="medium" type="primary" @click.native.prevent="handleSendSmsVerifyCode">
              <span>{{ buttonName }}</span>
            </el-button>
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
import { search, send, verify } from '@/api/sms'
import { getProfile } from '@/api/profile'

export default {
  name: 'ForgetPasswordSmsVerification',
  data() {
    return {
      profileid: '',
      buttonName: this.$t('forgetPassword.sendSmsVerifyCode'),
      isDisabled: false,
      time: 120,
      forgetPasswordForm: {
        mobile: '',
        regioncode: '',
        smsverifycode: '',
        uuid: ''
      },
      forgetPasswordRules: {
        smsverifycode: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.verifycodeisrequired') }]
      },
      loading: false,
      errorMsg: ''
    }
  },
  created() {
    document.title = this.$t('pages.smsVerification') + ' - ' + Config.webName;
    if (this.$route.query.profileid) {
      this.profileid = this.$route.query.profileid;
    }
    this.handlesearch();
  },
  methods: {
    handlesearch() {
      const me = this;
      getProfile(this.profileid).then(res => {
          me.forgetPasswordForm.mobile = res.mobile;
          me.forgetPasswordForm.regioncode = res.regioncode;
          search(this.forgetPasswordForm.regioncode, this.forgetPasswordForm.mobile).then(res => {
            if (res.remain > 0) {
              me.time = res.remain;
              me.forgetPasswordForm.uuid = res.uuid;
              me.buttonName = me.time + me.$t('forgetPassword.second');
              me.isDisabled = true;
              me.startCountDowner();
            }
          }).catch((errorMsg) => {
            this.errorMsg = errorMsg;
          });
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    handleBack() {
      this.$router.back(-1);
    },
    startCountDowner() {
      const me = this;
      me.buttonName = me.time + me.$t('forgetPassword.second');
      --me.time;
      me.isDisabled = true;
      const interval = window.setInterval(function() {
        me.buttonName = me.time + me.$t('forgetPassword.second');
        --me.time;
        if (me.time < 0) {
          me.buttonName = me.$t('forgetPassword.resend');
          me.time = 120;
          me.isDisabled = false;
          window.clearInterval(interval);
        }
      }, 1000);
    },
    handleSendSmsVerifyCode() {
      const me = this;
      me.isDisabled = true;
      send(this.forgetPasswordForm.regioncode, this.forgetPasswordForm.mobile, 'forgetpassword').then(res => {
        if (res.status === 1) {
          me.forgetPasswordForm.uuid = res.uuid;
          me.startCountDowner();
        } else {
          this.errorMsg = this.$t('forgetPassword.smssendingfailed');
        }
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    handleNext() {
      this.errorMsg = '';
      this.$refs.forgetPasswordForm.validate(valid => {
        if (valid) {
          const verifycode = this.forgetPasswordForm.smsverifycode;
          const uuid = this.forgetPasswordForm.uuid;
          this.loading = true
          verify(uuid, verifycode, this.profileid).then(res => {
            if (res.status === 'ok') {
              this.loading = false
              this.$router.push({ path: '/forgetPasswordSetNewPassword?token=' + res.token });
            }
          }).catch((errorMsg) => {
            this.errorMsg = errorMsg;
            this.loading = false
          });
        }
      });
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
