import request from "@/utils/request";
const getSceneList = (params) => {
  return request({
    url: "/scene/getByPage",
    method: "get",
    params,
  });
}

const addScene = (data) => {
  return request({
    url: "/scene/add",
    method: "post",
    data,
  });
}

const updateScene = (data) => {
  return request({
    url: "/scene/updateById",
    method: "post",
    data,
  });
}

const getSceneById = (id) => {
  return request({
    url: "/scene/getById/" + id,
    method: "get",
  });
}

const deleteScene = (id) => {
  return request({
    url: "/scene/deleteById/" + id,
    method: "delete",
  });
}

function getStockByAlgorithmId(params) {
  return request({
    url: "/scene/getStockDataById",
    method: "get",
    params,
  });
}

function getStockListById(params) {
  return request({
    url: "/scene/showStock",
    method: "get",
    params,
  });
}
export { getSceneList , deleteScene,getStockByAlgorithmId,
  addScene, getSceneById, getStockListById, updateScene};
