module Brahma
  module Utils
    module WikiHelper
      WIKI_ROOT="#{Rails.root}/tmp/wiki"
      module_function

      def update(url)
        #todo 防注入
        Dir.exist?(WIKI_ROOT) ? `cd #{WIKI_ROOT} && git pull` : `git clone #{url} #{WIKI_ROOT}`
      end

      def get(name)
        name = "#{WIKI_ROOT}/#{name}.md"
        if File.file?(name)
          File.open(name, 'r') { |f| Brahma::Utils::StringHelper.md2html f.read }
        end
      end

      def each
        len = WIKI_ROOT.size
        Dir.glob("#{WIKI_ROOT}/**/*.md").each do |fn|
          name = fn[len+1, fn.size-len-4]
          yield name
        end
      end
    end
  end
end