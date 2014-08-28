portal 网站系统
======

### 功能简介
 * 多站点管理
 * 多语言支持
 * 生成静态页面

### 依赖环境
 * Linux
 * ruby(2.1.2) 建议使用rbenv
 * bundle
 * imagemagick

### 安装
    git clone git@github.com:chonglou/portal.git
    bundle install

### 设置
    rake brahma:setup:keys
    rake brahma:setup:mysql
    rake brahma:setup:redis
    rake brahma:setup:secrets
    rake brahma:setup:site
    rake brahma:setup:search
    rake brahma:web:compile

### 启动
    rake brahma:web:start	# 站点服务
    rake brahma:timer:start	# 定时任务


### 部署
    cp config/deploy_env.rb.orig  config/deploy_env.rb
    vi config/deploy_env.rb # 设置域名
    cp config/deploy/production.rb.orig config/deploy/production.rb
    vi config/deploy/production.rb # 设置服务器ssh信息
    cap production deploy # 部署
    
