
var express = require('express');
var path = require('path');

var applicationRouter = require('./routes/application');
var shoppingCartRouter = require('./routes/shoppingCart');
var publicRouter = require('./routes/public');

var app = express();

app.use(express.urlencoded({extended:true}));
app.use(express.json());

app.use('/application/cart/', shoppingCartRouter);
app.use('/application/', applicationRouter);

app.use('/', publicRouter);

const PORT  = process.env.PORT || 3050
app.listen(PORT,()=> console.info(`Server has started on ${PORT}`))

module.exports = app;