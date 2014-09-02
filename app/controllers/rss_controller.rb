require 'brahma/web/fall'
require 'brahma/web/response'
require 'brahma/services/site'
require 'brahma/config/site'
require 'brahma/utils/string_helper'

class RssController < ApplicationController

  def index
    redirect_to action: :page, id: 1, status: 301
  end

  def page
    page_size=20
    title = '网络文摘'
    @title = title
    @total = Rss::Item.count/page_size+1
    @index = (params[:id] || 1).to_i
    if @index<1
      @index = 1
    elsif @index>@total
      @index = @total
    end

    @fall_card = Brahma::Web::FallCard.new title
    Rss::Item.select(:id, :title).order(id: :desc).limit(page_size).offset(page_size*(@index-1)).each { |i| @fall_card.add rss_item_path(i.id), i.title, nil, nil }

    render 'rss/list'
  end

  def feeds
    # todo 用于移动客户端
    page_size=4
    list = []
    title = Brahma::SettingService.get('site.title')
    case params[:act]
      when 'feed'
        site = Rss::Site.find_by id: params.fetch(:id)
        index = params.fetch(:index).to_i
        if site
          Rss::Item.select(:link, :title, :content).order(id: :desc).limit(page_size).offset(page_size*(index-1)).where(site_id: site.id).all.each { |i| list<<{link: i.link, title: i.title, summary: i.content} }
        else
          cfg = Brahma::Config::Site.new.load Rails.env
          server = cfg.fetch :server
          Cms::Article.select(:id, :title, :body).order(id: :desc).limit(page_size).offset(page_size*(index-1)).each { |a| list<<{link: "http://#{server}/cms/articles/#{a.id}", title: a.title, summary: a.body} }
        end
      else
        list<<{id: 0, title: title}
        Rss::Site.all.each { |s| list<<{id: s.id, title: s.name} }
    end
    render json: list
    # Rss::Site.all.each { |s| list<<{name: s.name, url: s.url, type: s.flag} }
    # bs = Brahma::SettingService
    # render json: {
    #     title:bs.get('site.title'),
    #     version:bs.version,
    #     feeds: list,
    #     created: Time.now
    # }
  end


  def setup
    if current_user
      bg = Brahma::Web::ButtonGroup.new
      bg.add rss_sites_path, '源列表', 'info'
      if admin?
        bg.add rss_items_path, '文章管理', 'warning'
      end
      render(json: bg.to_h)
    else
      not_found
    end
  end
end
