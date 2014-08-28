class AddDomainToCms < ActiveRecord::Migration
  def change
    add_column :cms_tags, :site_id, :integer, null: false
    add_column :cms_articles, :site_id, :integer, null: false
  end
end
