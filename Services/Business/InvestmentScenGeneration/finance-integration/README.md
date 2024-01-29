# [项目地址](https://gitee.com/xiaochenglong_admin_admin/finance_integration/tree/master/)
* [在线文档](https://docs.qq.com/doc/DQm1HZG5PYmR0R1NH)
* [项目进度]（https://docs.qq.com/sheet/DZVBOWm9DZEtPZG1U?tab=BB08J2&u=869c272df73e4b5d9230b0b126056e9a）
* [源项目](https://github.com/PanJiaChen/vue-admin-template) | [源项目文档](https://panjiachen.gitee.io/vue-element-admin-site/zh/) | [live demo](https://panjiachen.github.io/vue-admin-template/#/login?redirect=%2Fdashboard)

# 本项目使用教程
* https://learn.microsoft.com/zh-cn/windows/dev-environment/javascript/nodejs-on-windows
  * 运行setup.exe
    * nvm install 16.18.0
    * nvm use 16.18.0
  * gitlens插件
  * git clone <链接>
  * npm install
  * npm run dev
    * ctrl+C终止
  * 在router/index.js下修改内容，参照其他的部分
    * 在view文件夹下编写自己的代码
  * 每次pull dev分支的代码，然后Merge到自己的feat分支
    * 接着在自己的feat分支上修改
    * 修改完成Push到云端
    * 每过一段时间由成龙把多个feat的内容merge到dev上

> 3.20 增加.nvmrc文件，现在如果在启动时发现node版本出现问题，可以直接在shell中输入`nvm use`, nvm会自动帮你调整node版本到16.18.0
> [参考](https://github.com/nvm-sh/nvm/blob/master/README.md#nvmrc)


# 本项目用到的库和各种工具
* axios
* element-ui ui组件库
* sass  css预处理器
* vuex  集中式状态管理工具
* vuedraggable 拖拽组件库
* wangeditor 富文本编辑器
  * npm install @wangeditor/editor --save
  * npm install @wangeditor/editor-for-vue --save
* [nvm](https://github.com/nvm-sh/nvm#automatically-call-nvm-use)
* 图标的使用
  * https://panjiachen.github.io/vue-element-admin-site/zh/guide/advanced/icon.html#%E7%94%9F%E6%88%90%E5%9B%BE%E6%A0%87%E5%BA%93%E4%BB%A3%E7%A0%81
  * el-icon系列图标通过字体来改变大小和粗细
    * 推荐使用index.scss中的字体大小和字重来改变图标样式

