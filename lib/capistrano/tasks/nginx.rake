namespace :nginx do
  desc 'NGINX配置文件'
  task :conf do
    on roles(:web) do |host|
      puts "#{'#'*20} /etc/nginx/vhosts/#{fetch :domain}.conf #{'#'*20}"
      puts <<FILE
upstream #{host}_app {
  server unix://#{fetch :deploy_to}/current/tmp/sockets/puma.sock fail_timeout=0;
}

server {
  listen 80;
  server_name #{fetch :domain};

  root #{fetch :deploy_to}/current/public;
  try_files $uri/index.html $uri @#{host}_app;

  location ~* ^/(assets|3rd|favicon.ico|attachments)/  {
    expires max;
    add_header  Cache-Control public;
  }

  location @#{host}_app {
    proxy_set_header  Host $http_host;
    proxy_set_header  X-Real-IP $remote_addr;
    proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_redirect  off;
    proxy_pass http://#{host}_app;
  }
}
FILE
    end
  end
end
