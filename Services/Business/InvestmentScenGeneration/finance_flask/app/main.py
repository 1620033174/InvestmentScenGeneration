from flask import Flask, request
from flask_cors import CORS
# 引入自定义的工具函数
from utils.index import *
from decimal import Decimal
import glob
import os
import requests
app = Flask(__name__)
CORS(app)
CORS(app, supports_credentials=True)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
# app.run(host='0.0.0.0', port=5000)
@app.route("/")
def test_function():
    return "<p>Hello, World!</p>"


@app.get("/lwz/algorithm/file/get")
def get_file():
    # path =  request.args.get("path") # 获取path参数
    fileName = request.args.get('fileName')
    # print("getgetgetget")
    path = get_path(fileName)
    result = file_reader(path)
    return result_wrapper(result)

@app.get("/lwz/algorithm/file/getZonghe")
def get_fileZonghe():
    # path =  request.args.get("path") # 获取path参数
    fileName = request.args.get('fileName')
    # print("getgetgetget")
    path = get_path_zonghe(fileName)
    print(path)
    result = file_reader(path)
    print(result)
    return result_wrapper(result)

@app.get("/lwz/algorithm/file/run")
def run():
    print("runrunrun")
    fileName = request.args.get('fileName')
    startdate = request.args.get('startdate')
    enddate = request.args.get('enddate')
    initialCash = request.args.get('initialCash')
    # print(startdate)
    # print(enddate)
    # print(initialCash)
    path = get_path(fileName)
    print("my args",request.args)

    #TODO 预选算法比较进行回测时没有参数startdate,enddate,initialCash

    result = file_runner(path,startdate,enddate,initialCash) # 更多参数输入
    maximum_drawdown_info = get_maximum_drawdown_info(startdate,enddate)
    res = dict(result, **maximum_drawdown_info)
    # print("哈哈哈")
    # print(res)
    print(res['maximum'])
    strategy_drawdown = get_strategy_drawdown(res['cashValues'])
    res['strategyProfit'] = strategy_drawdown
    new_strategyProfit_list = [abs(num) for num in res['strategyProfit']]
    res['maximum'] = max(new_strategyProfit_list)
    print(res['maximum'])
    # result = file_runner(path)
    return result_wrapper(res)

def get_strategy_drawdown(array):
    list_new = []
    for i in range(len(array)):
        max_array = max(array[:i+1])
        drawdown = (array[i]/max_array -1)*100
        # drawdown = Decimal(drawdown).quantize(Decimal("0.01"), rounding = "ROUND_HALF_UP")
        drawdown = format(drawdown, '.2f')
        list_new.append(float(drawdown))
    # print("啦啦啦")
    # print(list_new)
    return list_new
# @app.get("/lwz/algorithm/file/get_maximum_drawdown")
# def run_maximum_drawdown():
    # print("get_maximum_drawdown")
    # fileName = request.args.get('fileName')
    # startdate = request.args.get('startdate')
    # enddate = request.args.get('enddate')
    # initialCash = request.args.get('initialCash')
    # print(startdate)
    # print(enddate)
    # # print(initialCash)
    # path = get_path(fileName)
    # print("my args",request.args)
    # result = file_runner(path,startdate,enddate,initialCash) # 更多参数输入
    # result = file_runner(path)
    # maximum_drawdown_info = get_maximum_drawdown_info()
    # return result_wrapper(maximum_drawdown_info)

@app.post("/lwz/algorithm/file/update")
def update():
    content = request.json.get("content")
    fileName = request.json.get("fileName")
    path = get_path(fileName)
    result = file_updater(path, content)
    return result_wrapper(result)

@app.post("/lwz/algorithm/file/create")
def create():
    # raw_data = request.data.decode('utf-8')  # Decode bytes to string
    # json_data = json.loads(raw_data)  # Parse string as JSON

    # id_value = json_data.get('id')
    # print(id_value)
    json_data = request.get_json()
    if json_data:
        id =json_data.get('id')
        print(id)
        path = get_path(id)
        # # 根据id新建文件
        # with open(path, 'w') as f:
        #     f.write('')
        source_file = open(get_path("template"), 'r',encoding='utf-8')
        target_file = open(path, 'w',encoding='utf-8')
        # 从源文件中读取内容并写入目标文件
        content = source_file.read()

        target_file.write(content)
        
        # stockList为返回的股票池
        # stockList = ['000001.XSHE','000002.XSH','000003.XSHG']
        stockList = request.get_json().get('stockList')
        source_file.close()
        target_file.close()
        print(stockList)
        print(path)
        if stockList is None :
            return result_wrapper(True)
        else:
            with open(path, 'r', encoding='utf-8') as file:
                lines = file.readlines()
            print(lines)
            # 查找包含关键字的行并修改
            for i, line in enumerate(lines):
                if "stockList =" in line:
                    print("123")
                    new_list_str = str(stockList)
                    new_str = lines[i].replace(lines[i][lines[i].find('['):lines[i].find(']') + 1], new_list_str)
                    print(new_str)
                    lines[i] = new_str
                    break

            # 将修改后的内容写回文件
            with open(path, 'w', encoding='utf-8') as file:
                file.writelines(lines)
                # 关闭文件
                source_file.close()
                target_file.close()
        return result_wrapper(True)
    else:
        return result_wrapper("json为空")
    
