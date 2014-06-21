class PersonalController < ApplicationController
  def index
    user = current_user
    if user
      @ctl_links = {
          '/cms/articles' => '文章列表',
          '/cms/comments' => '评论列表'
      }
      if admin?
        @ctl_links['/rss/sites'] = 'RSS源'
        @ctl_links['/wiki/git'] = '知识库'
        @ctl_links['/cms/tags'] = '标签列表'
        @ctl_links['/core/admin/users'] = '用户列表'
        @ctl_links['/core/admin/site'] = '站点参数'
        @ctl_links['/core/admin/advert'] = '广告设置'
        @ctl_links['/core/admin/seo'] = 'SEO设置'
        @ctl_links['/core/admin/notices'] = '消息通知'
      end
      @ctl_links['/core/attachments']='附件管理'
      @ctl_links['/core/user/logs']='日志列表'
      @index='/personal'
      goto_admin and return
    end
    not_found
  end
end
