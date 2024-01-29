<template>
  <div class="dashboard-container">
    <!-- <div class="dashboard-text">项目名称</div> -->
    <article class="title-bar">
      <div class="text-2xl font-bold">欢迎！</div>
    </article>
    <article class="main">
      <el-card
        :body-style="{ padding: '0px' }"
        class="card-item"
        v-for="item in cardItems"
        :key="item.path"
        @click.native="() => handleClick(item.path)"
      >
        <div style="width: 100%; text-align: center; margin-top: 0.5rem; margin-bottom: 0.5rem">

          <i class="el-icon-setting"></i>
          <span style="font-family: 'Microsoft YaHei UI Light'">{{ item.name}}</span>
          <i class="el-icon-setting"></i>
        </div>
        <el-image class="image" fit="cover" :src="item.src" />
      </el-card>
    </article>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

const routers = [
  {
    path: "/factor-manage/factor-manage",
    name: "因子管理",
    src: require("../../assets/dashboardImage/tool.png"),
    icon: "el-icon-setting"
  },
  {
    path: "/algorithm-manage/algorithm-manage",
    name: "算法管理",
    src: require("../../assets/dashboardImage/factor.png"),
    icon: "el-icon-data-line"
  },
  // {
  //   path: "/settings/settings",
  //   name: "个人信息管理",
  //   src: require("../../assets/dashboardImage/settings.png"),
  //   icon: "el-icon-user"
  // },
  {
    path: "/sceneManage/sceneManage",
    name: "场景管理",
    src: require("../../assets/dashboardImage/market.png"),
    icon: "el-icon-s-shop"
  },
  // {
  //   path: "/description",
  //   name: "选股逻辑描述",
  //   src: require("../../assets/dashboardImage/stock.png"),
  //   icon: "el-icon-data-line\n"
  // },
];

export default {
  name: "Dashboard",
  data() {
    return {
      cardItems: routers,
    };
  },
  computed: {
    ...mapGetters(["name"]),
  },
  methods: {
    handleClick(path) {
      this.$router.push(path);
    },
  },
};
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.title-bar {
  margin-bottom: 20px;
  text-align: center;
}
.main {
  display: flex;
  flex-wrap: wrap;
  width: 1100px;
  gap: 100px;
  margin: auto;
  @include text-base;
  .card-item {
    width: 300px;
    height: 200px;
    border-radius: 1rem;
    &:hover {
      /* box-shadow: 4px 2px 2px gray; */
      /* https://www.cssmatic.com/box-shadow */
      -webkit-box-shadow: 10px 10px 27px -4px rgba(0, 0, 0, 0.75);
      -moz-box-shadow: 10px 10px 27px -4px rgba(0, 0, 0, 0.75);
      box-shadow: 10px 10px 27px -4px rgba(0, 0, 0, 0.75);
      cursor: pointer;

    }
    .image {
      width: 100%;
    }
    &:hover .image {
      transition: all 0.6s;
      transform: scale(1.1);
    }

    &:not(:hover) .image {
      transition: all 0.6s;
    }
  }
}
</style>
