from portal import config

__author__ = 'flamen'

settings = {
    "static_path": config.http_statics,
    "cookie_secret": config.http_statics,
    "login_url": "/personal/login",
    "xsrf_cookies": True,
}

items = ["admin", "cms", "personal", "seo", "site"]
