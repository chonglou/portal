require 'brahma/utils/string_helper'
require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/table'
require 'brahma/web/validator'
require 'brahma/utils/rss'

class Rss::SitesController < ApplicationController

  def index
    user = current_user
    if user
      flag = "?site=#{params[:site]}"
      tab = Brahma::Web::Table.new "/rss/sites#{flag}", '站点列表', %w(ID 名称 类型 上次更新)
      sites = admin? ? Rss::UserSite.where(site_id:params[:site]).order(id: :desc) : Rss::UserSite.where(site_id:params[:site], user_id: user.id).order(id: :desc)
      sites.each do |us|
        s = us.rss_site
          tab.insert [s.id, "<a target='_blank' href='#{s.url}'>#{s.name}</a>", s.flag, s.last_sync], [
              ['info', 'GET', "/rss/sites/#{s.id}", '查看'],
              ['danger', 'DELETE', "/rss/sites/#{s.id}", '删除']
          ]

      end
      tab.toolbar = [['primary', 'GET', "/rss/sites/new#{flag}", '新增']]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    user = current_user
    if user
      dlg = Brahma::Web::Dialog.new
      Rss::UserSite.destroy_all(user_id: user.id, rss_site_id: params[:id])

      site = Rss::Site.find_by id: params[:id]
      size = Rss::UserSite.count rss_site_id: params[:id]
      if site && size == 0
        Rss::Site.update params[:id].to_i, enable: false
      end
      Brahma::LogService.add "删除RSS源[#{site.id}]"
      dlg.ok = true
      render(json: dlg.to_h)
    else
      not_found
    end
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

  def create
    user=current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :url, '地址'

      url = params[:url]
      name = 'UNKNOWN'
      dlg = Brahma::Web::Dialog.new
      unless name
        vat.add '链接有误'
      end

      if vat.ok?
        site = Rss::Site.find_by url: url
        unless site
          site = Rss::Site.create name: name,  flag: params[:flag], url: url, enable: true, created: Time.now
        end
        unless site.enable
          site.update enable:true
        end
        if Rss::UserSite.find_by(user_id: user.id, rss_site_id: site.id, site_id:params[:site] )
          dlg.add '地址已存在'
        else
          Rss::UserSite.create user_id: user.id, rss_site_id: site.id, site_id:params[:site]
          dlg.ok = true
        end

      else
        dlg.data += vat.messages
      end
      render json: dlg.to_h
    else
      not_found
    end
  end

  def new
    fm = Brahma::Web::Form.new '新增RSS源', '/rss/sites'
    fm.hidden 'site', params[:site]
    fm.radio 'flag', '类型', 'rss', rss_flag_options
    fm.text 'url', '地址', '', 720
    fm.ok = true
    render json: fm.to_h
  end

  private

  def rss_flag_options
    [%w(rss RSS), %w(atom ATOM)]
  end


end
