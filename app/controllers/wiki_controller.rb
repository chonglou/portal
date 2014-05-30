require 'brahma/utils/string_helper'
require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'
require 'brahma/utils/wiki'

class WikiController < ApplicationController


  def index
    @index = '/wiki'
    @title = '知识库列表'
    @fall_link = Brahma::Web::FallLink.new @title

    Brahma::Utils::WikiHelper.each {|name| @fall_link.add "/wiki/#{name}", name}
  end

  def show
    @index = '/wiki'
    name = params[:name]
    @title = "知识库-#{name}"
    @body = Brahma::Utils::WikiHelper.get name
  end

  def git
    if admin?
      sd = Brahma::SettingService
      case request.method
        when 'GET'
          wiki = sd.get('site.wiki') || {}
          fm = Brahma::Web::Form.new 'GIT源', '/wiki/git'
          fm.text 'url', '地址', wiki[:url], 560
          fm.ok = true
          render(json: fm.to_h) and return
        when 'POST'
          url = params[:url]
          dlg = Brahma::Web::Dialog.new
          dlg.data += Brahma::Utils::WikiHelper.update(url).split("\n")
          sd.set 'site.wiki', {url: url}
          dlg.ok = true
          render(json: dlg.to_h) and return
        else
      end
    end
    not_found
  end

end
