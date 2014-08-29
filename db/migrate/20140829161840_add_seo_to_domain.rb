class AddSeoToDomain < ActiveRecord::Migration
  def change
    add_column :domains, :google, :string
    add_column :domains, :baidu, :string
  end
end
