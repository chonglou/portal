class FixRssItemsIndex < ActiveRecord::Migration
  def change
    remove_index :rss_items, column: :title
    add_index :rss_items, :link, unique: true
  end
end
