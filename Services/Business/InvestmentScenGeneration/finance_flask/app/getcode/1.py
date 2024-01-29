import backtrader as bt
import pandas as pd
import json
import requests
def getData(date,colums):
    columns_string = ",".join(colums)
    print(columns_string)
    url = 'http://padq-business-investmentscengenerationtool.backend:8080/stock/getInitStockList'  # 替换为实际的API地址
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
# STAR: 选股通用策略
def select():
    # 筛选条件
    # 筛选retained_profit 大于平均值，turnover_ratio大于前百分之八十，company_year 最大的前五只股票
    colums = ["retained_profit","company_year","turnover_ratio"]
    date = '2020'
    data = getData(date,colums)
    data['retained_profit'] = pd.to_numeric(data['retained_profit'], errors='coerce')
    data['turnover_ratio'] = pd.to_numeric(data['turnover_ratio'], errors='coerce')
    data['company_year'] = pd.to_numeric(data['company_year'], errors='coerce')

    mean_profit = data['retained_profit'].mean()
    pct_turnover_ratio = data['turnover_ratio'].quantile(0.8)
    data = data[(data["retained_profit"] >= mean_profit) & (data["turnover_ratio"] >= pct_turnover_ratio)]
    data.sort_values(by="company_year",axis=0,ascending=False,inplace=True)
    data = data.head(5)
    print(data.head(5))
    # 将DataFrame转为json字符串
    df_json = data.to_json(orient='records')

    # 解析json字符串为List<Map<String, String>>
    data = json.loads(df_json)
    # 构造最终的数据结构
    result = {
        "code": 20000,
        "message": "success",
        "data": data
    }

    # 输出结果
    print(json.dumps(result, indent=4))
    return json.dumps(result, indent=4)     
# runSelect(MyStrategy)