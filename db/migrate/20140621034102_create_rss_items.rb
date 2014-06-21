class CreateRssItems < ActiveRecord::Migration
  def change
    create_table :rss_items do |t|
      t.text :content, null:false
      t.string :link, null:false
      t.string :title, null:false
      t.integer :site_id, null:false
      t.timestamp :created, null:false
    end
    add_index :rss_items, :title, unique: true
  end
end
