require 'brahma/utils/scheduler'

module Brahma::Utils
  module Scheduler
    module_function

    def rss
      Rss::Site.where(enable: true).each do |site|
        begin
          require 'brahma/utils/rss'
          i=0
          name = Brahma::Utils::RssHelper.list(site.url) do |link, title, content|
            unless Rss::Item.find_by(link: link)
              Rss::Item.create site_id: site.id, title: title, link: link, content: content, created: Time.now
              i+=1
            end
          end

          site.update name: name, last_sync: Time.now
          puts "抓取[#{name}] #{i}条"
        rescue OpenURI::HTTPError => e
          puts e
        end

      end
    end

    def wiki
      require 'brahma/services/site'
      require 'brahma/utils/wiki'
      wiki = Brahma::SettingService.get('site.wiki')
      if wiki
        puts Brahma::Utils::WikiHelper.update(wiki.fetch(:url))
      end
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
      puts '生成sitemap完毕'
    end
  end
end