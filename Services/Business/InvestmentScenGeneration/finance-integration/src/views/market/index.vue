<template>
  <div class="container">
    <div class="top-box">
      <h2>场景超市</h2>
      <div class="search-box">
        <el-input
          v-model="keyword"
          placeholder="请输入关键词"
          clearable
          @clear="handleClear"
          @keyup.enter.native="handleSearch"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
      </div>
      <div class="button-box">
        <el-button
          v-for="tag in tags"
          :key="tag"
          :type="selectedTag === tag ? 'primary' : 'default'"
          @click="handleTag(tag)"
        >
          {{ tag }}
        </el-button>
      </div>
    </div>

    <div class="card-box">
      <el-card v-for="scene in scenes" :key="scene.id" class="card">
        <div slot="header" class="header">
          <span>{{ scene.name }}</span>
          <el-tag style="float: right; font-size: medium;">{{ scene.tags }}</el-tag>

        </div>
        <div class="table-body">

          <img :src="scene.chart" alt="实盘收益折线图"/>
          <el-table :data="[scene]" stripe style="width: 100%">
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="days" label="实盘天数"></el-table-column>
            <el-table-column prop="profit" label="实盘收益"></el-table-column>
          </el-table>

        </div>
      </el-card>
    </div>

    <div class="pagination-box">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Market',
  data() {
    return {
      keyword: "", // 搜索关键词
      tags: ["大盘", "小盘", "创业", "对冲", "重组", "反转", "ST", "择时", "科创"], // 标签列表
      selectedTag: "", // 选中的标签
      list: [
        // 这里是一些投资场景的示例数据，你可以根据实际情况修改或从后端获取
        {
          name: "高送转预期",
          author: "阿瑾",
          profit: 12.5,
          days: 30,
          tags: '大盘',
          chart: "https://img1.imgtp.com/2023/04/09/yZsfQtHc.png",
        },
        {
          name: "高送转预期",
          author: "阿瑾",
          profit: 12.5,
          days: 30,
          tags: '大盘',
          chart: "https://img1.imgtp.com/2023/04/09/yZsfQtHc.png",
        },
        {
          name: "高送转预期",
          author: "阿瑾",
          profit: 12.5,
          days: 30,
          tags: '大盘',
          chart: "https://img1.imgtp.com/2023/04/09/yZsfQtHc.png",
        },
        {
          name: "低送转预期",
          author: "信手拈云",
          profit: -3.2,
          days: 15,
          tags: '大盘',
          chart: "https://img1.imgtp.com/2023/04/09/yZsfQtHc.png",
        },
        {
          name: "甘泉狮王争霸",
          author: "甘泉",
          profit: 8.7,
          days: 45,
          tags: '小盘',
          chart: "https://img1.imgtp.com/2023/04/09/v2hW3cEn.png",
        },
        {
          name: "甘泉狮王争霸",
          author: "甘泉",
          profit: 8.7,
          days: 45,
          tags: '小盘',
          chart: "https://img1.imgtp.com/2023/04/09/v2hW3cEn.png",
        },
        {
          name: "甘泉狮王争霸",
          author: "甘泉",
          profit: 8.7,
          days: 45,
          tags: '小盘',
          chart: "https://img1.imgtp.com/2023/04/09/v2hW3cEn.png",
        },
        {
          name: "甘泉狮王争霸",
          author: "甘泉",
          profit: 8.7,
          days: 45,
          tags: '小盘',
          chart: "https://img1.imgtp.com/2023/04/09/v2hW3cEn.png",
        },
        // ...
      ],
      total: 0, // 总数
      currentPage: 1, // 当前页码
      pageSize: 6, // 每页数量
    };
  },

  computed: {
    // 根据条件过滤出投资场景列表
    scenes() {
      let scenes = this.list;
      if (this.keyword) {
        scenes = scenes.filter((scene) =>
          scene.name.includes(this.keyword) || // 包含场景名
          scene.tags.includes(this.keyword) || // 包含场景标签
          scene.author.includes(this.keyword) // 包含作者
        );
      }
      if (this.selectedTag) {
        scenes = scenes.filter((scene) =>
          scene.tags.includes(this.selectedTag)
        );
      }
      this.total = scenes.length; // 更新总数
      return scenes.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      ); // 返回当前页的数据
    },
  },
  methods: {
    // 清空搜索框
    handleClear() {
      this.keyword = "";
    },
    // 点击搜索按钮或回车键
    handleSearch() {
      this.currentPage = 1; // 重置页码
    },
    // 点击标签按钮
    // 点击标签按钮
    handleTag(tag) {
      this.selectedTag = this.selectedTag === tag ? "" : tag;
      this.currentPage = 1; // 重置页码
    },
    // 点击分页组件
    handlePageChange(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 90%;
  margin-left: 5%;
  margin-bottom: 5%;
  .top-box{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-flow: column;
    width: 100%;
  }

  .search-box {
    margin-bottom: 10px;
    width: 80%;
  }

  .button-box {
    margin-bottom: 10px;
    text-align: center;

    .el-button {
      margin-right: 10px;
    }
  }

  .card-box {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 0px;
    width: 100%;

    .card {
      width: 30%;
      margin-bottom: 20px;

      .header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .author {
          font-size: 14px;
          color: #999;
        }
      }

      .table-body {
        display: flex;
        flex-direction: column;
        align-items: center;

        img {
          width: 80%;
          height: auto;
        }
      }
    }
  }

  .pagination-box {
    text-align: center;
  }
}
</style>
