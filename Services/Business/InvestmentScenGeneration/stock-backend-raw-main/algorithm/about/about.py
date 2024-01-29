import pandas as pd
import json

def get_result(stock_id):
    with open('algorithm/about/about.json', 'r', encoding='utf-8') as about_file:
        data = json.load(about_file)
        return data