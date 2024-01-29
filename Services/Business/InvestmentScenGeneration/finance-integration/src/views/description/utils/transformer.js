const getFactorsData = (factors, selectedFactors) => {
  const tableData = [];
  console.log(factors,selectedFactors)
    // tableData.push(factors.find((cur) => cur.value === value));
    for (let i = 0; i < selectedFactors.length; i++) {
      for (let j = 0; j < factors.length; j++) {
        if (factors[j].value === selectedFactors[i][0]) {
          for (let k = 0; k < factors[j].children.length; k++) {
            if (selectedFactors[i][1] === factors[j].children[k].value) {
              tableData.push(factors[j].children[k]);
            }
            }
          }
        }
      }
  return tableData.map((cur) => ({ description: "", ...cur , name: cur.label, isTop: 1,
    choiceType: 0, value: 1, nameUs: cur.value, logic: '大于'}));
};

const getPage2TagData = (factors, selectedFactors) => {
  const tagData = [];
  for (const id of selectedFactors) {
    tagData.push(factors.find((cur) => cur.id === id));
  }
  return tagData;
};

export { getFactorsData, getPage2TagData };
