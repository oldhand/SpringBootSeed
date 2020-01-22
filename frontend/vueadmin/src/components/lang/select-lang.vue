<template>
  <el-dropdown trigger="click" @command="handleSetLanguage">
    <span class="el-dropdown-link"><svg-icon icon-class="language" /><i class="el-icon-arrow-down el-icon--right" /></span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language==='zh'" command="zh">{{ $t('language.zh') }}</el-dropdown-item>
      <el-dropdown-item :disabled="language==='en'" command="en">{{ $t('language.en') }}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
  import { setLanguage, getLanguage } from '@/lang/index'
  export default {
    name: 'SelectLang',
    data() {
      return {
        language: getLanguage()
      };
    },

    methods: {
      handleSetLanguage(lang) {
        this.$i18n.locale = lang;
        setLanguage(lang);
        this.language = lang;
      }
    }
  };
</script>

<style lang="scss" scoped>
</style>
