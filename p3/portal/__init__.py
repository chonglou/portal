__author__ = 'flamen'

import importlib
import signal
import logging
import tornado.ioloop
import tornado.web
import tornado.options

_is_closing = False


def _signal_handler(signum, frame):
    global _is_closing
    logging.info("准备退出...")
    _is_closing = True


def _try_exit():
    global _is_closing
    if _is_closing:
        tornado.ioloop.IOLoop.instance().stop()
        logging.info("成功退出!")


def _setup():
    signal.signal(signal.SIGINT, _signal_handler)

    from tornado.options import define
    define("http_secret", type=str, help="COOKIE密钥")
    define("http_port", type=int, help="HTTP Server 监听端口")
    define("http_host", type=str, help="HTTP Server 监听地址")
    define("app_debug", type=bool, help="调试模式")
    define("app_store", type=str, help="站点数据存储目录")

    from portal import utils
    tornado.options.options.log_file_prefix = utils.path("../tmp/httpd.log")
    tornado.options.parse_config_file(utils.path("../web.cfg"))


def start():
    _setup()

    from portal.views import items
    routes = []
    for i in items:
        routes.extend(importlib.import_module("portal.views." + i).handlers)

    app = tornado.web.Application(routes, debug=tornado.options.options.app_debug)
    logging.info("开始监听端口%d", tornado.options.options.http_port)
    app.listen(
        port=tornado.options.options.http_port,
        address=tornado.options.options.http_host)
    tornado.ioloop.PeriodicCallback(_try_exit, 100).start()
    tornado.ioloop.IOLoop.instance().start()


