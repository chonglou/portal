require 'brahma/utils/string_helper'
require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/utils/wiki'

class WikiController < ApplicationController

  def index
    @title = t('web.title.wiki')
    @fall_link = Brahma::Web::FallLink.new @title
    Brahma::Utils::WikiHelper.each { |name| @fall_link.add wiki_show_path(name), name }
  end

  def show
    @name = params[:name]
    @title = "知识库-#{@name}"
    @body = Brahma::Utils::WikiHelper.get @name
  end

  def git
    if admin?
      case request.method
        when 'GET'
          fm = Brahma::Web::Form.new 'GIT源', wiki_git_path
          fm.hidden 'site', params[:site]
          fm.text 'url', '地址', Brahma::SettingService.get('site.wiki'), 560
          fm.ok = true
          render(json: fm.to_h) and return
        when 'POST'
          url = params[:url]
          dlg = Brahma::Web::Dialog.new
          #dlg.data += Brahma::Utils::WikiHelper.update(url).split("\n")
          Brahma::SettingService.set 'site.wiki', url
          dlg.ok = true
          render(json: dlg.to_h) and return
        else
      end
    end
    not_found
  end

end