@app.post("/lwz/algorithm/file/createZonghe")
def createZonghe():
    # raw_data = request.data.decode('utf-8')  # Decode bytes to string
    # json_data = json.loads(raw_data)  # Parse string as JSON

    # id_value = json_data.get('id')
    # print(id_value)
    json_data = request.get_json()
    if json_data:
        id =json_data.get('id')
        print(id)
        path = get_path_zonghe(id)
        # # 根据id新建文件
        # with open(path, 'w') as f:
        #     f.write('')
        source_file = open(get_path_zonghe("getcode_template"), 'r',encoding='utf-8')
        target_file = open(path, 'w',encoding='utf-8')
        # 从源文件中读取内容并写入目标文件
        content = source_file.read()

        target_file.write(content)
        
        # stockList为返回的股票池
        # stockList = ['000001.XSHE','000002.XSH','000003.XSHG']
        stockList = request.get_json().get('stockList')
        source_file.close()
        target_file.close()
        return result_wrapper(True)
    else:
        return result_wrapper(False)

@app.post("/lwz/algorithm/file/updateZonghe")
def updateZonghe():
    content = request.json.get("content")
    print(content)
    fileName = request.json.get("fileName")
    path = get_path_zonghe(fileName)
    print(path)
    result = file_updater(path, content)
    print(result)
    return result_wrapper(result)

@app.get("/lwz/algorithm/file/runZonghe")
def runZonghe():
    print("runrunrun")
    fileName = request.args.get('fileName')
    # file_list = glob.glob('./getcode/*.py')
    # print(file_list)
    path = get_path_zonghe(fileName)
    print(path)
    print("my args",request.args)
    
    function_name = "select"
    result = run_function(path, function_name)
    print("哈哈")
    print(result)
    if '运行失败' in result:
        print(result)
        return result_wrapper(result)
    else:
        result = json.loads(result)
        data = result["data"]
        # data = json.loads(data)
        print(data)
        columns = []
        for key in data[0].keys():
            columns.append({"prop": key, "label": key})

        rows = []
        for item in data:
            row = {column["prop"]: str(item[column["prop"]]) for column in columns}
            rows.append(row)

        output = {"columns": columns, "rows": rows}
        # print(output)
        output = set_output(output)
        print(output)
        output = column_sorting(output)
        print("不好玩")
        print(output)
        return output
def column_sorting(data):
    # 指定列的顺序
    desired_columns = ['股票代码', '股票名称', '公告日期', '日期']

    # 获取指定的4列
    desired_columns = ['股票代码', '股票名称', '公告日期', '日期']

    # 重新构造列数据
    new_columns = []
    for col in desired_columns:
        for column in data["columns"]:
            if column["prop"] == col:
                new_columns.append(column)

    # 保留其他无序列
    for column in data["columns"]:
        if column not in new_columns:
            new_columns.append(column)
        # print(data)
    # 按照新的列顺序更新data中的列数据
    data["columns"] = new_columns

    return data
# 进行名字转换
def set_output(data):
    for column in data['columns']:
        print()
        column['label'] = getName(column['label'])
        column['prop'] = getName(column['prop'])
    print("不好玩")
    # print(data)
    for row in data['rows']:
        new_row = {}
        for key, value in row.items():
            new_key = getName(key)
            new_row[new_key] = value
        new_row['code'] = new_row['股票代码']
        row.clear()
        row.update(new_row)
    return data
def getName(columnName):
    if columnName == 'date':
        return '日期'
    elif columnName == 'code':
        return '股票代码'
    elif columnName == 'stock_name':
        return '股票名称'
    elif columnName == 'pubdate':
        return '公告日期'
    else:
        return getNameByDataSource(columnName)
    
def getNameByDataSource(columnName):
    url = 'http://backend:8080/stock/getNameByDataSource'  # 替换为实际的API地址
    params = {'factorNameUS': columnName}  # 设置查询字符串参数
    response = requests.get(url, params=params)
    content = response.content.decode('utf-8')
    return content

def run_function(file_path, function_name, *args, **kwargs):
    try:
        # 读取文件内容
        with open(file_path, 'r' , encoding='utf-8') as file:
            code = file.read()

        # 创建一个全局命名空间
        namespace = {}

        # exec("from utils.common import *", namespace)   

        # 执行代码
        exec(code, namespace)

        # 获取要执行的函数
        func = namespace[function_name]

        # 运行函数并返回结果
        return func(*args, **kwargs)
    except Exception as e:
        return f'运行失败：{str(e)}'