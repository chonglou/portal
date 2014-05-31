# config valid only for Capistrano 3.1
lock '3.2.1'

require_relative 'deploy_env'

set :application, 'portal'
set :repo_url, 'git@github.com:chonglou/portal.git'
set :deploy_to, "/var/www/#{fetch(:domain)}"
set :linked_files, %w{config/database.yml config/site.yml config/jobber.yml config/keys.yml config/secrets.yml config/attachment.yml}
set :linked_dirs, %w{log tmp/pids tmp/cache tmp/wiki tmp/sockets public/3rd}
set :keep_releases, 7
set :log_level, :debug

set :rbenv_ruby, '2.1.2'

namespace :deploy do

  desc '重启应用'
  task :restart do
    on roles(:app), in: :sequence, wait: 5 do
    end
  end

  after :restart, :'puma:restart'
  after :publishing, :restart

  after :restart, :clear_cache do
    on roles(:web), in: :groups, limit: 3, wait: 10 do
      within release_path do
        execute :rake, "RAILS_ENV=#{fetch :rails_env} brahma:web:seo"
      end
    end
  end
end
