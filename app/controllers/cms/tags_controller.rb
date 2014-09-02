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
    respond_to do |fmt|
      fmt.json do
        if admin?
          tab = Brahma::Web::Table.new cms_tags_path, '标签列表', %w(ID 名称 类型 创建时间)
          Brahma::TranslationService.each('tag', I18n.locale) do |tr|
            t = Cms::Tag.find_by id:tr.send(I18n.locale)
            btns = [['info', 'GOTO', cms_tag_path(t.id), '查看']]
            Brahma::LOCALES_OPTIONS.each {|l| btns << ['warning', 'GET', edit_cms_tag_path(t.id, lang:l[0]), "#{l[1]}版"]}
            btns << ['danger', 'DELETE', cms_tag_path(t.id), '删除']
            tab.insert [t.id, t.name, t.flag, t.created], btns
          end
          tab.toolbar = [['primary', 'GET', new_cms_tag_path, '新增']]
          tab.ok = true
          render json: tab.to_h
        else
          not_found
        end
      end

      fmt.html do
        @fall_link = Brahma::Web::FallLink.new t('web.title.tags')
        Brahma::TranslationService.each('tag', I18n.locale) do |tr|
          t = Cms::Tag.find_by id:tr.send(I18n.locale)
          @fall_link.add cms_tag_path(t.id), t.name
        end

      end
    end
  end

  def destroy
    if admin?
      dlg = Brahma::Web::Dialog.new
      tid = params[:id]
      if Cms::ArticleTag.count(tag_id:tid) == 0
        Brahma::TranslationService.delete('tag', tid, I18n.locale){|tid| Cms::Tag.destroy tid}
        dlg.ok = true
      else
        dlg.add '非空 不能删除'
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
        #todo 需要优化
        tag.articles.each { |a| @fall_card.add cms_article_path(a.id), a.title, a.summary, a.logo }
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

      if vat.ok?
        Cms::Tag.update params[:id], name: params[:name], flag: params[:flag]
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

      tag = Cms::Tag.find_by id: tid
      lang = params[:lang]
       unless lang == I18n.locale
          tag = Brahma::TranslationService.translate(
              'tag', tid,
              I18n.locale, lang,
              ->(id) { Cms::Tag.find_by id: id },
              ->(trid) { Cms::Tag.create name: tag.name, tid: trid, flag:tag.flag, created: Time.now }
          )
      end
      fm = Brahma::Web::Form.new "编辑标签[#{tag.id}, #{lang}]", cms_tag_path(tag.id)
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

      if vat.ok?
        Brahma::TranslationService.create('tag', I18n.locale){|tid| Cms::Tag.create(name: name, tid:tid, flag: params[:flag], created: Time.now).id}
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
      fm = Brahma::Web::Form.new '新增标签', cms_tags_path
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
