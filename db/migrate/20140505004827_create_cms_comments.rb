class CreateCmsComments < ActiveRecord::Migration
  def change
    create_table :cms_comments do |t|
      t.integer :article_id, null: false
      t.integer :user_id, null: false
      t.string :lang, null: false, limit:5
      t.integer :comment
      t.text :content, null:false
      t.integer :visits, null: false, default: 0
      t.datetime :last_edit, null: false
      t.datetime :created, null: false
      t.integer :version, null: false, default: 0
    end
    add_index :cms_comments, :lang
  end
end
