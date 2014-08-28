require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/utils/string_helper'

class NoticesController < ApplicationController
  def index
    respond_to do |fmt|
      fmt.html do
        render 'brahma_bodhi/main/notices'
      end
      fmt.json do
        if admin?
          flag = "?site=#{params[:site]}"
          tab = Brahma::Web::Table.new "#{notices_path}#{flag}", '公告列表', %w(ID 内容 最后修改)
          BrahmaBodhi::Notice.where(site_id: params[:site]).each do |n|
            tab.insert [n.id, n.content.truncate(50), n.last_edit],
                       [
                           ['success', 'GET', "#{brahma_bodhi.admin_notices_path}/#{n.id}", '查看'],
                           ['warning', 'GET', "#{brahma_bodhi.admin_notices_path}/#{n.id}/edit", '编辑'],
                           ['danger', 'DELETE', "#{brahma_bodhi.admin_notices_path}/#{n.id}", '删除']
                       ]
          end
          tab.toolbar = [['primary', 'GET', "#{notices_path}/new#{flag}", '新增']]
          tab.ok = true
          render(json: tab.to_h)
        end
      end
    end
    
  end


  def create
    if admin?
      vat = Brahma::Web::Validator.new params
      vat.empty? :content, '内容'
      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        BrahmaBodhi::Notice.create content: params[:content], site_id: params[:site], last_edit: Time.now, created: Time.now
        dlg.ok = true
      else
        dlg.data += vat.messages
      end
      render(json: dlg.to_h)
    end
  end

  def new
    if admin?
      fm = Brahma::Web::Form.new '新增公告', notices_path
      fm.hidden 'site', params[:site]
      fm.textarea 'content', '内容'
      fm.ok = true
      render(json: fm.to_h)
    end
  end
end
