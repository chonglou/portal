require 'brahma/web/fall'

class MainController < ApplicationController
  def index
    articles = Cms::Article.order(created: :desc).limit(20)

    respond_to do |fmt|
      fmt.html do
        @title = '主页'
        @fall_card = Brahma::Web::FallCard.new nil
        articles.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
      end
      fmt.atom do
        @articles=articles
        render action: 'index', layout: false
      end
    end
  end

  def about_me
    render 'brahma_bodhi/main/about_me'
  end
end
