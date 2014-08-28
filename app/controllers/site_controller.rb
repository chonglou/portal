require 'brahma/web/response'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/locales'

class SiteController < ApplicationController
  def info
    if current_user
      s = Site.find_by id: params[:site]

      case request.method
        when 'POST'
          if admin?
            dlg = Brahma::Web::Dialog.new
            s.update home: params[:home], title: params[:title], keywords: params[:keywords], description: params[:description], about_me: params[:about_me]
            dlg.ok = true
            render json: dlg.to_h
          end
        when 'GET'
          if admin?
            res = Brahma::Web::Form.new "设置站点[#{s.id}]信息", site_info_path
            res.hidden 'site', s.id
            res.text 'home', '默认主页', s.home
            res.text 'title', '标题', s.title
            res.text 'keywords', '关键字', s.keywords, 640
            res.textarea 'description', '说明信息', s.description
            res.html 'about_me', '关于我们', s.about_me
            res.ok = true
          else
            res = Brahma::Web::List.new "站点[#{s.id}]信息"
            res.add "标题：#{s.title}"
            res.add "关键字：#{s.keywords}"
            res.add "默认主页：#{s.home}"
            res.add "说明信息：#{s.description}"
            res.add "关于我们：#{s.about_me}"
          end
          render json: res.to_h
        else
      end
    end
  end

  def setup
    user = current_user
    s = Site.find_by domain_id: params[:id], lang: params[:lang]
    if user
      bg = Brahma::Web::ButtonGroup.new

      unless s
        s = Site.create title: '', keywords: '', description: '', home: '/main', about_me: '', domain_id: params[:id], lang: params[:lang]
      end

      flag = "?site=#{s.id}"
      bg.add "/site/info#{flag}", '基本信息', 'info'
      bg.add "#{cms_articles_path}#{flag}", '文章列表', 'primary'
      bg.add "#{cms_comments_path}#{flag}", '评论列表', 'success' #todo
      bg.add "#{rss_setup_path}#{flag}", 'RSS设置', 'warning' #todo

      if admin?
        bg.add "#{cms_tags_path}#{flag}", '标签列表', 'danger'
        #todo bg.add @ctl_links["#{wiki_git_path}#{flag}"] = '知识库', 'info'
      end

      render json: bg.to_h
    end
  end

  def index
    user = current_user
    if user
      @ctl_links = {}
      bl = Brahma::Locales
      Domain.all.each do |d|
        bl.options.each { |l| @ctl_links["/site/setup?id=#{d.id}&lang=#{l[0]}"] = "#{d.name}（#{l[1]}）" }
      end
      @ctl_links[site_status_path] = '当前状态'
      goto_admin
    else
      not_found
    end
  end

  def status
    if current_user
      lst = Brahma::Web::List.new '当前状态'
      lst.add "当前支持语言：#{Brahma::Locales.options.map { |l| l[1] }}"
      render json: lst.to_h
    end
  end
end
