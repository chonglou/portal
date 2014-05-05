class CreateWikis < ActiveRecord::Migration
  def change
    create_table :wikis do |t|
      t.string :title, null: false
      t.string :name, null: false
      t.string :body, null: false
      t.integer :author, null: false
      t.datetime :last_edit, null: false
      t.datetime :created, null: false
      t.integer :version, null: false, default: 0
    end
    add_index :wikis, :name, unique: true
  end
end
