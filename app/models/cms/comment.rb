class Cms::Comment < ActiveRecord::Base
  belongs_to :article
  belongs_to :user, class_name: 'BrahmaBodhi::User'
end
