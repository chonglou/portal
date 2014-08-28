module Brahma
  module Locales
    ZH_CN=0
    EN_US=1

    module_function
    def label(v)
      case v
        when EN_US
          'English'
        when ZH_CN
          '简体中文'
        else
      end
    end

    def options
      [[EN_US, label(EN_US)], [ZH_CN, label(ZH_CN)]]
    end
  end
end