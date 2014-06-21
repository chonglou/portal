portal 网站系统
======

### 安装
    rake brahma:setup:keys
    rake brahma:setup:mysql
    rake brahma:setup:redis
    rake brahma:setup:secrets
    rake brahma:setup:site
    rale brahma:web:compile

### 启动
    rake brahma:web:start	# 站点服务
    rake brahma:timer:start	# 定时任务

