require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/services/site'
require 'nokogiri'

class Cms::TagsController < ApplicationController
  def index
    if admin?
      tab = Brahma::Web::Table.new '/cms/tags', '标签列表', %w(ID 名称 类型 创建时间)
      Cms::Tag.order(id: :desc).all.each do |t|
        tab.insert [t.id, t.name, t.flag, t.created], [
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
      size = Cms::ArticleTag.count tag_id: params[:id]
      dlg = Brahma::Web::Dialog.new
      if tag && size == 0
        Brahma::LogService.add "删除标签[#{tag.id}]", current_user.fetch(:id)
        tag.destroy
        dlg.ok = true
      else
        dlg.add '没有权限'
      end
      render(json: dlg.to_h) and return
    end
    not_found
  end

  def show
    id = params[:id]
    if id
      tag = Cms::Tag.find_by id: id
      if tag
        tag.update visits: (tag.visits+1)
        title = "标签-#{tag.name}[#{tag.articles.size}]"
        @title = title
        @fall_card = Brahma::Web::FallCard.new title
        @index = "/cms/tags/#{tag.id}"
        #todo 需要优化
        tag.articles.each { |a| @fall_card.add "/cms/articles/#{a.id}", a.title, a.summary, a.logo }
        render 'cms/articles/list'
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

      ex = Cms::Tag.find_by(name: name)
      if ex && ex.id !=params[:id].to_i
        vat.add '名称已存在'
      end

      if vat.ok?
        tag = Cms::Tag.find_by id: params[:id]
        tag.update name: name, flag: params[:flag]
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
      fm.radio 'flag', '类型', tag.flag, tag_flag_options
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
        Cms::Tag.create name: name, flag: params[:flag], created: Time.now
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
      fm.radio 'flag', '类型', 'default', tag_flag_options
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  private
  def tag_flag_options
    [%w(default 默认), %w(top_nav 顶部导航)]
  end

end
