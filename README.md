# gitlabPatchManage
基于gitlab二次开发的针对git仓库中的项目代码库进行打包、补丁包管理工具

#### 平时我们开发时经常出现以下问题：
1、同一个产品由N多人开发，那么比如涉及产品版本升级时，我们是基于编译过的文件(.class、jar包)进行升级操作的，
   所以针对这种交叉式的开发对于补丁包发布来讲是非常头疼的事情。

####  功能介绍： 
1、登陆功能-- 根据登陆的用户信息，将获取gitlab代码服务器中的该用户具有的所有访问权限的代码仓库列表

2、打包功能-- 针对某一个代码仓库，输入开始提交id(commit的ID)代号、最后提交id代号，
    那么将针对两个提交区间id中的代码进行编译(调用maven命令)、打包(调用maven命令)、压缩操作(程序执行)
    
3、补丁包管理-- 针对每一个人每一次的打包操作都进行记录和补丁包维护操作，比如后期涉及大版本升级(多个补丁包覆盖)可以有效追溯


#### 二次开发说明： 
1、编译gitlab项目的源码包成最新的jar包()

2、将编译的gitlab源码包添加到项目中、当然还有其他依赖的jar包，可以查看pom.xml文件的依赖配置

3、将本项目导入你的IDE中，然后编译成war包，部署到web服务器中运行即可看到系统界面

#### 配置说明： 
1、src\main\resources是所有的资源、配置文件目录

2、gitInfo.xml配置本地仓库的地址和打包后的补丁包的存放地址

3、sql\文件夹下面存放数据库的相关信息表结构和初始化的数据

4、context\文件夹下面存放oracle数据库连接的配置信息

#### 声明： 
本项目是完全根据自己随性的一个想法自由发挥开发出来的一个工具类项目，完全从什么都不了解、一头雾水到慢慢的搜集资料慢慢的琢磨出来的，

所以开发过程中系统没有经过架构和层级的设计，完全是堆代码的方式开发的，所以代码写的比较龊，后期会慢慢的重构，忘理解！


