require 'brahma/web/response'

class PersonalController < ApplicationController
  def index
    user = current_user
    if user
      @ctl_links = {
          brahma_bodhi.personal_info_path => '个人信息',
      }
      if admin?
        @ctl_links[domains_path] = '域名管理'
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
