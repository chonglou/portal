require 'brahma/daemon'
require 'brahma/services/site'
require 'brahma/utils/wiki'
require 'brahma/utils/rss'
require 'eventmachine'

module Brahma

  class Timer < Brahma::Daemon
    def initialize
      super 'portal-worker', "#{Rails.root}/tmp/pids"
    end

    def run
      super do

        EventMachine.run do
          Signal.trap('INT') { EventMachine.stop }
          Signal.trap('TERM') { EventMachine.stop }

          EventMachine.add_periodic_timer(60*60*24) do
            begin
              @logger.info '更新知识库'
              wiki = Brahma::SettingService.get('site.wiki')
              if wiki
                @logger.info Brahma::Utils::WikiHelper.update(wiki)
              end

              @logger.info '更新RSS文章'
              Rss::Site.all.each do |site|
                require 'brahma/utils/rss'
                i=0
                Brahma::Utils::RssHelper.list(site.url) do |link, title, content|
                  unless Rss::Item.find_by(link: link)
                    Rss::Item.create site_id: site.id, title: title, link: link, content: content, created: Time.now
                    i+=1
                  end
                end
                @logger.info "[抓取#{site.name}] #{i}条"
              end

              @logger.info '生成SEO文件'
              @logger.info `cd #{Rails.root} && rake brahma:web:seo`

            rescue => e
              @logger.error e.message
            end
          end
        end
      end
    end
  end
end