<script>

import {addScene} from "@/api/scene";

export default({
  data(){
    return {

    }
  },
  computed: {
    form() {
      return this.$store.state.scene.scene
    },
    pictureList() {
      let pictureData = []
      this.$store.state.scene.pictureList.forEach(item => {
        if (item.checked){
          pictureData.push(item)
        }
      })
      console.log(pictureData)
      return pictureData
    },
    stockTableData() {
      return this.$store.state.scene.stockTableData
    },
    selectedAlgorithms() {
      return this.$store.state.scene.selectedAlgorithms
    },
    stockList() {
      let stockArray = []
      this.$store.state.scene.stockTableData.forEach(item => {
        item.rows.forEach(row => {
          stockArray.push(row.code)
        })
      })
      return stockArray
    },
    date() {
      return this.$store.state.scene.date
    }
  },
  methods: {
    publish() {
      this.$confirm("确认发布？")
        .then(_ => {
          let tags = []
          if (this.form.tags.length === 0){
            this.$message.warning('请至少选择一个标签')
            return
          }
          this.form.tags.forEach(item =>{
            tags.push(item.name)
          })
          this.form.tags = tags
          if (this.selectedAlgorithms.length === 0){
            this.$message.warning('请至少选择一个算法')
            return
          }
          addScene({...this.form,
          algorithmList: this.selectedAlgorithms,
            pictureList: this.pictureList,
            date: this.date,
          stockList: this.stockList}).then(res =>{
            if (res.data.code !== 20000) {
              this.$message({
                type: "error",
                message: "发布失败"
              })
            } else {
              this.$store.dispatch('scene/setDialogVisible', false)
              this.$message({
                type: "success",
                message: "发布成功"
              })
              //清空数据
              this.$store.dispatch('scene/setScene', {})
              this.$store.dispatch('scene/setPictureList', [])
              this.$store.dispatch('scene/setStockTableData', [])
              this.$store.dispatch('scene/setSelectedAlgorithms', [])
            }
          })
        })
        .catch(_ => {})
    }
  },
})
</script>

<template>
  <div>
    <div class="preview" style="margin-left: 30px;">

      <h3 style="align-self: center;">场景名：{{ form.sceneName }}</h3>
      <p>场景主题：{{ form.theme }}</p>
      <p>场景描述：
        <div v-html=" form.description "></div>
      </p>

      <p>场景标签：
        <el-tag v-for="(tag, index) in form.tags" :key="index">{{ tag.name }}</el-tag>
      </p>

    </div>
    <div style="margin-left: 30px;">
      <p>入选股票池：</p>
      <div class="table" v-for="(o, index) in stockTableData" :key="index">
        <h3>{{selectedAlgorithms[index].name}}股票池：</h3>
        <el-table max-height="400" :data="o.rows" border style="width: 100%">
          <el-table-column v-for="(item,index) in o.columns" :key="index" :prop="item.prop"
                           :label="item.label"></el-table-column>
        </el-table>
      </div>
    </div>

    <div style="text-align: center; margin-top: 30px;">
      <el-button @click="publish" type="primary" plain>生成场景并发布</el-button>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>
