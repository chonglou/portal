require 'brahma/web/table'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/web/validator'
require 'brahma/web/response'
require 'brahma/services/site'


class Cms::ArticlesController < ApplicationController
  def index
    user = current_user
    lang = I18n.locale
    if user
      articles = admin? ?
          Cms::Article.where(lang: lang).order(last_edit: :desc) :
          Cms::Article.where(user_id: user.id, lang: lang).order(last_edit: :desc)

      tab = Brahma::Web::Table.new cms_articles_path, '文章列表', %w(ID 标题 上次修改)
      articles.each do |a|
        btns = [['info', 'GOTO', cms_article_path(a.id), '查看']]
        Brahma::LOCALE_OPTIONS.each {|l| btns<<['warning', 'GET', edit_cms_article_path(a.id, lang:l[0]), "#{l[1]}版"]}
        btns << ['danger', 'DELETE', cms_article_path(a.id), '删除']
        tab.insert [a.id, a.title, a.last_edit], btns
      end
      tab.toolbar = [['primary', 'GET', new_cms_article_path, '新增']]
      tab.ok = true
      render json: tab.to_h
    else
      not_found
    end
  end

  def destroy
    user= current_user
    if user
      a = Cms::Article.find_by id: params[:id]
      dlg = Brahma::Web::Dialog.new
      if can_edit?(a)
        Brahma::LogService.add "删除文章[#{a.title}]", user.id
        Brahma::TranslationService.delete('article', a.id, I18n.locale) do |aid|
          Cms::Article.destroy aid
          Cms::ArticleTag.destroy_all article: aid
        end

        dlg.ok = true
      else
        dlg.add '没有权限'
      end
      render(json: dlg.to_h)
    else
      not_found
    end
  end

  def show
    article = Cms::Article.find_by id: params[:id]
    if article
      lang = I18n.locale.to_s
      unless article.lang == lang
        tr = BrahmaBodhi::Translation.find_by id: article.tid
        oid = tr.send lang
        if oid
          redirect_to(cms_article_path(oid)) and return
        end
      end

      article.update visits: article.visits+1
      @article = article
      @tags = article.tags
      @title = article.title
      render 'cms/articles/show'
    else
      not_found
    end

  end

  def update
    user = current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :title, '名称'
      vat.empty? :summary, '摘要'
      vat.empty? :body, '内容'
      a = Cms::Article.find_by id: params[:id]
      unless can_edit?(a)
        vat.add '没有权限'
      end
      dlg = Brahma::Web::Dialog.new
      if vat.ok?
        a.update title: params[:title], summary: params[:summary], body: params[:body],
                 logo: first_logo(params[:body]), last_edit: Time.now

        Cms::ArticleTag.destroy_all article_id: a.id

        if params[:tag]
          params[:tag].each do |tid|
            Cms::ArticleTag.create article_id: a.id, tag_id: tid, created: Time.now
          end
        end
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
    user = current_user
    if user
      article = Cms::Article.find_by id: params[:id]
      lang = params[:lang]

      if can_edit?(article)
        unless lang == I18n.locale.to_s
          article = Brahma::TranslationService.translate(
              'article', article.id,
              I18n.locale, lang,
              ->(id) { Cms::Article.find_by id: id },
              ->(trid) { Cms::Article.create user_id: user.id, logo: article.logo, title: article.title, summary: article.summary, body: article.body, tid: trid, lang: lang, last_edit: Time.now, created: Time.now }
          )
        end

        fm = Brahma::Web::Form.new "编辑文章[#{article.id}, #{lang}]", cms_article_path(article.id)

        fm.text 'title', '标题', article.title
        fm.textarea 'summary', '摘要', article.summary
        fm.html 'body', '内容', article.body
        fm.checkbox 'tag', '标签',
                    Cms::ArticleTag.where(article: params[:id]).map { |at| at.tag_id },
                    Cms::Tag.where(lang: lang).map { |t| [t.id, t.name] }
        fm.method = 'PUT'
        fm.ok = true
      else
        fm.add '没有权限'
      end
      render json: fm.to_h
    else
      not_found
    end
  end

  def create
    user = current_user
    if user
      vat = Brahma::Web::Validator.new params
      vat.empty? :title, '名称'
      vat.empty? :summary, '摘要'
      vat.empty? :body, '内容'
      dlg = Brahma::Web::Dialog.new

      if vat.ok?
        lang = I18n.locale
        Brahma::TranslationService.create('article', lang) do |trid|
          a = Cms::Article.create user_id: user.id, logo: first_logo(params[:body]), lang:lang,
                                  title: params[:title], summary: params[:summary], body: params[:body], tid: trid,
                                  last_edit: Time.now, created: Time.now

          if params[:tag]
            params[:tag].each { |tid| Cms::ArticleTag.create article_id: a.id, tag_id: tid, created: Time.now }
          end
          a.id
        end
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
    if current_user
      fm = Brahma::Web::Form.new '新增文章', cms_articles_path
      fm.text 'title', '标题'
      fm.textarea 'summary', '摘要'
      fm.html 'body', '内容'
      fm.checkbox 'tag', '标签', '', Cms::Tag.where(lang: I18n.locale).map { |t| [t.id, t.name] }
      fm.ok = true
      render json: fm.to_h
    else
      not_found
    end
  end

  private
  def can_edit?(article)
    article && ((article.user_id==current_user.id)||admin?)
  end

  def first_logo(html)
    doc = Nokogiri::HTML(html)
    img = doc.xpath('//img').first
    img.attr('src') if img
  end
end
