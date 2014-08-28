require 'fileutils'
require 'brahma/locales'
require 'brahma/utils/scheduler'

module Brahma::Utils
  module Scheduler
    module_function

    def check_dir!(d)
      unless Dir.exist?(d)
        FileUtils.mkdir_p d
        Rails.logger.info "创建目录#{d}"
      end
    end

    def export
      log = Rails.logger
      bl = Brahma::Locales

      Domain.all.each do |domain|
        log.info "开始导出[#{domain.name}]"

        release = "#{Rails.root}/tmp/html/#{domain.name}"
        check_dir! release
        File.open("#{release}/index.html", 'w') { |f| f.puts "<!DOCTYPE html><html><head><meta http-equiv='refresh' content='0; url=http://#{domain.name}/#{domain.lang}' /></head><body></body></html>" }

        domain.sites.each do |site|
          log.info "处理语言#{bl.label site.lang}"
          root="#{release}/#{site.lang}"
          check_dir! root
          #todo /404.html /favicon.ico /baidu_verify_aaa.html /google_bbb.html
          #todo /main
          #todo /about_me
          #todo /search
          #todo /user /user/1

          #todo /cms /cms/tags /cms/articles/1
          #todo /wiki /wiki/path/name
          #todo /rss /rss/items/1 /rss/page/1
          #todo /archive/2014/03 /archive/2014/03/09

          #todo /sitemap.xml.gz /robots.txt
        end

        log.info '站点导出完毕'
      end
    end

    def search

    end

    def rss
      Rss::Site.where(enable: true).each do |site|
        # if !site.last_sync.nil? && site.last_sync+60*60*24 >= Time.now
        #   Rails.logger.info "[#{site.name}]已更新"
        #   next
        # end
        site.update last_sync: Time.now
        begin
          require 'brahma/utils/rss'
          i=0
          name = Brahma::Utils::RssHelper.list(site.url) do |link, title, content|
            unless Rss::Item.find_by(link: link)
              Rss::Item.create site_id: site.id, title: title, link: link, content: content, created: Time.now
              i+=1
            end
          end
          unless name == site.name
            site.update name: name
          end
          Rails.logger.info "抓取[#{name}] #{i}条"
        rescue => e
          Rails.logger.error e
        end

      end
    end

    def wiki
      require 'brahma/utils/wiki'
      Wiki.all.each { |w| Rails.logger.info Brahma::Utils::WikiHelper.update(w.url) }
    end

    def sitemap
      require 'sitemap_generator'
      require 'brahma/config/site'
      require 'brahma/utils/wiki'
      cfg = Brahma::Config::Site.new.load Rails.env
      SitemapGenerator::Sitemap.default_host = "http://#{cfg.fetch :host}"
      SitemapGenerator::Sitemap.create do
        #'always', 'hourly', 'daily', 'weekly', 'monthly', 'yearly' or 'never'
        add '/main', changefreq: 'weekly', priority: 0.9
        add '/wiki', changefreq: 'weekly', priority: 0.9
        add '/rss', changefreq: 'daily', priority: 0.9
        add '/about_me', changefreq: 'yearly', priority: 0.9
        Cms::Tag.select(:id).all.each { |t| add "/cms/tags/#{t.id}", changefreq: 'monthly' }
        Cms::Article.select(:id).all.each { |a| add "/cms/articles/#{a.id}", changefreq: 'weekly' }
        Brahma::Utils::WikiHelper.each { |w| add "/wiki/#{w}", changefreq: 'monthly' }
        Rss::Item.select(:id).all.each { |i| add "/rss/item/#{i.id}", changefreq: 'yearly' }
      end
      Rails.logger.info '生成sitemap完毕'
    end
  end
end