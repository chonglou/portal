__author__ = 'flamen'

import tornado.web

class ArticleHandler:
    pass


class TagHandler(tornado.web.RequestHandler):
    def get(self, tagId):
        if not tagId:
            raise tornado.web.HTTPError(404)
        self.write("标签"+tagId)

handlers = [
    (r"/article/([0-9]+)", ArticleHandler),
    (r"/tag/([0-9]+)", TagHandler),
]

