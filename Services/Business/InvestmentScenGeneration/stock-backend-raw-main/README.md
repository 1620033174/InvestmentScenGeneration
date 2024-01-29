# stock-backend
冷启动更新数据库、数据库表由算法生成

# Jupyter Token
1233ccdb3d1f642dca21b7ed2646df01224d15670aeea220

# Watcher Feature
当在 `windows` 操作系统中使用 `docker` 进行测试时，需要打开 `poll` feature，操作方法为修改 `Dockerfile` 中的第 `24` 行代码为
```bash
RUN cargo build --release --features poll && cp target/release/stock-backend . && rm -rf ./target
```
相比于之前的代码增加了 `--features poll`

# 算法元数据约定
元数据包括 `stock_id`，`_time`，`_stock_name`，这些列不会在页面上显示，这些列用于数据库查询，请在算法生成时包含这些数据。

## python-src 目录
该目录为算法和数据集所在的目录，程序遍历目录下的文件夹，并执行与文件夹同名的 python 文件的 `get_result` 方法获取 Dataframe 并存储进数据库。

### about 目录
该目录为「综合评价与区间预测」页面的数据，


### stocks 目录
该目录为「入选股票」数据集和算法
