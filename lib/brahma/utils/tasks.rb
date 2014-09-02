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

      html = "#{Rails.root}/tmp/html"
      view = ActionView::Base.new "#{Rails.root}/app/views"

      Domain.all.each do |domain|
        log.info "开始导出[#{domain.name}]"

        target="#{html}/#{domain.name}"
        release = "#{html}/#{Time.now.strftime '%Y%m%d%H%M%S'}"
        check_dir! release

        #静态资源
        %w(3rd assets favicon.ico).each { |r| FileUtils.cp_r "#{Rails.root}/public/#{r}", release }
        #主页
        File.open("#{release}/index.html", 'w') { |f| f.puts "<!DOCTYPE html><html><head><meta http-equiv='refresh' content='0; url=http://#{domain.name}/#{domain.lang}/#{domain.home}.html' /></head><body></body></html>" }

        #-------------seo及error文件---------------------
        File.open("#{release}/google#{domain.google}.html", 'w') { |f| f.write "google-site-verification: google#{domain.google}.html" }
        File.open("#{release}/baidu_verify_#{domain.baidu}.html", 'w') { |f| f.write domain.baidu }
        File.open("#{release}/robots.txt", 'w') do |f|
          f.puts 'User-agent: *'
          f.puts 'Disallow: '
          f.puts 'Crawl-delay: 10'
          f.puts "Sitemap: http://#{domain.name}/sitemap.xml.gz"
        end
        File.open("#{release}/404.html", 'w') do |f|
          f.write view.render('extra/e404')
        end

        require 'brahma/utils/wiki'

        SitemapGenerator::Sitemap.default_host = "http://#{domain.name}"
        SitemapGenerator::Sitemap.create do
          #'always', 'hourly', 'daily', 'weekly', 'monthly', 'yearly' or 'never'
          add '/main.html', changefreq: 'weekly', priority: 0.9
          add '/wiki.html', changefreq: 'weekly', priority: 0.9
          add '/rss.html', changefreq: 'daily', priority: 0.9
          add '/about_me.html', changefreq: 'yearly', priority: 0.9
          domain.sites.each do |site|
            Cms::Tag.select(:id).where(site_id:site.id).each { |t| add "/cms/tags/#{t.id}.html", changefreq: 'monthly' }
            Cms::Article.select(:id).where(site_id:site.id).each { |a| add "/cms/articles/#{a.id}.html", changefreq: 'weekly' }
            Wiki.select(:url).where(site_id:site.id).each{|wiki|Brahma::Utils::WikiHelper.each(wiki.url) { |w| add "/wiki/#{w}.html", changefreq: 'monthly' }}
            Rss::UserSite.select(:rss_site_id).where(site_id:site.id).each{|us| Rss::Item.select(:id).where(site_id:us.rss_site_id).each { |i| add "/rss/items/#{i.id}", changefreq: 'yearly' }}
          end

        end
        log.info '生成sitemap完毕'

        domain.sites.each do |site|
          log.info "处理语言#{bl.label site.lang}"
          root="#{release}/#{site.lang}"
          check_dir! root


          #---------CMS页面--------------

          #todo /about_me
          #todo /search
          #todo /user /user/1

          #todo /cms /cms/tags /cms/articles/1
          #todo /wiki /wiki/path/name
          #todo /rss /rss/items/1 /rss/page/1
          #todo /archive/2014/03 /archive/2014/03/09


        end


        if Dir.exist?("#{target}-bak")
          FileUtils.rm_r "#{target}-bak"
        end
        if Dir.exist?(target)
          FileUtils.mv "#{html}/#{domain.name}", "#{html}/#{domain.name}-bak"
        end
        FileUtils.mv release, "#{html}/#{domain.name}"

        log.info '站点导出完毕'
      end

    end

    def search
      #todo
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
      require 'brahma/services/site'
      require 'brahma/utils/wiki'
      Rails.logger.info Brahma::Utils::WikiHelper.update(Brahma::SettingService.get('site.wiki'))
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
        add '/cms', changefreq: 'weekly', priority: 0.9
        add '/wiki', changefreq: 'weekly', priority: 0.9
        add '/rss', changefreq: 'daily', priority: 0.9
        add '/about_me', changefreq: 'yearly', priority: 0.9
        Cms::Tag.select(:id).all.each { |t| add "/cms/tags/#{t.id}", changefreq: 'monthly' }
        Cms::Article.select(:id).all.each { |a| add "/cms/articles/#{a.id}", changefreq: 'weekly' }
        Brahma::Utils::WikiHelper.each { |w| add "/wiki/#{w}", changefreq: 'monthly' }
        Rss::Item.select(:id).all.each { |i| add "/rss/items/#{i.id}", changefreq: 'yearly' }
      end
      Rails.logger.info '生成sitemap完毕'
    end
  end
end