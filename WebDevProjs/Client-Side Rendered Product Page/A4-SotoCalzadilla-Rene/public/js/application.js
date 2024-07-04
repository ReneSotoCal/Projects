//The Model is used to retrive and manipulate data
class PageModel {
    
    //Call REST API using XMLHttpRequest
	GetProducts(){
        
        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/product`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);
                let page = document.querySelector(".page");
                let event = new CustomEvent('ProductDataReceived', {detail: data});
                page.dispatchEvent(event);
            } else 
                console.log("server error code: " + this.status);
        };
        
        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();    
	}

    getProductByIndex(index, next) {

        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/product/${index}`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);

                if(!next){
                    let productCard = document.querySelector(`#product${index}`);
                    let event = new CustomEvent('IndexedProductReceived', {detail: data});
                    productCard.dispatchEvent(event); 
                } else {
                    let nextLink = document.querySelector("#next");
                    let nextEvent = new CustomEvent('NextProductReceived', {detail: data});
                    nextLink.dispatchEvent(nextEvent);
                }

            } else 
                console.log("server error code: " + this.status);
        };

        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();
    }

    getProductsBySearch(search) {

        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/product?search=${search}`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);
                let searchBt = document.querySelector("#searchBt");
                let event = new CustomEvent('SearchContentReceived', {detail: data});
                searchBt.dispatchEvent(event);
            } else 
                console.log("server error code: " + this.status);
        };
        
        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();
    }

    getProductsByFilter(filter) {

        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/product?filter=${filter}`;

        xhttp.onreadystatechange = function() {
            
            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);
                
                let trimmedFilter = '_';
                trimmedFilter += filter.toString().replace(/[\s.]+/g, '-');
                let filterLink = document.querySelectorAll(`#${trimmedFilter}`);

                filterLink.forEach(link => {
                    let event = new CustomEvent('FilteredProductsReceived', {detail: {data: data, filter: filter}});
                    link.dispatchEvent(event);
                });

            } else 
                console.log("server error code: " + this.status);
        };

        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();
    }

    getFilters() {

        let data = [];
        var xhttp = new XMLHttpRequest();
        let url = `./application/product/filters`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);
                let page = document.querySelector(".page");
                let event = new CustomEvent('FilterDataReceived', {detail: data});
                page.dispatchEvent(event);
            } else 
                console.log("server error code: " + this.status);
        };

        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();

    }

    getCartProducts(){

        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/cart`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                data = JSON.parse(xhttp.responseText);
                let page = document.querySelector(".page");
                let event = new CustomEvent('CartDataReceived', {detail: data});
                page.dispatchEvent(event);
            } else 
                console.log("server error code: " + this.status);
        };
        
        xhttp.open("GET", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.send();    
	}

    addCartProductByIndex(index){

        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/cart`;

        xhttp.onreadystatechange = function() {
            if (this.status == 200 && this.readyState == 4)
                data = JSON.parse(xhttp.responseText);  
            else
                console.log("server error code: " + this.status);        
        };
        
        xhttp.open("POST", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(JSON.stringify({index: index}));
    }

    deleteCartProductByIndex(index){
        let data = {};
        var xhttp = new XMLHttpRequest();
        let url = `./application/cart/${index}`;

        xhttp.onreadystatechange = function() {

            if (this.status == 200 && this.readyState == 4){
                if(xhttp.responseText){
                    try{
                      data = JSON.parse(xhttp.responseText);  
                    } catch(ex){
                        console.log("Exception thrown in deleteCartProductByIndex(): " + ex);
                    }
                }
            } else 
                console.log("server error code: " + this.status);
        };

        xhttp.open("DELETE", url);
        xhttp.setRequestHeader("Accept", "application/json");
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send(JSON.stringify({index: index}));
    }
}

//The View is used to update the page content
class PageView {

    //Update the HTML with the main page content
    CreateMainPage(data, type, value) {
        
        let root;
        this.resultsCt = 0;

        let content = `<div class="left-section"></div>
                        <div class="middle-section"></div>
                        <div class="right-section"></div>`;

        root = document.querySelector(".page");
        root.replaceChildren();
        root.innerHTML = content;
        let cards = '';

        data.forEach(item => {
            cards += `<div id="product${item.index}" class="product-card" onclick="app.createProductPage(${item.index}, false)">
                            <img src="../${item.image}" alt="${item.name}">
                            <h3 class="product-name">${item.name}</h3>
                            <h3 class="price">$${item.price}</h3>
                            <p class="brief-description">
                            ${item.shortDescription}</p>
                            </div>`;
            this.resultsCt++;
        });

        switch(type){
            case 'search':
                cards = `<div id="resultsContainer"><h4>${this.resultsCt} products shown for search text "${value}"</h4></div>${cards}`;
                break;
            case 'filter':
                cards = `<div id="resultsContainer"><h4>${this.resultsCt} products shown for "${value}"</h4></div>${cards}`;
                break;
            default:
                cards = `<div id="resultsContainer"><h4>${this.resultsCt} products shown</h4></div>${cards}`;
                break;
            }
        
        root = document.querySelector(".middle-section");
        root.replaceChildren();
        root.innerHTML = cards;
    }

