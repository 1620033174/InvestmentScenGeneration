const todoAlgorithms = Array.from({ length: 5 }, (_, idx) => ({
  id: "" + (idx + 1), // 不显示,但用于操作
  name: "算法" + (idx + 1),
  author: "- -", // 一个普通的人名或者null,null表示未编辑时,显示为--
  lastModifiedTime: "- -", //后端处理,为一个时间或者是null,null表示还未编辑过,显示为--
}));

export { todoAlgorithms };
