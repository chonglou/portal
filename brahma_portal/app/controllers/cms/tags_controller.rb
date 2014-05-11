require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/services/site'

class Cms::TagsController < ApplicationController
  def index
    if admin?
      tab = Brahma::Web::Table.new '/cms/tags', '标签列表', %w(ID 名称 创建时间)
      Cms::Tag.all.each do |t|
        tab.insert [t.id, t.name, t.created], [
            ['info', 'GOTO', "/cms/tags/#{t.id}", '查看'],
            ['warning', 'GET', "/cms/tags/#{t.id}/edit", '编辑'],
            ['danger', 'DELETE', "/cms/tags/#{t.id}", '删除']
        ]
      end
      tab.toolbar = [%w(primary GET /cms/tags/new 新增)]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    if admin?
      tag = Cms::Tag.find_by id: params[:id]
      dlg = Brahma::Web::Dialog.new

      Brahma::LogService.add "删除标签[#{tag.id}]", current_user.fetch(:id)
      tag.destroy
      dlg.ok = true
      render json: dlg.to_h
    else
      not_found
    end
  end

  def show
    id = params[:id]
    if id
      tag = Cms::Tag.find_by id: id
      if tag
        tag.update visits: (tag.visits+1)
        articles = Cms::ArticleTag.where(tag: id)
        title = "标签-#{tag.name}[#{articles.size}]"
        @title = title
        @fall_card = Brahma::Web::FallCard.new title
        articles.map { |at| Cms::Article.select(:id, :logo, :summary).find_by(id: at.article) }.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.logo, a.title, a.summary }
      else
        not_found
      end
    end
  end

  def update
    if admin?
      vat = Brahma::Web::Validator.new params
      vat.empty? :name, '名称'
      dlg = Brahma::Web::Dialog.new

      name = params[:name]
      if name && Cms::Tag.find_by(name: name)
        vat.add '名称已存在'
      end

      if vat.ok?
        tag = Cms::Tag.find_by id: params[:id]
        tag.update name: name
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
    if admin?
      tid = params[:id]
      fm = Brahma::Web::Form.new "编辑标签[#{tid}]", "/cms/tags/#{tid}"
      tag = Cms::Tag.find_by id: tid
      fm.method = 'PUT'
      fm.text 'name', '名称', tag.name

      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  def create
    if admin?
      vat = Brahma::Web::Validator.new params
      vat.empty? :name, '名称'
      name = params[:name]

      dlg = Brahma::Web::Dialog.new
      if name && Cms::Tag.find_by(name: name)
        vat.add '名称已存在'
      end

      if vat.ok?
        Cms::Tag.create name: name, created: Time.now
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
    if admin?
      fm = Brahma::Web::Form.new '新增标签', '/cms/tags'
      fm.text 'name', '名称'
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end
end
