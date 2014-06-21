require "#{Gem.path[0].sub '/gems/', '/'}/rss"
require 'open-uri'

module Brahma
  module Utils
    module RssHelper
      module_function

      def list(url)
        open(url) do |rss|
          feed = RSS::Parser.parse(rss)
          feed.items.each do |item|
            yield item.link, item.title, item.content_encoded || item.description
          end
          feed.channel.title
        end
      end
    end
  end
end
