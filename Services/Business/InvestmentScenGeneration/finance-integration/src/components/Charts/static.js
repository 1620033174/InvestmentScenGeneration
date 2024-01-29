function getDayAll(starDay, endDay) {
  const arr = [];
  const dates = [];
  // 设置两个日期UTC时间
  const db = new Date(starDay);
  const de = new Date(endDay);
  // 获取两个日期GTM时间
  const s = db.getTime() - 24 * 60 * 60 * 1000;
  const d = de.getTime() - 24 * 60 * 60 * 1000;

  // 获取到两个日期之间的每一天的毫秒数
  for (let i = s; i <= d; ) {
    i = i + 24 * 60 * 60 * 1000;
    arr.push(parseInt(i));
  }

  // 获取每一天的时间  YY-MM-DD
  for (const j in arr) {
    const time = new Date(arr[j]);
    const year = time.getFullYear(time);
    const mouth =
      time.getMonth() + 1 >= 10
        ? time.getMonth() + 1
        : "0" + (time.getMonth() + 1);
    const day = time.getDate() >= 10 ? time.getDate() : "0" + time.getDate();
    const YYMMDD = year + "-" + mouth + "-" + day;
    dates.push(YYMMDD);
  }

  return dates;
}
const colors = ["#aa4643", "#4572a7", "#4c9155"];
// const today = new Date();
// const yesterday = [
//   today.getFullYear(),
//   today.getMonth() + 1,
//   today.getDate() + 1,
// ].join("-");
// const dataAll = getDayAll("2023-01-01", yesterday);

const dataZ = [
  172.50999451,
  172.52000427,
  175.8999939,
  176.05999756,
  175.49000549,
  173.74000549,
  173.25,
  174.33000183,
  175.71000671,
  177.03999329,
  177.41999817,
  178.38000488,
  179.63999939,
  179.8999939,
  180.38999939,
  181.77000427,
  182.69999695,
  183.8999939,
  ,
  184.69000244,
  185.46000671,
  186.11999512,
  187.44000244,
  188.47000122,
  189.55999756,
  190.63000488,
  191.83000183,
  192.8999939,
  193.99000549,
  195.16999817,
  196,
  197.08000183,
  198.11000061,
  199.21000671,
  200.38999939,
  201,
  202.08000183,
  203.21000671,
  204.38999939,
].map((cur) => cur - 150);
const dataY = [
  1136.81994629, 1068.89001465, 1070.70996094, 1084.86999512, 1073.90002441,
  1068.43005371, 1077.32995605, 1078.58996582, 1095.17004395, 1105.25,
  1119.17004395, 1123.43005371, 1131.92004395, 1144.20996094, 1155.97998047,
  1169.89001465, 1186.89001465, 1190.80004883, 1206.19995117, 1216.32995605,
  1227.43005371, 1238.17004395, 1249.30004883, 1260.80004883, 1272.68005371,
  1284, 1295.80004883, 1307.92004395, 1320.30004883,
].map((cur) => cur - 1000);
const dataX = [
  20, 17, -4, 11, 0, 6, 15, -3, -6, -30, -23, -9, -13, -30, -30, -27, -3, -25,
  -22, -10, -27, -30, -30, -30, -24, -30, -25, -30, -30, -27, -30, -6, -29, -22,
  -29, -16, 3, 21, 4, -1, 24, 29, 46, 59, 54, 30, 5, 25, 44, 50,
].map((cur) => cur * 2);

