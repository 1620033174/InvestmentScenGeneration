<template>
  <div>
    <div v-if="algorithm.id != '0'"><b>算法id:</b> {{ algorithm.id }}</div>
    <div><b>算法名称:</b> {{ algorithm.name }}</div>
    <div>
      <b>算法描述:</b>
      {{ algorithm.algorithmDescription }}
    </div>
    <div>
<!--      <b>选股描述: </b>-->
<!--      <ul style="margin: 0" v-if="algorithm.type === '1'">-->
<!--        <li v-for="factor in algorithm.factors" :key="factor.id">-->
<!--          因子：{{ factor.name }}，{{ factor.logic}}&ensp;{{factor.defaultValue}}-->
<!--        </li>-->
<!--      </ul>-->
<!--      <span-->
<!--        v-if="algorithm.type === '2'"-->
<!--        v-html="algorithm.logicDescription"-->
<!--      ></span>-->
      <el-table max-height="400"
                :data="tableData"
                :row-class-name="tableRowClassName">
        <el-table-column
          label="排名"
        >
          <template #default = "scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column
          prop="code"
          label="股票代码"
        ></el-table-column>
        <el-table-column v-for="item in columns"
                         :label="item.label"
                         :key="item.prop"
                         :prop="item.prop"
        >
        </el-table-column>
      </el-table>
    </div>
    <div>
<!--      <div style="display: flex; gap: 10px; flex-wrap: wrap">-->
<!--        <b>选股因子:</b>-->
<!--        <el-tag v-for="factor in algorithm.factors" :key="factor.id">{{-->
<!--          factor.name-->
<!--        }}</el-tag>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script>
import factor from "../../views/factor/index.vue";
import {algorithms} from "@/views/backtesting/components/static";

export default {
  computed: {
    factor() {
      return factor
    },
    algorithmNew() {
      return this.algorithm
    }
  },
  watch: {
   algorithmNew: {
     handler: function (val, oldVal) {
       this.algorithm.factors.forEach(factor => {
         if (factor.logic === 'higherequal') {
           factor.logic = '大于等于'
         } else if (factor.logic === 'lowerequal') {
           factor.logic = '小于等于'
         } else if (factor.logic === 'higher') {
           factor.logic = '大于'
         } else if (factor.logic === 'lower') {
           factor.logic = '小于'
         } else if (factor.logic === 'equal') {
           factor.logic = '等于'
         } else {
           factor.logic = '大于'
         }
       });

     },
     deep: true
   }
  },
  mounted() {
    this.algorithm.factors.forEach(factor => {
      if(factor.logic === 'higherequal'){
        factor.logic = '大于等于'
      }else if(factor.logic === 'lowerequal'){
        factor.logic = '小于等于'
      }else if(factor.logic === 'higher'){
        factor.logic = '大于'
      }else if(factor.logic === 'lower'){
        factor.logic = '小于'
      } else if(factor.logic === 'equal'){
        factor.logic = '等于'
      } else{
        factor.logic = '大于'
      }
    });
  },
  props: {
    algorithm: {
      type: Object,
      required: true,
      // TODO:默认数据为测试用数据,待剔除
      // default: function () {
      //   return {
      //     id: "0",
      //     name: "高送转预期",
      //     algorithmDescription:
      //       "高送转股票（简称：高送转）是指送红股或者转增股票的比例很大。实质是股东权益的内部结构调整，对净资产收益率没有影响，对公司的盈利能力也并没有任何实质性影响。",
      //     logicDescription: "",
      //     type: "",
      //     factors: [
      //       { id: "1", name: "总市值", description: "总市值<==300亿" },
      //       {
      //         id: "2",
      //         name: "每股收益",
      //         description: "每股收益>=2",
      //       },
      //       {
      //         id: "3",
      //         name: "每股净资产",
      //         description: "每股净资产>1.5",
      //       },
      //     ],
      //   };
      // },
    },
    columns: {
      required: true,
    },
    tableData: {
      required: true,
    },
  },
  // id为0 | "0"时为新增
  data() {
    return {};
  },
  methods: {
    tableRowClassName({row, rowIndex}) {
      console.log(rowIndex)
      if (rowIndex < 10) {
        return 'success-row';
      }
      return '';
    }
  },
};
</script>

<style >
.el-table .success-row {
  background: #f0f9eb;
}
</style>
