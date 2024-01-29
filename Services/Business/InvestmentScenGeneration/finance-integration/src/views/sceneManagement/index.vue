<template>
<div class="manage-scene-box">
  <h3>场景管理</h3>
  <el-form class="search-form"
           :inline="true" :model="selectForm">
    <el-form-item label="场景名查询：" prop="name">
      <el-input prefix-icon="el-icon-search" @blur="selectNames"
                placeholder="请输入场景" v-model="selectForm.name"></el-input>
    </el-form-item>
  </el-form>
  <el-table :data="tableData" v-loading="tableLoading">
    <el-table-column type="selection" width="55">
    </el-table-column>
    <el-table-column label="id" prop="id" align="center">
    </el-table-column>
    <el-table-column label="场景名称" prop="sceneName" align="center">
    </el-table-column>
    <el-table-column label="场景主题"  align="center">
      <template #default="scope">
        <el-tag size="medium" type="success">{{scope.row.theme}}</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="算法"  align="center">
      <template #default="scope">
        <div v-for="item in scope.row.algorithmList">
          <el-tag
            style="margin: 0.25rem"
            size="small"
            type="warning">
            {{item.name}}</el-tag>
          <br/>
        </div>

      </template>
    </el-table-column>
    <el-table-column label="场景描述"  align="center">
      <template #default="scope">
        {{scope.row.description.substring(0, 10) + '...'}}
      </template>
    </el-table-column
    ><el-table-column label="场景标签"  align="center">
    <template #default="scope">
      <div v-for="item in scope.row.tags">
        <el-tag size="small"
                style="margin: 0.25rem"
        >
          {{item}}
        </el-tag>
      </div>

    </template>
  </el-table-column>
    <el-table-column align="center" min-width="200">
      <template #header>
        <el-button type="success" @click="clickAdd" icon="el-icon-plus" plain>创建场景</el-button>
      </template>
      <template #default="scope">
        <el-button
          plain
          size="small"
          type="info"
          @click="clickPreview(scope.$index, scope.row)"
          icon="el-icon-data-analysis"
        >查看
        </el-button>
        <el-button
          plain
          size="small"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          icon="el-icon-delete"
        >删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
    class="pageList"
    :page-sizes="[5, 10, 20, 30, 40]"
    :page-size="pageSize"
    layout="total, sizes, prev, pager, next, jumper"
    :total="counts"
    @size-change="handleSizeChange"
    :current-page="page"
    @current-change="handleCurrentChange">

  </el-pagination>
  <el-dialog center :visible.sync="dialogVisible"
             destroy-on-close width="90%" :title="dialogTitle">
      <SceneCreate :selectScene="selectData"/>
  </el-dialog>
  <el-dialog  center :visible.sync = "previewDialogVisible" fullscreen
              width="90%" >
<!--  这里 sjj 修改了，为了部署的时候与原有的前端页面不冲突，所以这里要使用不同的 src 地址，注释掉的是原有的 src  -->
<!--    <iframe src="http://localhost:8081/portrait/about"-->

<!--    <iframe src="http://127.0.0.1/portrait/about"-->
    <iframe src="http://localhost:81/portrait/about"
            style="height: 650px; width: 100%"
            id="inner-frame"
            name="inner-frame"
    ></iframe>
  </el-dialog>
  <el-dialog width="80%" append-to-body center title="画像查看" :visible.sync="previewTableVisible">
    <div>
      <el-table :data="previewSceneTableData" max-height="400" v-loading="previewTableLoading">
          <el-table-column v-for="(item, index) in previewSceneTableColumns" :key="index"
                           :label="item.label" :prop="item.prop">

          </el-table-column>
        <el-table-column>
          <template #default="scope">
            <el-button
              plain
              size="small"
              type="info"
              @click="handlePreview(scope.$index, scope.row)"
              icon="el-icon-data-analysis"
            >查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-dialog>
</div>
</template>

