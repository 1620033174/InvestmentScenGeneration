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

stockList = ['002323.XSHE', '688111.XSHG', '600763.XSHG', '000088.XSHE', '300770.XSHE', '003008.XSHE', '603258.XSHG', '603444.XSHG', '600191.XSHG', '000429.XSHE', '600804.XSHG', '600012.XSHG', '002517.XSHE', '002159.XSHE', '300347.XSHE', '002027.XSHE', '002467.XSHE', '600643.XSHG', '002207.XSHE', '600705.XSHG'] # 设置股票池

strategyLoader(MyStrategy,stockList=stockList,mode="simple")