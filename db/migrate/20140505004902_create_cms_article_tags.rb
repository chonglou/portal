class CreateCmsArticleTags < ActiveRecord::Migration
  def change
    create_table :cms_article_tags do |t|
      t.integer :article, null: false
      t.integer :tag, null: false
      t.datetime :created, null: false
    end
  end
end
