class CreateCmsTags < ActiveRecord::Migration
  def change
    create_table :cms_tags do |t|
      t.string :name, null: false
      t.integer :visits, null: false, default: 0
      t.datetime :created, null: false
    end
    add_index :cms_tags, :name, unique: true
  end
end
