class CreateCmsTags < ActiveRecord::Migration
  def change
    create_table :cms_tags do |t|

      t.timestamps
    end
  end
end
