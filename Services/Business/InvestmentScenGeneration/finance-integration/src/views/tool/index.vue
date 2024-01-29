<template>
  <div id="tool-page-box">
  <div class="card-container" v-show="page === 1">
  <el-card class="card" >
    <el-container>
        <el-header><div class="header-text">数据集选择</div></el-header>
        <el-main>
        <el-form  label-width="140px" >
            <el-form-item label="股票池：">
                <el-select v-model="stockPool">
                  <el-option
                    v-for="item in stockPoolOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-form-item>


            <!--
              <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              -->
            <el-form-item label="回测周期：">
              <el-select v-model="stockYear">
                <el-option
                  v-for="item in stockYearOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="场景主题：">
                <el-select v-model="Theme">
                  <el-option
                    v-for="item in themeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-form-item>
           <!--
            <el-form-item label="手续费及滑点：">
               <el-radio-group v-model="radio4">
                <el-radio :label="1">无</el-radio>
                <el-radio :label="2">3‰佣金+1‰印花税+无滑点</el-radio>
                <el-radio :label="3">3‰佣金+1‰印花税+1‰滑点</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="过滤涨停及停牌股：">
               <el-radio-group v-model="radio5">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="2">否</el-radio>
                </el-radio-group>
            </el-form-item>
             -->

        </el-form>
        </el-main>
    </el-container>

    <el-container>
        <el-header>
          <div class="header-text">
          因子选择
        </div>
        </el-header>
            <el-main>
            <el-form  label-width="120px" >
            <el-form-item label="选择因子：">
             <el-col :span="6">
                <div class="block">
                  <span class="demonstration"></span>
                  <el-cascader
                    placeholder="请选择"
                    ref="xzCascader"
                    :options="options1"
                    :props="{ multiple: true }"
                    filterable
                    @change="handleMallCatergoryChange"
                    :show-all-levels="false"
                    collapse-tags
                    collapse-tags-tooltip
                    v-model="factorValues"
                    clearable>
                  </el-cascader>
                </div>
              </el-col>
               <!-- <el-col :span="8">
                  <el-button type="primary">生成因子分析</el-button>
              </el-col>
              -->
              <el-col :span="8">
                  <el-button type="primary" @click="handleGetTable">生成因子分析</el-button>
              </el-col>
              <!-- @change = "handleChange"
              <el-col :span="4">
                  <el-button type="primary">添加因子</el-button>
              </el-col>
              <el-col :span="4">
                  <el-button type="primary" @click="dialogFormVisible = true">新增因子</el-button>
                  <el-dialog title="新增因子" :visible.sync="dialogFormVisible">
                    <el-form :label-position="labelPosition" label-width="140px" :model="formLabelAlign">
                      <el-form-item label="因子名称：">
                      <el-input v-model="input1" placeholder="请输入内容"></el-input>
                      </el-form-item>

                      <el-form-item label="因子类别：">
                      <el-input v-model="input2" placeholder="请输入内容"></el-input>
                      </el-form-item>

                      <el-form-item label="计算公式：">
                      <el-input v-model="input3" placeholder="请输入内容"></el-input>
                      </el-form-item>

                      <el-form-item label="更新时间：">
                      <el-input v-model="input4" placeholder="请输入内容"></el-input>
                      </el-form-item>

                      <el-form-item label="数据处理：">
                      <el-input v-model="input5" placeholder="请输入内容"></el-input>
                      </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                    </div>
                  </el-dialog>
              </el-col>
              <el-col :span="4">
                  <el-button type="primary">相关性分析</el-button>
              </el-col>

              <el-col :span="4">
                  <el-button type="primary" @click="dialogVisible = true">生成因子集</el-button>
                  <el-dialog
                    title="提示"
                    :visible.sync="dialogVisible"
                    width="30%"
                    :before-close="handleClose">
                    <span>是否确认生成因子集</span>
                    <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
                    </span>
                  </el-dialog>
              </el-col>
              -->

            </el-form-item>

            <el-form-item label="单因子分析：">
            <el-table
                :data="tableData1"
                height="460"
                style="width: 100%">

                <el-table-column
                fixed=left
                prop="factorname"
                width="150"
                label="因子名称">
                </el-table-column>

                <el-table-column
                fixed=left
                prop="missing"
                width="120"
                label="缺失百分比">
                </el-table-column>

                <el-table-column
                fixed=left
                prop="variance"
                width="120"
                label="方差">
                </el-table-column>

                <el-table-column
                fixed=left
                prop="standard_deviation"
                width="120"
                label="标准差">
                </el-table-column>

                <el-table-column
                fixed=left
                prop="correlation"
                width="150"
                label="与场景主题相关性">
                </el-table-column>

                <el-table-column
                fixed
                label="操作"
                width="137">

                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="HandleShowDetail(scope.row)">查看</el-button>
                </template>

                </el-table-column>

            </el-table>
            </el-form-item>

          <el-form-item label="因子间相关性：">
            <el-table
                :data="tableData2"
                max-height="300"
                style="width: 100%"
            >

                <el-table-column
                fixed = left
                prop="factorName"
                width="150"
                label="因子名称">
                </el-table-column>

                <el-table-column
                  v-for="(item, index) in tableFactors"
                  :key="index"
                :prop="item[1]"
                :label="item[0]">
                </el-table-column>

