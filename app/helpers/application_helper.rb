require 'brahma/factory'
module ApplicationHelper
  include BrahmaBodhi::ApplicationHelper

  def nav_links
    links = {'/main' => '本站首页'}
    if current_user
      links['/personal'] = '用户中心'
    end
    #todo
    links['/cms/tags/2'] = '知识库'
    links['/cms/tags/3'] = '论坛'
    links['/user'] = '用户列表'
    links['/about_me']='关于我们'
    links
  end

  def personal_links
    if current_user
      {'/personal' => '个人中心', '/core/personal/logout' => '安全退出'}
    else
      auth = Brahma::FACTORY.oauth2
      state = auth.state
      oauth2_state! state
      {
          auth.authorization(['info'], state) => 'BRAHMA通行证'
      }
    end
  end

  def tag_links
    links = {}
    Cms::Tag.all.each { |t| links["/cms/tags/#{t.id}"] = t.name }
    BrahmaBodhi::FriendLink.all.each { |fl| links["http://#{fl.domain}"] = fl.name }
    links
  end

end
