class CreateWikis < ActiveRecord::Migration
  def change
    create_table :wikis do |t|
      t.string :url, null:false
      t.integer :lang, null:false
      t.integer :domain, null:false
      t.timestamps
    end
  end
end