<!--                <el-table-column-->
<!--                fixed=left-->
<!--                prop="maxYearIncome"-->
<!--                width="120"-->
<!--                label="销售净利率">-->
<!--                </el-table-column>-->

<!--                <el-table-column-->
<!--                fixed=left-->
<!--                prop="mixTurnoverRate"-->
<!--                width="120"-->
<!--                label="成交量比率">-->
<!--                </el-table-column>-->

<!--                <el-table-column-->
<!--                fixed=left-->
<!--                prop="maxTurnoverRate"-->
<!--                width="150"-->
<!--                label="平均换手率">-->
<!--                </el-table-column>-->

<!--                <el-table-column-->
<!--                fixed=left-->
<!--                prop="maxTurnoverRate"-->

<!--                label="意愿指标">-->
<!--                </el-table-column>-->

            </el-table>
            </el-form-item>

        </el-form>
        </el-main>
    </el-container>
    </el-card>
  </div>

  <div class="card-container" v-if="page === 2">
    <el-card class="card">
      <el-container>
        <el-header>
          <div class="header-text">
            因子详情
          </div>
        </el-header>
        <el-table
        :data="tableDataDetail"
        max-height="150"
        style="width: 100%">
        <el-table-column
          prop="factorname"
          label="因子名称"
          width="200">
        </el-table-column>
        <el-table-column
          prop="missing"
          label="缺失百分比">
        </el-table-column>
        <el-table-column
          prop="variance"
          label="方差">
        </el-table-column>
        <el-table-column
          prop="standard_deviation"
          label="标准差">
        </el-table-column>
        <el-table-column
          prop="correlation"
          label="与场景主题相关性">
        </el-table-column>
      </el-table>
        <el-main>
          <el-main>
            <facePop :stock-pool="stockPool"
                     :stock-year="stockYear"
                     :factorname="factorname"
                      ></facePop>
          </el-main>
          <el-main>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="box-card1">
                  <el-container>
                    <el-header>
                      <div class="header-text">
                        标准差、均值
                      </div>
                    </el-header>
                    <el-main style="width: 100%">
                      <ICdiagram :stock-pool="stockPool"
                                 :stock-year="stockYear"
                                 :factorname="factorname"
                                 :theme="Theme"
                                 >

                      </ICdiagram>
                    </el-main>
                  </el-container>
                </el-card>
              </el-col>

              <el-col :span="12">
                <el-card class="box-card2">
                  <el-container>
                    <el-header>
                      <div class="header-text">
                        方差
                      </div>
                    </el-header>
                    <el-main>
                      <turnoverRate
                        :stockPool="stockPool"
                        :stockYear="stockYear"
                        :factorname="factorname"
                        :theme="Theme"
                      >
                      </turnoverRate>
                    </el-main>
                  </el-container>
                </el-card>
              </el-col>
            </el-row>
          </el-main>

          <el-main>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="box-card1">
                  <el-container>
                    <el-header>
                      <div class="header-text">
                        因子值最大的20只股票
                      </div>
                    </el-header>
                    <el-main>
                      <el-table
                        max-height="800"
                        :data="tableDataMax"
                        style="width: 100%">
                        <el-table-column
                          prop="code"
                          label="股票代码">
                        </el-table-column>
                        <el-table-column
                          prop="stock_name"
                          label="股票名称">
                        </el-table-column>
                        <el-table-column
                          prop="value"
                          label="因子值">
                        </el-table-column>
                      </el-table>
                    </el-main>
                  </el-container>
                </el-card>
              </el-col>

              <el-col :span="12">
                <el-card class="box-card1">
                  <el-container>
                    <el-header>
                      <div class="header-text">
                        因子值最小的20只股票
                      </div>
                    </el-header>
                    <el-main>

                      <el-table
                        max-height="800"
                        :data="tableDataMin"
                        style="width: 100%">
                        <el-table-column
                          prop="code"
                          label="股票代码"
                          >
                        </el-table-column>
                        <el-table-column
                          prop="stock_name"
                          label="股票名称"
                          >
                        </el-table-column>
                        <el-table-column
                          prop="value"
                          label="因子值">
                        </el-table-column>
                      </el-table>
                    </el-main>
                  </el-container>
                </el-card>
              </el-col>
            </el-row>
          </el-main>

        </el-main>
      </el-container>
    </el-card>
  </div>

