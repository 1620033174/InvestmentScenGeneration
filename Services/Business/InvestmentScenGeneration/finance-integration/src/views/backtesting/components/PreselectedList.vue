<template>
  <el-checkbox-group v-model="checkedItems" :min="0" :max="3" size="small">
    <!-- TODO:  -->
    <!-- 两行,纵向添加,从左往右,从上往下,右侧drawer为单列列表 -->
    <div class="list-stack">
      <el-checkbox
        class="item"
        v-for="item in options"
        :label="item.id"
        :key="item.id"
      >
        <el-card class="list-item">
          {{ item.name }}
        </el-card>
      </el-checkbox>
    </div>
    <!-- TODO:最终类似这样https://juejin.cn/post/7057449650952863781 -->
    <!-- item点击添加的前三个是自动选中的,后续的不会自动选中 -->
    <!-- list自动前置选定的三个,并且变为高亮的 -->
    <!-- 点击省略右侧的配置按钮则打开侧边的drawer进行配置 -->
  </el-checkbox-group>
</template>

<script>
export default {
  props: {
    list: {
      type: Array,
    },
    selectedList: {
      type: Array,
    },
  },
  data() {
    return {
      checkedItems: this.selectedList,
      options: this.list,
    };
  },
  watch: {
    checkedItems: function (cur, pre) {
      // console.log("change!!!:", cur, pre);
      this.$emit("update:selectedList", cur);
    },
  },
};
</script>

<style lang="scss" scoped>
.item {
  margin: auto;
  display: flex;
  align-items: center;
}
.list-stack {
  display: flex;
  flex-direction: column;
  width: 350px;
  flex-wrap: nowrap;
  @include scrollBar;
}
.list-item {
  @include text-base;
  width: 390px;
  /* padding: 10px; */
  .item-content {
    display: flex;
    justify-content: space-between;
  }
}
</style>
