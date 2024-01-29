import pandas as pd
import schedule
import time
import datetime

def select_stockset(data, stype='', dtstate=-1, year=-1):
    temp = data.copy()
    # 按类型筛选
    if stype == 'mb':
        temp = temp[(temp['股票代码'].str[:2] == '60') | (temp['股票代码'].str[:3] == '000')]
    elif stype == 'smb':
        temp = temp[temp['股票代码'].str[:3] == '002']
    elif stype == 'gemb':
        temp = temp[temp['股票代码'].str[:3] == '300']

    # 按送转状态筛选
    if dtstate == 0 or dtstate == 1 or dtstate == 2:
        temp = temp[temp['送转状态3'] == dtstate]
    elif dtstate == 3:
        temp = temp[temp['是否送转'] == 1]

    # 按年份筛选
    if year != -1:
        temp = temp[temp['财报发布日'].astype(str).str[:4] == str(year)]

    return temp

# 筛选满足高送转新规的个股
def select_by_stipuls(data):
    r = data.copy()

    # 过滤净利润为负的个股
    r = r[(r['净利润(元)'] >= 0)]

    # 过滤净利润同比下降50%以上的个股
    r = r[r['净利润同比增长率(%)'] >= -0.5]

    # 送转后每股收益不低于0.2元
    mbset = select_stockset(r, 'mb', -1, -1)
    mbset = mbset[mbset['每股收益(元)'] >= 0.3]

    smbset = select_stockset(r, 'smb', -1, -1)
    smbset = smbset[smbset['每股收益(元)'] >= 0.36]

    gembset = select_stockset(r, 'gemb', -1, -1)
    gembset = gembset[gembset['每股收益(元)'] >= 0.4]

    r = pd.concat([mbset, smbset, gembset], axis=0)

    return r

# 按统计特征规律选股
def select_by_staconditions(data, n):
    return data[(data['流通股本(亿股)'] < 4.5)].nlargest(n, '每股净利润(元)')

def get_by_quarter(df):
    temp = select_by_stipuls(df)
    stockset = select_by_staconditions(temp, 1000)
    stockset = stockset[['股票代码', '中文名称', '每股收益(元)', '每股经营现金流(元)',
              '每股公积金(元)', '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)', '财报发布日', 'stock_id', '_stock_name']]
    stockset['每股收益(元)'] = stockset['每股收益(元)'].apply(lambda x:round(x,2))
    stockset['每股经营现金流(元)'] = stockset['每股经营现金流(元)'].apply(lambda x: round(x, 2))
    stockset['每股公积金(元)'] = stockset['每股公积金(元)'].apply(lambda x: round(x, 2))
    stockset['每股未分配利润(元)'] = stockset['每股未分配利润(元)'].apply(lambda x: round(x, 2))
    stockset['流通股本(亿股)'] = stockset['流通股本(亿股)'].apply(lambda x: round(x, 2))
    stockset['流通市值(亿元)'] = stockset['流通市值(亿元)'].apply(lambda x: round(x, 2))
    stockset.rename(columns={"财报发布日": "_quarter"}, inplace=True)
    return stockset

def get_top10_by_quarter(year, now_time):
    df = pd.read_csv("python-src/stocks/stock_year.csv", dtype={"董事会预案公告日": str})
    stockset = df.copy()
    stockset = stockset[['股票代码', '中文名称', '每股收益(元)', '每股经营现金流(元)',
              '每股公积金(元)', '每股未分配利润(元)', '流通股本(亿股)', '流通市值(亿元)', '财报发布日', 'stock_id', '_stock_name']]
    stockset['_selected_time'] = now_time
    #删掉stock_id为空的行
    stockset = stockset.dropna(subset=['stock_id'])
    stockset.loc['每股收益(元)'] = stockset['每股收益(元)'].apply(lambda x:round(x,2))
    stockset.loc['每股经营现金流(元)'] = stockset['每股经营现金流(元)'].apply(lambda x: round(x, 2))
    stockset.loc['每股公积金(元)'] = stockset['每股公积金(元)'].apply(lambda x: round(x, 2))
    stockset.loc['每股未分配利润(元)'] = stockset['每股未分配利润(元)'].apply(lambda x: round(x, 2))
    stockset.loc['流通股本(亿股)'] = stockset['流通股本(亿股)'].apply(lambda x: round(x, 2))
    stockset.loc['流通市值(亿元)'] = stockset['流通市值(亿元)'].apply(lambda x: round(x, 2))
    stockset.rename(columns={"财报发布日": "_quarter"}, inplace=True)
#     quarters = df['_quarter'].unique()
#
#     groups = df.groupby(df._quarter)
#     # 获取得到当年的q3，如2023q3
#     year_quarter = year + 'q3'
#     if year_quarter not in quarters:
#         # 获得前一年的数据
#         year_quarter = str(int(year)-1) + 'q3'
#         df_quarter = groups.get_group(year_quarter)
#     else:
#         df_quarter = groups.get_group(year_quarter)
#     df_new = get_by_quarter(df_quarter)
#     df_new['id'] = range(0, len(df_new))
    return stockset

# 检测更新，如果根据stock_year.csv生成的stock.csv条数发生变化
# 变化情况：1，如果是当年的，可能会出现当年的数据出现不一致的情况，如果数据有变化，与现在的csv进行对比，进行变化数据的更换。
#         2. 如果数据有新增，与现在的数据进行比较：1，更换变化数据
# 见上一步,这里我们写成一个函数的形式
def get_result():
    now_time = datetime.datetime.now().strftime('%Y-%m-%d')
    year = datetime.datetime.now().strftime('%Y')
    stock = get_top10_by_quarter(year, now_time)
    #写入csv
    stock.to_csv('python-src/stocks/stock1.csv', index=False, encoding='utf-8')
    return stock
    
