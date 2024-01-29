import backtrader as bt
import pandas as pd
from utils.common import BaseStrategy,strategyLoader

# STAR: 通用策略
class MyStrategy(BaseStrategy):
	def __init__(self,stockList,mode):
		super().__init__(stockList,mode)
		# STAR一些策略的内容
		# self.sma = bt.indicators.SimpleMovingAverage(self.data[stock], period=20)

	def should_buy(self, data):
		# STAR编写你的买入条件判断逻辑
		# 返回 True 表示应该买入，否则返回 False
		return data.close[0] > data.close[-1] and data.volume[0] > data.volume[-1]
		# return data.close[0] > self.sma[0]

	def should_sell(self, data):
		# STAR编写你的卖出条件判断逻辑
		# 返回 True 表示应该卖出，否则返回 False
		return self.position.size > 0 and data.close[0] < data.close[-1]
		# return data.close[0] < self.sma[0]
# 
stockList = ['600519.XSHG', '601398.XSHG', '601939.XSHG', '601318.XSHG', '601628.XSHG', '601288.XSHG', '601988.XSHG', '600036.XSHG', '000858.XSHE', '601857.XSHG'] # 设置股票池

strategyLoader(MyStrategy,stockList=stockList,mode="simple")