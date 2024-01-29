import request from "@/utils/request";

function getSimulatedStock(data) {
  return request({
    url: "/stock/getSimulatedStock",
    method: "post",
    data,
  });
}

function getStockList(data) {
  return request({
    url: "/stock/getStockList",
    method: "post",
    data,
  });
}


export { getSimulatedStock, getStockList};
