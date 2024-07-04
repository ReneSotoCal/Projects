var path = require('path');
var express = require('express');
var router = express.Router();

var applicationController = require('../application/applicationController');


router.get('/', function(req, res, next) {
    appController = new applicationController(req);
    html = appController.getApplicationPage();
    res.send(html);
});

router.post('/query', function(req, res, next) {
    appController = new applicationController(req);
    html = appController.getQueriedPage(req.body);
    res.send(html);
});

router.get('/filter/:product', function(req, res, next) {
    console.log("filter get");
    appController = new applicationController(req);
    html = appController.getFilteredPage(req.params.product);
    res.send(html);
});

router.get('/*', function(req, res, next) {
    console.log("content get");
    appController = new applicationController(req);
    html = appController.getContentPage();
    res.send(html);
});














module.exports = router;