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
          <el-input v-model="forgetPasswordForm.smsverifycode" :placeholder="$t('forgetPassword.smsVerifycode')" auto-complete="off" maxlength="4" style="width: 60%" @keyup.enter.native="handleLogin">
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
import { search, send } from '@/api/sms'
import { searchUser, verifyCode } from '@/api/login'
export default {
  name: 'ForgetPasswordSmsVerification',
  data() {
    return {
      codeUrl: '',
      profileid: '',
      uuid: '',
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
    document.title = this.$t('pages.forgetPassword') + ' - ' + Config.webName;
    if (this.$route.query.profileid) {
      this.profileid = this.$route.query.profileid;
    }
    if (this.$route.query.mobile) {
      this.forgetPasswordForm.mobile = this.$route.query.mobile;
    }
    if (this.$route.query.regioncode) {
      this.forgetPasswordForm.regioncode = this.$route.query.regioncode;
    }
    this.handlesearch();
  },
  methods: {
    handlesearch() {
      const me = this;
      search(this.forgetPasswordForm.regioncode, this.forgetPasswordForm.mobile).then(res => {
        console.log('______search____' + JSON.stringify(res) + '______');
        if (res.remain > 0) {
          me.time = res.remain;
          me.buttonName = me.time + '秒';
          me.isDisabled = true;
          me.startCountDowner();
        }
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    handleBack() {
      this.$router.back(-1);
    },
    startCountDowner() {
      const me = this;
      me.buttonName = me.time + '秒';
      --me.time;
      me.isDisabled = true;
      const interval = window.setInterval(function() {
        me.buttonName = me.time + '秒';
        --me.time;
        if (me.time < 0) {
          me.buttonName = '重新发送';
          me.time = 120;
          me.isDisabled = false;
          window.clearInterval(interval);
        }
      }, 1000);
    },
    handleSendSmsVerifyCode() {
      const me = this;
      send(this.forgetPasswordForm.regioncode, this.forgetPasswordForm.mobile, 'forgetpassword').then(res => {
        console.log('______send___' + JSON.stringify(res) + '______');
        if (res.status === 1) {
          me.uuid = res.uuid;
          me.startCountDowner();
        } else {
          this.errorMsg = '短信发送失败';
        }
      }).catch((errorMsg) => {
        this.errorMsg = errorMsg;
      });
    },
    handleNext() {
      this.errorMsg = '';
      this.$refs.forgetPasswordForm.validate(valid => {
        if (valid) {
          const username = this.forgetPasswordForm.username;
          const code = this.forgetPasswordForm.code;
          const uuid = this.forgetPasswordForm.uuid;
          this.loading = true
          verifyCode(uuid, code).then(res => {
            console.log('______POST___body____' + JSON.stringify(res) + '______');
            if (res === 'ok') {
              searchUser(username).then(res => {
                console.log('______POST___body____' + JSON.stringify(res) + '______')
                this.loading = false
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
