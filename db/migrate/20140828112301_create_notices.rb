class CreateNotices < ActiveRecord::Migration
  def change
    create_table :notices do |t|
      t.text :content, null:false
      t.integer :site_id, null:false
      t.timestamps
    end
  end
end
