class CreateCmsTags < ActiveRecord::Migration
  def change
    create_table :cms_tags do |t|
      t.string :name, null: false
      t.integer :visits, null: false, default: 0
      t.datetime :created, null: false
      t.boolean :keep, null: false, default: false
    end

    add_index :cms_tags, :name, unique: true

    Cms::Tag.create id:1, name:'置顶', keep: true, created:Time.now
    Cms::Tag.create id:2, name:'知识库', keep: true, created:Time.now
    Cms::Tag.create id:3, name:'论坛', keep: true, created:Time.now
  end
end
