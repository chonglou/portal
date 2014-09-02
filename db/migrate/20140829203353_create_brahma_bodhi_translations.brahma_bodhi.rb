# This migration comes from brahma_bodhi (originally 20140829200941)
class CreateBrahmaBodhiTranslations < ActiveRecord::Migration
  def change
    create_table :brahma_bodhi_translations do |t|
      t.integer :zh_CN
      t.integer :en_US
      t.timestamps
    end
  end
end
