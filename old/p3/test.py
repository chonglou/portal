__author__ = 'flamen'

import unittest
from portal import utils


class Encrypt(unittest.TestCase):
    def setUp(self):
        self.key = "012345678912a456"

    def test_1(self):

        text = '12第三wr方3456'
        code = utils.encrypt(text, self.key)
        raw = utils.decrypt(code, self.key)

        print(text, code, type(code), raw, type(raw))

        self.assertTrue(text == raw)

class Json(unittest.TestCase):
    def test_1(self):
        from portal.models import Setting
        s = Setting("aaa", 123)
        json = utils.obj2json(s)
        o = utils.json2obj(json)
        print(s, json, type(json), o, type(o))



if __name__ == '__main__':
    unittest.main()
