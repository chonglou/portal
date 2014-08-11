unless defined?(RSS)
  $:.each do |p|
    rs = "#{p.sub '/gems/', '/'}/rss"
    if File.exist?("#{rs}.rb")
      require rs
    end
  end
end
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
