<template>
  <div class="component">
    <el-tabs type="border-card">
      <el-tab-pane label="日志" class="tab1 tab">{{
        consoleContent
      }}</el-tab-pane>
      <!-- <el-tab-pane label="测试" class="tab2 tab"
        >不知道干嘛</el-tab-pane
      > -->
    </el-tabs>
  </div>
</template>

<script>
export default {
  props: {
    consoleContent: {
      type: String,
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.component {
  color: #eeee8e;
  width: 100%;
  .tab {
    background-color: #141414;
    width: 100%;
    height: 150px;
  }
}
</style>
