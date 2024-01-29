package org.spring.finance.utils.result;

import org.spring.finance.entity.hz.StockData;

import java.util.List;
import java.util.stream.Collectors;

public class FactorToList {

    public static List<String> factor_list(String name_us, List<StockData> resultList){
        if (name_us.equals("net_profit_margin")){
            return resultList.stream().map(StockData::getNetProfitMargin).collect(Collectors.toList());
        } else if(name_us.equals("inventory_turnover_rate")) {
            return resultList.stream().map(StockData::getInventoryTurnoverRate).collect(Collectors.toList());
        }else if(name_us.equals("admin_expense_rate")) {
            return resultList.stream().map(StockData::getAdminExpenseRate).collect(Collectors.toList());
        }else if(name_us.equals("market_cap")) {
            return resultList.stream().map(StockData::getMarketCap).collect(Collectors.toList());
        }else if(name_us.equals("sale_expense")) {
            return resultList.stream().map(StockData::getSaleExpense).collect(Collectors.toList());
        }else if(name_us.equals("financial_expense")) {
            return resultList.stream().map(StockData::getFinancialExpense).collect(Collectors.toList());
        }else if(name_us.equals("turnover_ratio")) {
            return resultList.stream().map(StockData::getTurnoverRatio).collect(Collectors.toList());
        }else if(name_us.equals("eps")) {
            return resultList.stream().map(StockData::getEps).collect(Collectors.toList());
        }else if(name_us.equals("surplus_reserve_fund")) {
            return resultList.stream().map(StockData::getSurplusReserveFund).collect(Collectors.toList());
        }else if(name_us.equals("operating_revenue")) {
            return resultList.stream().map(StockData::getOperatingRevenue).collect(Collectors.toList());
        }else if(name_us.equals("retained_profit_per_share")) {
            return resultList.stream().map(StockData::getRetainedProfitPerShare).collect(Collectors.toList());
        }else if(name_us.equals("capital_reserve_fund_per_share")) {
            return resultList.stream().map(StockData::getCapitalReserveFundPerShare).collect(Collectors.toList());
        }else if(name_us.equals("net_profit_per_share")) {
            return resultList.stream().map(StockData::getNetProfitPerShare).collect(Collectors.toList());
        }else if(name_us.equals("operating_profit_per_share")) {
            return resultList.stream().map(StockData::getOperatingProfitPerShare).collect(Collectors.toList());
        }
        else if(name_us.equals("net_profit")) {
            return resultList.stream().map(StockData::getNetProfit).collect(Collectors.toList());
        }
        else if(name_us.equals("net_profit_to_total_operating_revenue")) {
            return resultList.stream().map(StockData::getNetProfitToTotalOperatingRevenue).collect(Collectors.toList());
        }
        else  {//name_us.equals("net_operate_cash_flow_per_share"))
            return resultList.stream().map(StockData::getNetOperateCashFlowPerShare).collect(Collectors.toList());
        }
    }

    public static List<String> theme_list(String factorTheme, List<StockData> resultList) {
        return resultList.stream().map(StockData::getMarketCap).collect(Collectors.toList());
//        if (factorTheme.equals("market_cap")) {

//        } else if(factorTheme.equals("inventory_turnover_rate")) {
//            return resultList.stream().map(StockData::getInventoryTurnoverRate).collect(Collectors.toList());
//        }else if(factorTheme.equals("admin_expense_rate")) {
//            return resultList.stream().map(StockData::getAdminExpenseRate).collect(Collectors.toList());
//        }

    }
}
