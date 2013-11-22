__author__ = 'flamen'

import tornado.web


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Hello, Tornado!")

handlers = [
    (r"/", MainHandler),
]