<script>
import SceneCreate from '@/views/scenecreate/index.vue'
import Review from "@/views/sceneManagement/review/index.vue";
import {deleteScene, getSceneById, getSceneList, getStockListById} from "@/api/scene";
export default ({
  components: {
    SceneCreate,
    Review
  },
  data() {
    return {
      selectForm: {
        name: ''
      },
      dialogTitle: '投资场景创建',
      previewDialogVisible: false,
      previewTableVisible: false,
      editDialogVisible: false,
      page: 1,
      pageSize: 5,
      counts: 0,
      tableLoading: false,
      previewTableLoading: false,
      isEdit: false,
      tableData: [
        {
          id: 1,
          name: '场景1',
          theme: '教育',
          algorithms: '算法1',
          description: '描述1',
          tags: ['创新']
        },
        {
          id: 2,
          name: '场景2',
          theme: '医疗',
          algorithms: '算法2',
          description: '描述1',
          tags: ['创新']
        },
        {
          id: 3,
          name: '更改了的投资场景3',
          theme: '教育',
          algorithm: '低波动低换手',
          description: '更改描述',
          tags: ['可靠','安全']
        }
      ],
      selectData: {},
      previewSceneData: {},
      previewSceneTableData: [],
      previewSceneTableColumns: [],
    }
  },
  mounted() {
    this.initData()
  },
  computed: {
    dialogVisible: {
      get() {
        return this.$store.state.scene.dialogVisible
      },
      set(val) {
        this.$store.dispatch('scene/setDialogVisible', val)
      }
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.initData()
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val
      this.initData()
    },
    handleCurrentChange(val) {
      this.page = val
      this.initData()
    },
    clickAdd() {
      this.dialogTitle = '投资场景创建'
      this.isEdit = false
      this.dialogVisible = true
    },
    clickEdit(index, row) {
      this.isEdit = true
      this.selectData = row
      this.editDialogVisible = true
    },
    selectNames() {
      this.initData()
    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该场景, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteScene(row.id).then(res => {
          this.initData()
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    clickPreview(index, row) {
      this.$store.dispatch('scene/setPreviewSceneId', row.id)
      this.previewTableVisible = true
      this.previewTableLoading = true
      getStockListById({id: row.id}).then(res => {
        if (res.data.code !== 20000) {
          this.$message({
            type: "error",
            message: "获取股票列表失败"
          })
        } else {
          this.previewSceneTableData = res.data.data.data.rows
          this.previewSceneTableColumns = res.data.data.data.columns
          this.previewTableLoading = false
        }
      })
    },
    handlePreview(index, row) {
      this.previewDialogVisible = true
      //row.date格式为2022/9/30 改为2022-09-30
      let date = row.pubdate.split('/')
      let year = date[0]
      let month = date[1]
      let day = date[2]
      if (month.length === 1) {
        month = '0' + month
      }
      if (day.length === 1) {
        day = '0' + day
      }
      date = year + '-' + month + '-' + day
      //等待iframe加载完毕再获取iframe的window对象
        // let frame = document.getElementById('inner-frame').contentWindow
      let frame = window.frames['inner-frame']
      frame.postMessage({
          "股票代码": row.code,
          "中文名称": row.stock_name,
          "每股收益(元)": row.eps,
          "每股经营现金流(元)": 5.37,
          "每股公积金(元)": row.capital_reserve_fund_per_share,
          "每股未分配利润(元)": row.retained_profit_per_share,
          "流通股本(亿股)": 1.02,
          "流通市值(亿元)": 57.0,
          "_quarter": date,
          "stock_id": row.code,
          "_stock_name": row.stock_name,
          "id": index,
          "_selected_time": "2023-10-30",
          "当前股价(元)": 23.42
        }, '*')
    },
    initData() {
      this.tableLoading = true
      getSceneList({
        page: this.page,
        pageSize: this.pageSize,
        name: this.selectForm.name
      }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.data.records
        this.counts = res.data.data.total
        console.log(this.tableData)
      })
    }
  }
})
</script>



<style scoped lang="scss">
.manage-scene-box{
  width: 100%;
  display: flex;
  flex-flow: column;
  justify-content: flex-start;
  align-items: center;
}
.search-form{
  width: 100%;
  margin-top: 1rem;
  margin-left: 10%;
}
</style>
