import backtrader as bt
import pandas as pd
import json
import requests
# STAR: 选股通用策略
def select():
    # 筛选条件
    # 筛选retained_profit_per_share 大于平均值，sale_expense大于前百分之八十，market_cap 最大的前五只股票
    colums = ["retained_profit_per_share","market_cap","sale_expense"]
    date = '2020'
    data = getData(date,colums)
    data['retained_profit_per_share'] = pd.to_numeric(data['retained_profit_per_share'], errors='coerce')
    data['sale_expense'] = pd.to_numeric(data['sale_expense'], errors='coerce')
    data['market_cap'] = pd.to_numeric(data['market_cap'], errors='coerce')

    mean_profit = data['retained_profit_per_share'].mean()
    pct_sale_expense = data['sale_expense'].quantile(0.8)
    data = data[(data["retained_profit_per_share"] >= mean_profit) & (data["sale_expense"] >= pct_sale_expense)]
    data.sort_values(by="market_cap",axis=0,ascending=False,inplace=True)
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

def getData(date,colums):
    columns_string = ",".join(colums)
    print(columns_string)
    url = 'http://padq-business-investmentscengenerationtool.backend:8080/stock/getInitStockList'  # 替换为实际的API地址
    params = {'date': date, 'factorsName': columns_string}  # 设置查询字符串参数
    
    try:
        response = requests.get(url, params=params,)
        content = response.content.decode('utf-8')  # 使用 UTF-8 编码解码响应内容
        data = json.loads(content) 
        data = data['data']
        df = pd.DataFrame(data)
        return df
    except requests.RequestException as e:
        print('调用接口出错:', str(e))
        return None