
<!---->
<!--策略详细分析-->
<template>
<div>

  <div class="dashboard-container">
    <div class="dashboard-text">策略详细分析</div>
    <!--分割线-->
    <el-divider></el-divider>
  </div>


  <!--第一个容器 文字信息-->
  <!--第一个容器 文字信息-->
  <!--第一个容器 文字信息-->
  <!--第一个容器 文字信息-->
  <h1 class="header-title">{{ title }}</h1>
  <el-container class="basic-outline container-margin">
  <!--文字信息 标题-->
  
  <el-main>
  <div style="display: flex; justify-content: space-between;">
    <div>
      <el-descriptions 
      class="margin-top" 
      :column="2" 
      :size="size" 
      :contentStyle="CS" :label-style="LS ">
        <el-descriptions-item label="作者">kooriookami</el-descriptions-item>
        <el-descriptions-item label="调仓时间">14:30</el-descriptions-item>
        <el-descriptions-item label="风格">小盘</el-descriptions-item>
        <el-descriptions-item label="创建日期">2019.01.01</el-descriptions-item>
        <el-descriptions-item label="调仓周期">一个交易日</el-descriptions-item>
        <el-descriptions-item label="下个调仓">2021.01.01</el-descriptions-item>
        <el-descriptions-item label="回测日期">至2021.01.01</el-descriptions-item>
        <el-descriptions-item label="持仓股票">2只</el-descriptions-item>
      </el-descriptions>
    </div>
    <div>
     
      <el-descriptions 
      class="margin-top" 
      :column="1" 
      direction="vertical"
      :contentStyle="CS" :label-style="LS ">
        <el-descriptions-item label="策略描述">
          容易爆发的高收益策略
        </el-descriptions-item>
      </el-descriptions>
      <el-divider direction="vertical"></el-divider>
    </div>
    <div id="radar-chart" style="width: 300px; height: 300px; ">
    </div>
  </div>
  </el-main>
  
  </el-container>
  


  <!--第二个容器 回测结果图表-->
  <!--第二个容器 回测结果图表-->
  <!--第二个容器 回测结果图表-->
  <!--第二个容器 回测结果图表-->
  <el-container class="basic-outline container-margin">
  <el-header class='second-header'>
    <el-menu :default-active="activeIndex" class="el-menu-stat" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1" @click.native="showProfitStat">收益统计</el-menu-item>
      <el-menu-item index="2" @click.native="showTradeStat">交易统计</el-menu-item>
    </el-menu>
    <div class="ProfitStat" v-if="selectedStat === 'profit'">
      <table-stat-profit :tableData="tableStatProfitData"/>
    </div>
    <div class="TradeStat" v-if="selectedStat === 'trade'">
      <table-stat-trade :tableData="tableStatTradeData"/>
    </div>
  </el-header>
  <!--下面是main-->
  <el-main>
    <el-menu :default-active="activeIndex" class="el-menu-chart" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1" @click.native="showChartA" >收益曲线</el-menu-item>
      <el-menu-item index="2" @click.native="showChartB">收益分布</el-menu-item>
      <el-menu-item index="3" @click.native="showChartC">最大回撤分布</el-menu-item>
      <el-menu-item index="4" @click.native="showChartD">换手率分布</el-menu-item>
      <el-menu-item index="5" @click.native="showChartE">年度收益统计</el-menu-item>
      <el-menu-item index="6" @click.native="showChartF">月度收益统计</el-menu-item>
      <el-menu-item index="7" @click.native="showChartG">收益周期统计</el-menu-item>
    </el-menu>
    <!--A.收益曲线折线图-->
    <div class="ChartA" v-if="chartToShow === 'a'">
      <chart-a :chartData="chartAData" />
    </div>
    <!--B.收益分布图-->
    <div class="ChartB" v-if="chartToShow === 'b'">
      <chart-b :chartData="chartBData" />
    </div>
    <!--C.最大回撤分布-->
    <div class="ChartC" v-if="chartToShow === 'c'">
      <chart-c :chartData="chartCData" />
    </div>
    <!--D.换手率分布-->
    <div class="ChartD" v-if="chartToShow === 'd'">
      <chart-d :chartData="chartDData" />
    </div>
    <!--E.年度收益统计 表格-->
    <div class="ChartE" v-if="chartToShow === 'e'">
      <chart-e :tableData="chartEData" />
    </div>
    <!--F.月度收益统计 表格-->
    <div class="ChartF" v-if="chartToShow === 'f'">
      <chart-f :tableData="chartFData"/>
    </div>
    <!--G.收益周期统计-->
    <div class="ChartG" v-if="chartToShow === 'g'">
      <chart-g :chartData="chartGData"/>
    </div>



  </el-main>
  </el-container>
  
  <!--第三个容器 操作-->
  <!--第三个容器 操作-->
  <!--第三个容器 操作-->
  <!--第三个容器 操作-->
  <el-container>
   <!--只有main没有header-->
  <el-main class="basic-outline container-margin">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1" @click.native="showHoldings">持仓股票</el-menu-item>
      <el-menu-item index="2" @click.native="showInstruction">调仓指令</el-menu-item>
      <el-menu-item index="3" @click.native="showHistory" >调仓记录</el-menu-item>
      <!--这一步展示历史指令记录&下一指令-->
    </el-menu>
    <div class="TableHoldings" v-if= "tableToShow === 'holdings'">
      <table-holdings :tableData="tableHoldingsData" />
    </div>
    <div class="TableInstruction" v-if="tableToShow === 'instruction'">
      <table-instruction :tableData="tableInstructionData" />
    </div>
    <div class="TableHistory" v-if="tableToShow === 'history'">
      <table-history :tableData="tableHistoryData" />
    </div>


  </el-main>
  </el-container>
    

