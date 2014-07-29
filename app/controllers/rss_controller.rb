require 'brahma/web/fall'
require 'brahma/services/site'

class RssController < ApplicationController
  PAGE_SIZE=20

  def index
    redirect_to action: :page, id: 1, status: 301
  end

  def page
    title = '网络文摘'
    @title = title
    @total = Rss::Item.count/PAGE_SIZE+1
    @index = (params[:id] || 1).to_i
    if @index<1
      @index = 1
    elsif @index>@total
      @index = @total
    end

    @fall_card = Brahma::Web::FallCard.new title
    Rss::Item.select(:id, :title).order(id: :desc).limit(PAGE_SIZE).offset(PAGE_SIZE*(@index-1)).each { |i| @fall_card.add "/rss/item/#{i.id}", i.title, nil, nil }


    render 'rss/list'
  end

  def feeds
    list = []
    Rss::Site.all.each { |s| list<<{name: s.name, url: s.url, type: s.flag} }
    bs = Brahma::SettingService
    render json: {
        title:bs.get('site.title'),
        version:bs.version,
        feeds: list,
        created: Time.now
    }
  end

  def show
    id = params[:id]
    if id
      i = Rss::Item.find_by id: id
      if i
        @title = i.title
        @index = '/rss'
        @item = i
        @items = Rss::Item.select(:id, :title).where('id < ?', i.id+6).last(10)
        render 'rss/show'
      else
        not_found
      end
    end
  end
end
