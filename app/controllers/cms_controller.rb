require 'brahma/web/fall'

class CmsController < ApplicationController
  def index

    @title = '主页'
    @fall_card = Brahma::Web::FallCard.new nil
    Cms::Article.select(:id, :title, :summary, :logo).order(created: :desc).limit(20).each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
    @tags = Cms::Tag.select(:id, :name).order(visits: :desc).limit(10)
  end
end
