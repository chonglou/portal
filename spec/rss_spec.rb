require 'spec_helper'

describe :rss do
  it 'RSS下载' do
    require 'brahma/utils/rss'
    url = 'http://www.infoq.com/feed?token=Vy6Mzf750zt9dGFp0hcqcVWJ1ZHM8zl0'
    #url = 'http://coolshell.cn/feed'
    Brahma::Utils::RssHelper.list(url) do |link, title, content|
       #puts content
      puts link, title
    end
  end

end