require 'brahma/factory'
require 'brahma/utils/string_helper'

module ApplicationHelper
  include BrahmaBodhi::ApplicationHelper
  include ShareHelper

  def nav_links
    links = {main_app.main_path => '本站首页'}
    if current_user
      links['/personal'] = '用户中心'
      links['/site'] = '网站中心'
    end
    links['/cms'] = '资源中心'
    links['/wiki'] = '知识库'
    links['/rss'] = '网络文摘'
    links['/user'] = '用户列表'
    links['/about_me']='关于我们'
    links
  end

  def personal_bar
    if current_user
      label = "欢迎你, #{session.fetch :username}"
      links={
          personal_path => '个人中心',
          site_path => '网站中心',
          brahma_bodhi.personal_logout_path => '安全退出'
      }
    else
      label = '注册/登录'
      links={
          Brahma::Factory.instance.oauth2.authorize_url => 'BRAHMA通行证'
      }
    end
    {label: label, links: links}
  end

  def tag_links
    links = {}
    Cms::Tag.all.each { |t| links["/cms/tags/#{t.id}"] = t.name }
    #BrahmaBodhi::FriendLink.all.each { |fl| links["http://#{fl.domain}"] = fl.name }
    links
  end

  def hot_bars
    sh = Brahma::Utils::StringHelper
    bars = []

    abar = {title: '热门文章', links: []}
    Cms::Article.select(:id, :title, :last_edit).order(visits: :desc).limit(12).each { |a| abar[:links] << ["/cms/articles/#{a.id}", a.title.truncate(80), time_ago_in_words(a.last_edit)] }
    bars << abar

    cbar = {title: '最新评论', links: []}
    Cms::Comment.select(:id, :content, :last_edit).order(id: :desc).limit(12).each { |c| cbar[:links] << ["/cms/comments/#{c.id}", sh.html2text(c.content).truncate(80), time_ago_in_words(c.last_edit)] }
    bars << cbar

    bars << notice_bar
    bars
  end

end