    //Update the HTML with the product page content
    CreateProductPage(prodData) {
        
        this.content = "";
        this.content = `<div class="left-section">
		<div id="content-left-section"></div>
		</div>
		<div class="prod-mid-section">
			<span onclick="app.createProductPage(${prodData.nextIndex}, true)" id="next">Next &#10157;</span>
            <img id="prod-img" src="../${prodData.image}" alt="${prodData.name}">
            <div class="content-container">
                <h3 id="prod-name">${prodData.name}</h3>
                <h2 id="prod-price">$${prodData.price} <button class="add-to-cart-bt" onclick="app.addCartProductByIndex(${prodData.index})">Add to Cart &#x1f6d2;</button></h2>
                <h3 class="descript-header">About this item</h3>
                <p id="prod-description">
                    ${prodData.longDescription}
                </p>
                <h3 class="tag-header">Tags</h3> 
                <p class="tags" id="prod-tags">
                    <b>Brand</b> ${prodData.tags[0].brand}
                    <br><br><b>Model</b> ${prodData.tags[1].model}
                    <br><br><b>Screen Size</b> ${prodData.tags[2].screenSize}"
                    <br><br><b>Operating System</b> ${prodData.tags[3].operatingSystem}
                    <br><br><b>Color</b> ${prodData.tags[4].color}
                </p>
            </div>
		</div>
		<div class="right-section"></div>`;

        let contentDiv = document.querySelector(".page");
        contentDiv.replaceChildren();
        contentDiv.innerHTML = this.content;
    }

    createFilterSection(filterCategories) {
     
        let leftSection = document.querySelector(".left-section");
        leftSection.replaceChildren();

        leftSection.innerHTML = `<div id="filters">
				<div id="clearFilterCont"></div>
				<div id="brands"></div>
				<div id="models"></div>
				<div id="screen-sizes"></div>
				<div id="colors"></div>	
				<div id="operating-systems"></div>
			</div>`;

        this.brands = [];
        this.models = [];
        this.screenSizes = [];
        this.opSystems = [];
        this.colors = [];

        for(let i = 0; i < filterCategories.length; i++){
            filterCategories[i].forEach(tag => {
                switch(i){
                    case 0: this.brands.push(tag);
                        break;
                    case 1: this.models.push(tag);
                        break;
                    case 2: this.screenSizes.push(tag);
                        break;
                    case 3: this.opSystems.push(tag);
                        break;
                    case 4: this.colors.push(tag);
                        break;
                }
            });
        }

        let clearFiltersDiv = document.querySelector("#clearFilterCont");
        clearFiltersDiv.replaceChildren();
        clearFiltersDiv.innerHTML = `<h4 class="filter-lbls" id="clearFilter" onclick="app.createMainPage()">Clear All</h4>`;

        let brandFilters = '';
        brandFilters = this.getFilterTemplate(this.brands);
        brandFilters = `<h4 class="filter-lbls">Brands</h4>${brandFilters}`;

        let brandDiv = document.querySelector("#brands");
        brandDiv.replaceChildren();
        brandDiv.innerHTML = brandFilters;
    
        let modelFilters = '';
        modelFilters = this.getFilterTemplate(this.models);
        modelFilters = `<h4 class="filter-lbls">Models</h4>${modelFilters}`;

        let modelDiv = document.querySelector("#models");
        modelDiv.replaceChildren();
        modelDiv.innerHTML = modelFilters;

        let scrSizeFilters = '';
        scrSizeFilters = this.getFilterTemplate(this.screenSizes);
        scrSizeFilters = `<h4 class="filter-lbls">Screen Sizes</h4>${scrSizeFilters}`;

        let scrSizeDiv = document.querySelector("#screen-sizes");
        scrSizeDiv.replaceChildren();
        scrSizeDiv.innerHTML = scrSizeFilters;

        let colorFilters = '';
        colorFilters = this.getFilterTemplate(this.colors);
        colorFilters = `<h4 class="filter-lbls">Colors</h4>${colorFilters}`;

        let colorDiv = document.querySelector("#colors");
        colorDiv.replaceChildren();
        colorDiv.innerHTML = colorFilters;
    
        let osFilters = '';
        osFilters = this.getFilterTemplate(this.opSystems);
        osFilters = `<h4 class="filter-lbls">Operating Systems</h4>${osFilters}`;

        let osDiv = document.querySelector("#operating-systems");
        osDiv.replaceChildren();
        osDiv.innerHTML = osFilters;
    }

