Rails.application.routes.draw do
  get 'main/index'
  mount BrahmaBodhi::Engine, at:'/core'
  root 'main#index'
end
