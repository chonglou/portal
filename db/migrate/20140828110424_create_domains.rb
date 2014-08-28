class CreateDomains < ActiveRecord::Migration
  def change
    create_table :domains do |t|
      t.string :name, null:false
      t.integer :lang, null:false, default:0
      t.timestamps
    end
    add_index :domains, :name, unique: true
  end
end
