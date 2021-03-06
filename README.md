## :sunny:top-housekeeper

<p align='center'>Web试验地</p>

>这是实践Web知识的地方，眼过千遍不如手过一遍，很多时候在动手时会碰到新问题，发现以前从未关注过的点。再加上工作中，很多框架底层都是大佬们搭建好的，我们大多数时刻只能关注业务逻辑，无法更上一层楼，所以就有了这个试验地，让自己成为一次上帝，建造自己的代码世界~

本项目是基于Springboot搭建的，里面使用到的知识，有些会分享在<a href='https://www.cnblogs.com/top-housekeeper/'>我的博客</a>，有任何疑问欢迎`留言和讨论`

如果想使用本项目，需要安装`Redis`，`Mysql`，将src/resources/下的application配置文件格式从.txt改为.properties并填写好内容

>咳咳，如果有收获的话记得给个赞哦！

项目演示地址：<a href='https://www.top-housekeeper.xyz/welcome' target = '_blank'>顶上管家</a>

以下编辑更新于：2020/12/17

## :loudspeaker:项目主要用到的技术

**前端**：Freemark作为模板、Bootstrap框架、JQuery框架、Vue框架

**后端**：Springboot框架、Mybatis框架、Log4j2框架

**数据库**：Mysql

**服务器**：腾讯云服务器（包括域名，证书，Https）

## :golf:项目主要已实现的底层框架

* 前后端统一的交互方式
    * 前端对$.ajax封装，能快速访问后端功能，统一成功与错误处理，数据解析等
    * 后端统一安全拦截（如XSS，CSRF检测） 
    * 后端统一验证码验证

* 后端统一的异常捕获和处理
    * 快速抛出异常信息在前端显示
    * 正常访问与ajax访问的错误跳转

* 后端增强Spring缓存功能（使用Redis）
    * 能自动更新快要过期的缓存
    
## :partly_sunny:项目主要已实现的功能

* 登录与注册功能
    * 实现了自动登录
    * 基于JWT生成的Token进行登录状态的维护
    
* 验证码功能
    * 滑动图片验证
  
## :rainbow:总结

虽然目前实现的功能很少，主要是在搭建底层框架，在这过程中会去了解RESTFUL风格，会去搜索别人实际是如何实现的，会去模仿他人的设计模式，会去感受到技术不是十全十美的，有很多取舍...每一点思考，都是在进步呀~
