import request from "@/utils/request";

export function getFactors(params) {
  return request({
    // url: "https://mock.apifox.cn/m1/2612594-0-default/factors/list",
    url: "/lwz/factors/group",
    method: "get",
    params,
  });
}

export function getAllFactors(params) {
  return request({
    url: "/lwz/factors/getAllFactors",
    method: "get",
    params,
  });
}
