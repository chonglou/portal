require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/services/site'
require 'brahma/locales'

class DomainsController < ApplicationController
  def index
    if admin?
      tab = Brahma::Web::Table.new '/domains', '域名列表', %w(ID 名称 创建时间)
      Domain.all.each do |d|
        tab.insert [d.id, d.name, d.created_at], [
            ['info', 'GET', "/domains/#{d.id}", '查看'],
            ['warning', 'GET', "/domains/#{d.id}/edit", '编辑'],
            ['danger', 'DELETE', "/domains/#{d.id}", '删除']
        ]
      end
      tab.toolbar = [%w(primary GET /domains/new 新增)]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    if admin?
      dlg = Brahma::Web::Dialog.new
      dlg.add '暂不支持'
      render(json: dlg.to_h) and return
    end
    not_found
  end

  def show

    if admin?
      id = params[:id]
      d = Domain.find_by id: id
      if d
        lst = Brahma::Web::List.new "域名[#{d.id}]"
        lst.add "名称：#{d.name}"
        lst.add "默认语言：#{Brahma::Locales.label d.lang}"
        lst.add "创建：#{d.created_at}"
        lst.add "更新：#{d.updated_at}"
        render(json: lst.to_h) and return
      end
    end

    not_found
  end

  def update
    if admin?
      vat = Brahma::Web::Validator.new params
      vat.empty? :name, '名称'
      dlg = Brahma::Web::Dialog.new

      name = params[:name]

      ex = Domain.find_by(name: name)
      if ex && ex.id !=params[:id].to_i
        vat.add '名称已存在'
      end

      if vat.ok?
        d = Domain.find_by id: params[:id]
        d.update name: name, lang:params[:lang]
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
      did = params[:id]
      fm = Brahma::Web::Form.new "编辑域名[#{did}]", "/domains/#{did}"
      d = Domain.find_by id: did
      fm.method = 'PUT'
      fm.text 'name', '名称', d.name
      fm.radio 'lang', '默认语言', d.lang, Brahma::Locales.options
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
      if name && Domain.find_by(name: name)
        vat.add '名称已存在'
      end

      if vat.ok?
        Domain.create name: name, lang:params[:lang]
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
      fm = Brahma::Web::Form.new '新增域名', '/domains'
      fm.text 'name', '名称'
      fm.radio 'lang', '默认语言', Brahma::Locales::ZH_CN, Brahma::Locales.options
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

end
