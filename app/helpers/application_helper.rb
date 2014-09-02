require 'brahma/factory'
require 'brahma/utils/string_helper'

module ApplicationHelper
  include BrahmaBodhi::ApplicationHelper

  def nav_links
    links = {main_path => t('web.title.main')}
    if current_user
      links[personal_path] = t('web.title.personal')
    end
    links[cms_path] = t('web.title.cms')
    links[wiki_path] = t('web.title.wiki')
    links[rss_path] = t('web.title.rss')
    links[users_path] = t('web.title.users')
    links[about_me_path]=t('web.title.about_me')
    links
  end

  def personal_bar
    if current_user
      label = "#{t('web.label.welcome')}, #{session.fetch :username}"
      links={
          personal_path => t('web.title.personal'),
          brahma_bodhi.personal_logout_path => t('web.link.logout')
      }
    else
      label = t('web.label.register_or_login')
      links={
          Brahma::Factory.instance.oauth2.authorize_url => t('web.link.login')
      }
    end
    {label: label, links: links}
  end

  def tag_links
    links = {}
    Cms::Tag.where(lang: I18n.locale).each { |t| links[cms_tag_path(id: t.id)] = t.name }
    #BrahmaBodhi::FriendLink.all.each { |fl| links["http://#{fl.domain}"] = fl.name }
    links
  end

  def hot_bars
    sh = Brahma::Utils::StringHelper
    bars = []

    abar = {title: t('web.title.hot_articles'), links: []}
    Cms::Article.select(:id, :title, :last_edit).where(lang: I18n.locale).order(visits: :desc).limit(12).each { |a| abar[:links] << [cms_article_path(a.id), a.title.truncate(80), time_ago_in_words(a.last_edit)] }
    bars << abar

    cbar = {title: t('web.title.recent_comments'), links: []}
    Cms::Comment.select(:id, :content, :last_edit).where(lang: I18n.locale).order(id: :desc).limit(12).each { |c| cbar[:links] << [cms_comment_path(a.id), sh.html2text(c.content).truncate(80), time_ago_in_words(c.last_edit)] }
    bars << cbar

    bars << notice_bar
    bars
  end

end
