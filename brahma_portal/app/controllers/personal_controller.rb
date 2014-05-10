
class PersonalController < ApplicationController
  def index
    user = current_user
    if user
      @ctl_links = {
          '/cms/articles' => '文章列表',
          '/cms/comments' => '评论列表',
          '/wikis' => '知识库'
      }
      if admin?
        @ctl_links['/cms/tags'] = '标签列表'
        @ctl_links['/core/admin/site'] = '站点参数'
        @ctl_links['/core/admin/advert'] = '广告设置'
        @ctl_links['/core/admin/seo'] = 'SEO设置'
      end
      @ctl_links['/core/user/logs']='日志列表'
      @index='/personal'
      goto_admin and return
    end
    not_found
  end
end
