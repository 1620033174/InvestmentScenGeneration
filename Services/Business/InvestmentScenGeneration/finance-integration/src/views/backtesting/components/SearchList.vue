<template>
  <div>
    <el-input
      placeholder="请输入内容"
      v-model="inputValue"
      class="input-with-select"
      @keydown.enter.native="search"
    >
      <!-- TODO:增加❌按钮一键清除输入框的内容 -->
      <el-select
        v-model="searchQuery.searchType"
        slot="prepend"
        class="type-area"
      >
        <el-option label="算法名称" value="name"></el-option>
        <el-option label="算法编号" value="id"></el-option>
      </el-select>
      <el-button
        slot="append"
        icon="el-icon-search"
        @click="search"
      ></el-button>
      <!-- TODO：支持回车触发搜索 -->
    </el-input>
    <div class="infinite-list-wrapper">
      <div class="list">
        <el-card
          class="list-item"
          v-for="item in list"
          :key="item.id"
          @click.native="() => handleClick(item)"
        >
          <div class="item-content">
            <div class="id">
              {{ item.id }}
            </div>
            <div class="name">{{ item.name }}</div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { getAlgorithmList } from "@/api/algorithm";
export default {
  data() {
    return {
      list: [],
      inputValue: "",
      searchQuery: {
        algorithmStatus: "done",
        searchType: "name",
        searchContent: "",
      },
    };
  },
  mounted() {},
  methods: {
    handleClick(item) {
      this.$emit("clickItem", item.id);
    },
    search() {
      this.searchQuery.searchContent = this.inputValue;
      this.fetchData();
    },
    fetchData() {
      getAlgorithmList(this.searchQuery).then((response) => {
        this.list = response.data.data;
      });
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style lang="scss" scoped>
.infinite-list-wrapper {
  /* border: 0.1px solid black; */
  width: 390px;
  padding: 10px;
  height: 300px;
  overflow: auto;
  @include scrollBar;
}
.type-area {
  width: 110px;
}
.list-item {
  @include text-base;
  .item-content {
    display: flex;
    .id {
      flex: 0 0 40px;
    }
    .name {
      flex: 1;
    }
  }
  &:hover{
    cursor: pointer;
  }
}
</style>
