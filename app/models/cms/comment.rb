class Cms::Comment < ActiveRecord::Base
  belongs_to :article
end
