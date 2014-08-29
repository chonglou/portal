require 'fileutils'
require 'brahma/utils/string_helper'

module Brahma
  module Utils
    module WikiHelper
      WIKI_ROOT="#{Rails.root}/tmp/wiki"
      module_function

      def update(url)
        #todo 防注入
        # unless Dir.exist?(WIKI_ROOT)
        #   FileUtils.mkpath WIKI_ROOT
        # end
        base = "#{WIKI_ROOT}/#{Brahma::Utils::StringHelper.md5 url}"
        Dir.exist?("#{base}/.git") ? `cd #{base} && git pull` : `git clone #{url} #{base}`
      end

      def get(name)
        name = "#{WIKI_ROOT}/#{name}.md"
        if File.file?(name)
          File.open(name, 'r') { |f| Brahma::Utils::StringHelper.md2html f.read }
        end
      end

      def each(url=nil)
        len = WIKI_ROOT.size
        Dir.glob("#{WIKI_ROOT}#{url ? "/#{Brahma::Utils::StringHelper.md5 url}" : ''}/**/*.md").each do |fn|
          name = fn[len+1, fn.size-len-4]
          yield name
        end
      end
    end
  end
end