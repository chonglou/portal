# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20140828113150) do

  create_table "brahma_bodhi_attachments", force: true do |t|
    t.integer  "user_id",    null: false
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "file"
  end

  create_table "brahma_bodhi_friend_links", force: true do |t|
    t.string   "logo"
    t.string   "domain",  null: false
    t.string   "name",    null: false
    t.datetime "created", null: false
  end

  create_table "brahma_bodhi_logs", force: true do |t|
    t.integer  "user_id",           default: 0, null: false
    t.string   "message",                       null: false
    t.integer  "flag",    limit: 1, default: 0, null: false
    t.datetime "created",                       null: false
  end

  create_table "brahma_bodhi_notices", force: true do |t|
    t.text     "content",   null: false
    t.datetime "last_edit", null: false
    t.datetime "created",   null: false
  end

  create_table "brahma_bodhi_permissions", force: true do |t|
    t.string   "resource",              null: false
    t.string   "role",                  null: false
    t.string   "operation",             null: false
    t.datetime "startup",               null: false
    t.datetime "shutdown",              null: false
    t.datetime "created",               null: false
    t.integer  "version",   default: 0, null: false
  end

  add_index "brahma_bodhi_permissions", ["operation"], name: "index_brahma_bodhi_permissions_on_operation", using: :btree
  add_index "brahma_bodhi_permissions", ["resource"], name: "index_brahma_bodhi_permissions_on_resource", using: :btree
  add_index "brahma_bodhi_permissions", ["role"], name: "index_brahma_bodhi_permissions_on_role", using: :btree

  create_table "brahma_bodhi_rbacs", force: true do |t|
    t.string   "resource",                                   null: false
    t.string   "operation",                                  null: false
    t.string   "role",                                       null: false
    t.datetime "startup",    default: '9999-12-31 23:59:59', null: false
    t.datetime "shutdown",   default: '1000-01-01 00:00:00', null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "brahma_bodhi_rbacs", ["operation"], name: "index_brahma_bodhi_rbacs_on_operation", using: :btree
  add_index "brahma_bodhi_rbacs", ["resource"], name: "index_brahma_bodhi_rbacs_on_resource", using: :btree
  add_index "brahma_bodhi_rbacs", ["role"], name: "index_brahma_bodhi_rbacs_on_role", using: :btree

  create_table "brahma_bodhi_settings", force: true do |t|
    t.string   "key",                 null: false
    t.binary   "val",                 null: false
    t.integer  "version", default: 0, null: false
    t.datetime "created",             null: false
  end

  add_index "brahma_bodhi_settings", ["key"], name: "index_brahma_bodhi_settings_on_key", unique: true, using: :btree

  create_table "brahma_bodhi_users", force: true do |t|
    t.string   "open_id",                          null: false
    t.string   "username",                         null: false
    t.integer  "flag",       limit: 1, default: 0, null: false
    t.integer  "state",      limit: 1, default: 0, null: false
    t.binary   "contact",                          null: false
    t.datetime "last_login"
    t.datetime "created",                          null: false
  end

  add_index "brahma_bodhi_users", ["open_id"], name: "index_brahma_bodhi_users_on_open_id", unique: true, using: :btree

  create_table "cms_article_tags", force: true do |t|
    t.integer  "article_id", null: false
    t.integer  "tag_id",     null: false
    t.datetime "created",    null: false
  end

  create_table "cms_articles", force: true do |t|
    t.integer  "user_id",               null: false
    t.string   "logo"
    t.string   "title",                 null: false
    t.string   "summary"
    t.text     "body",                  null: false
    t.integer  "visits",    default: 0, null: false
    t.datetime "last_edit",             null: false
    t.datetime "created",               null: false
    t.integer  "version",   default: 0, null: false
    t.integer  "domain",                null: false
    t.integer  "lang",                  null: false
  end

  create_table "cms_comments", force: true do |t|
    t.integer  "article_id",             null: false
    t.integer  "user_id",                null: false
    t.integer  "comment"
    t.text     "content",                null: false
    t.integer  "visits",     default: 0, null: false
    t.datetime "last_edit",              null: false
    t.datetime "created",                null: false
    t.integer  "version",    default: 0, null: false
  end

  create_table "cms_tags", force: true do |t|
    t.string   "name",                          null: false
    t.integer  "visits",            default: 0, null: false
    t.datetime "created",                       null: false
    t.integer  "flag",    limit: 2, default: 0, null: false
    t.integer  "domain",                        null: false
    t.integer  "lang",                          null: false
  end

  add_index "cms_tags", ["name"], name: "index_cms_tags_on_name", unique: true, using: :btree

  create_table "domain_rss_sites", force: true do |t|
    t.integer  "rss_site",   null: false
    t.integer  "lang",       null: false
    t.integer  "domain",     null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "domains", force: true do |t|
    t.string   "name",                   null: false
    t.integer  "lang",       default: 0, null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "domains", ["name"], name: "index_domains_on_name", unique: true, using: :btree

  create_table "notices", force: true do |t|
    t.text     "content",    null: false
    t.integer  "lang",       null: false
    t.integer  "domain",     null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "rss_items", force: true do |t|
    t.text     "content", null: false
    t.string   "link",    null: false
    t.string   "title",   null: false
    t.integer  "site_id", null: false
    t.datetime "created", null: false
  end

  add_index "rss_items", ["link"], name: "index_rss_items_on_link", unique: true, using: :btree

  create_table "rss_sites", force: true do |t|
    t.string   "name",                      null: false
    t.integer  "flag",      default: 0,     null: false
    t.string   "url",                       null: false
    t.datetime "created",                   null: false
    t.boolean  "enable",    default: false, null: false
    t.datetime "last_sync"
  end

  add_index "rss_sites", ["url"], name: "index_rss_sites_on_url", using: :btree

  create_table "rss_user_sites", force: true do |t|
    t.integer  "user_id",    null: false
    t.integer  "site_id",    null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "sites", force: true do |t|
    t.string   "title",       null: false
    t.string   "keywords",    null: false
    t.text     "description", null: false
    t.text     "about_me",    null: false
    t.integer  "domain_id",   null: false
    t.integer  "lang",        null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "wikis", force: true do |t|
    t.string   "url",        null: false
    t.integer  "lang",       null: false
    t.integer  "domain",     null: false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
