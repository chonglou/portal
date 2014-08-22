require 'brahma/daemon'
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
              @logger.info `cd #{Rails.root} && rake brahma:web:wiki`

              @logger.info '更新RSS文章'
              @logger.info `cd #{Rails.root} && rake brahma:web:rss`

              @logger.info '生成SEO文件'
              @logger.info `cd #{Rails.root} && rake brahma:web:seo`

              @logger.info '导入搜索数据库'
              @logger.info `cd #{Rails.root} && rake brahma:web:search`
            rescue => e
              @logger.error e.message
            end
          end
        end
      end
    end
  end
end