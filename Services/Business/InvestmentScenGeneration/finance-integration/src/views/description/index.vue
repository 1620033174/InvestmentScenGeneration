<template>
  <main class="view">
    <div class="page0" v-show="page === 0">
      <article class="tutorial">选择算法类型和选股因子</article>
      <div class="type-select-area">
        <div class="type-select-box">
          <div style="font-weight: bold">算法类型选择：</div>
          <el-card
            shadow="always"
            class="card-button"
            :class="{
            selected: algorithm.type === AlgorithmTypeEnum.conditional,
          }"
            @click.native="algorithm.type = AlgorithmTypeEnum.conditional"
          >
            <el-tooltip >
              <template #content>
                <div style="text-align: center" v-html="conditionText"></div>
              </template>
              <div class="card-title">条件选股</div>
            </el-tooltip>
          </el-card>
          <el-card
            shadow="always"
            class="card-button"
            :class="{
            selected: algorithm.type === AlgorithmTypeEnum['non-conditional'],
          }"
            @click.native="algorithm.type = AlgorithmTypeEnum['non-conditional']"
          >
            <el-tooltip >
              <template #content>
                <div style="text-align: center" v-html="comprehensiveText"></div>
              </template>
              <div class="card-title">综合选股</div>
            </el-tooltip>

          </el-card>
        </div>

      </div>
      <article class="factor-select-area">
        <el-card >
          <div style="font-weight: bold">因子选择：</div>
<!--          <selected-bar-->
<!--            :factors="factors"-->
<!--            @change="(e) => (selectedFactors = e)"-->
<!--          />-->
          <el-cascader
            placeholder="请选择"
            ref="xzCascader"
            :options="factors"
            :props="{ multiple: true }"
            filterable
            :show-all-levels="false"
            collapse-tags
            collapse-tags-tooltip
            v-model="selectedFactors"
            clearable>
          </el-cascader>
        </el-card>
      </article>
      <article class="step-area">
        <el-button type="primary" @click="handleNextStep">下一步</el-button>
      </article>
    </div>
    <div class="page1" v-show="page === 1">
      <article class="algorithm-name-bar">
        <div class="content-box">
          <span>算法名称:</span>
          <el-input placeholder="请输入算法名称" v-model="algorithm.name" />
        </div>
      </article>
      <article class="description-edit-area">
        <el-card>
          <el-table :data="algorithm.factors" stripe style="width: 100%">
            <el-table-column prop="name" label="因子名称" width="180">
            </el-table-column>
            <el-table-column prop="description" label="选股逻辑描述" min-width="250">
              <template slot-scope="scope">
                <el-select style="width: 30%"
                           v-model="scope.row.choiceType"
                           size="small"
              >
                <el-option label="百分比" :value="0"></el-option>
                <el-option label="排名" :value="1"></el-option>
                <el-option label="数字" :value="2"></el-option>
              </el-select>
                <el-select v-model="scope.row.logic"
                           placeholder="请选择"
                           size="small"
                           style="width: 30%; margin-right: 0.5rem"
                           v-if="scope.row.choiceType === 2"
                >
                  <el-option
                    v-for="(item, index) in descriptionOptions"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-select v-model="scope.row.isTop"
                                       size="small"
                                       style="width: 30%; margin-right: 0.5rem"
                           v-if="scope.row.choiceType !== 2"
                >
                <el-option label="前" :value="1"></el-option>
                <el-option label="后" :value="0"></el-option>
              </el-select>
                <el-tooltip placement="top" v-if="scope.row.choiceType === 2">
                  <div slot="content">该因子最小值为{{scope.row.minValue}}<br/>
                    最大值为{{scope.row.maxValue}}<br/>
                    精度为{{scope.row.accuracy}}
                  </div>
                <el-input-number
                                  v-if="scope.row.choiceType === 2"
                                 :min="scope.row.minValue"
                                 v-model="scope.row.defaultValue"
                                 :max="scope.row.maxValue"
                                 :step="scope.row.accuracy"
                                 size="small"
                                  style="width: 30%; margin-right: 0.5rem"

                ></el-input-number>
                </el-tooltip>

                <el-input-number
                  style="width: 30%; margin-right: 0.5rem"
                  :min="1"
                  v-model="scope.row.value"
                  :max="100"
                  :step="1"
                  step-strictly
                  size="small"
                  v-if="scope.row.choiceType !== 2"
                ></el-input-number>

              </template>
            </el-table-column>
          </el-table>
        </el-card>
