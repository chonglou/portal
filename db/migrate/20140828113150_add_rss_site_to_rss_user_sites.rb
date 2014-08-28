class AddRssSiteToRssUserSites < ActiveRecord::Migration
  def change
    add_column :rss_user_sites, :rss_site_id, :integer, null:false
  end
end
