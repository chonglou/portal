__author__ = 'flamen'

import redis
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from portal.models import *


class Redis:
    def __init__(self, host="localhost", port=6379, db="0"):
        self.host = host
        self.port = port
        self.db=db

    def client(self):
        return redis.StrictRedis(self.host, self.port,self.db)


class Database:
    def __init__(self, uri, echo):
        self._session = sessionmaker()
        engine = create_engine(uri, echo)
        Base.metadata.create_all(engine)
        self._session.configure(bind=engine)

    def session(self):
        return self._session()


