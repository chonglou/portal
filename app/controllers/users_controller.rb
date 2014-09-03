require 'brahma/web/fall'
class UsersController < ApplicationController
  def index
    title = t('web.title.users')
    @title = title

    @fall_link = Brahma::Web::FallLink.new title
    BrahmaBodhi::User.select(:id, :username).all.each { |u| @fall_link.add user_show_path(u.id), u.username }
    render 'users/list'
  end

  def show
    id = params[:id]
    if id
      user = BrahmaBodhi::User.find_by id: id
      if user
        articles = Cms::Article.where(user_id: id).all
        title = t('web.title.user', name:user.username, size:articles.size)
        @title = title
        @fall_card = Brahma::Web::FallCard.new title
        #todo 需要优化
        articles.each { |a| @fall_card.add cms_article_path(a.id), a.title, a.summary, a.logo }
        render 'cms/articles/list'
      else
        not_found
      end
    end
  end
end
