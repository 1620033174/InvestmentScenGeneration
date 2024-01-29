<template>
  <div>
    <el-drawer
      :title="algorithm.name"
      :visible="drawer"
      direction="ltr"
      size="40%"
      :before-close="handleClose"
    >
      <div class="drawer">
        <!-- TODO：导入，修改语言，编辑初始的变量 -->
        <!-- <div>语言</div>
        <div>初始因子</div>
        <div>重置代码框内初始内容</div> -->
        <div>
          <b>选股因子:</b>
          <div style="display: flex; gap: 10px; flex-wrap: wrap">
            <el-tag v-for="item in algorithm.factors" :key="item.id">{{
              item.name
            }}</el-tag>
          </div>
        </div>
        <div>
          <b>选股描述:</b>
          <div v-html="algorithm.logicDescription"></div>
        </div>
        <div class="text-base font-bold">算法描述:</div>
        <!-- change仅在输入框失去焦点时触发 -->
        <el-input
          type="textarea"
          :rows="4"
          placeholder="请输入内容"
          v-model="textarea"
          style="width: 400px; margin-left: 30px"
          @change="handleChangeTextarea"
        >
        </el-input>

        <div class="bottom-bar">
          <el-button type="primary" class="to-right" @click="save"
            >保存</el-button
          >
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { updateAlgorithm } from "@/api/algorithm";
import { message } from "@/views/description/static";
export default {
  props: ["drawer", "algorithm"],
  data() {
    return {
      language: "javascript",
      textarea: this.algorithm.algorithmDescription,
    };
  },
  methods: {
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          this.$emit("update:drawer", false);
          done();
        })
        .catch((_) => {});
    },
    handleChangeTextarea(e) {
      this.$emit("changeAlgorithmDescription", e);
    },
    save() {
      // update方法接口 this.textarea
      // 算法描述可改
      updateAlgorithm(this.algorithm).then((response) => {
        this.$emit("update:drawer", false);
        this.$message({
          message: "保存成功",
          type: "success",
        });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.drawer {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  @include text-base;
  padding-left: 30px;
  /* justify-content: space-between; */
}
.bottom-bar {
  margin: 10px;
}
</style>
