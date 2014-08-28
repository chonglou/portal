class Rss::Site < ActiveRecord::Base
  has_many :items
  enum flag: {rss: 0, atom: 1}
  has_many :sites, through: :domain_rss_sites
end
