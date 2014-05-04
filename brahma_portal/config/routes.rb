Rails.application.routes.draw do
  get 'personal'=>'personal#index'

  get 'main' => 'main#index'
  root 'main#index'
  mount BrahmaBodhi::Engine, at:'/core'
end
