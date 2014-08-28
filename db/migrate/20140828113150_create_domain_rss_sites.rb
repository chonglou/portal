class CreateDomainRssSites < ActiveRecord::Migration
  def change
    create_table :domain_rss_sites do |t|
      t.integer :rss_site, null:false
      t.integer :lang, null:false
      t.integer :domain, null:false
      t.timestamps
    end
  end
end
