class CreateSites < ActiveRecord::Migration
  def change
    create_table :sites do |t|
      t.string :home, null:false
      t.string :title, null:false
      t.string :keywords, null:false
      t.text :description, null:false
      t.text :about_me, null:false
      t.integer :domain_id, null:false
      t.integer :lang, null:false
      t.timestamps
    end
  end
end
