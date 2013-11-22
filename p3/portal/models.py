__author__ = 'flamen'

import datetime
from sqlalchemy import Column, Integer, String,DateTime, Sequence
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, Sequence('user_id_seq'), primary_key=True)
    name = Column(String(255))
    email = Column(String(255), unique=True)
    password = Column(String(512), nullable=False)
    created = Column(DateTime, nullable=False)
    lastLogin = Column(DateTime)

    def __init__(self, email, name, password):
        self.name = name
        self.email = email
        self.password = password
        self.created = datetime.datetime.now()

    def __repr__(self):
        return "<User('%s', '%s')>" % (self.email, self.name)