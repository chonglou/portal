require 'brahma/search/helper'

class SearchController < ApplicationController
  def index
    result = Brahma::Search::Helper.search params[:keyword]
    if result.key?(:items)
      @items = result[:items]
    else
      redirect_to result.fetch(:url)
    end
  end
end
