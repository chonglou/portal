require 'brahma/web/table'
require 'brahma/web/dialog'

class Rss::ItemsController < ApplicationController
  def index
    tab = Brahma::Web::Table.new '/rss/items', '站点列表', %w(ID 名称)
    if admin?
      Rss::UserSite.where(site_id: params[:site]).each do |drs|
        Rss::Item.where(site_id: drs.rss_site_id).each do |r|
          tab.insert [r.id, "<a target='_blank' href='/rss/items/#{r.id}'>#{r.title}</a>"], [
              ['danger', 'DELETE', "/rss/items/#{r.id}", '删除']
          ]
        end

      end
      tab.ok = true
    else
      tab.add '没有权限'
    end
    render json: tab.to_h
  end

  def destroy
    dlg = Brahma::Web::Dialog.new
    if admin?
      Rss::Item.destroy params[:id]
      dlg.add '操作成功'
      dlg.ok = true
    else
      dlg.add '没有权限'
    end
    render json: dlg.to_h
  end


  def show
    id = params[:id]
    if id
      i = Rss::Item.find_by id: id
      if i
        @title = i.title
        @item = i
        @items = Rss::Item.select(:id, :title).where('id < ?', i.id+6).last(10)
        render 'rss/items/show'
      else
        not_found
      end
    end
  end
end