</div>
</template>

<script>
import {
  getCorrelationFactors,
  getFactorInfo,
  getFactorsData,
  getMax,
  getMin,
} from "@/api/factor";
  import ICdiagram from "@/views/tool/components/ICdiagram.vue";
  import turnoverRate from "@/views/tool/components/turnoverRate.vue";
  import facePop from "@/views/tool/components/facePop.vue";

  export default {
    data() {
      return {
        page:1,
        stockPool:"沪深300",
        stockYear:"2020",
        Theme:"行业龙头",
        radio4:1,
        radio5:1,
        input1:'',
        input2:'',
        input3:'',
        input4:'',
        input5:'',
        checkList: ['选中且禁用','复选框 A'],
        addStockFackor: false,
        checkFactor:false,
        dialogVisible: false,
        deleteWarning:false,
        dialogFormVisible: false,
        stockPoolOptions:[
          {value:'沪深300', label: '沪深300' },
          {value:'中证500', label: '中证500' },
          {value:'中证800', label: '中证800' },
          {value:'中证1000', label: '中证1000' }
        ],
        stockYearOptions:[
          {value:'2020', label: '2020' },
          {value:'2021', label: '2021' },
          {value:'2022', label: '2022' },
          ],
        themeOptions: [
          {value:'行业龙头', label: '行业龙头' },
          {value:'高股息', label: '高股息' },
          {value:'高成长', label: '高成长' },
          {value:'行业龙头高股息', label: '行业龙头高股息' },
          ],

        tableData1: [],
        tableData2: [],
        tableFactors: [],
        // tableDataDetail:  [{
        //   label:'心里线指标',
        //   cumulativeIncome:'3.09%',
        //   AnnualizedIncome:'1.05%',
        //   overAnnualizedIncome:'-6.19%',
        //   benchmarkAnnualizedIncome:'7.72%',
        //   MaximumFallback:'32.35%',
        //   sharpeRatio:'-0.14',
        //   TurnoverRate:'3.03%'
        // }],
        tableDataDetail: [],
        tableDataMin:[],
        tableDataMax:[],
        options1: [],
        value: '',
        factors: [],
        factorname: '每股营业收入',
        factorValues: []
        // missing: '',
        // variance:'',
        // standard_deviation:'',
        // correlation:'',
      }
    },
    components: {
      ICdiagram,
      turnoverRate,
      facePop,
    },
    created() {
      this.handleGetFactorsInfo();
    },
    methods: {
      handleGetFactorsInfo(){
        console.log("hello");
        console.log(this.stockPool);
        getFactorInfo().then((response)=> {
          console.log(response.data);
          this.options1 = response.data.data;
        });
      },
      handleMallCatergoryChange () {
        if (this.options1.length != 0) {
          const textArr = [];
          let arr = this.$refs['xzCascader'].getCheckedNodes();
          console.log(this.$refs['xzCascader'].getCheckedNodes()[0].hasChildren);
          arr.forEach((i) => {
            if(i.hasChildren == false){
              textArr.push(i.pathLabels[1]);
            }
          });
          for (let i = 0; i < textArr.length; i++) {
            this.factorValues[i][0] = textArr[i];
          }
          this.factors = textArr
          console.log(this.factorValues)
        }
      },
      handleGetTable(){
        console.log(encodeURIComponent(this.factors));
        getFactorsData({
          stockPool:this.stockPool,
          stockYear:this.stockYear,
          Theme:this.Theme,
          factorsname:encodeURIComponent(this.factors)
        }).then((response)=> {
          console.log(response.data);
          this.tableData1 = response.data.data;
        });
        this.tableFactors = JSON.parse(JSON.stringify(this.factorValues))
        getCorrelationFactors({
          stockPool:this.stockPool,
          stockYear:this.stockYear,
          Theme:this.Theme,
          factorsname:encodeURIComponent(this.factors)
        }).then((response)=> {
          let data = response.data.data;
          let finalData = []
          for (let i = 0; i < data.length; i++) {
            let temp = data[i]
            let toObj = {}
            for (let j = 0; j < temp.length; j++) {
              Object.keys(temp[j]).forEach(key => {
                toObj[key] = temp[j][key]
              })

            }
            toObj.factorName = this.factorValues[i][0]
            finalData.push(toObj)
          }
          this.tableData2 = finalData
        });
      },
      HandleShowDetail(row){
        this.tableDataDetail = []
        // const tab = this.tableData1;
        // const tab1 = [];
        // console.log(tab);
        // for(let i = 0; i < tab.length; i++) {
        //   if(tab[i].factorname == row.factorname){
        //     tab1.push(tab[i]);
        //     this.factorname = row.factorname
        //   }
        // }
        // tab1.push(row)

        this.tableDataDetail.push(row);
        this.factorname = row.factorname
        console.log(this.factorname);
        //先获取
        this.page=2;
        // this.row_factorname = row.factorname;
        // this.row_missing = row.missing;
        // this.row_variance = row.variance;
        // this.row_standard_deviation = row.standard_deviation;
        // this.row_correlation = row.correlation;
        getMax({
          stockPool:this.stockPool,
          factorname:row.factorname,
        }).then((response)=> {
          // console.log(response.data);
          this.tableDataMax = response.data.data;
        });
        getMin({
          stockPool:this.stockPool,
          factorname:row.factorname,
        }).then((response)=> {
          // console.log(response.data);
          this.tableDataMin = response.data.data;
        });
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      handleChange(value) {
        console.log(this.$refs["options1"].getCheckedNodes()[0].pathLabels)
      },
      open1() {
        this.$notify({
          title: '删除',
          message: '删除成功',
          type: 'success'
        });
      }
    }
  }
</script>

<style scoped>
  #tool-page-box{
    height: 100%;
  }

  .el-header, .el-footer{
    background-color: #E9EEF3;
    color: #333;
    text-align: left;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 80px;
  }

  .el-main {
    background-color: #FFFFFF;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  body > .el-container {
    margin-bottom: 60px;
  }

  .card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
    width: 100%;
  }

.card {
  max-width: 1000px;
  width: 100%;
}

.el-cascader .el-input .el-input__inner:focus, .el-cascader .el-input.is-focus .el-input__inner{
	height: 33px;
}

.el-cascader__tags {
	display: inline-flex;
	margin-right: 60px;
	flex-wrap: nowrap;
}

.header-text{
  font-family: 'Microsoft YaHei UI';
  font-weight: bold;
}
.el-main{
  line-height: normal;
}
</style>
