atom_feed(language:'zh-CN') do |feed|
  feed.title site_info('title')
  feed.updated( @articles.first.created) unless @articles.empty?
  @articles.each do |article|
    feed.entry(article) do |entry|
      entry.title article.title
      entry.content article.body, type:'html'
      entry.author do |author|
        author.name article.user.username
      end
    end
  end
end