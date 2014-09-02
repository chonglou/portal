class CreateCmsTags < ActiveRecord::Migration
  def change
    create_table :cms_tags do |t|
      t.string :name, null: false
      t.string :lang, null: false, limit:5
      t.integer :tid, null:false
      t.integer :visits, null: false, default: 0
      t.datetime :created, null: false
      t.integer :flag, null: false, default: 0, limit: 2
    end

    add_index :cms_tags, :name
    add_index :cms_tags, :lang
  end
end