<!--        <el-card>-->
<!--          <el-table :data="algorithm.factors" stripe >-->
<!--            <el-table-column prop="name" label="因子名称" width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column label="选股逻辑需求" min-width="250">-->
<!--              <template #default = "scope">-->
<!--                <el-select v-model="scope.row.isTop"-->
<!--                           size="small"-->
<!--                           style="width: 30%; margin-right: 0.5rem">-->
<!--                  <el-option label="前" :value="1"></el-option>-->
<!--                  <el-option label="后" :value="0"></el-option>-->
<!--                </el-select>-->
<!--                <el-input-number-->
<!--                                  style="width: 30%; margin-right: 0.5rem"-->
<!--                                  :min="1"-->
<!--                                 v-model="scope.row.value"-->
<!--                                 :max="100"-->
<!--                                 :step="1"-->
<!--                                  step-strictly-->
<!--                                 size="small"-->
<!--                ></el-input-number>-->
<!--                <el-select style="width: 30%"-->
<!--                           v-model="scope.row.choiceType"-->
<!--                           size="small"-->
<!--                >-->
<!--                <el-option label="百分比" :value="0"></el-option>-->
<!--                <el-option label="排名" :value="1"></el-option>-->
<!--                <el-option label="数字" :value="2"></el-option>-->
<!--              </el-select>-->
<!--              </template>-->

<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </el-card>-->
        <div style="margin-top: 1rem">
          <div class="font-bold text-lg">算法描述:</div>
          <el-input
            type="textarea"
            v-model="algorithm.algorithmDescription"
            placeholder="请输入算法描述"
          ></el-input>
        </div>
      </article>
      <article class="step-bar">
        <el-button @click="page = 0">上一步</el-button>
        <el-button type="primary" @click="handlePreview"
         plain >预览</el-button
        >
        <el-button type="primary" @click="handleCreateNewAlgorithm"
        >{{buttonText}}</el-button
        >
      </article>
    </div>
    <div class="page2" v-show="page === 2">
      <article class="algorithm-name-bar">
        <div class="content-box">
          <span>算法名称:</span>
          <el-input placeholder="请输入算法名称" v-model="algorithm.name" />
        </div>
      </article>
      <article class="description-edit-area">
        <div style="display: flex; gap: 10px; margin-bottom: 15px">
          <el-button type="primary" plain  v-for="tag in algorithm.factors"
                      :key="tag.id" @click="handleAddTagText(tag.name)" >
            {{tag.name}}
          </el-button>
        </div>
        <div class="text-lg font-bold" style="margin-bottom: 10px">
          选股逻辑描述:
        </div>
<!--        <editor-->
<!--          :editorContent="algorithm.logicDescription"-->
<!--          @changeEditorContent="handleChangeEditorContent"-->
<!--        />-->
        <el-input v-model="algorithm.logicDescription"
                  type="textarea" show-word-limit maxlength="1000"
                  :autosize="{minRows: 10, maxRows: 20}"
        >

        </el-input>
      </article>
      <article class="step-bar">
        <el-button @click="page = 0">上一步</el-button>
        <el-button type="primary" @click="handleCreateComprehensive"
          >生 成</el-button
        >
        <!-- <el-button @click="test">测试</el-button> -->
      </article>
    </div>

    <!-- 新手帮助dialog -->
    <!-- page2 preview dialog -->
    <el-dialog
      title="预览"
      :visible.sync="previewDialogVisible"
      width="50%"
      destroy-on-close
      append-to-body
    >
      <div class="text-base">
        <algorithm-preview :algorithm="algorithm"
                           :tableData="previewTableData"
                           :columns="previewFactorColumns"/>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </main>
