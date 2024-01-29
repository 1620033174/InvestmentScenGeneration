import subprocess
import json
import os
from .strategy import strategyLoader
import pandas as pd
import numpy as np
test_file_path = "./app/scripts/test.py"


def test_fn(a, *args, b):
    print(a, *args, b)

# posix,nt,java， 对应linux/windows/java虚拟机
def get_sys_code():
    if(os.name== "nt"): 
        return "gbk"
    return "utf-8"


def file_runner(path, *args):
    # strategyLoader("28")
    # print("file_runner")
    # print(*args[0])
    result = subprocess.run(["python", path, *args],
                            stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # python .\app\scripts\fn_2.py
    if result.returncode == 0:
        lines = result.stdout.decode(get_sys_code()).splitlines()
        obj = {
            "result":eval(lines[0]),
            "dates":eval(lines[1]),
            "endValue":eval(lines[2]),
            "yieldRate":eval(lines[3]),
            "info":"运行成功",
            "cashValues":eval(lines[4]),
        }
        return obj
        # return json.dumps(obj=obj)
    else:
        print(result.stderr.decode(get_sys_code()))
        obj = {
            "info":result.stderr.decode(get_sys_code()) # 传递的报错
        }
        return obj
def MaxDrawdown(return_list):
    '''最大回撤率'''
    i = np.argmax((np.maximum.accumulate(return_list) - return_list) / np.maximum.accumulate(return_list))  # 结束位置
    if i == 0:
        return 0
    j = np.argmax(return_list[:i])  # 开始位置
    return (return_list[j] - return_list[i]) / (return_list[j])

def get_maximum_drawdown_info(startdate,enddate):
    filePath = os.path.join(os.path.abspath(os.curdir),"app","scripts","data",("hs_300_drawdown"+".csv"))
    data = pd.read_csv(filePath)
    print("get_maximum_drawdown_info")  
    # print(startdate)
    # print(enddate)
    data = data[(data["date"] >= startdate) & (data["date"] <= enddate)]
    print(data)
    shanghaiIndex = data['maximum_drawdown'].to_list()
    shanghaiIndex = [float(s.rstrip('%')) for s in shanghaiIndex]
    updated_list = [abs(num) for num in shanghaiIndex]
    print(updated_list)
    maximum = max(updated_list)
    print(maximum)
    obj = {
        "time":data['date'].to_list(),
        "shanghaiIndex":shanghaiIndex,
        "maximum":maximum
    }
    # obj = {
    #     "time": [
    #     '20210101','20210102','20210103','20210104','20210105','20210106','20210107', 
    #     '20210108','20210109','20210110','20210111','20210112','20210113','20210114',
    #     '20210115','20210116','20210117','20210118','20210119','20210121','20210121',
    #     '20210122','20210123','20210124','20210125','20210126','20210127','20210128',
    #     '20210129','20210130','20210131','20210201','20210202','20210203','20210204',
    #     '20210205','20210206','20210207','20210208','20210209','20210210','20210211',
    #   ],
    #   "shanghaiIndex": [
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #     0, -0.137, -0.087, -0.178, -0.130, -0.295, -0.227, -0.227,
    #   ],
    # }
    # print(data)
    return obj
def file_reader(path):
    # path为以项目跟根目录为初始目录的相对路径
    file_content = ""
    with open(path, 'r',encoding='utf-8') as f:  # with方式可以避免没有关闭资源文件产生错误
        # print ("文件名为: ", f.name)
        # print("读取的数据为:")
        file_content = f.read()
        # print(file_content)
    return file_content


def file_updater(path, file_content):
    with open(path, 'w',encoding='utf-8') as f:  # with方式可以避免没有关闭资源文件产生错误
        f.write(file_content)
    return True


def result_wrapper(result):
    # TODO　转成枚举的形式,比如result_wrapper.success, result_wrapper.failure
    response = {}
    response["code"] = 20000
    response["message"] = "success"
    response["data"] = result
    return json.dumps(response)


def get_path(fileName):
    # return "./app/scripts/"+fileName+".py"
    # 这样应该可以避免系统差异造成的路径问题
    return os.path.join(os.path.abspath(os.curdir),"app","scripts",f"fn_{fileName}.py")

def get_path_zonghe(fileName):
    # return "./app/scripts/"+fileName+".py"
    # 这样应该可以避免系统差异造成的路径问题
    return os.path.join(os.path.abspath(os.curdir),"app","getcode",f"{fileName}.py")
