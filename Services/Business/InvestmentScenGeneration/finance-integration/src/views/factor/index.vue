<template>
  <div class="card-container">
    <el-card class="card">
      <el-container>
      <el-header>因子详情</el-header>
      <el-main>
        <el-table
        :data="tableData"
        height="124"
        border
        style="width: 100%">
        <el-table-column
          prop="label"
          label="因子名称"
          width="200">
        </el-table-column>
        <el-table-column
          prop="cumulativeIncome"
          label="缺失百分比">
        </el-table-column>
        <el-table-column
          prop="AnnualizedIncome"
          label="方差">
        </el-table-column>
        <el-table-column
          prop="overAnnualizedIncome"
          label="频度">
        </el-table-column>
        <el-table-column
          prop="benchmarkAnnualizedIncome"
          label="与场景主题相关性">
        </el-table-column>
        </el-table>
      <el-main>
      <facePop></facePop>
      </el-main>
      <el-main>
        <el-row>
        <el-col :span="12">
        <el-card class="box-card1">
          <el-container>
          <el-header>标准差，均值</el-header>
          <el-main>
          <ICdiagram></ICdiagram>
          </el-main>
          </el-container>
        </el-card>
        </el-col>

        <el-col :span="12">
        <el-card class="box-card2">
          <el-container>
          <el-header>方差</el-header>
          <el-main>
          <turnoverRate></turnoverRate>
          </el-main>
          </el-container>
        </el-card>
        </el-col>
        </el-row>
      </el-main>

      <el-main>
        <el-row>
          <el-col :span="12">
          <el-card class="box-card1">
          <el-container>
          <el-header>因子值最大的20只股票</el-header>
          <el-main>
          <el-table
            :data="tableData1"
            style="width: 100%">
            <el-table-column
              prop="ID"
              label="股票代码"
              width="120">
            </el-table-column>
            <el-table-column
              prop="name"
              label="股票名称"
              width="140">
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
            <el-header>因子值最小的20只股票</el-header>
            <el-main>
              <el-table
                :data="tableData2"
                style="width: 100%">
                <el-table-column
                  prop="ID"
                  label="股票代码"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="name"
                  label="股票名称"
                  width="140">
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
</template>

<style>
  .el-header, .el-footer {
    background-color: #E9EEF3;
    color: #333;
    text-align: left;
    line-height: 60px;
  }
  .el-main {
    background-color: #FFFFFF;
    color: #333;
    text-align: left;
    line-height: 50px;
  }
  body > .el-container {
    margin-bottom: 60px;
  }

  .card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  }

.card {
  max-width: 1000px;
  width: 100%;
}
</style>

<script>
import facePop from './components/facePop';     //1、引入组件 import后的名字一般与组件名称相同，也可不一样
import ICdiagram from './components/ICdiagram';
import turnoverRate from './components/turnoverRate';
  export default {
    data() {
      return {
        tableData1: [{
          ID:'600064.XSHG',
          name:'南京高科',
          value:'0.866427'
        },{
          ID:'002192.XSHE',
          name:'融捷股份',
          value:'0.831331'
        }],
        tableData2: [{
          ID:'000540.XSHE',
          name:'中天金融',
          value:'-1.988439'
        },{
          ID:'688256.XSHG',
          name:'寒武纪',
          value:'-1.551533'
        }],
        tableData: [{
          label:'心里线指标',
          cumulativeIncome:'3.09%',
          AnnualizedIncome:'1.05%',
          overAnnualizedIncome:'-6.19%',
          benchmarkAnnualizedIncome:'7.72%',
          MaximumFallback:'32.35%',
          sharpeRatio:'-0.14',
          TurnoverRate:'3.03%'
        }],
        options1: [{
          value1: '选项1',
          label: '纯多头组合'
        }, {
          value1: '选项2',
          label: '多空组合Ⅰ'
        }, {
          value1: '选项3',
          label: '多空组合Ⅱ'
        }],
        value1: '',
        options2: [{
          value2: '选项1',
          label: '沪深300'
        }, {
          value2: '选项2',
          label: '中证500'
        }, {
          value2: '选项3',
          label: '中证800'
        }, {
          value2: '选项4',
          label: '中证1000'
        }, {
          value2: '选项5',
          label: '中证全指'
        }],
        value2: '',
        options3: [{
          value3: '选项1',
          label: '近三个月'
        }, {
          value3: '选项2',
          label: '近一年'
        }, {
          value: '选项3',
          label: '近三年'
        }, {
          value3: '选项4',
          label: '近十年'
        }],
        value3: '',
        options4: [{
          valu4e: '选项1',
          label: '是'
        }, {
          value4: '选项2',
          label: '否'
        }],
        value4: '',
        options5: [{
          value5: '选项1',
          label: '无滑点'
        }, {
          value5: '选项2',
          label: '3‰佣金+1‰印花税+无滑点'
        }, {
          value5: '选项3',
          label: '3‰佣金+1‰印花税+1‰滑点'
        }],
        value5: ''
      }
    },
    components:{
     facePop,   //2、注册组件 一般直接取一个名字  即：facePop：facePop
     ICdiagram,
     turnoverRate
    }
  }
</script>

