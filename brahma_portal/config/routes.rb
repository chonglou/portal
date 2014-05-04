Rails.application.routes.draw do
  get 'main' => 'main#index'
  mount BrahmaBodhi::Engine, at:'/core'
  root 'main#index'
end