    createShoppingCartList(cartData) {

        this.sum = 0;
        let cards = ``;

        if(cartData.length !== 0){
            if(cartData.length > 0){
                cartData.forEach(product => {
                    this.sum += product.price;
                });

                cartData.forEach(card => {
                    cards += `<div class="shopping-cart-card">
                                <img src="${card.image}" class="cart-card-img">
                                <div class="cart-content">
                                <h4 class="cart-card-name">${card.name}</h4>
                                <h3 class="cart-card-price">$${card.price}<button class="delete-button" onclick="app.deleteCartProductByIndex(${card.index})">&#x1f5d1;</button></h3>    
                                </div>
                                </div>`;
                });
            } else {
                this.sum = cartData.price;
                cards += `<div class="shopping-cart-card">
                            <img src="${cartData.image}" class="cart-card-img">
                            <div class="cart-content">
                            <h4 class="cart-card-name">${cartData.name}</h4>
                            <h3 class="cart-card-price">$${cartData.price}<button class="delete-button" onclick="app.deleteCartProductByIndex(${cartData.index})">&#x1f5d1;</button></h3>    
                            </div>
                            </div>`;
            }
            
            let rightSection = document.querySelector(".right-section");
            rightSection.replaceChildren();
            rightSection.innerHTML = `<div id="cart-container">
                                            <div id="total-container"><h3 id=total-price>Subtotal: <br>$${this.sum.toFixed(2)}</h3></div>
                                            <div id="shopping-cart">${cards}</div>
                                        </div>`;
        } else {
            let rightSection = document.querySelector(".right-section");
            rightSection.replaceChildren();
        }
    }

    getFilterTemplate(filterCategory) {
        let filters = "";

        filterCategory.forEach(filter => {
            let trimmedFilter = '_';
            trimmedFilter += filter.filter.toString().replace(/[\s.]+/g, '-');
            filters += `<li class="filter-links" id="${trimmedFilter}" onclick="app.createFilteredPage('${filter.filter}')">${filter.filter}<span class="tagCounts">${filter.count}</span></li>`;      
        });

        filters = `<ul>${filters}</ul>`;
        return filters;
    }
}

//The Controller handles the flow of control
class PageController {

    constructor(pageModel, pageView) {
        this.pageModel = pageModel;
        this.pageView = pageView;
        
        let page = document.querySelector(".page");
        page.addEventListener('ProductDataReceived', event => {
            app.handleProductData(event.detail);
        });

        let searchBt = document.querySelector("#searchBt");
        searchBt.addEventListener('SearchContentReceived', event => {
            app.handleSearchData(event.detail);
        });

        page.addEventListener('FilterDataReceived', event => {
            app.handleFilterData(event.detail);
        });

        page.addEventListener('CartDataReceived', event => {
            app.handleProductCardData(event.detail);
        });

        this.createMainPage();
	}

    createMainPage() {
        this.pageModel.GetProducts();
        this.pageModel.getFilters();
        this.pageModel.getCartProducts();
    }

    createFilteredPage(filter) {
        
        this.pageModel.getProductsByFilter(filter);
        this.pageModel.getFilters();
        this.pageModel.getCartProducts();

        let trimmedFilter = '_';
        trimmedFilter += filter.toString().replace(/[\s.]/g, '-');
        let filterEl = document.querySelectorAll(`#${trimmedFilter}`);

        filterEl.forEach(el => {
            el.addEventListener('FilteredProductsReceived', event => {
                app.handleFilterClicked(event.detail.data, event.detail.filter);
            });
        });
    }

    createProductPage(index, next) {
        this.pageModel.getProductByIndex(index, next);
        this.pageModel.getCartProducts();
        let productCard = document.querySelector(`#product${index}`);

        if(productCard){
            productCard.addEventListener('IndexedProductReceived', event => {
                app.handleProductCardClicked(event.detail);
            });
        }
    }

    search() {
        let searchInput = document.getElementById("searchInput");
        let searchText = searchInput.value;

        this.pageModel.getProductsBySearch(searchText);
        this.pageModel.getFilters();
        this.pageModel.getCartProducts();
    }

    addCartProductByIndex(index) {
        this.pageModel.addCartProductByIndex(index);
        this.pageModel.getCartProducts();
    }

    deleteCartProductByIndex(index) {
        this.pageModel.deleteCartProductByIndex(index);
        this.pageModel.getCartProducts();
    }

    handleProductData(data) {
        this.pageView.CreateMainPage(data);
    }

    handleProductCardData(data) {
        this.pageView.createShoppingCartList(data);
    }

    handleProductCardClicked(data){
        this.pageView.CreateProductPage(data);
        let nextLink = document.querySelector("#next");
        if(nextLink){
            nextLink.addEventListener('NextProductReceived', event => {
                app.handleNextClicked(event.detail);
            }); 
        }
    }

    handleNextClicked(data){
        this.pageView.CreateProductPage(data);
        let nextLink = document.querySelector("#next");
        if(nextLink){
            nextLink.addEventListener('NextProductReceived', event => {
                app.handleNextClicked(event.detail);
            }); 
        }
    }

    handleSearchData(data) {
        let searchInput = document.getElementById("searchInput");
        let searchText = searchInput.value;
        this.pageView.CreateMainPage(data, 'search', searchText);
    }

    handleFilterData(data){
        this.pageView.createFilterSection(data);
    }

    handleFilterClicked(data, filter) {
        this.pageView.CreateMainPage(data, "filter", filter);
    }
}

const app = new PageController(new PageModel(), new PageView());