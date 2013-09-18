server {
    listen       80;
    server_name www.0-dong.com 0-dong.com;
    access_log /var/log/nginx/0dong_access.log;
    error_log /var/log/nginx/0-dong_error.log;

	location ~ .*\.(gif|jpg|png|htm|html|css|js|flv|ico|swf)(.*) {
              proxy_pass http://localhost:8080;
              proxy_redirect off;
              proxy_set_header Host $host;
              proxy_cache cache_one;
              proxy_cache_valid 200 302 1h;
              proxy_cache_valid 301 1d;
              proxy_cache_valid any 1m;
              expires 30d;
        }

    location / { 
	proxy_pass http://localhost:8080; 
	proxy_set_header   Host    $host; 
	proxy_set_header   X-Real-IP   $remote_addr; 
    	proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for; 
    }
}
