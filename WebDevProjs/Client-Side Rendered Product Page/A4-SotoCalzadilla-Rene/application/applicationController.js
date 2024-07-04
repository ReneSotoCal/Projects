var ApplicationModel = require('./applicationModel');
var applicationModel = new ApplicationModel();

var ShoppingCartModel = require('./shoppingCartModel');
var shoppingCartModel = new ShoppingCartModel();

class application {

    getAllProducts() {
        return applicationModel.getAllProducts();
    }
    
    getAllFilters() {
            return applicationModel.getAllFilters();
    }
    
    getProductByIndex(index){
        return applicationModel.getProductByIndex(index);
    }

    getProductsByFilter(filter){
        return applicationModel.getProductsByFilter(filter);
    }

    getProductsBySearch(search){
        return applicationModel.getProductsBySearch(search);
    }


    //Shopping Cart Controller

    
    getAllCartProducts() {
        return shoppingCartModel.getAllCartProducts();
    }

    getCartProductByIndex(index){
        return shoppingCartModel.getCartProductByIndex(index);
    }

    addCartProductByIndex(index) {
        let cartProd = applicationModel.getProductByIndex(index);
        return shoppingCartModel.addCartProduct(cartProd);
    }    

    deleteCartProductByIndex(index) {
        return shoppingCartModel.deleteCartProductByIndex(index);
    }
}


module.exports = application;