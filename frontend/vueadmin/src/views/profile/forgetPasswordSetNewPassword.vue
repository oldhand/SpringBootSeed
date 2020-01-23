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
import { getProfile } from '@/api/profile'
import { getToken } from '@/api/sms'
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
        confirmpassword: ''
      },
      forgetPasswordRules: {
        newpassword: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.newpasswordisrequired') }],
        confirmpassword: [{ required: true, trigger: 'change', message: this.$t('forgetPassword.confirmpasswordisrequired') }]
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
    document.title = this.$t('pages.setNewPassword') + ' - ' + Config.webName;
    if (this.$route.query.token) {
      this.token = this.$route.query.token;
    }
    this.handlesearch();
  },
  methods: {
    handlesearch() {
      const me = this;
      getToken(this.token).then(res => {
        console.log('______getToken___' + JSON.stringify(res) + '______');
        me.profileid = res.parameter;
      }).catch((errorMsg) => {
        this.$alert(errorMsg + '，请重新操作忘记密码!', this.$t('tip'), {
          confirmButtonText: '确定',
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
          const newpassword = this.forgetPasswordForm.newpassword;
          const confirmpassword = this.forgetPasswordForm.confirmpassword;
          if (newpassword !== confirmpassword) {
            const errorMsg = '两次输入密码不一致';
            this.$message(errorMsg);
            this.errorMsg = errorMsg;
          }
          console.log('______POST___body____' + JSON.stringify(newpassword) + '____' + JSON.stringify(confirmpassword) + '____');
          getProfile(this.profileid).then(res => {
            console.log('______getProfile___' + JSON.stringify(res) + '______');
            this.$router.push({ path: '/forgetPasswordResetCompleted' });
          }).catch((errorMsg) => {
            this.errorMsg = errorMsg;
          });
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
