class AddLastSyncToRssSites < ActiveRecord::Migration
  def change
    add_column :rss_sites, :last_sync, :datetime
  end
end
