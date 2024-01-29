<template>
<div class="manage-algorithm-box">
  <h3>算法管理</h3>
  <el-form class="search-form"
           :inline="true" :model="selectForm">
    <el-form-item label="算法名查询：" prop="name">
      <el-input prefix-icon="el-icon-search" @blur="selectNames" placeholder="请输入算法" v-model="selectForm.name"></el-input>
    </el-form-item>
    <el-form-item label="类别查询：" prop="type">
      <el-select  @change="selectTypes" placeholder="请选择类别" v-model="selectForm.type">
        <el-option label="全部" value=""></el-option>
        <el-option label="条件选股" value="1"></el-option>
        <el-option label="综合选股" value="2"></el-option>
      </el-select>
    </el-form-item>
  </el-form>
  <el-table ref="algorithmTable" v-loading="tableLoading" style="width: 90%;"
            :data="algorithmData"
            @selection-change="handleSelectionChange"
  >
    <el-table-column
      type="selection"
      width="55">
    </el-table-column>
    <el-table-column label="id" prop="id" align="center">
    </el-table-column>
    <el-table-column label="算法名称" prop="name" align="center">
    </el-table-column>
    <el-table-column label="类别"  align="center">
      <template #default = "scope">
        <el-tag type="primary">
          {{scope.row.type === '1'?"条件选股":"综合选股"}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="状态"  align="center">
      <template #default = "scope">
        <el-tag :type="scope.row.status === 'done'? 'success': 'warning'">
          {{scope.row.status === "done"?"已完成":"未完成"}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="作者" prop="author" align="center">
    </el-table-column>
    <el-table-column align="center" min-width="250">
      <template #header>
        <el-button type="success" @click="addNewAlgorithm" icon="el-icon-plus" plain>创建算法</el-button>
        <el-button type="primary" @click="clickBackTest"  plain>回测</el-button>
      </template>
      <template #default="scope">
        <el-button size="small" type="primary" @click="clickEdit(scope.$index, scope.row)"
                   plain icon="el-icon-edit">编辑
        </el-button>
        <el-button
          plain
          size="small"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
          icon="el-icon-delete"
        >删除
        </el-button>
        <el-button
          plain
          size="small"
          type="info"
          @click="handleInfo(scope.$index, scope.row)"
          icon="el-icon-document"
        >详情
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
  <el-dialog destroy-on-close :visible.sync="addDialogVisible" center  width="80%" >
    <template #title>
      <h3>{{dialogTitle}}</h3>
    </template>
    <div style="width: 100%">
      <AddDescription :isEdit="isEdit" :editData="editData" @success="handleSuccess"/>
    </div>

  </el-dialog>
  <el-dialog :visible.sync="previewDialogVisible" center  width="60%">
    <template #title>
      <h3>详细信息</h3>
    </template>
    <div>
      <el-row :gutter="40">
        <el-col :span="6">
          <span style="font-weight: bold">算法名称：</span>
          {{previewData.name}}
        </el-col>
        <el-col :span="6">
          <span style="font-weight: bold">算法类别：</span>
          {{previewData.type === '1'?"条件选股":"综合选股"}}
        </el-col>
        <el-col :span="6">
          <span style="font-weight: bold">算法作者：</span>
          {{previewData.author}}
        </el-col>
      </el-row>
      <el-row style="margin-top: 2rem">
        <el-col :span="24">
          <span style="font-weight: bold">算法描述：</span>
          {{previewData.algorithmDescription}}
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          算法因子：
          <el-table :data="previewData.factors">
            <el-table-column label="因子名称" prop="name" align="center">
            </el-table-column>
            <el-table-column label="类别" prop="type" align="center">
              <template #default="scope">
                <el-tag>
                  {{scope.row.type}}
                </el-tag>
              </template>

            </el-table-column>
            <el-table-column label="公式" prop="formula" align="center">
            </el-table-column>
            <el-table-column label="英文名" prop="nameUs" align="center">
            </el-table-column>
            <el-table-column label="描述" prop="description" align="center">
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
  <el-drawer destroy-on-close :visible.sync="drawerVisible" size="80%">
    <DoneAlgorithm :row="incompleteRow"/>
  </el-drawer>
  <el-drawer destroy-on-close size="85%" direction="ltr" :visible.sync="backTestDrawerVisible">
    <BackTesting :selectedAlgorithms="selectedAlgorithms" />
  </el-drawer>
</div>
</template>

<script>
import AddDescription from '@/views/description/index.vue'
import {deleteAlgorithmById, getAlgorithmById, getAlgorithmPage} from "@/api/algorithm";
import {prev} from "dom7";
import DoneAlgorithm from "@/views/configuration/index.vue";
import BackTesting from "@/views/backtesting/index.vue";
export default ({
  components:{
    AddDescription,
    DoneAlgorithm,
    BackTesting
  },
  data(){
    return{
      algorithmData:[],
      selectForm:{
        name:'',
        type:''
      },
      selectedAlgorithms: [],
      incompleteRow:{},
      drawerVisible:false,
      backTestDrawerVisible:false,
      addDialogVisible:false,
      dialogTitle:'新增算法',
      tableLoading:false,
      isEdit: false,
      page:1,
      pageSize:5,
      counts:0,
      isConditional: true,
      previewDialogVisible: false,
      previewData: {},
      editData: {}
    }
  },
  methods: {
    selectNames(){
      this.page = 1
      this.initData()
    },
    selectTypes(){
      this.page = 1
      this.initData()
    },
    getAlgorithmData(){
      // this.tableLoading = true
      // this.$axios.get('/api/algorithm/getAlgorithmData',{
      //   params:{
      //     name:this.selectForm.name,
      //     type:this.selectForm.type,
      //     page:this.page,
      //     pageSize:this.pageSize
      //   }
      // }).then(res=>{
      //   this.tableLoading = false
      //   this.algorithmData = res.data.data
      //   this.counts = res.data.counts
      // }).catch(err=>{
      //   this.tableLoading = false
      //   this.$message.error('获取算法数据失败')
      // })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.initData()
    },
    handleCurrentChange(val) {
      this.page = val
      this.initData()
    },
    handleSelectionChange(val) {
      if (val.length > 3){
        this.limitFn(val)
        this.$message.warning('最多只能选择三个算法')
        return
      }
      this.selectedAlgorithms = val
    },
    addNewAlgorithm(){
      this.dialogTitle = '创建算法'
      this.isEdit = false
      this.addDialogVisible = true

    },
    limitFn(list) {
      // this.$refs.algorithmTable.clearSelection(); //用于多选表格，清空用户的选择
      this.$refs.algorithmTable.toggleRowSelection(list[list.length-1], false); //用于多选表格，选中用户选择的第一项

    },
    clickBackTest(){
      if (this.selectedAlgorithms.length === 0){
        this.$message.warning('请先选择算法')
        return
      }
      this.backTestDrawerVisible = true
    },
    handleSuccess(){
      this.addDialogVisible = false
      this.initData()
    },
    clickDone(index,row){
      this.incompleteRow = row
      this.drawerVisible = true
    },
    clickEdit(index,row){
      console.log(row)
      if (row.type === '1'){
        this.dialogTitle = '编辑算法'
        getAlgorithmById({id: row.id})
          .then(res=>{
            row = res.data.data
            this.editData = row
            this.isEdit = true
            this.addDialogVisible = true
          }).catch(err=>{
          this.$message.error('获取算法详情失败')
        })
      } else {
        this.$confirm('请选择是修改代码还是编辑算法', '选择', {
          distinguishCancelAndClose: true,
          confirmButtonText: '修改代码',
          cancelButtonText: '编辑算法'
        })
          .then(() => {
            this.incompleteRow = row
            this.$message.warning(row.toString())
            this.drawerVisible = true
          })
          .catch(action => {
            this.dialogTitle = '编辑算法'
            getAlgorithmById({id: row.id})
              .then(res=>{
                row = res.data.data
                this.editData = row
                this.isEdit = true
                this.addDialogVisible = true
              }).catch(err=>{
              this.$message.error('获取算法详情失败')
            })
          });
      }
    },
    handleDelete(index,row){
      this.$confirm('此操作将永久删除该算法, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAlgorithmById(row.id)
          .then(res=>{
          this.$message.success('删除成功')
          this.initData()
        }).catch(err=>{
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleInfo(index,row){
      getAlgorithmById({id: row.id})
        .then(res=>{
          this.previewDialogVisible = true
          this.previewData = res.data.data
      }).catch(err=>{
        this.$message.error('获取算法详情失败')
      })
    },
    initData(){
      this.tableLoading = true
      getAlgorithmPage({page: this.page, pageSize: this.pageSize,
        name: this.selectForm.name, type: this.selectForm.type})
        .then(res =>{
        this.tableLoading = false
        this.algorithmData = res.data.data.records
        this.counts = res.data.data.total
      }).catch(err => {
        this.tableLoading = false
        this.$message.error('获取算法数据失败')
      })
    }
  },
  mounted() {
    this.initData()
  }
})
</script>
<style scoped lang="scss">
.manage-algorithm-box{
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
