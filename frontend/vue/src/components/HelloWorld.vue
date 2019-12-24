<template>
  <div class="hello">
  {{msg}}
    <br><br><br>
    <el-button type="success" @click.native="handleClick">Test Profile</el-button>
  </div>
</template>

<script>
import {getverifycode, generator} from '@/apis/api';

export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Vue Rest Api Demo'
    }
  },
  mounted () {
    console.log('______HelloWorld________');
  },
  methods: {
    async handleClick() {
      try {
         console.log('_____Test___starting___');
         await getverifycode();
         await generator();
         console.log('_____Test___finished___');
      } catch (errormsg) {
        console.log('_________Test__errormsg_________' + errormsg + "____________");
      }
    }
  }
}
</script>
