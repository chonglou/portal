class CreateRssUserSites < ActiveRecord::Migration
  def change
    create_table :rss_user_sites do |t|
      t.integer :user_id, null:false
      t.integer :site_id, null:false
      t.timestamps
    end
  end
end
