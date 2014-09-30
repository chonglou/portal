require 'brahma/web/response'
require 'brahma/web/fall'
require 'brahma/services/site'
require 'brahma/utils/rss'

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

  def reading
    case request.method
      when 'POST'
        val = Brahma::Web::Response.new

        begin
          items = []
          title = Brahma::Utils::RssHelper.list(params.fetch(:url)) { |link, title, body| items << {link: link, title: title, body: body} }
          val.add title
          val.add items
          val.ok = true
        rescue => e
          val.add e.message
        end
        render json: val.to_json
      when 'GET'
        render 'main/reading'
      else
        not_found
    end

  end

  def about_me
    render 'brahma_bodhi/main/about_me'
  end

  def errors
    redirect_to brahma_bodhi.main_errors_path(id: params[:id]), status: 301
  end

  def notices
    render 'brahma_bodhi/main/notices'
  end


end
