var redis = require("redis");

var error = function(error){
    if(error){
        console.log(error);
    }
};

exports.client = function(){
    return redis.createClient();
};