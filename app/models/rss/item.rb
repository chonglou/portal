class Rss::Item < ActiveRecord::Base
  belongs_to :rss_site, :class_name => 'Rss::Site'
end
