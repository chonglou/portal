var winston = require('winston');
var config = require('./config');

var logger = new (winston.Logger)({
    transports: [
        new (winston.transports.Console)({ json: false, timestamp: true }),
        new winston.transports.File({ filename: config.log+'/debug.log', json: false })
    ],
    exceptionHandlers: [
        new (winston.transports.Console)({ json: false, timestamp: true }),
        new winston.transports.File({ filename: config.log+'/error.log', json: false })
    ],
    exitOnError: false
});



module.exports = logger;