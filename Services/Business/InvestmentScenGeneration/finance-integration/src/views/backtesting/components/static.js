const algorithms =
  //  [
  //   { id: "1", name: "算法1" },
  //   { id: "2", name: "算法2" },
  //   { id: "3", name: "算法3" },
  // ];
  new Array(10).fill("").map((cur, idx) => ({
    id: "" + idx,
    name: "算法" + idx,
  }));

export { algorithms };
