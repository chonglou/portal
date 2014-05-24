class Cms::Article < ActiveRecord::Base
  has_many :comments
  has_many :article_tags
  has_many :tags, through: :article_tags
  belongs_to :user, class_name: 'BrahmaBodhi::User'
end
