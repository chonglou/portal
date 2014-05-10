require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/utils/string_helper'
require 'brahma/services/site'

class WikisController < ApplicationController
  def page
    if params[:name]
      @wiki = Wiki.find_by name:params[:name]
      render 'wikis/show'
    else
      @fall_link = Brahma::Web::FallLink.new '知识库列表'
      Wiki.all.each {|w| @fall_link.add "/wiki/#{w.name}", w.title}
      render 'wikis/index'
    end
  end

  def index
    user = current_user
    if user
      wikis = admin? ? Wiki.all : Wiki.find_by(author: user.fetch(:id))
      tab = Brahma::Web::Table.new '/wikis', '知识库列表', %w(ID 名称 标题 上次编辑)
      wikis.each do |w|
        tab.insert [w.id, w.name, w.title.truncate(50), w.last_edit], [
            ['info', 'GOTO', "/wikis/#{w.id}", '查看'],
            ['warning', 'GET', "/wikis/#{w.id}/edit", '编辑'],
            ['danger', 'DELETE', "/wikis/#{w.id}", '删除']
        ]
      end
      tab.toolbar = [%w(primary GET /wikis/new 新增)]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    wiki = Wiki.find_by id: params[:id]
    dlg = Brahma::Web::Dialog.new
    if can?(wiki)
      Brahma::LogService.add "删除知识库[#{wiki.name}]", current_user.fetch(:id)
      wiki.destroy
      dlg.ok = true
    else
      dlg.add '没有权限'
    end
    render json: dlg.to_h
  end

  def show
    @wiki = Wiki.find_by id: params[:id]
  end

  def update
    vat = Brahma::Web::Validator.new params
    vat.empty? :title, '标题'
    vat.empty? :body, '内容'
    dlg = Brahma::Web::Dialog.new
    if vat.ok?
      wiki = Wiki.find_by id: params[:id]
      if can?(wiki)
        wiki.update title: params[:title], body: params[:body], last_edit: Time.now
        dlg.ok = true
      else
        dlg.add '没有权限'
      end
    else
      dlg.data += vat.messages
    end
    render json: dlg.to_h
  end

  def edit
    wid = params[:id]
    fm = Brahma::Web::Form.new "编辑知识库[#{wid}]", "/wikis/#{wid}"
    wiki = Wiki.find_by id: wid
    if can?(wiki)
      fm.method = 'PUT'
      fm.text 'title', '标题', wiki.title, 560
      fm.textarea 'body', '内容', wiki.body
      fm.ok = true
    else
      fm.add '没有权限'
    end
    render json: fm.to_h
  end

  def create
    vat = Brahma::Web::Validator.new params
    vat.empty? :name, '名称'
    vat.empty? :title, '标题'
    vat.empty? :body, '内容'

    user = current_user
    if user.nil?
      vat.add '没有权限'
    end
    if params[:name] && Wiki.find_by(name: params[:name])
      vat.add '名称已存在'
    end

    dlg = Brahma::Web::Dialog.new
    if vat.ok?
      Wiki.create name: params[:name], title: params[:title], body: params[:body], last_edit: Time.now, created: Time.now, author: user.fetch(:id)
      dlg.ok = true
    else
      dlg.data += vat.messages
    end
    render json: dlg.to_h
  end

  def new
    if current_user
      fm = Brahma::Web::Form.new '新增知识库', '/wikis'
      fm.text 'name', '名称'
      fm.text 'title', '标题', '', 560
      fm.textarea 'body', '内容'
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  private
  def can?(wiki)
    user = current_user
    user && wiki && (wiki.author == user.fetch(:id) || admin?)
  end
end
