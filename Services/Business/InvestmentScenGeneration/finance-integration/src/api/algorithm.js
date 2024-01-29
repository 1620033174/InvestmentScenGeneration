import request from "@/utils/request";

// spring-boot相关的接口
function getAlgorithmById(params) {
  return request({
    url: "/lwz/algorithm/get",
    method: "get",
    params,
  });
}

function getAllAlgorithm(params) {
  return request({
    url: "/lwz/algorithm/all",
    method: "get",
    params
  });
}

const setAlgorithm = (data) =>{
  return request({
    // url: "https://mock.apifox.cn/m1/2612594-0-default/algorithm/save",
    url: "/lwz/algorithm/save",
    method: "post",
    data,
  });
}

function getAlgorithmList(params) {
  return request({
    // url: "https://mock.apifox.cn/m1/2612594-0-default/algorithm/list",
    url: "/lwz/algorithm/list",
    method: "get",
    params,
  });
}

function updateAlgorithm(data) {
  return request({
    url: "/lwz/algorithm/update",
    method: "post",
    data,
  });
}

function getAlgorithmPage(params) {
  return request({
    url: "/lwz/algorithm/getAlgorithmByPage",
    method: "get",
    params,
  });
}

function deleteAlgorithmById(id) {
  return request({
    url: "/lwz/algorithm/deleteAlgorithmById/" + id,
    method: "delete",
  });
}

// flask相关的接口

function getAlgorithmContentByFileName(params) {
  return request({
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/get",
    // url: "http://16ab7778.r17.cpolar.top/lwz/algorithm/file/get",
    method: "get",
    params,
  });
}

function getAlgorithmContentZongHeByFileName(params) {
  return request({
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/getZonghe",
    // url: "http://2ca0dac3.r12.cpolar.top/lwz/algorithm/file/getZonghe",
    method: "get",
    params,
  });
}

function executeAlgorithmContentZongheByFileName(params) {
  return request({
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/runZonghe",
    // url: "http://2ca0dac3.r12.cpolar.top/lwz/algorithm/file/runZonghe",
    method: "get",
    params,
  });
}

function executeAlgorithmContentByFileName(params) {
  return request({
    // url: "http://localhost:5000/lwz/algorithm/file/run",
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/run",
    method: "get",
    params,
  });
}

function createAlgorithmContentZongHe(data) {
  return request({
    url: process.env.VUE_APP_PY_URL  + "/lwz/algorithm/file/createZonghe",
    method: "post",
    data,
  });
}

function createAlgorithmByFileName(data) {
  return request({
    // url: "http://localhost:5000/lwz/algorithm/file/create",
    url: process.env.VUE_APP_PY_URL  + "/lwz/algorithm/file/create",
    method: "post",
    data,
  });
}

function updateAlgorithmContent(data) {
  return request({
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/update",
    method: "post",
    data,
  });
}

function updateAlgorithmContentZongHe(data) {
  return request({
    url: process.env.VUE_APP_PY_URL + "/lwz/algorithm/file/updateZonghe",
    // url: "http://2ca0dac3.r12.cpolar.top/lwz/algorithm/file/updateZonghe",
    method: "post",
    data,
  });
}

export {
  setAlgorithm,
  getAlgorithmList,
  getAlgorithmById,
  getAlgorithmContentByFileName,
  executeAlgorithmContentByFileName,
  createAlgorithmByFileName,
  updateAlgorithm,
  updateAlgorithmContent,
  getAlgorithmPage,
  deleteAlgorithmById,
  getAllAlgorithm,
  getAlgorithmContentZongHeByFileName,
  executeAlgorithmContentZongheByFileName,
  createAlgorithmContentZongHe,
  updateAlgorithmContentZongHe
};
