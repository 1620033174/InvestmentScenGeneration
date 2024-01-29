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

# 送转股票中查找与stock最相似的十只股票
def get_result(stock):
    stock = json.loads(stock)
    history_data = pd.read_csv("algorithm/stock_year.csv", dtype={"董事会预案公告日": str})

    h_data = history_data[
        ['stock_id', '股票代码', '中文名称', '财报发布日',  '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
         '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)']]

    origindata = [stock['每股收益(元)'], stock['每股经营现金流(元)'], stock['每股公积金(元)'], stock['每股未分配利润(元)'], stock['流通股本(亿股)'],
                  stock['流通市值(亿元)']]

    i = 0
    similarity = []
    while i < len(h_data):
        stockdata = h_data.iloc[i, -6:].values.tolist()
        i = i + 1
        simi = cosin_distance(origindata, stockdata)
        similarity.append(simi)
    h_data['Similarity'] = similarity
    h_data.sort_values(by="Similarity", inplace=True, ascending=True)
    h_data.insert(3, 'Similarity', h_data.pop('Similarity'))
    return h_data.iloc[:10]
