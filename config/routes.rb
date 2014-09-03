Rails.application.routes.draw do


  #-------------rss---------------
  namespace :rss do
    resources :sites, except: [:edit, :update ]
    resources :items, only:[:index, :destroy, :show]
  end
  get 'rss' => 'rss#index'
  get 'rss/page/:id' => 'rss#page', as: :rss_page
  get 'rss/feeds'
  get 'rss/setup'

  #----------------知识库--------------------
  namespace :wiki do
    get 'git'
    post 'git'
  end
  get 'wiki' => 'wiki#index'
  get 'wiki/*name' => 'wiki#show', as: :wiki_show

  #-----------------内容系统------------
  namespace :cms do
    resources :comments, :tags, :articles
  end
  get 'users' => 'users#index'
  get 'users/:id' => 'users#show', as: :user_show
  get 'cms' => 'cms#index'
  #----------------其它---------------
  get 'archive'=>'archive#index'
  post 'search' => 'search#index'

  #----------个人中心------------------------------------------
  get 'personal' => 'personal#index'

  #---------站点其它-------------------------------------------
  get 'about_me' => 'main#about_me'
  get 'main' => 'main#index'
  get 'notices' => 'main#notices'
  %w(404 422 500 505).each { |e| match "/#{e}" => 'main#errors', id: e, via: [:get, :post, :put, :patch, :delete] }

  root 'main#index'

  mount BrahmaBodhi::Engine, at: '/core'

end
