class ResetTagName < ActiveRecord::Migration
  def change
    remove_index :cms_tags, :name
    add_index :cms_tags, :name
  end
end
