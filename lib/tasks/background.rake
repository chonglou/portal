namespace :brahma do
  namespace :timer do

    desc '启动'
    task :start do
      require 'brahma/backgrounds/timer'
      Brahma::Timer.new.run
    end

    desc '停止'
    task :stop do
      require 'brahma/backgrounds/timer'
      Brahma::Timer.new.kill
    end
  end
end