require 'brahma/web/fall'
require 'brahma/services/site'

class MainController < ApplicationController
  def index
    main = Brahma::SettingService.get 'site.index'
    if main && !['/main', '', '/'].include?(main)
      redirect_to main, status: 301
      return
    end

    articles = Cms::Article.order(created: :desc).limit(20)

    respond_to do |fmt|
      fmt.html do
        @title = '主页'
        @fall_card = Brahma::Web::FallCard.new nil
        articles.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
        render 'cms/articles/list'
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

  def notices
    render 'brahma_bodhi/main/notices'
  end

end
