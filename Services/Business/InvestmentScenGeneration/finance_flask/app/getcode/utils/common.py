import requests
import json
import pandas as pd
# 根据日期获取数据
def getData(date,colums):
    columns_string = ",".join(colums)
    print(columns_string)
    url = 'http://localhost:8080/stock/getInitStockList'  # 替换为实际的API地址
    params = {'date': date, 'factorsName': columns_string}  # 设置查询字符串参数
    
    try:
        response = requests.get(url, params=params,)
        content = response.content.decode('utf-8')  # 使用 UTF-8 编码解码响应内容
        data = json.loads(content) 
        # data = response.json()  # 解析响应数据为JSON格式
        data = data['data']
        df = pd.DataFrame(data)

        # df['stock_name'] = df['stock_name'].str.decode('utf-8')
        # print(df)
        return df
    except requests.RequestException as e:
        print('调用接口出错:', str(e))
        return None
def runSelect(strategy_class):
    print("123")
    # 需要发送给java后端，对数据库进行操作获取股票
    # 需要什么参数：日期，因子值
    # 因子列为如下，选择公司年限为3年及以内的，每股收益超过
    columns=['total_profit','accounts_payable','company_year']
    pass