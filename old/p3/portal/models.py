__author__ = 'flamen'


from sqlalchemy import Column, Integer, String,DateTime, Sequence,Text
from sqlalchemy.ext.declarative import declarative_base
from portal import utils

Base = declarative_base()

class Setting(Base):
    __tablename__ = "settings"
    key = Column(String(255), name="kkk", primary_key=True)
    val = Column(Text, name="vvv")
    created = Column(DateTime, nullable=False)

    def __init__(self, key, val=None):
        self.key = key
        self.val = val
        self.created = utils.now()

    def __repr__(self):
        return "<Setting(%s, %s)>" %(self.key, self.created)

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, Sequence('user_id_seq'), primary_key=True)
    name = Column(String(255))
    email = Column(String(255), unique=True)
    password = Column(String(512), nullable=False)
    salt = Column(String(8), nullable=False)
    created = Column(DateTime, nullable=False)
    lastLogin = Column(DateTime)

    def __init__(self, email, name, password):
        self.name = name
        self.email = email
        self.password = password
        self.salt = utils.random_str(8)
        self.created = utils.now()

    def __repr__(self):
        return "<User('%s', '%s')>" % (self.email, self.name)