Rails.application.routes.draw do

  #-------------rss---------------
  namespace :rss do
    resources :sites, except: [:edit, :update ]
  end
  get 'rss' => 'rss#index'
  get 'rss/item/:id' => 'rss#show'
  get 'rss/page/:id' => 'rss#page'
  get 'rss/feeds' => 'rss#feeds'

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
  get 'archive/:year/:month/:day' => 'archive#index'
  get 'archive/:year/:month' => 'archive#index'
  post 'search' => 'search#index'

  #----------个人中心------------------------------------------
  get 'personal' => 'personal#index'

  #---------站点其它-------------------------------------------
  get 'notices' => 'main#notices'
  get 'about_me' => 'main#about_me'
  get 'main' => 'main#index'

  root 'main#index'
  mount BrahmaBodhi::Engine, at: '/core'

end
