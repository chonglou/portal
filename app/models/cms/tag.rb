class Cms::Tag < ActiveRecord::Base
  has_many :article_tags
  has_many :articles, through: :article_tags
  enum protocol: {default: 0, top_nav: 1}
end
