import pandas as pd

def get_result():
    data = pd.read_csv("python-src/historyTime/historyTime.csv")
    return data
