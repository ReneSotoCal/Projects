var path = require('path');
var express = require('express');
var router = express.Router();

var applicationController = require('../application/applicationController');


router.get('/product', function(req, res, next) {
    appController = new applicationController();

    if(req.query.filter)
        data = appController.getProductsByFilter(req.query.filter);
    else if(req.query.search)
        data = appController.getProductsBySearch(req.query.search);
    else
        data = appController.getAllProducts();

    res.send(data);
});

router.get('/product/filters', function(req, res, next) {
    appController = new applicationController();
    data = appController.getAllFilters();
    res.send(data);
});

router.get('/product/:productIndex', function(req, res, next) {
    appController = new applicationController();
    data = appController.getProductByIndex(req.params.productIndex);
    res.send(data);
});


module.exports = router;