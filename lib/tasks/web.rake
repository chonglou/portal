namespace :brahma do
  namespace :web do
    desc 'WIKI抓取'
    task wiki: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.wiki
    end

    desc 'RSS采集'
    task rss: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.rss
    end

    desc '生成SEO相关文件'
    task seo: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.sitemap
    end
  end
end