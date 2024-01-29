package org.spring.finance.utils.result;

import org.spring.finance.entity.hz.StockData;

import java.util.List;

public class StockData_Max_Min_GetMethod {
    public static String get_Method_Value(String name_us, int i, List<StockData> maxList) {
        if (name_us.equals("net_profit_margin")){
            return maxList.get(i).getNetProfitMargin();
        } else if(name_us.equals("inventory_turnover_rate")) {
            return maxList.get(i).getInventoryTurnoverRate();
        }else if(name_us.equals("admin_expense_rate")) {
            return maxList.get(i).getAdminExpenseRate();
        }else if(name_us.equals("market_cap")) {
            return maxList.get(i).getMarketCap();
        }else if(name_us.equals("sale_expense")) {
            return maxList.get(i).getSaleExpense();
        }else if(name_us.equals("financial_expense")) {
            return maxList.get(i).getFinancialExpense();
        }else if(name_us.equals("turnover_ratio")) {
            return maxList.get(i).getTurnoverRatio();
        }else if(name_us.equals("eps")) {
            return maxList.get(i).getEps();
        }else if(name_us.equals("surplus_reserve_fund")) {
            return maxList.get(i).getSurplusReserveFund();
        }else if(name_us.equals("operating_revenue")) {
            return maxList.get(i).getOperatingRevenue();
        }else if(name_us.equals("retained_profit_per_share")) {
            return maxList.get(i).getRetainedProfitPerShare();
        }else if(name_us.equals("capital_reserve_fund_per_share")) {
            return maxList.get(i).getCapitalReserveFundPerShare();
        }else if(name_us.equals("net_profit_per_share")) {
            return maxList.get(i).getNetProfitPerShare();
        }else if(name_us.equals("operating_profit_per_share")) {
            return maxList.get(i).getOperatingProfitPerShare();
        }
        else if(name_us.equals("net_profit")) {
            return maxList.get(i).getNetProfit();
        }
        else if(name_us.equals("net_profit_to_total_operating_revenue")) {
            return maxList.get(i).getNetProfitToTotalOperatingRevenue();
        }
        else  {//name_us.equals("net_operate_cash_flow_per_share"))
            return maxList.get(i).getNetProfitMargin();
        }
    }
}
