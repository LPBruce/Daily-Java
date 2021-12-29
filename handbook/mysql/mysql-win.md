### Window安装与使用Mysql

参考：https://blog.csdn.net/ychgyyn/article/details/84404217

1. 初始化数据库：用管理员打开CMD，切换到MySql的解压目录下的bin目录：`mysqld --initialize --console`
2. 输入`mysqld -install`将mysql安装为Windows的服务：
3. `net start mysql`或`sc start mysql`启动mysql服务
4. `mysql -u root -p`登录数据库
5. `alter user 'root'@'localhost' identified by '密码';` 修改密码
   `commit;`提交
6. `quit`退出数据库
7. 配置环境变量
8. 停止MySQL服务：`net stop mysqld`或`sc stop mysqld`
9. 删除MySQL服务：`sc delete mysqld`或`mysqld -remove`（需先停止服务）

