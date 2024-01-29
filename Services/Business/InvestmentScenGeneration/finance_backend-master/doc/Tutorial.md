技术栈
* spring-boot
* mybatis-plus 简化开发,增强可维护性,基本不用手写基础sql了
* lombok 简化代码
  * https://projectlombok.org/features/
* hutool 第三方java工具类库,简化代码

项目结构
* controller 处理请求, 接口相关的和业务逻辑的内容都在这
* entity 实体类
  * po 每个类对应一个数据库表,严格对应(Persistant Object 持久对象)
  * vo （Value Object）值对象,展示用的数据,主要的存在形式就是js里面的对象
* mapper 只有一些模式化的代码了
* service 只有一些模式化的代码了
* utils 自定义的工具方法


连接数据库相关
```shell
cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
.\mysql.exe -u root -P 32769 -h localhost -p
```