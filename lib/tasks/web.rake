namespace :brahma do
  namespace :web do
    desc '导出静态话站点'
    task export: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.export
    end

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

    desc '导入搜索库'
    task search: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.search
    end

    desc '生成SEO相关文件'
    task seo: :environment do
      require 'brahma/utils/tasks'
      Brahma::Utils::Scheduler.sitemap
    end
  end
end