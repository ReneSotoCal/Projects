const fs = require('fs');

//Typically the model will access a database of some type, but this is omitted in this example for simplicity
class shoppingCartModel {
	constructor() {
		this.cartProducts = [];
		this.initialize();
	}

	initialize() {
		fs.readFile('./application/data/shoppingCartData.json', (err, data) => {
			if(err){
				console.log("Error reading file: " + err);
				this.cartProducts = [];
			} else {
				try{
					this.cartProducts = JSON.parse(data.toString());
				} catch(ex) {
					this.cartProducts = [];
				}
			}
		});
	}

	getAllCartProducts() {
		return this.cartProducts;
	}

	getCartProductByIndex(index) {
		return this.cartProducts.find(product => product.index == index);
	}

	addCartProduct(newProd) {
		if(this.getCartProductByIndex(newProd.index))
			return {};
		
		newProd.cartId = this.cartProducts.length + 1;
		this.cartProducts.push(newProd);
		return newProd;
	}
	
	deleteCartProductByIndex(index) {
		let deletedProd = this.getCartProductByIndex(index);
		this.cartProducts = this.cartProducts.filter(item => item.index != index); 
		return deletedProd;
	}
}

module.exports = shoppingCartModel;
	