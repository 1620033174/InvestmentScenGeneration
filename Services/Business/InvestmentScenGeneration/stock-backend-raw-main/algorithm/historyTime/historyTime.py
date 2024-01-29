import pandas as pd
import json

# 生成给定个股的历史高送转时刻及相关信息
def get_result(stock_id):
    history_data = pd.read_csv("algorithm/stock_year.csv")
    
    # 选出送转股票
    history_data = history_data[history_data["送转总和"] > 0]
    # 选出高送转潜力股
    history_data = history_data[history_data["股票代码"]==stock_id]

    history_data['_time'] = history_data['财报发布日']
    history_data['历史时刻(日期)'] = history_data['财报发布日']
    history_data['数据来源'] = history_data['_quarter']
    simi_history_data = history_data[
        ['stock_id', '_time', '历史时刻(日期)', '送股比例', '转股比例', '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
         '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)', '数据来源']]
         
    # n = len(simi_history_data)+1
    # nlist = range(1,n)
    # simi_history_data['id'] = nlist
    # simi_history_data.columns = ['id'] + simi_history_data.columns[1:].tolist()
    # simi_history_data.columns = ['id','stock_id', '_time', '历史时刻(日期)', '送股比例', '转股比例', '每股收益(元)', '每股经营现金流(元)', '每股公积金(元)',
    #      '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)', '数据来源']
    # print(simi_history_data.to_json())

    return simi_history_data

print(get_result("002821.XSHE"))


