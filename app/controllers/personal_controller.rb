class PersonalController < ApplicationController
  def index
    user = current_user
    if user
      @ctl_links = {
          brahma_bodhi.personal_info_path => '个人信息',
          cms_articles_path => '文章列表',
          cms_comments_path => '评论列表',
          rss_setup_path => 'RSS设置'
      }
      if admin?
        @ctl_links[wiki_git_path] = '知识库'
        @ctl_links[cms_tags_path] = '标签列表'


        @ctl_links[brahma_bodhi.admin_site_path] = '站点参数'
        @ctl_links[brahma_bodhi.admin_advert_path] = '广告设置'
        @ctl_links[brahma_bodhi.admin_notices_path] = '公告通知'
        @ctl_links[brahma_bodhi.admin_seo_path] = 'SEO设置'
        @ctl_links[brahma_bodhi.admin_users_path] = '用户列表'

      end
      @ctl_links[brahma_bodhi.attachments_path]='附件管理'
      @ctl_links[brahma_bodhi.user_logs_path]='日志列表'
      goto_admin
    else
      not_found
    end
  end
end
