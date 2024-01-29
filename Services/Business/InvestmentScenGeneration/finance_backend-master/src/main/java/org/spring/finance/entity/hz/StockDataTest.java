package org.spring.finance.entity.hz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class StockDataTest {
    @TableId(type = IdType.AUTO)
    private int id;
    private String code;
    private String pubdate;
    private String stadate;
    private String netProfit;
    private String totalOperatingRevenue;
    private String accountsPayable;
    private String netProfitMargin;
    private String operatingRevenue;
    private String inventories;
    private String operatingCost;

    private String administrationExpense;
    private String operatingExpenseToTotalRevenue;
    private String gaExpenseToTotalRevenue;
    private String marketCap;
    private String saleExpense;

    private String financialExpense;
    private String turnoverRatio;
    private String totalProfit;
    private String eps;
    private String capitalization;

    private String surplusReserveFund;
    private String retainedProfit;
    private String capitalReserveFund;
    private String operatingProfit;
    private String netOperateCashFlow;

    private String retainedProfitPerShare;
    private String capitalReserveFundPerShare;
    private String netProfitPerShare;
    private String operatingProfitPerShare;
    private String netOperateCashFlowPerShare;

    private String netProfitToTotalOperatingRevenue;
    private String inventoryTurnoverRate;
    private String adminExpenseRate;
    private String price;
    private String stockName;

    private String startDate;
    private String companyName;
    private String companyYear;
    private String industryName;

    private String peRatio;
    private String psRatio;
    private String incReturn;
    private String incTotalRevenueYearOnYear;
    private String incRevenueYearOnYear;
    private String incOperationProfitYearOnYear;
    private String incNetProfitYearOnYear;
    private String debtRatio;
    private String dividendYield;
}
