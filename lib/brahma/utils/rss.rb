require 'rss'
require 'open-uri'
#
# module RSS
#   class Rss
#     install_ns CONTENT_PREFIX, CONTENT_URI
#     class Channel
#       class Item; include ContentModel; end
#     end
#   end
# end

module Brahma
  module Utils
    module RssHelper
      module_function
      def all(url)
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
