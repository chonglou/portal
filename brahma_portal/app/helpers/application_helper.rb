module ApplicationHelper
  include BrahmaBodhi::ApplicationHelper


  def nav_links
    links = {'/main' => '本站首页'}
    if current_user
      links['/personal'] = '用户中心'
    end
    links['/about_me']='关于我们'
    links
  end

  def personal_links
    if current_user
      {'/personal'=>'个人中心', '/personal/logout'=>'安全退出'}
    else
      {
          '/personal/login' => '用户登录',
          '/personal/register' => '账户注册',
          '/personal/active' => '激活账户',
          '/personal/reset_pwd' => '找回密码'
      }
    end
  end

  def tag_links
    links = {}
    BrahmaBodhi::FriendLink.all.each {|fl| links["http://#{fl.domain}"] = fl.name}
    links
  end

  def archive_links
    {}
  end
end
