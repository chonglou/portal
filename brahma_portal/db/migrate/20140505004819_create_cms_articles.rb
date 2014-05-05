class CreateCmsArticles < ActiveRecord::Migration
  def change
    create_table :cms_articles do |t|

      t.timestamps
    end
  end
end
