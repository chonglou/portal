class CreateCmsArticles < ActiveRecord::Migration
  def change
    create_table :cms_articles do |t|
      t.integer :user_id, null: false
      t.integer :tid, null:false
      t.string :lang, null: false, limit:5
      t.string :logo
      t.string :title, null: false
      t.string :summary
      t.text :body, null: false
      t.integer :visits, null: false, default: 0
      t.datetime :last_edit, null: false
      t.datetime :created, null: false
      t.integer :version, null: false, default: 0
    end
    add_index :cms_articles, :lang
  end
end
