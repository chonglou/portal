require 'brahma/utils/string_helper'
require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'
require 'brahma/web/fall'

class WikiController < ApplicationController
  WIKI_ROOT="#{Rails.root}/tmp/wiki"

  def index
    @index = '/wiki'
    @title = '知识库列表'
    @fall_link = Brahma::Web::FallLink.new @title
    len = WIKI_ROOT.size
    Dir.glob("#{WIKI_ROOT}/**/*.md").each do |fn|
      name = fn[len+1, fn.size-len-4]
      @fall_link.add "/wiki/#{name}", name
    end
  end

  def show
    @index = '/wiki'
    @title = "知识库-#{params[:name]}"
    name = "#{WIKI_ROOT}/#{params[:name]}.md"
    if File.file?(name)
      File.open(name, 'r') { |f| @body =Brahma::Utils::StringHelper.md2html f.read }
    else
      not_found
    end
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
          dlg.data += update_wiki_repo(url).split("\n")
          sd.set 'site.wiki', {url: url}
          dlg.ok = true
          render(json: dlg.to_h) and return
        else
      end
    end
    not_found
  end

  private
  def update_wiki_repo(url)
    #todo 防注入
    Dir.exist?(WIKI_ROOT) ? `cd #{WIKI_ROOT} && git pull` : `git clone #{url} #{WIKI_ROOT}`
  end
end