const demoChartInput = {
  // dates: getDayAll(...["2023-01-01", new Date(2023, 1, 19)]),
  dates: [
    "2023-01-03",
    "2023-01-04",
    "2023-01-05",
    "2023-01-06",
    "2023-01-09",
    "2023-01-10",
    "2023-01-11",
    "2023-01-12",
    "2023-01-13",
    "2023-01-16",
    "2023-01-17",
    "2023-01-18",
    "2023-01-19",
    "2023-01-20",
    "2023-01-30",
    "2023-01-31",
    "2023-02-01",
    "2023-02-02",
    "2023-02-03",
    "2023-02-06",
    "2023-02-07",
    "2023-02-08",
    "2023-02-09",
    "2023-02-10",
    "2023-02-13",
    "2023-02-14",
    "2023-02-15",
    "2023-02-16",
    "2023-02-17",
    "2023-02-20",
    "2023-02-21",
    "2023-02-22",
    "2023-02-23",
    "2023-02-24",
    "2023-02-27",
    "2023-02-28",
    "2023-03-01",
    "2023-03-02",
    "2023-03-03",
    "2023-03-06",
    "2023-03-07",
    "2023-03-08",
    "2023-03-09",
    "2023-03-10",
    "2023-03-13",
    "2023-03-14",
    "2023-03-15",
    "2023-03-16",
    "2023-03-17",
    "2023-03-20",
    "2023-03-21",
    "2023-03-22",
    "2023-03-23",
    "2023-03-24",
    "2023-03-27",
    "2023-03-28",
    "2023-03-29",
    "2023-03-30",
    "2023-03-31",
    "2023-04-03",
    "2023-04-04",
    "2023-04-06",
    "2023-04-07",
    "2023-04-10",
    "2023-04-11",
    "2023-04-12",
    "2023-04-13",
    "2023-04-14",
    "2023-04-17",
    "2023-04-18",
    "2023-04-19",
    "2023-04-20",
    "2023-04-21",
    "2023-04-24",
    "2023-04-25",
    "2023-04-26",
    "2023-04-27",
    "2023-04-28",
    "2023-05-04",
    "2023-05-05",
    "2023-05-08",
    "2023-05-09",
    "2023-05-10",
    "2023-05-11",
    "2023-05-12",
    "2023-05-15",
    "2023-05-16",
  ],
  lines: [
    { name: "A", data: dataX },
    { name: "B", data: dataY },
    { name: "C", data: dataZ },
  ],
};
const getLineChartOption = (chartInput) => {
  const lineChartOption = {
    //   title: {
    //     text: "账户收益",
    //     left: "center",
    //   },
    title: {
      text: "最大收益分布",
      left: "center"
    },
    legend: {
      // 图例相关
      data: chartInput.lines.map((cur) => cur.name), // STAR
      inactiveColor: "#777",
      textStyle: {
        color: "black",
      },
      x: 'left',
      orient: 'vertical',
    },
    tooltip: {
      trigger: "axis",
    },
    xAxis: {
      boundaryGap: false,
      axisLine: {
        onZero: false,
      },
      axisTick: {
        show: true,
      },
      data: chartInput.dates,
    },
    yAxis: {
      splitNumber: 6,
      axisLabel: {
        formatter: "{value} %",
      },
      axisLine: {
        show: false,
      },
      axisTick: {
        show: false,
      },
    },
    series: chartInput.lines.map((cur, idx) => {
      const item = {
        type: "line",
        name: cur.name,
        showSymbol: false,
        color: colors[idx],
        data: cur.data,
      };
      return item;
    }),
    grid: {
      top: '30%',
      left: '2%',
      right: '2%',
      bottom: '12%',
      containLabel: true
    },
    dataZoom: [
      {
        type: "inside",
        textStyle: {
          color: "#8392A5",
        },
        handleSize: "80%",
        dataBackground: {
          areaStyle: {
            color: "#8392A5",
          },
          lineStyle: {
            opacity: 0.8,
            color: "#8392A5",
          },
        },
        handleStyle: {
          color: "#fff",
          shadowBlur: 3,
          shadowColor: "rgba(0, 0, 0, 0.6)",
          shadowOffsetX: 2,
          shadowOffsetY: 2,
        },
      },
      {
        type: "slider",
      },
    ],
  };
  return lineChartOption;
};

export { getLineChartOption, demoChartInput };
