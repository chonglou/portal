Rails.application.routes.draw do

  #-------网站管理-------
  get 'site'=>'site#index'
  get 'site/status'
  get 'site/setup'
  get 'site/info'
  post 'site/info'

  resources :friend_links, only: [:new, :create, :index]
  resources :notices, only: [:new, :create, :index]

  #----------域名管理----------------
  resources :domains

  #-------------rss---------------
  namespace :rss do
    resources :sites, except: [:edit, :update ]
    resources :items, only:[:index, :destroy, :show]
  end
  get 'rss' => 'rss#index'
  get 'rss/page/:id' => 'rss#page'
  get 'rss/feeds'
  get 'rss/setup'

  #----------------知识库--------------------
  namespace :wiki do
    get 'git'
    post 'git'
  end
  get 'wiki' => 'wiki#index'
  get 'wiki/*name' => 'wiki#show'

  #-----------------内容系统------------
  namespace :cms do
    resources :comments, :tags, :articles
  end
  get 'user' => 'user#index'
  get 'user/:id' => 'user#show'
  get 'cms' => 'cms#index'
  #----------------其它---------------
  get 'archive'=>'archive#index'
  post 'search' => 'search#index'

  #----------个人中心------------------------------------------
  get 'personal' => 'personal#index'

  #---------站点其它-------------------------------------------
  get 'about_me' => 'main#about_me'
  get 'main' => 'main#index'
  %w(404 422 500 505).each { |e| match "/#{e}" => 'main#errors', id: e, via: [:get, :post, :put, :patch, :delete] }

  root 'main#index'
  mount BrahmaBodhi::Engine, at: '/core'

end