</div>



</template>

<!--script-->
<!--script-->
<!--script-->
<!--script-->
<!--script-->
<script>
import { mapGetters } from 'vuex';

import ChartRadar from "./components/ChartRadar";
import DescriptionA from "./components/DescriptionA";
import DescriptionB from "./components/DescriptionB";
import TableStatProfit from "./components/TableStatProfit";
import TableStatTrade from "./components/TableStatTrade";
import ChartA from "./components/ChartA";
import ChartB from "./components/ChartB";
import ChartC from "./components/ChartC";
import ChartD from "./components/ChartD";
import ChartE from "./components/ChartE";
import ChartF from "./components/ChartF";
import ChartG from "./components/ChartG";
import TableHoldings from "./components/TableHoldings";
import TableInstruction from "./components/TableInstruction";
import TableHistory from "./components/TableHistory";

export default {

  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  components: {
    ChartRadar,
    DescriptionA,
    DescriptionB,
    TableStatProfit,
    TableStatTrade,
    ChartA,
    ChartB,
    ChartC,
    ChartD,
    ChartE,
    ChartF,
    ChartG,
    TableHoldings,
    TableInstruction,
    TableHistory,
  },

  data() {
  return {
    descripAData:{
      author:'author',
      adjustTime:'14:30',
      style:'1',
      foundDate:'1',
      adjustPeriod:'1',
      adjustDateNext:'1',
      backtest:'1',
      holdNum:'1',
    },
    tableStatProfitData:[
      {
        group:'本策略',
        yearYield:'131.66%',
        sharpeRatio:'3.27',
        maxDrawdown:'32.78%',
        volatility:'34.13%',
        infoRatio:'4.73',
        beta:'0.93',
        alpha:'106.61%',
        days:'188',
        yieldSinceInception:'101.14%',
      },
      {
        group:'中证1000',
        yearYield:'9.55%',
        sharpeRatio:'0.2',
        maxDrawdown:'79.35%',
        volatility:'27.76%',
        infoRatio:'-',
        beta:'-',
        alpha:'-',
        days:'-',
        yieldSinceInception:'4.85%',
      }
    ],
    tableStatTradeData:[
      {
        annualTurnoverRate:'1393.24%',
        averageProceed:'7.09%',
        winRate:'69.22%',
        winRateMonthAve:'76.16%',
        exchangeNum:'789',
        holdNum:'3.95',
      }
    ],
    chartAData: {
      time: [
        '20210101','20210102','20210103','20210104','20210105','20210106','20210107', 
        '20210108','20210109','20210110','20210111','20210112','20210113','20210114',
        '20210115','20210116','20210117','20210118','20210119','20210121','20210121',
        '20210122','20210123','20210124','20210125','20210126','20210127','20210128',
        '20210129','20210130','20210131','20210201','20210202','20210203','20210204',
        '20210205','20210206','20210207','20210208','20210209','20210210','20210211',
      ],
      shanghaiIndex: [
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
        0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
      ],
      strategyProfit: [
        0, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
        0.013, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
        0.013, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
        0.013, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
        0.013, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
        0.013, -0.287, -0.077, -0.008, -0.060, -0.178, -0.210, -0.127,
      ],
    },
    chartBData: {
      time: [
        '2009','2010','2011','2012','2013', 
        '2014','2015','2016','2017','2018',
        '2019','2020','2021','2022','2023'],
      shanghaiIndex: [
        1.2870,0.1704,-0.3296,0.3159,-0.2576,
        1.2870,0.1704,-0.3296,0.3159,-0.2576,
        1.2870,0.1704,-0.3296,0.3159,-0.2576,
      ],
      strategyProfit: [
        2.8585, 0.7036, 0.0796, 0.5873, 0.6898, 
        2.8585, 0.7036, 0.0796, 0.5873, 0.6898, 
        2.8585, 0.7036, 0.0796, 0.5873, 0.6898, 
      ],
    },
    chartCData: {
      year: [
        '2009','2010','2011','2012','2013', 
        '2014','2015','2016','2017','2018',
        '2019','2020','2021','2022','2023'
      ],
      basedrawdown: [
        120, 111, 105, 60, 220, 
        218, 145, 26,111, 105,
        120,218,60,111,205,
      ],
      strategydrawdown: [
        20, 111, 105, 60, 20, 
        120, 111, 105, 60, 220, 
        218, 145, 26,111, 105,
      ],
    },
    chartDData: {
      date: [
        '20210101','20210102','20210103','20210104','20210105','20210106','20210107', 
        '20210108','20210109','20210110','20210111','20210112','20210113','20210114',
        '20210115','20210116','20210117','20210118','20210119','20210121','20210121',
        '20210122','20210123','20210124','20210125','20210126','20210127','20210128',
        '20210129','20210130','20210131','20210201','20210202','20210203','20210204',
        '20210205','20210206','20210207','20210208','20210209','20210210','20210211',
      ],
      turnoverrate: [
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
        0, 0.137, 0.087, 0.178, 0.130, 0.295, 0.227, 0.227,
      ],
    },
    chartEData:[
      {
        year:"2009",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        volatility:"26.93%",
        informationratio:"17.08",
        sharperatio:"7.06",

      },
      {
        year:"2010",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        volatility:"26.93%",
        informationratio:"17.08",
        sharperatio:"7.06",

      },
      {
        year:"2011",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        volatility:"26.93%",
        informationratio:"17.08",
        sharperatio:"7.06",

      },
    ],
    chartFData:[
      {
        month:"200901",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        informationratio:"17.08",
        volatility:"26.93%",
        sharperatio:"7.06",

      },
      {
        month:"200902",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        informationratio:"17.08",
        volatility:"26.93%",
        sharperatio:"7.06",

      },
      {
        month:"200903",
        strategyprofit:"285.5%",
        baseprofit:"128.7%",
        strategydrawdown:"16.33%",
        basedrawdown:"20.89%",
        informationratio:"17.08",
        volatility:"26.93%",
        sharperatio:"7.06",

      },
    ],
    chartGData: {
      positivenum: [144, 81, 51, 30, 9, 0, 1, 0],
      negativenum: [118, 55, 27, 9, 4, 2, 1, 0],
    },
    tableHoldingsData:[
      {
        no:'1',
        sharename:'直真科技',
        purchasedate:'2022-10-09',
        purchaseprice:'20.28',
        price:'19.55',
        scale:'78.25%',
        rate:'-5.73%',
      },
      {
        no:'2',
        sharename:'整合工业',
        purchasedate:'2022-12-17',
        purchaseprice:'10.28',
        price:'19.55',
        scale:'21.75%',
        rate:'97.73%',
      },
    ],
    tableInstructionData:[
      {
        no:'1',
        sharename:'整合工业',
        purchasedate:'2022-12-17',
        signal:'卖出',
        price:'19.55',
        scale:'100%',
      }
    ],
    tableHistoryData:[
      {
        no:'1',
        sharename:'天原股份',
        purchasedate:'2022-10-09',
        selldate:'2022-12-09',
        purchaseprice:'1.67',
        sellprice:'3.47',
        rate:'208.64%',
      },
      {
        no:'2',
        sharename:'真爱美佳',
        purchasedate:'2022-10-09',
        selldate:'2022-12-09',
        purchaseprice:'1.67',
        sellprice:'3.47',
        rate:'208.64%',
      },
      {
        no:'3',
        sharename:'泰坦股份',
        purchasedate:'2022-10-09',
        selldate:'2022-12-09',
        purchaseprice:'1.67',
        sellprice:'3.47',
        rate:'208.64%',
      },
    ],
    
    title: '4股小狮子牛牛板',
    size: '',
    activeIndex: '1',
    chartToShow: 'a',
    selectedStat: 'profit',
    tableToShow: 'holdings',
    CS: {  //标签列
      'text-align': 'left',  //左对齐
      'min-width': '200px',   //最小宽度
      'word-break': 'break-all',  //过长时自动换行
      'width': '100px'
    },
    LS: {  //内容列
      'color': '#000',
      'text-align': 'left',
      'font-weight': '600',
      'height': '40px',
      'word-break': 'break-all',
      //'background-color': 'rgba(0,155,226, 0.1)',
      'min-width': '100px',
      'word-break': 'keep-all',
      'width': '100px'
    }

    
  }
  },
  
  methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
        if (key === 'profit' || key === 'trade') {
          this.selectedStat = key;
        }
        else if(key ==='history'|| key ==='instruction'||key==='holdings'){
          this.tableToShow = key;
        }
        else if(key ==='e'|| key ==='f'){
          this.chartToShow = key;
        }
      },
      
      showChartA() {
        this.chartToShow = 'a';
      },
      showChartB() {
        this.chartToShow = 'b';
      },
      showChartC() {
        this.chartToShow = 'c';
      },
      showChartD() {
        this.chartToShow = 'd';
      },
      showChartE() {
        this.chartToShow = 'e';
      },
      showChartF() {
        this.chartToShow = 'f';
      },
      showChartG() {
        this.chartToShow = 'g';
      },
      showProfitStat() {
        this.selectedStat = 'profit';
      },
      showTradeStat() {
        this.selectedStat = 'trade';
      },
      showHoldings(){
        this.tableToShow = 'holdings';
      },
      showInstruction(){
        this.tableToShow = 'instruction';
      },
      showHistory(){
        this.tableToShow = 'history';
      },
      

  },


  //钩子函数
  mounted() {

    // 基于准备好的dom，初始化echarts实例
    var myChart = this.$echarts.init(document.getElementById('radar-chart'));

    // 指定图表的配置项和数据
    var option = {
      // ... 雷达图配置项
      title: {
      },
      legend: {
        data: ['Allocated Budget']
      },
      tooltip: {
        trigger: 'axis'
      },
      radar: {
        //shape: 'circle',
        indicator: [
        { name: '收益', color: "rgba(0, 0, 0, 1)", max: 100 },
        { name: '抗风险', color: "rgba(0, 0, 0, 1)", max: 100 },
        { name: '稳定性', color: "rgba(0, 0, 0, 1)", max: 100 },
        { name: '实盘', color: "rgba(0, 0, 0, 1)", ax: 100 },
        { name: '盈利能力', color: "rgba(0, 0, 0, 1)", max: 100 }
        ]       
      },
      name: {
        textStyle: {
          padding: [-10, -12]  // 控制文字padding
        }
      },

      radius:20,
      label:{
        position:'outer',
        alignTo:'edge',
        margin:10
      },
      series: [
      {
        name: 'Budget vs spending',
        type: 'radar',
      
        data: [
          {
            value: [80, 70, 80, 90, 65]
          }
        ],

        itemStyle: {     //此属性的颜色和下面areaStyle属性的颜色都设置成相同色即可实现线条色填充色更改

          color: '#4B7CF3',
          borderColor: '#4B7CF3',
        },
        areaStyle: {
          color: '#4B7CF3',
        }
      }
      ]
    };

    option && myChart.setOption(option);

    // 使用刚指定的配置项和数据显示'radar-chart'
    myChart.setOption(option);
    
  }

}

</script>





<!--css样式-->
<!--css样式-->
<!--css样式-->
<!--css样式-->
<!--css样式-->
<style lang="scss" scoped>

  //dashboard-container的margin属性被设置为30px
  //表示元素的外边距为30px
  .dashboard {
  	&-container {
    margin: 30px;
	  }
	  &-text {
    font-size: 30px;
    line-height: 46px;
  	}
  }
  
  //容器

  //所有容器的边框
  .basic-outline {
  border: 1px solid white;
  border-radius: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  }
  //所有容器的边界margin
  .container-margin {
  margin-left: 60px;
  margin-right: 60px;
  margin-bottom: 60px;
  } 
  //第一个容器的header标题
  //设置间距字体什么的
  .header-title {
    margin-left: 60px;
    margin-bottom: 40px;
    font-family: "Hiragino Sans GB";
    font-size: 28px;
    font-weight: normal;
  }
  
  .el-header {
    height: auto !important;
  }
  
  </style>
