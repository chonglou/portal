class Rss::Site < ActiveRecord::Base
  has_many :rss_items, :class_name => 'Rss::Item'
  enum flag: {rss: 0, atom: 1}
end
