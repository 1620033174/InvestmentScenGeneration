import backtrader as bt
import pandas as pd
from utils.common import BaseStrategy,strategyLoader

# STAR: ͨ�ò���
class MyStrategy(BaseStrategy):
	def __init__(self,stockList,mode):
		super().__init__(stockList,mode)
		# STARһЩ���Ե�����
		# self.sma = bt.indicators.SimpleMovingAverage(self.data[stock], period=20)

	def should_buy(self, data):
		# STAR��д������������ж��߼�
		# ���� True ��ʾӦ�����룬���򷵻� False
		return data.close[0] > data.close[-1] and data.volume[0] > data.volume[-1]
		# return data.close[0] > self.sma[0]

	def should_sell(self, data):
		# STAR��д������������ж��߼�
		# ���� True ��ʾӦ�����������򷵻� False
		return self.position.size > 0 and data.close[0] < data.close[-1]
		# return data.close[0] < self.sma[0]

stockList = ['000001.XSHE', '002829.XSH', '603707.XSHG'] # ���ù�Ʊ��

strategyLoader(MyStrategy,stockList=stockList,mode="simple")