require 'brahma/web/fall'

class RssController < ApplicationController
  def index
    title = '网络文摘'
    @title = title
    @index = '/rss'
    @fall_card = Brahma::Web::FallCard.new title
    Rss::Item.select(:id, :title).last(60).each { |i| @fall_card.add "/rss/item/#{i.id}", i.title, nil, nil }

    render 'rss/list'
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
