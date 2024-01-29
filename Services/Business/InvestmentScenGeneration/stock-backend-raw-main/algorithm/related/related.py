import pandas as pd
import numpy as np
import json

pd.options.mode.chained_assignment = None
# def get_result(stock_json):
#     data = pd.read_csv("related.csv")
#     return data

# 计算余弦相似度
def cosin_distance(vector1, vector2):

    user_item_matric = np.vstack((vector1, vector2))
    sim = user_item_matric.dot(user_item_matric.T)
    norms = np.array([np.sqrt(np.diagonal(sim))])
    user_similarity = (sim / norms / norms.T)[0][1]
    return user_similarity

# 送转股票中查找与stock最相似的十只股票
def get_result(stock_json):
    stock_json = json.loads(stock_json)
    history_data = pd.read_csv("algorithm/stock_year.csv")
    historydata = history_data[history_data["送转总和"] > 0]
    h_data = historydata[
        ['stock_id', '股票代码', '中文名称', '财报发布日',  '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
         '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)']]

    origindata = [stock_json['每股收益(元)'], stock_json['每股经营现金流(元)'], stock_json['每股公积金(元)'], stock_json['每股未分配利润(元)'], stock_json['流通股本(亿股)'],
                  stock_json['流通市值(亿元)']]

    i = 0
    similarity = []
    while i < len(h_data):
        stockdata = h_data.iloc[i, -6:].values.tolist()
        i = i + 1
        simi = cosin_distance(origindata, stockdata)
        similarity.append(simi)
    h_data['相似度'] = similarity
    h_data.sort_values(by="相似度", inplace=True, ascending=False)
    h_data = h_data[
        ['stock_id', '股票代码', '中文名称', '相似度', '财报发布日',  '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
         '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)']]
    return h_data.iloc[:10]

stock_json = {
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
    }

# mostsimilar_history = get_other_high_transfer_similar_list(stock_json)
# print(mostsimilar_history)