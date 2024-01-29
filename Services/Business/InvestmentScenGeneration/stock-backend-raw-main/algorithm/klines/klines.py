import tushare as ts
import json
import datetime
from datetime import datetime
from dateutil.relativedelta import relativedelta
import pandas as pd

pro = ts.pro_api('1bbd3d73b50fc602f215cf81ccf49d10798a237244b0829b493a4289')

stock_json = """{
        "stock_id": "603095.XSHG",
        "_quarter": "2020-10-28"
    }"""

def get_result(stock_json):
    stock_json = json.loads(stock_json)
    stock_id = stock_json.get('stock_id')
    stock_code = stock_id[0:6] + '.SZ'
    stock_time = stock_json['_quarter'].replace('-', '')
    print(stock_time)
    t = datetime.strptime(stock_time, "%Y%m%d")
    star = (t - relativedelta(days=60)).strftime("%Y%m%d")
    end = (t + relativedelta(days=30)).strftime("%Y%m%d")

    df = pro.query('daily', ts_code=stock_code, start_date=star, end_date=end)
    # print(df)
    
    df['_time'] = stock_json['_quarter']
    df['stock_id'] = stock_id
    # time为交易日
    df['trade_date'] = pd.to_datetime(df['trade_date']).dt.strftime('%Y-%m-%d')
    df.rename(columns={'open': 'start', 'close': 'end', 'low': 'lowest', 'high': 'highest', 'vol': 'volume', 'trade_date': 'time'}, inplace=True)
    df = df[['stock_id', '_time', 'time', 'start', 'end', 'lowest', 'highest', 'volume']]
    return df

print(get_result(stock_json).to_json())
