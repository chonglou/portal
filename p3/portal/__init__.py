__author__ = 'flamen'

import importlib
import signal
import logging
import tornado.ioloop
import tornado.web

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


def start():
    signal.signal(signal.SIGINT, _signal_handler)

    from portal.views import items
    routes = []
    for i in items:
        routes.extend(importlib.import_module("portal.views."+i).handlers)

    from portal import config
    app = tornado.web.Application(routes,debug=config.app_debug)
    logging.info("开始监听端口%d",config.http_port)
    app.listen(port=config.http_port, address=config.http_host)
    tornado.ioloop.PeriodicCallback(_try_exit, 100).start()
    tornado.ioloop.IOLoop.instance().start()


