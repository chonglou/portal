__author__ = 'flamen'


from tornado.options import options
from portal import utils

settings = {
    "static_path": utils.path("statics"),
    "cookie_secret": options.http_secret,
    "login_url": "/personal/login",
    "xsrf_cookies": True,
}

items = ["admin", "cms", "personal", "seo", "site"]
