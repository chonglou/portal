require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'


class Cms::ArticlesController < ApplicationController
  def index
    user = current_user
    if user
      articles = admin? ?
          Cms::Article.all.order(last_edit: :desc) :
          Cms::Article.where(author: user.fetch(:id)).order(last_edit: :desc)

      tab = Brahma::Web::Table.new '/cms/articles', '文章列表', %w(ID 标题 上次修改)
      articles.each do |a|
        tab.insert [a.id, a.title, a.last_edit], [
            ['info', 'GOTO', "/cms/articles/#{a.id}", '查看'],
            ['warning', 'GET', "/cms/articles/#{a.id}/edit", '编辑'],
            ['danger', 'DELETE', "/cms/articles/#{a.id}", '删除']
        ]
      end
      tab.toolbar = [%w(primary GET /cms/articles/new 新增)]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    user= current_user
    if user
      a = Cms::Article.find_by id: params[:id]
      dlg = Brahma::Web::Dialog.new
      if can_edit?(a)
        Brahma::LogService.add "删除文章[#{a.title}]", user.fetch(:id)
        a.destroy
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
      article = Cms::Article.find_by id: id
      if article
        article.update visits: article.visits+1
        @article = article
        @tags = {}
        Cms::ArticleTag.where(article: id).each { |at| @tags[at.tag] = Cms::Tag.find_by(at.tag).name }
        render 'cms/articles/show'
      else
        not_found
      end
    end
  end

  def update
    user = current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :title, '名称'
      vat.empty? :summary, '摘要'
      vat.empty? :body, '内容'
      a = Cms::Article.find_by id: params[:id]
      unless can_edit?(a)
        vat.add '没有权限'
      end
      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        a.update title: params[:id], summary: params[:summary], body: params[:body],
                 last_edit: Time.now
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
      a = Cms::Article.find_by id: params[:id]
      fm = Brahma::Web::Form.new '编辑文章', "/cms/articles/#{params[:id]}"
      if can_edit?(a)
        fm.text 'title', '标题', a.title
        fm.textarea 'summary', '摘要', a.summary
        fm.html 'body', '内容', a.body
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
      vat.empty? :title, '名称'
      vat.empty? :summary, '摘要'
      vat.empty? :body, '内容'
      dlg = Brahma::Web::Dialog.new

      if vat.ok?
        Cms::Article.create author: user.fetch(:id),
                            title: params[:title], summary: params[:summary], body: params[:body],
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
      fm = Brahma::Web::Form.new '新增文章', '/cms/articles'
      fm.text 'title', '标题'
      fm.textarea 'summary', '摘要'
      fm.html 'body', '内容'
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  private
  def can_edit?(article)
    article && ((article.author==current_user.fetch(:id))||admin?)
  end
end
