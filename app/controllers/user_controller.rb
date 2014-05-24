require 'brahma/web/fall'
class UserController < ApplicationController
  def index
    title = '用户列表'
    @title = title
    @index = '/user'
    @fall_link = Brahma::Web::FallLink.new title
    BrahmaBodhi::User.select(:id, :username).all.each { |u| @fall_link.add "/user/#{u.id}", u.username }
    render 'user/list'
  end

  def show
    id = params[:id]
    if id
      user = BrahmaBodhi::User.find_by id: id
      if user
        articles = Cms::Article.where(user_id: id).all
        title = "用户-#{user.username}[#{articles.size}]"
        @title = title
        @fall_card = Brahma::Web::FallCard.new title
        @index = '/user'
        #todo 需要优化
        articles.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
        render 'cms/articles/list'
      else
        not_found
      end
    end
  end
end
