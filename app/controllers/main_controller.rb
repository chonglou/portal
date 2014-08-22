require 'brahma/web/fall'
require 'brahma/services/site'

class MainController < ApplicationController
  def index
    main = Brahma::SettingService.get 'site.index'
    if main && !['/main', '', '/'].include?(main)
      redirect_to main, status: 301
      return
    end

    respond_to do |fmt|
      fmt.html do
        render 'main/index'
      end
      fmt.atom do
        @articles=Cms::Article.order(created: :desc).limit(20)
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
