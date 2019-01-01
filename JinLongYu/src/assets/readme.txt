开发人员需注意以下事项：
1.若新建页面需加入跟踪统计，必须继承BasePage，并在页面内跳转使用父类push方法；
2.统计上传接口在本项目有若干入口，如需修改则全部需修改。具体在
    1)app.component.ts(启动跳转),LoginPage.ts(登录后setRoot处理),TabsPage.ts(tab切换方法)