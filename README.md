portal
======

### 安装
    rake brahma:setup:keys
    rake brahma:setup:mysql
    rake brahma:setup:redis
    rake brahma:setup:secrets
    rake brahma:setup:site
    rale brahma:web:compile

### 启动
    rake brahma:web:start

== README


## 笔记
 * 创建项目：
   rails new brahma_portal -d mysql
 * 创建Model
   rails generate model name --no-test-framework
 * 创建控制器：
   rails generate controller name act1 act2 --no-test-framework --no-assets --no-helper
