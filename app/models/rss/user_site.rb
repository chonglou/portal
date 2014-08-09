class Rss::UserSite < ActiveRecord::Base
  belongs_to :user, class_name: 'BrahmaBodhi::User'
  belongs_to :site, class_name: 'Rss::Site'
end
