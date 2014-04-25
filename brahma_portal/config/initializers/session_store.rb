# Be sure to restart your server when you modify this file.
require 'brahma/config/site'
Rails.application.config.session_store :cookie_store,  key: Brahma::Config::Site.new.load(Rails.env, :server).fetch(:session_key)
