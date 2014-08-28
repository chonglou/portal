require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/validator'
require 'brahma/web/response'

class FriendLinksController < ApplicationController
  def index
    if admin?
      flag = "?site=#{params[:site]}"
      tab = Brahma::Web::Table.new "#{friend_links_path}#{flag}", '友情链接列表', %w(ID 名称 地址 图标)
      BrahmaBodhi::FriendLink.where(site_id:params[:site]).each do |fl|
        tab.insert [fl.id,
                    fl.name,
                    "<a href='http://#{fl.domain}' target='_blank'>http://#{fl.domain}</a>",
                    "<img src='#{fl.logo}'>"],
                   [
                       ['success', 'GET', "#{brahma_bodhi.admin_friend_links_path}/#{fl.id}", '查看'],
                       ['warning', 'GET', "#{brahma_bodhi.admin_friend_links_path}/#{fl.id}/edit", '编辑'],
                       ['danger', 'DELETE', "#{brahma_bodhi.admin_friend_links_path}/#{fl.id}", '删除']
                   ]
      end
      tab.toolbar = [['primary', 'GET', "#{friend_links_path}/new#{flag}", '新增']]
      tab.ok = true
      render(json: tab.to_h)
    end
  end

  def create
    if admin?
      vat = Brahma::Web::Validator.new params
      vat.empty? :name, '名称'
      vat.empty? :domain, '域名'
      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        BrahmaBodhi::FriendLink.create domain: params[:domain], site_id: params[:site], name: params[:name], logo: params[:logo], created: Time.now
        dlg.ok = true
      else
        dlg.data += vat.messages
      end
      render(json: dlg.to_h)
    end
  end

  def new
    if admin?
      fm = Brahma::Web::Form.new '新增友情链接', friend_links_path
      fm.ok = true
      fm.hidden 'site', params[:site]
      fm.text 'name', '名称'
      fm.text 'domain', '域名'
      fm.text 'logo', '图标'
      render(json: fm.to_h)
    end
  end
end
