require 'brahma/utils/string_helper'
require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/table'
require 'brahma/web/validator'

class Rss::SitesController < ApplicationController
  before_action :must_admin!

  def index
    tab = Brahma::Web::Table.new '/rss/sites', '站点列表', %w(ID 名称 类型 创建时间)
    Rss::Site.order(id: :desc).all.each do |s|
      tab.insert [s.id, "<a target='_blank' href='#{s.url}'>#{s.name}</a>", s.flag, s.created], [
          ['info', 'GET', "/rss/sites/#{s.id}", '查看'],
          ['warning', 'GET', "/rss/sites/#{s.id}/edit", '编辑'],
          ['danger', 'DELETE', "/rss/sites/#{s.id}", '删除']
      ]
    end
    tab.toolbar = [%w(primary GET /rss/sites/new 新增)]
    tab.ok = true
    render json: tab.to_h
  end

  def destroy
    site = Rss::Site.find_by id: params[:id]
    size = Rss::Item.count site_id: params[:id]
    dlg = Brahma::Web::Dialog.new
    if site && size == 0
      Brahma::LogService.add "删除RSS源[#{site.id}]", current_user.fetch(:id)
      site.destroy
      dlg.ok = true
    else
      dlg.add '没有权限'
    end
    render(json: dlg.to_h)
  end

  def show
    s = Rss::Site.find_by id: params[:id]
    if s
      lst = Brahma::Web::List.new "站点#{s.id}"
      lst.add "名称：#{s.name}"
      lst.add "地址：#{s.url}"
      render json: lst.to_h
    else
      not_found
    end
  end

  def update
    vat = Brahma::Web::Validator.new params
    vat.empty? :name, '名称'
    vat.empty? :url, '地址'
    dlg = Brahma::Web::Dialog.new

    url = params[:url]
    ex = Rss::Site.find_by url: url
    if ex && ex.id !=params[:id].to_i
      vat.add '地址已存在'
    end
    if vat.ok?
      s = Rss::Site.find_by id: params[:id]
      s.update name: params[:name], url: url, flag: params[:flag]
      dlg.ok = true
    else
      dlg.data += vat.messages
    end
    render json: dlg.to_h
  end

  def edit
    tid = params[:id]
    fm = Brahma::Web::Form.new "编辑RSS[#{tid}]", "/rss/sites/#{tid}"
    site = Rss::Site.find_by id: tid
    fm.method = 'PUT'
    fm.text 'name', '名称', site.name
    fm.radio 'flag', '类型', site.flag, rss_flag_options
    fm.text 'url', '地址', site.url, 720
    fm.ok = true
    render json: fm.to_h
  end

  def create
    vat = Brahma::Web::Validator.new params
    vat.empty? :name, '名称'
    vat.empty? :url, '地址'

    url = params[:url]
    dlg = Brahma::Web::Dialog.new
    if url && Rss::Site.find_by(url: url)
      vat.add '地址已存在'
    end

    if vat.ok?
      Rss::Site.create name: params[:name], flag: params[:flag], url: url, created: Time.now
      dlg.ok = true
    else
      dlg.data += vat.messages
    end
    render json: dlg.to_h
  end

  def new
    fm = Brahma::Web::Form.new '新增RSS源', '/rss/sites'
    fm.text 'name', '名称'
    fm.radio 'flag', '类型', 'rss', rss_flag_options
    fm.text 'url', '地址', '', 720
    fm.ok = true
    render json: fm.to_h
  end

  private
  def must_admin!
    unless admin?
      not_found
    end
  end

  def rss_flag_options
    [%w(rss RSS), %w(atom ATOM)]
  end

end
