require 'brahma/web/fall'

class CmsController < ApplicationController
  def index

    @title = '主页'
    @fall_card = Brahma::Web::FallCard.new nil
    lang = I18n.locale
    Cms::Article.select(:id, :title, :summary, :logo).where(lang: lang).order(created: :desc).limit(20).each { |a| @fall_card.add cms_article_path(a.id), a.title, a.summary, a.logo }
    @tags = Cms::Tag.select(:id, :name).where(lang: lang).order(visits: :desc).limit(10)
  end
end
