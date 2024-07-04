var path = require('path');
var express = require('express');
var router = express.Router();

var applicationController = require('../application/applicationController');


router.get('/', function(req, res, next) {
    appController = new applicationController(req);
    data = appController.getAllCartProducts();
    res.send(data);
});

router.get('/:productIndex', function(req, res, next) {
    appController = new applicationController();
    data = appController.getCartProductByIndex(req.params.productIndex);
    res.send(data);
});

router.post('/', function(req, res, next) {
    appController = new applicationController(); 
    data = appController.addCartProductByIndex(req.body.index);
    res.send(data);
});

router.delete('/:productIndex', function(req, res, next) {
    appController = new applicationController(); 
    data = appController.deleteCartProductByIndex(req.params.productIndex);
    res.send(data);
});

module.exports = router;