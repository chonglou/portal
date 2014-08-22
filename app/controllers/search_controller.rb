require 'brahma/config/site'
class SearchController < ApplicationController
  def index
    if Rails.env.protection?
      keys = params[:keyword].gsub(' ', '+')
      domain = Brahma::Config::Site.new.load(Rails.env).fetch :host
      redirect_to "http://www.google.com/search?q=site:#{domain}+#{keys}"
      #redirect_to "http://www.baidu.com/s?wd=site%3A#{domain}+#{keys}"
    end
  end
end
