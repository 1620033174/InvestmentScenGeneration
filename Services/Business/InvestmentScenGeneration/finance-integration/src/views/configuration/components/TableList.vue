<template>
  <el-table :data="tableData" style="width: 100%" stripe>
    <el-table-column label="名称" width="180" prop="name" align="center" />
    <el-table-column label="业务人员" width="180" align="center">
      <template slot-scope="scope">
        <el-tag size="medium" type="warning">{{ scope.row.author }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="创建时间" width="180" align="center">
      <template slot-scope="scope">
        <i class="el-icon-time"></i>
        <span style="margin-left: 10px">{{ scope.row.createdAt }}</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center">
      <template slot-scope="scope">
        <el-button size="mini" type="primary" plain @click="handleEdit(scope.$index, scope.row)"
          >编辑</el-button
        >
        <!-- TODO:👇不知道最终是否需要这个东西 -->
        <!-- <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          >删除</el-button
        > -->
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { getAlgorithmList } from "@/api/algorithm";
export default {
  data() {
    return {
      // tableData: todoAlgorithms,
      tableData: [],
    };
  },
  methods: {
    handleEdit(index, row) {
      this.$emit("handleEdit", index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    },
    fetchData() {
      // { searchType: "", searchContent: "" }
      getAlgorithmList({ algorithmStatus: "todo" }).then((response) => {
        // console.log(response.data, "smile");
        this.tableData = response.data.data;
      });
    },
  },
  created() {
    this.fetchData();
  },
};
</script>
<style lang="scss" scoped></style>
