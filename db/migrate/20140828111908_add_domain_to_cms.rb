class AddDomainToCms < ActiveRecord::Migration
  def change
    add_column :cms_tags, :domain, :integer, null: false
    add_column :cms_tags, :lang, :integer, null: false
    add_column :cms_articles, :domain, :integer, null: false
    add_column :cms_articles, :lang, :integer, null: false
  end
end
