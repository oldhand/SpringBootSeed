<template>
  <el-dropdown trigger="click" @command="handleSetLanguage">
    <svg-icon icon-class="language" />
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language==='zh'" command="zh">中文</el-dropdown-item>
      <el-dropdown-item :disabled="language==='en'" command="en">English</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { mapGetters } from 'vuex';
import { setLanguage } from '@/lang/index'
export default {
  name: 'LoginLang',
  data() {
    return {};
  },
  computed: {
    ...mapGetters(['language', 'tag'])
  },
  created() {},
  mounted() {},
  methods: {
    handleSetLanguage(lang) {
      this.$i18n.locale = lang;
      setLanguage(lang);
      const tag = this.tag;
      const title = this.$router.$avueRouter.generateTitle(
        tag.label,
        (tag.meta || {}).i18n
      );
      // 根据当前的标签也获取label的值动态设置浏览器标题
      this.$router.$avueRouter.setTitle(title);
    }
  }
};
</script>

<style lang="scss" scoped>
</style>
