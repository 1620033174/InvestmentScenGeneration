
<template>
<div class="manage-out-box" >
  <h3>因子管理</h3>
  <el-form class="search-form"
           :inline="true" :model="selectForm">
        <el-form-item label="因子名查询：" prop="name">
          <el-input prefix-icon="el-icon-search" @blur="selectNames" placeholder="请输入因子" v-model="selectForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类别查询：" prop="type">
          <el-select prefix-icon="el-icon-search" filterable
                     @change="selectTypes" placeholder="请输入类别"
                     v-model="selectForm.type">
            <el-option v-for="item in typeOptions"
                       :value="item.value"
                       :label="item.label">
            </el-option>
          </el-select>
        </el-form-item>
  </el-form>
  <el-table v-loading="tableLoading" style="width: 90%;" :data="factorData">
    <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left"  class="demo-table-expand">

          <el-form-item label="默认值">
            <span>{{ props.row.defaultValue }}</span>
          </el-form-item>
          <el-form-item label="最大值">
            <span>{{ props.row.maxValue}}</span>
          </el-form-item>
          <el-form-item label="最小值">
            <span>{{ props.row.minValue}}</span>
          </el-form-item>
          <el-form-item label="精度">
            <span>{{ props.row.accuracy}}</span>
          </el-form-item>
          <el-form-item label="描述">
            <span>{{ props.row.description }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <el-table-column label="id" prop="id" align="center">
    </el-table-column>
    <el-table-column label="因子名称" prop="name" align="center">
    </el-table-column>
    <el-table-column label="类别" prop="type" align="center">
    </el-table-column>
    <el-table-column label="公式" prop="formula" align="center">
    </el-table-column>
    <el-table-column label="英文名" prop="nameUs" align="center">
    </el-table-column>
    <el-table-column align="center" min-width="150">
      <template #header>
        <el-button type="warning" @click="addNewFactor" icon="el-icon-plus" plain>创建因子</el-button>
        <el-button type="primary"  icon="el-icon-plus" plain @click="importDialogVisible = true">导入</el-button>
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
  <el-dialog :visible.sync="dialogVisible" center  width="50%">
    <template #title>
      <h3>{{dialogTitle}}</h3>
    </template>
    <el-form style="width: 60%" status-icon :rules="editFactorRule" :model="chosenFactor" ref="typeFormRef">
      <el-form-item label="因子名称：" prop="name">
        <el-input placeholder="请填写名称" v-model="chosenFactor.name"></el-input>
      </el-form-item>
      <el-form-item label="英文名：" prop="formula">
        <el-input placeholder="请填写名称" v-model="chosenFactor.nameUs"></el-input>
      </el-form-item>
      <el-form-item label="类型：" prop="type">
        <el-select  filterable
                    placeholder="请输入类别"
                   v-model="chosenFactor.type">
          <el-option v-for="item in chooseTypeOptions"
                     :value="item.value"
                     :label="item.label">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="公式：" prop="formula">
        <br>
        <el-row>
          <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += '('">(</el-button>
          </el-col>
          <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += ')'">)</el-button>
          </el-col>
          <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += '+'">+</el-button>
          </el-col>
        <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += '-'">-</el-button>
          </el-col>
        <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += '*'">*</el-button>
          </el-col>
        <el-col :span="4">
            <el-button size="small" type="primary" plain @click="chosenFactor.formula += '/'">/</el-button>
          </el-col>
        </el-row>
        <el-input placeholder="请填写公式" v-model="chosenFactor.formula"></el-input>
      </el-form-item>
      <el-form-item label="默认值：" prop="defaultValue">
        <el-input-number placeholder="请填写默认值" v-model="chosenFactor.defaultValue"></el-input-number>
      </el-form-item>
      <el-form-item label="最大值：" prop="maxValue">
        <el-input-number placeholder="请填写最大值" v-model="chosenFactor.maxValue"></el-input-number>
      </el-form-item>
      <el-form-item label="最小值：" prop="minValue">
        <el-input-number placeholder="请填写最小值" v-model="chosenFactor.minValue"></el-input-number>
      </el-form-item>
      <el-form-item label="精度：" prop="accuracy">
        <el-input-number placeholder="请填写精度" v-model="chosenFactor.accuracy"></el-input-number>
      </el-form-item>
      <el-form-item label="描述：" prop="description">
        <el-input placeholder="请填写描述" type="textarea" v-model="chosenFactor.description"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div id="dialogFooter">
        <el-button type="success" plain  style="width: 15%" round @click="handleEdit">确认
        </el-button>
        <el-button type="danger" style="width: 15%; margin-left: 20px" plain round @click="dialogVisible = false">
          取消
        </el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog title="批量导入" :visible.sync="importDialogVisible" center destroy-on-close>
    <el-link href="http://localhost:8080/demo.xlsx" target="_blank" type="success" style="margin-bottom: 2.5rem">下载模板</el-link>
    <el-upload
      ref="upload"
      class="upload-demo"
      action="/api/hz/factor/import"
      :limit="1"
      :before-upload="beforeUpload"
      :on-change="handleChange"
      :on-exceed="handleExceed"
      :file-list="fileList"
      :data="{name: ''}"
      :auto-upload="false"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传xls.xlsx文件</div>
    </el-upload>
    <template #footer>
      <div id="dialogFooter">
        <el-button type="primary" plain  style="width: 15%"  @click="handleImport">确认
        </el-button>
        <el-button type="danger" style="width: 15%; margin-left: 20px" plain  @click="importDialogVisible = false">
          取消
        </el-button>
      </div>
    </template>
  </el-dialog>
</div>
</template>
<script>

import {addFactorInfo, deleteFactorInfo, getFactorPage, updateFactorInfo} from "@/api/factor";

export default ({
  data(){
    return{
      factorData: [{
        id: '1',
        name: 'hhh',
        type: 'hhh',
        formula: 'hhh',
        defaultValue: 0,
        maxValue: 0,
        minValue: 0,
        accuracy: 1,
        description: 'h',
        nameUs: 'h'
      }],
      typeOptions: [
        {
        value: '质量类因子',
        label: '质量类因子'
        },
        {
          value: '',
          label: '全部'
        },
        {
          value: '市值因子',
          label: '市值因子'
        },
        {
          value: '基础因子',
          label: '基础因子'
        },
        {
          value: '情绪因子',
          label: '情绪因子'
        },
        {
          value: '每股因子',
          label: '每股因子'
        },
        {
          value: '财务指标因子',
          label: '财务指标因子'
        },
        {
          value: '资产因子',
          label: '资产因子'
        }
      ],
      chooseTypeOptions: [
        {
          value: '质量类因子',
          label: '质量类因子'
        },
        {
          value: '市值因子',
          label: '市值因子'
        },
        {
          value: '基础因子',
          label: '基础因子'
        },
        {
          value: '情绪因子',
          label: '情绪因子'
        },
        {
          value: '每股因子',
          label: '每股因子'
        },
        {
          value: '财务指标因子',
          label: '财务指标因子'
        },
        {
          value: '资产因子',
          label: '资产因子'
        }
      ],
      tableLoading: false,
      page: 1,
      pageSize: 10,
      counts: 0,
      dialogVisible: false,
      importDialogVisible: false,
      dialogTitle: '增加因子',
      isEdit: false,
      editFactorRule: {
        name: [
          {required: true, message: '请输入因子名称', trigger: 'blur'},
        ],
        type: [
          {required: true, message: '请选择因子类别', trigger: 'blur'},
        ],
        formula: [
          {required: true, message: '请输入因子公式', trigger: 'blur'},
        ],
        defaultValue: [
          {required: true, message: '请输入因子默认值', trigger: 'blur'},
        ],
        maxValue: [
          {required: true, message: '请输入因子最大值', trigger: 'blur'},
        ],
        minValue: [
          {required: true, message: '请输入因子最小值', trigger: 'blur'},
        ],
        accuracy: [
          {required: true, message: '请输入因子精度', trigger: 'blur'},
        ],
        description: [
          {required: true, message: '请输入因子描述', trigger: 'blur'},
        ],
      },
      fileList: [],
      fileListTemp: [],
      chosenFactor: {
        name: '',
        type: '',
        formula: '',
        defaultValue: 0,
        maxValue: 0,
        minValue: 0,
        accuracy: 0,
        description: '',
        nameUs: ''
      },
      selectForm: {
        name: '',
        type: '',
        formula: '',
        defaultValue: 0,
        maxValue: 0,
        minValue: 0,
        accuracy: 0,
        description: '',
        nameUs: ''
      }
    }
  },
  methods:{
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeUpload(file){
      const isExcel = (file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isExcel) {
        this.$message.error('上传文件只能是 xls/xlsx 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 2MB!')
      }
      return isExcel && isLt2M
    },
    handleChange(file, fileList){
      this.fileListTemp = fileList
    },
    handleImport(){
      if (this.fileListTemp.length === 0){
        this.$message.error('请先选择文件')
        return
      }
      this.$refs.upload.submit()
      this.$message({
        type: 'success',
        message: '导入成功',
      })
      this.importDialogVisible = false
    },
    handleDelete(index, row){
      this.$confirm('此操作将永久删除该因子, 是否继续?', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning',
      })
        .then(async () => {
          let res = await deleteFactorInfo(row.id)
          if (res?.data.code === 20000){
            this.$message({
              type: 'success',
              message: '删除成功',
            })
            await this.initData()
          }else {
            this.$message.error(res.data.msg)
          }
        }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除',
        })
      })
    },
    handleSizeChange(val){
      this.page = 1
      this.pageSize = val
      this.initData()
    },
    handleCurrentChange(val){
      this.page = val
      this.initData()
    },
    selectNames(){
      this.page = 1
      this.initData()
    },
    selectTypes(){
      this.page = 1
      this.initData()
    },
    async addNewFactor(){
      this.dialogVisible = true
      this.isEdit = false
      this.dialogTitle = '增加因子'
      this.chosenFactor = {
        name: '',
        type: '',
        formula: '',
        defaultValue: 0,
        maxValue: 0,
        minValue: 0,
        accuracy: 0,
        description: '',
        nameUs: ''
      }
    },
    async handleEdit(){
      if (this.isEdit){
        let res = await updateFactorInfo(this.chosenFactor)
        if (res?.data.code === 20000){
          this.$message({
            type: 'success',
            message: '修改成功',
          })
          this.dialogVisible = false
          await this.initData()
        }else {
          this.$message.error(res.data.msg)
        }
      } else {
        let res = await addFactorInfo(this.chosenFactor)
        if (res?.data.code === 20000){
          this.$message({
            type: 'success',
            message: '添加成功',
          })
          this.dialogVisible = false
          await this.initData()
        }else {
          this.$message.error(res.data.msg)
        }
      }
    },
    clickEdit(index, row){
      this.chosenFactor = row
      this.isEdit = true
      this.dialogVisible = true
      this.dialogTitle = '修改因子'
    },
    async initData(){
      this.tableLoading = true
      let res = await getFactorPage({page: this.page, pageSize: this.pageSize, name: this.selectForm.name, type: this.selectForm.type})
      if (res?.data.code === 20000){
        this.factorData = res.data.data.records
        this.counts = res.data.data.total
      }else {
        this.$message.error('获取数据失败')
      }
      this.tableLoading = false
    }
  },
  mounted(){
    this.initData()
  }
})
</script>
<style scoped lang="scss">
.manage-out-box{
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
