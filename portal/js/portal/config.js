var path = require('path');
var mkdirp = require('mkdirp');
var env = {'port':10000};

env['port'] = 8080;
env['debug'] = true;

var dirs =["log","attach"];
for(var i in dirs){
    var d = path.join(__dirname, "../tmp/"+dirs[i]);
    console.log("检查目录["+d+"]");
    mkdirp(d, function(err){
        if(err){
            console.error(err);
            process.exit(-1);
        }
    });

    exports[dirs[i]]=d;
}

exports.port = 8080;
exports.debug = true;

