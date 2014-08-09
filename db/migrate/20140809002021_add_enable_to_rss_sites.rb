class AddEnableToRssSites < ActiveRecord::Migration
  def change
    add_column :rss_sites, :enable, :boolean, null:false, default: false
  end
end
