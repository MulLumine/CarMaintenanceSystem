# 汽车保养管理系统
___
本项目是由SpringBoot和Vue2实现的一个小管理系统，是个大二学生做的期末小课设，希望能帮助到其他的大学生们
### 后端部分
#### 主要技术栈为：
SpringBoot，MybatisPlus，SpringCache，SpringSecurity，SpringDataRedis，
JWT，AOP，Lombok，Maven，PostMan，
MySql，Redis
</br>
- 其中SpringBoot作为基础框架
- PostMan用于controller层接口的测试
- Maven作为项目依赖仓库管理工具，以及对后端项目的打包
- MybatisPlus对Mysql数据库进行基本操作
- SpringCache，SpringDataRedis，以及Redis实现数据库缓存的功能
- SpringSecurity，JWT保证安全以及权限和token校验
- AOP作为对接口的增强开发以及简化代码
- Lombok用于简化代码
---
### 前端部分
#### 主要技术栈为：
NodeJs，Vue，Vuex，Element UI，Npm，Axios，HTML，CSS，JavaScript，Vue-admin-template
</br>
- NodeJs，Vue作为前端基础框架，以及对前端项目的打包
- Vuex实现不同路由页面之间的数据互通
- Element UI，HTML，CSS作为页面基本布局的实现
- Axios用于发送异步请求
- JavaScript用于前后端数据的绑定和用户交互的设定
- 前端部分主要使用vue-admin-template开源脚手架进行二次开发
---
### 其它
#### 项目部署使用的技术栈有：
Linux，Nginx，FinalShell，Git
<br>
- Git用于对代码进行版本控制和协同开发（虽然是一个人写的，所以并没有协同开发）
- 项目部署在linux服务器上，使用FinalShell进行远程连接，
- 而Nginx则部署静态资源（前端部署），以及反向代理和负载均衡
