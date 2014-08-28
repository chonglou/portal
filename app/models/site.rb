class Site < ActiveRecord::Base
  has_many :rss_sites, through: :domain_rss_sites
  belongs_to :domain
end
