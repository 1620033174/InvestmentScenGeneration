<template>
  <div>
<!--    <el-checkbox-group v-model="selected" size="medium" @change="handleChange">-->
<!--      <el-checkbox v-for="i in factors" :label="i.id" :key="i.id">{{-->
<!--        i.name-->
<!--      }}</el-checkbox>-->
<!--    </el-checkbox-group>-->
      <el-cascader
        placeholder="请选择"
        ref="xzCascader"
        :options="factors"
        :props="{ multiple: true }"
        filterable
        @change="handleChange"
        :show-all-levels="false"
        collapse-tags
        collapse-tags-tooltip
        v-model="selected"
        clearable>
      </el-cascader>
    <!-- <el-button @click="handleTest">测试</el-button> -->
  </div>
</template>

<script>
export default {
  props: {
    factors: {
      required: true,
    },
  },
  data() {
    return {
      selected: [],
    };
  },
  methods: {
    handleTest() {
      console.log(this.factors, this.selectedFactors, "selectedBar");
    },
    handleChange(e) {
      this.$emit("change", e);
      // console.log(e, "handleChange");
    },
  },
};
</script>

<style lang="scss" scoped></style>
