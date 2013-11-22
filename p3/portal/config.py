__author__ = 'flamen'

import configparser
import os
import tornado.options

_config = configparser.ConfigParser()
_file = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../p3/web.cfg"))
_config.read(_file)

http_port = _config.getint("http", "port")
http_host = _config.get("http", "host")
http_secret = _config.get("http", "secret")
http_statics = os.path.abspath(os.path.join(os.path.dirname(__file__), "../portal/statics"))

app_name = _config.get("app", "name")
app_secret = _config.get("app", "secret")
app_store = _config.get("app", "store")
app_debug = _config.getboolean("app", "debug")

sql_uri = _config.get("sql", "uri")
sql_echo = _config.getboolean("sql", "echo")

redis_host = _config.get("redis", "host")
redis_port = _config.getint("redis", "port")
redis_db = _config.get("redis", "db")


tornado.options.options.logging = _config.get("log", "level")
tornado.options.options.log_to_stderr = _config.getboolean("log","console")
tornado.options.options.log_file_max_size = _config.getint("log", "size")
tornado.options.options.log_file_num_backups = _config.getint("log", "backups")
tornado.options.options.log_file_prefix = os.path.join(app_store, "httpd.log")
tornado.options.parse_command_line()




