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
      tab = Brahma::Web::Table.new domains_path, '域名列表', %w(ID 名称 创建时间)
      Domain.all.each do |d|
        tab.insert [d.id, d.name, d.created_at], [
            ['info', 'GET', domain_path(d.id), '查看'],
            ['warning', 'GET', edit_domain_path(d.id), '编辑'],
            ['danger', 'DELETE', domain_path(d.id), '删除']
        ]
      end
      tab.toolbar = [['primary', 'GET', new_domain_path, '新增']]
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
        lst.add "默认语言：#{d.lang}"
        lst.add "Google File: google#{d.google}.html"
        lst.add "百度文件：baidu_verify_#{d.baidu}.html"
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
        d.update name: name, lang: params[:lang],baidu:params[:baidu], google:params[:google]
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
      fm = Brahma::Web::Form.new "编辑域名[#{did}]", domain_path(did)
      d = Domain.find_by id: did
      fm.method = 'PUT'
      fm.text 'name', '名称', d.name
      fm.radio 'lang', '默认语言', d.lang, Brahma::LOCALES_OPTIONS
      fm.text 'google', 'Google ID', d.google
      fm.text 'baidu', '百度 ID', d.baidu
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
        Domain.create name: name, lang: params[:lang],baidu:params[:baidu], google:params[:google]
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
      fm = Brahma::Web::Form.new '新增域名', domains_path
      fm.text 'name', '名称'
      fm.radio 'lang', '默认语言', Brahma::Brahma::LOCALES[0], Brahma::LOCALES_OPTIONS
      fm.text 'google', 'Google ID'
      fm.text 'baidu', '百度 ID'
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

end
