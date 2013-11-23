__author__ = 'flamen'


def now():
    import datetime
    return datetime.datetime.now()


def path(uri):
    import os
    print(os.path.abspath(os.path.join(os.path.dirname(__file__), uri)))
    return os.path.abspath(os.path.join(os.path.dirname(__file__), uri))


def md5(s):
    import hashlib
    return hashlib.md5(s.encode()).hexdigest()


def sha512(s):
    import hashlib
    return hashlib.md5(s.encode()).hexdigest()


def uuid():
    import uuid
    return uuid.uuid4().hex


def random_str(size):
    import random
    import string
    return ''.join(random.sample(string.ascii_uppercase + string.digits, size))


def encrypt(text, key=None):
    import binascii
    from Crypto import Random
    from Crypto.Cipher import AES

    def pad(s):
        x = AES.block_size - len(s) % AES.block_size
        return s + (bytes([x]) * x)

    padded = pad(text.encode())

    if not key:
        from tornado.options import options
        key = options.app_secret

    iv = Random.OSRNG.posix.new().read(AES.block_size)
    cipher = AES.new(key, AES.MODE_CBC, iv)

    return binascii.hexlify(iv + cipher.encrypt(padded)).decode()


def decrypt(code, key=None):
    if not key:
        from tornado.options import options
        key = options.app_secret

    import binascii
    from Crypto.Cipher import AES

    code = binascii.unhexlify(code.encode())
    un_pad = lambda s: s[:-s[-1]]
    iv = code[:AES.block_size]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    return (un_pad(cipher.decrypt(code))[AES.block_size:]).decode()


def obj2json(obj):
    import json
    return json.dumps(obj)

def json2obj(text):
    import json
    return json.loads(text)

