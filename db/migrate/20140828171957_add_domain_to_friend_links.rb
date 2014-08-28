class AddDomainToFriendLinks < ActiveRecord::Migration
  def change
    add_column :brahma_bodhi_friend_links, :site_id, :integer, null: false, default: 0
  end
end
