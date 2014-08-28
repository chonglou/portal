require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/services/site'

class Cms::CommentsController < ApplicationController

  def index
    user = current_user
    if user
      comments = admin? ?
          Cms::Comment.all.order(last_edit: :desc) :
          Cms::Comment.where(user_id: user.id).order(last_edit: :desc)

      tab = Brahma::Web::Table.new "/cms/comments?site=#{params[:site]}", '评论列表', %w(ID 内容 上次修改)
      comments.each do |c|
        #todo N+1查询问题
        if c.article.site_id == params[:site].to_i
          tab.insert [c.id, c.content, c.last_edit], [
              ['info', 'GOTO', "/cms/comments/#{c.id}", '查看'],
              ['warning', 'GET', "/cms/comments/#{c.id}/edit", '编辑'],
              ['danger', 'DELETE', "/cms/comments/#{c.id}", '删除']
          ]
        end
      end
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    user= current_user
    if user
      c = Cms::Comment.find_by id: params[:id]
      dlg = Brahma::Web::Dialog.new
      if can_edit?(c)
        Brahma::LogService.add "删除评论[#{c.id}]", user.id
        c.destroy
        dlg.ok = true
      else
        dlg.add '没有权限'
      end
      render(json: dlg.to_h)
    else
      not_found
    end
  end

  def show
    id = params[:id]
    if id
      c = Cms::Comment.find_by id: id
      if c
        redirect_to "/cms/articles/#{c.article_id}#comment-#{c.id}"
      else
        not_found
      end
    end
  end

  def update
    user = current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :content, '内容'
      c = Cms::Comment.find_by id: params[:id]
      unless can_edit?(c)
        vat.add '没有权限'
      end
      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        c.update content: params[:content], last_edit: Time.now
        dlg.ok = true
      else
        dlg.data += vat.messages
      end
      render json: dlg.to_h
    else
      not_found
    end
  end

  def edit
    user = current_user
    if user
      c = Cms::Comment.find_by id: params[:id]
      fm = Brahma::Web::Form.new '编辑评论', "/cms/comments/#{params[:id]}"
      if can_edit?(c)
        fm.html 'content', '内容', c.content
        fm.method = 'PUT'
        fm.ok = true
      else
        fm.add '没有权限'
      end
      render json: fm.to_h
    else
      not_found
    end
  end

  def create
    user = current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :content, '内容'
      vat.empty? :article, '文章id'
      unless Cms::Article.find_by(id: params[:article])
        vat.add '文章不存在'
        vat.ok = false
      end

      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        Cms::Comment.create article_id: params[:article], user_id: user.id, content: params[:content],
                            last_edit: Time.now, created: Time.now
        dlg.ok = true
      else
        dlg.data += vat.messages
      end
      render json: dlg.to_h
    else
      not_found
    end
  end

  def new
    if current_user
      fm = Brahma::Web::Form.new '发表评论', '/cms/comments'
      fm.hidden 'article', params[:article]
      fm.html 'content', '内容'
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  private
  def can_edit?(comment)
    comment && ((comment.user_id==current_user.id)||admin?)
  end

end
