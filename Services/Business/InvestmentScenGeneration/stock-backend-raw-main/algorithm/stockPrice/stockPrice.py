import tushare as ts
import datetime

pro = ts.pro_api('1bbd3d73b50fc602f215cf81ccf49d10798a237244b0829b493a4289')

def get_result(stock_id):
    now_time_standard = datetime.datetime.now()
    now_time = now_time_standard.strftime('%Y%m%d')
    stock_code = stock_id[0:6] + '.SZ'
#     df = pro.query('daily', ts_code=stock_code, start_date=now_time, end_date=now_time)

#     open = df['open']
    open_value = 0.0
    return open_value
    # 股价取值为当日开盘价
#     if open.empty:
#         return 0.0
#     else:
#         open_value = df['open'].values[0]
#         return open_value

