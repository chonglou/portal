class CreateRssSites < ActiveRecord::Migration
  def change
    create_table :rss_sites do |t|
      t.string :name, null:false
      t.integer :flag, null:false, default:0
      t.string :url, null:false
      t.timestamp :created, null:false
    end
    add_index :rss_sites, :url, unique: true
  end

end
