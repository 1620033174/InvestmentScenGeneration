"""
@File: get_mostsimilar_history.py
@author: Chenglong Xiao
@Describtion: 
"""
import pandas as pd
import numpy as np
import json

pd.options.mode.chained_assignment = None
# 计算余弦相似度


def cosin_distance(vector1, vector2):

    user_item_matric = np.vstack((vector1, vector2))
    sim = user_item_matric.dot(user_item_matric.T)
    norms = np.array([np.sqrt(np.diagonal(sim))])
    user_similarity = (sim / norms / norms.T)[0][1]
    return user_similarity

# 在历史数据里找到stock数据的最相似的时刻，不管发没发生高送转


def get_result(stock_json):
    stock_json = json.loads(stock_json)
    history_data = pd.read_csv("algorithm/stock_year.csv", dtype={"董事会预案公告日": str})

    h_data = history_data[['stock_id', '_time', '财报发布日', '_quarter', '送股比例', '转股比例', '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
                           '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)']]
    # 得到股票id和年份
    stock_id = stock_json.get('股票代码')
    year = int(stock_json.get('_quarter')[0:4])

    # 初步筛选出  stock_id 的所有时刻
    h_data1 = h_data[h_data["stock_id"] == stock_id]

    # 新增year列，用来进行比较
    h_data1['year'] = h_data1['_quarter'].str[0:4].astype(int)

    # 二次筛选出  小于当前年份的所有数据作为历史数据
    h_data2 = h_data1[h_data1["year"] < year]

    origindata = [stock_json['每股收益(元)'], stock_json['每股经营现金流(元)'], stock_json['每股公积金(元)'], stock_json['每股未分配利润(元)'], stock_json['流通股本(亿股)'],
                  stock_json['流通市值(亿元)']]
    i = 0
    similarity = []
    while i < len(h_data2):
        stockdata = h_data2.iloc[i, -6:].values.tolist()
        i = i + 1
        simi = cosin_distance(origindata, stockdata)
        similarity.append(simi)
    h_data2['相似度'] = similarity
    h_data2.sort_values(by='相似度', inplace=True, ascending=False)
    df_new = h_data2.iloc[:1]
    return df_new


stock_json = """{
        "Unnamed: 0": 10836,
        "股票代码": "002932.XSHE",
        "中文名称": "明德生物",
        "每股收益(元)": 4.4079,
        "每股经营现金流(元)": 4.615023199,
        "每股公积金(元)": 4.482158576,
        "每股未分配利润(元)": 13.78391155,
        "流通股本(亿股)": 0.58979312,
        "流通市值(亿元)": 36.5613,
        "_quarter": "2021-10-28",
        "stock_id": "002932.XSHE",
        "_stock_name": "明德生物"
    }"""

mostsimilar_histry = get_result(stock_json)

print(mostsimilar_histry.to_dict())
