class AddDomainToNotices < ActiveRecord::Migration
  def change
    add_column :brahma_bodhi_notices, :site_id, :integer, null: false, default:0
  end
end
