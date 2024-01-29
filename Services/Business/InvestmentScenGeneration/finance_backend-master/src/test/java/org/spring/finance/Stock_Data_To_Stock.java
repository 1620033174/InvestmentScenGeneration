package org.spring.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Stock_Data_To_Stock {
    @Test
    public void Stock_Data_To_Json() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/finance_all?useUnicode=true&characterEncoding=utf8&timeZone=GMT%2B8&useSSL=false";
        String username = "root";
        String password = "123456";

        try {
            // 连接到MySQL数据库
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // 查询原始stock_data表
            String selectQuery = "SELECT * FROM stock_data_test";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            // 插入数据到新的stock表
            String insertQuery = "INSERT INTO stock (code, date, factor_name, factor_value) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String date = resultSet.getString("pubdate");
                String netProfit = resultSet.getString("net_profit");
                String totalOperatingRevenue = resultSet.getString("total_operating_revenue");
                String accountsPayable = resultSet.getString("accounts_payable");

                // 插入net_profit数据
                insertStatement.setString(1, code);
                insertStatement.setString(2, date);
                insertStatement.setString(3, "net_profit");
                insertStatement.setString(4, netProfit);
                insertStatement.executeUpdate();

                // 插入total_operating_revenue数据
                insertStatement.setString(1, code);
                insertStatement.setString(2, date);
                insertStatement.setString(3, "total_operating_revenue");
                insertStatement.setString(4, totalOperatingRevenue);
                insertStatement.executeUpdate();

                // 插入accounts_payable数据
                insertStatement.setString(1, code);
                insertStatement.setString(2, date);
                insertStatement.setString(3, "accounts_payable");
                insertStatement.setString(4, accountsPayable);
                insertStatement.executeUpdate();
            }

            // 关闭数据库连接
            resultSet.close();
            selectStatement.close();
            insertStatement.close();
            connection.close();

            System.out.println("数据导入完成");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // 从stock_data表中读取数据，插入到stock表中
    @Test
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/finance_all?useUnicode=true&characterEncoding=utf8&timeZone=GMT%2B8&useSSL=false";
        String username = "root";
        String password = "123456";
        try {
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // 从stock_data表中读取数据
            String selectQuery = "SELECT * FROM stock_data_test";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStatement.executeQuery();

            // 创建因素名称到列名的映射，用于动态生成插入语句
            Map<String, String> columnMappings = new HashMap<>();
            columnMappings.put("net_profit", "net_profit");
            columnMappings.put("total_operating_revenue", "total_operating_revenue");
            columnMappings.put("accounts_payable", "accounts_payable");
            columnMappings.put("net_profit_margin", "net_profit_margin");
            columnMappings.put("operating_revenue", "operating_revenue");
            columnMappings.put("inventories", "inventories");
            columnMappings.put("operating_cost", "operating_cost");
            columnMappings.put("administration_expense", "administration_expense");
            columnMappings.put("operating_expense_to_total_revenue", "operating_expense_to_total_revenue");
            columnMappings.put("ga_expense_to_total_revenue", "ga_expense_to_total_revenue");
            columnMappings.put("market_cap", "market_cap");
            columnMappings.put("sale_expense", "sale_expense");
            columnMappings.put("financial_expense", "financial_expense");
            columnMappings.put("turnover_ratio", "turnover_ratio");
            columnMappings.put("total_profit", "total_profit");
            columnMappings.put("eps", "eps");
            columnMappings.put("capitalization", "capitalization");
            columnMappings.put("surplus_reserve_fund", "surplus_reserve_fund");
            columnMappings.put("retained_profit", "retained_profit");
            columnMappings.put("capital_reserve_fund", "capital_reserve_fund");
            columnMappings.put("operating_profit", "operating_profit");
            columnMappings.put("net_operate_cash_flow", "net_operate_cash_flow");
            columnMappings.put("retained_profit_per_share", "retained_profit_per_share");
            columnMappings.put("capital_reserve_fund_per_share", "capital_reserve_fund_per_share");
            columnMappings.put("net_profit_per_share", "net_profit_per_share");
            columnMappings.put("operating_profit_per_share", "operating_profit_per_share");
            columnMappings.put("net_operate_cash_flow_per_share", "net_operate_cash_flow_per_share");
            columnMappings.put("net_profit_to_total_operating_revenue", "net_profit_to_total_operating_revenue");
            columnMappings.put("inventory_turnover_rate", "inventory_turnover_rate");
            columnMappings.put("admin_expense_rate", "admin_expense_rate");
            columnMappings.put("price", "price");
            columnMappings.put("start_date", "start_date");
            columnMappings.put("company_name", "company_name");
            columnMappings.put("company_year", "company_year");
            columnMappings.put("industry_name", "industry_name");
            columnMappings.put("pe_ratio", "pe_ratio");
            columnMappings.put("ps_ratio", "ps_ratio");
            columnMappings.put("inc_return", "inc_return");
            columnMappings.put("inc_total_revenue_year_on_year", "inc_total_revenue_year_on_year");
            columnMappings.put("inc_revenue_year_on_year", "inc_revenue_year_on_year");
            columnMappings.put("inc_operation_profit_year_on_year", "inc_operation_profit_year_on_year");
            columnMappings.put("inc_net_profit_year_on_year", "inc_net_profit_year_on_year");
            columnMappings.put("debt_ratio", "debt_ratio");
            columnMappings.put("dividend_yield", "dividend_yield");


            // 创建用于插入数据的SQL语句模板
            String insertQuery = "INSERT INTO stock (code,stock_name, pubdate, date, factor_name, factor_value) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            // 遍历查询结果并插入新表
            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String stockName = resultSet.getString("stock_name");
                String pubdate = resultSet.getString("pubdate");
                String date = resultSet.getString("stadate");

                for (Map.Entry<String, String> entry : columnMappings.entrySet()) {
                    String factorName = entry.getKey();
                    String columnName = entry.getValue();
                    String factorValue = resultSet.getString(columnName);

                    // 插入数据
                    insertStatement.setString(1, code);
                    insertStatement.setString(2, stockName);
                    insertStatement.setString(3, pubdate);
                    insertStatement.setString(4, date);
                    insertStatement.setString(5, factorName);
                    insertStatement.setString(6, factorValue);
                    insertStatement.executeUpdate();
                }
            }

            // 关闭资源
            resultSet.close();
            selectStatement.close();
            insertStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
