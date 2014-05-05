class CreateCmsArticleTags < ActiveRecord::Migration
  def change
    create_table :cms_article_tags do |t|

      t.timestamps
    end
  end
end