</template>

<script>
import Editor from "@/components/Editor";
import SelectedBar from "./components/SelectedBar.vue";
import AlgorithmPreview from "@/components/AlgorithmPreview";
import { getFactors } from "@/api/description";
import {createAlgorithmByFileName, createAlgorithmContentZongHe, setAlgorithm, updateAlgorithm} from "@/api/algorithm";
import { getFactorsData, getPage2TagData } from "./utils/transformer";
import { AlgorithmTypeEnum } from "@/utils/enum";
import {getAllFactors} from "@/api/description";
import axios from "axios";
import {getFactorInfo} from "@/api/factor";
import {getSimulatedStock, getStockList} from "@/api/stock";

function camel (data) {
  let newData = data.replace(/_([a-z])/g, (p, m) => m.toUpperCase())
  console.log(newData)
  return newData
}
const Page = {
  initial: 0,
  page1: 1,
  page2: 2,
};
export default {
  components: {
    Editor,
    SelectedBar,
    AlgorithmPreview,
  },
  props: {
    isEdit: {
      required: true,
    },
    editData: {
      required: true,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      factors: [],
      selectedFactors: [],
      previewFactorColumns: [],
      previewTableData: [],
      buttonText: '生成',
      // 预览和最终发送到后端的信息
      /** 发送信息的结构
      post请求
      {
        name:算法名称
        algorithmDescription:算法描述(综合选股时发送"")
        type: 1 条件选股 | 2 综合选股
        factors:
      }

      */
      descriptionOptions: [
        {
          value: "higher",
          label: "大于",
        },
        {
          value: "higherequal",
          label: "大于等于",
        },
        {
          value: "lower",
          label: "小于",
        },
        {
          value: "lowerequal",
          label: "小于等于",
        },
        {
          value: "equal",
          label: "等于",
        }
      ],
      algorithm: {
        id: "0", // id为0 | "0"时为新增
        name: "",
        algorithmDescription: "",
        logicDescription: "",
        type: AlgorithmTypeEnum.empty,
        factors: [],
      },
      comprehensiveText: ' 综合选股是根据提供系统或用户编制的条件选股公式 '+ '<br/>'+ '进行选股选定一个条件选股公式或多个组合条件后，'+ '<br/>' +
        '计算机自动帮您选出当时或历史上某一段时间内满足条件的所有股票',
      conditionText: ' 条件选股是根据提供系统或用户编制的条件选股公式进行选股选定一个' + '<br/>' +
        '条件选股公式或多个组合条件后，'+'<br/>'+'计算机自动帮您选出当时或历史上某一段时间内满足条件的所有股票',
      // NOTE:测试时便于更改
      page: Page.initial,
      previewDialogVisible: false,
      // data中声明枚举让模板中也能使用
      AlgorithmTypeEnum,
    };
  },

  watch: {
    // 如果 `question` 发生改变，这个函数就会运行
    selectedFactors: function (cur, pre) {
      console.log("change!!!:", cur, pre);
    },
    isEdit: {
      handler(cur, pre) {
        if (cur){
          this.algorithm.type = this.editData.type
          this.buttonText = '修改'
        } else {
          this.buttonText = '生成'
          this.algorithm =  {
              id: "0", // id为0 | "0"时为新增
              name: "",
              algorithmDescription: "",
              logicDescription: "",
              type: AlgorithmTypeEnum.empty,
              factors: [],
          }
          this.selectedFactors = []
        }
      },
      immediate: true
    },
    editData: {
      handler(val){
        if (!val.factors || val.factors.length === 0){
          return
        }
        this.page = Page.initial
        this.selectedFactors = []
        val.factors.forEach(item => {
          let arr = []
          arr.push(item.type)
          arr.push(item.nameUs)
          this.selectedFactors.push(arr)
        })
        this.algorithm.id = val.id
        this.algorithm.type = val.type
        if (val.logicDescription !== null){
          this.algorithm.logicDescription = val.logicDescription
        }
        this.algorithm.name = val.name
        if (val.algorithmDescription !== null) {
          this.algorithm.algorithmDescription = val.algorithmDescription
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    async handleCreateComprehensive() {
      if (this.isEdit) {
        const loading = this.$loading({
          text: "正在修改",
        });
        let res = await updateAlgorithm(this.algorithm)
        if (res.data.code === 20000){
          loading.close()
          this.$emit('success')
          this.$message.success('修改成功')
        } else {
          loading.close()
          this.$message.error('修改失败')
        }
      } else {
        const loading = this.$loading({
          lock: true,
          text: "正在生成算法",
        });
        let res = await setAlgorithm(this.algorithm)
        createAlgorithmByFileName({ id: res.data.data }).then((res) => {
        }).catch(err => {
          this.$message.error("生成失败");
          loading.close();
        });
        createAlgorithmContentZongHe({ id: res.data.data }).then((res) => {
          loading.close();
          if (res.data.code === 20000) {
            this.$message.success("生成成功");
            this.$emit("success");
          } else {
            this.$message.error("生成失败");
          }
        }).catch(err => {
          this.$message.error("生成失败");
          loading.close();
        });
      }
    },
    async handleCreateNewAlgorithm() {
      if (this.isEdit){
        const loading = this.$loading({
          text: "正在修改",
        });
        let numberCount = 0;
        for (let i = 0; i < this.algorithm.factors.length; i++) {
          if(this.factors[i].isNumber === 1){
            numberCount++;
          }
        }
        if (numberCount > 1) {
          this.$message.warning('选股只能有一个数值型排序因子')
        }
        for (let i = 0; i < this.algorithm.factors.length; i++) {
          if (this.algorithm.factors[i].type === 2) {
            this.algorithm.factors[i].value = this.algorithm.factors[i].defaultValue;
          }

        }
        let res = await updateAlgorithm(this.algorithm)
        if (res.data.code === 20000){
          loading.close()
          this.$emit('success')
          this.$message.success('修改成功')
        } else {
          loading.close()
          this.$message.error('修改失败')
        }
      } else {
        const loading = this.$loading({
          lock: true,
          text: "正在生成算法",
        });
        let numberCount = 0;
        for (let i = 0; i < this.algorithm.factors.length; i++) {
          if(this.factors[i].isNumber === 1){
            numberCount++;
          }
        }
        if (numberCount > 1) {
          this.$message.warning('选股只能有一个数值型排序因子')
        }
        for (let i = 0; i < this.algorithm.factors.length; i++) {
          if (this.algorithm.factors[i].type === 2) {
            this.algorithm.factors[i].value = this.algorithm.factors[i].defaultValue;
          }

        }
        // let res = await setAlgorithm(this.algorithm)
        let res = await setAlgorithm(this.algorithm)
        let res1 = await getStockList(this.algorithm)
        console.log(res.data)
        console.log(res1.data)
        createAlgorithmByFileName({ id: res.data.data, stockList: res1.data.data }).then((res) => {
          loading.close();
          if (res.data.code === 20000) {
            this.$message.success("生成成功");
            this.$emit("success");
          } else {
            this.$message.error("生成失败");
          }
        }).catch(err => {
          this.$message.error("生成失败");
          loading.close();
        });
      }

    },
    handlePreview() {
      this.previewFactorColumns = []
      for (let i = 0; i < this.selectedFactors.length; i++) {
        let foundFactor = this.algorithm.factors.find(item => item.nameUs === this.selectedFactors[i][1])
        this.previewFactorColumns.push(
          {
            label: foundFactor.name,
            prop: this.selectedFactors[i][1]
          })
      }

      this.previewDialogVisible = true;
      let numberCount = 0;
      for (let i = 0; i < this.algorithm.factors.length; i++) {
        if(this.factors[i].isNumber === 1){
          numberCount++;
        }
      }
      if (numberCount > 1) {
        this.$message.warning('综合选股只能有一个数值型排序因子')
      }
      for (let i = 0; i < this.algorithm.factors.length; i++) {
        if (this.algorithm.factors[i].type === 2) {
          this.algorithm.factors[i].value = this.algorithm.factors[i].defaultValue;
        }
      }
      console.log(this.algorithm)
      getSimulatedStock(this.algorithm).then(res => {
        this.previewTableData = res.data.data
      }).catch(err => {
        console.log(err);
      })
    },
    // TODO: nextstep时清除之前编辑的内容,比如仅综合选股才能业务人员配置的算法逻辑
    handleNextStep() {
      switch (this.algorithm.type) {
        case AlgorithmTypeEnum.conditional: {
          if (this.selectedFactors.length > 0) {
            if (this.isEdit){
              this.algorithm.factors = JSON.parse(JSON.stringify(this.editData.factors))
            } else {
              this.algorithm.factors = getFactorsData(
                this.factors,
                this.selectedFactors
              );
            }

            this.page = Page.page1;
          } else {
            this.$message({
              message: "至少选择一个选股因子",
              type: "warning",
            });
          }
          break;
        }
        case AlgorithmTypeEnum["non-conditional"]:
          if (this.selectedFactors.length > 0) {
            this.algorithm.factors = getFactorsData(
              this.factors,
              this.selectedFactors
            );
            this.page = Page.page2;
          } else {
            this.$message({
              message: "至少选择一个选股因子",
              type: "warning",
            });
          }
          break;
        default:
          this.$message({
            message: "请先选择算法类型",
            type: "warning",
          });
          break;
      }
    },
    handleAddTagText(text) {
      console.log(text)
      this.algorithm.logicDescription += text;
    },
    fetchData() {
      getFactorInfo().then((response) => {
        console.log("response", response);
        this.factors = response.data.data;
        // this.factors.forEach((item) => {
        //   item.isNumber = 0;
        //   item.rankNumber = 10;
        // });
        // this.list = response.data.items
        // this.listLoading = false
      });
    },
    handleChangeEditorContent(newContent) {
      // console.log("!!!",newContent);
      this.algorithm.logicDescription = newContent;
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style lang="scss" scoped>
$card-selected: hsl(212, 100%, 86%);

.page0 {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;

  @include text-base;
  .tutorial {
    margin: 20px;
    height: 50px;
    text-align: center;
    font-weight: bold;

    @include text-2xl;
  }
  .type-select-area {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin: 0 200px 20px 200px;
    .type-select-box{
      box-shadow: 1px 1px 1px 1px #ececec;
      display: flex;
      padding: 0.5rem;
      border-radius: 0.5rem;
      justify-content: space-between;
      align-items: center;
      width: 500px;
    }
    .card-button {
      width: 150px;
      min-height: 50px;
      cursor: pointer;

      @include text-base;
      &:hover {
        background-color: $card-selected;
        .card-title::after {
          content: "✅";
        }
      }
      .card-title {
        margin-bottom: 10px;
        font-weight: bold;
        text-align: center;

        @include text-lg;
      }

      .card-content {
        font-family: "Microsoft YaHei UI Light";
      }
    }
    .selected {
      background-color: $card-selected;
      .card-title::after {
        content: "✅";
      }
    }
  }
  .factor-select-area {
    margin: auto;
    width: 500px;
  }
  .step-area {
    display: flex;
    justify-content: space-around;
    align-items: center;
    height: 60px;
  }
}
.content-box{
  margin-top: 2rem;
}
.page1 {
  margin: auto;
  width: 600px;

  @include text-base;
  .algorithm-name-bar {
    margin-bottom: 10px;
    width: 300px;
    font-weight: bold;

    @include text-lg;
  }
  .step-bar {
    display: flex;
    justify-content: space-between;
    margin: auto;
    margin-top: 20px;
    width: 300px;
  }
}

.description-edit-area{
  margin-top: 2rem;
}
.page2 {
  margin: auto;
  width: 600px;

  @include text-base;
  .algorithm-name-bar {
    margin-bottom: 10px;
    width: 300px;
    font-weight: bold;

    @include text-lg;
  }
  .step-bar {
    display: flex;
    justify-content: space-between;
    margin: auto;
    margin-top: 20px;
    width: 300px;
  }
}
</style>
