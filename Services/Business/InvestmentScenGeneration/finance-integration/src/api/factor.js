import request from "@/utils/request";

function getFactorInfo(params) {
  return request({
    url: "/hz/results/getFactorsInfo",
    method: "get",
    params,
  });
}

function getFactorsData(params) {
  return request({
    url: "/hz/results/getFactorsData",
    method: "get",
    params,
  });
}



function getMin(params) {
  return request({
    url: "/hz/detail/getMin",
    method: "get",
    params,
  });
}

function getMax(params) {
  return request({
    url: "/hz/detail/getMax",
    method: "get",
    params,
  });
}

function getVariancePic(params) {
  return request({
    url: "/hz/detail/getVariancePic",
    method: "get",
    params,
  });
}

function getQuantilePic(params) {
  return request({
    url: "/hz/detail/getQuantilePic",
    method: "get",
    params,
  });
}

function getCorrelationFactors(params) {
  return request({
    url: "/hz/results/getCorrelationFactors",
    method: "get",
    params,
  });
}

function updateFactorInfo(data) {
  return request({
    url: "/hz/factor/update",
    method: "post",
    data,
  });
}

function deleteFactorInfo(id) {
  return request({
    url: "/hz/factor/deleteById/" + id,
    method: "delete",
  });
}

function addFactorInfo(data) {
  return request({
    url: "/hz/factor/add",
    method: "post",
    data,
  });
}

function getFactorPage(params) {
  return request({
    url: "/hz/factor/getByPage",
    method: "get",
    params,
  });
}

export{
  getFactorInfo,
  getFactorsData,
  getMax,
  getMin,
  getVariancePic,
  getQuantilePic,
  getCorrelationFactors,
  updateFactorInfo,
  deleteFactorInfo,
  addFactorInfo,
  getFactorPage
}
