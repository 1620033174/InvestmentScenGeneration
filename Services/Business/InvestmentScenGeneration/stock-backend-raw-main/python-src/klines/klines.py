import pandas as pd

def get_result():
    data = pd.read_csv("python-src/klines/klines.csv")
    return data
