class Rss::Site < ActiveRecord::Base
  belongs_to :user
  has_many :items
  enum flag: {rss: 0, atom: 1}
end
