require 'brahma/utils/time_helper'
require 'brahma/web/fall'

class ArchiveController < ApplicationController
  def index
    year = params[:year].to_i
    month = params[:month].to_i

    if params[:day]
      day = params[:day].to_i
      start, stop = Brahma::Utils::TimeHelper.day_r year, month, day
    else
      start, stop = Brahma::Utils::TimeHelper.month_r year, month
    end

    articles = Cms::Article.where('created>=? AND created<=?', start, stop).all
    title = "文章列表[#{start.strftime '%Y-%m-%d'}, #{stop.strftime '%Y-%m-%d'}]"
    @title = title
    @fall_card = Brahma::Web::FallCard.new title
    @index = '/main'
    articles.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
    render 'cms/articles/list'
  end
end
