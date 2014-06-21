namespace :brahma do
  namespace :web do
    desc '生成SEO相关文件'
    task seo: :environment do
      require 'sitemap_generator'
      require 'brahma/config/site'
      require 'brahma/utils/wiki'
      cfg = Brahma::Config::Site.new.load Rails.env
      SitemapGenerator::Sitemap.default_host = "http://#{cfg.fetch :host}"
      SitemapGenerator::Sitemap.create do
        #'always', 'hourly', 'daily', 'weekly', 'monthly', 'yearly' or 'never'
        add '/main', changefreq: 'weekly', priority: 0.9
        add '/wiki', changefreq: 'weekly', priority: 0.9
        add '/about_me', changefreq: 'yearly', priority: 0.9
        Cms::Tag.select(:id).all.each { |t| add "/cms/tags/#{t.id}", changefreq: 'monthly' }
        Cms::Article.select(:id).all.each { |a| add "/cms/articles/#{a.id}", changefreq: 'weekly' }
        Brahma::Utils::WikiHelper.each { |w| add "/wiki/#{w}", changefreq: 'monthly' }
      end
      puts '生成sitemap完毕'
    end
  end
end