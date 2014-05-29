require 'brahma/services/site'
require 'brahma/web/form'
require 'brahma/web/dialog'

class WikiController < ApplicationController
  WIKI_ROOT="#{Rails.root}/tmp/wiki"

  def index
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
          dlg.data += update_wiki_repo( url).split("\n")
          sd.set 'site.wiki', {url:url}
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
