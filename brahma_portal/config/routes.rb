Rails.application.routes.draw do

  #-----------------内容系统------------
  namespace :cms do
    get 'tag/:id' => 'tag#page'
    get 'article/:id' => 'article#page'
    get 'comment/:id' => 'comment#page'
    resources :comments, :tags, :articles
  end

  #-------------------知识库------------
  get 'wiki' => 'wikis#page'
  resources :wikis

  #----------------其它---------------
  get 'archive/:year/:month/:day' => 'archive#index'
  get 'archive/:year/:month' => 'archive#index'
  post 'search' => 'search#index'

  #----------个人中心------------------------------------------
  get 'personal' => 'personal#index'

  #---------站点其它-------------------------------------------
  get 'about_me' => 'main#about_me'
  get 'main' => 'main#index'

  root 'main#index'
  mount BrahmaBodhi::Engine, at: '/core'
end
