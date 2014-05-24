Rails.application.routes.draw do

  #-----------------内容系统------------
  namespace :cms do
    resources :comments, :tags, :articles
  end
  get 'user' => 'user#index'
  get 'user/:id' => 'user#show'

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
