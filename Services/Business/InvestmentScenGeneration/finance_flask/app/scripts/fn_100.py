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

stockList = ['300511.XSHE', '603138.XSHG', '300612.XSHE', '603559.XSHG', '300208.XSHE', '002236.XSHE', '603887.XSHG', '000803.XSHE', '002746.XSHE', '300504.XSHE', '300115.XSHE', '603707.XSHG', '000903.XSHE', '300267.XSHE', '300860.XSHE', '300692.XSHE', '002829.XSHE', '300620.XSHE', '603936.XSHG', '603319.XSHG'] # 设置股票池

strategyLoader(MyStrategy,stockList=stockList,mode="simple")