require 'rufus-scheduler'

scheduler = Rufus::Scheduler.new

scheduler.cron '00 03 * * *' do
  require 'brahma/utils/tasks'
  sch = Brahma::Utils::Scheduler
  sch.wiki
  sch.rss
  sch.sitemap
end
