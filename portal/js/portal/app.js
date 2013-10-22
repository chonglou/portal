
var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var http = require('http');
var path = require('path');
var config= require('./config');
var logger = require('./logger');

var app = express();

//环境变量
app.set('port', config['port'] || process.env.PORT || 8080);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('eijah8Aek2Aipace1leu'));
app.use(express.session());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

//第三方路径
app.use("/bootstrap", express.static(path.join(__dirname,"3rd/bootstrap-3.0.0")));
app.use("/attachments", express.static(path.join(__dirname,"../tmp/attach")));

//调试模式
if (config.debug) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/users', user.list);

http.createServer(app).listen(app.get('port'), function(){
    logger.info("启动服务，监听端口%s", app.get('port'));
});
