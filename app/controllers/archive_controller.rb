require 'brahma/utils/time_helper'
require 'brahma/web/fall'

class ArchiveController < ApplicationController
  def show
    year = params[:year].to_i
    month = params[:month].to_i

    if params[:day]
      day = params[:day].to_i
      start, stop = Brahma::Utils::TimeHelper.day_r year, month, day
    else
      start, stop = Brahma::Utils::TimeHelper.month_r year, month
    end

    articles = Cms::Article.where('lang=? AND created>=? AND created<=?',I18n.locale, start, stop).all
    title = "#{t('web.title.archives')}[#{start.strftime '%Y-%m-%d'}, #{stop.strftime '%Y-%m-%d'}]"
    @title = title
    @fall_card = Brahma::Web::FallCard.new title
    articles.each { |a| @fall_card.add cms_article_path(a.id), a.title, a.summary, a.logo }
    render 'cms/articles/list'
  end
end
