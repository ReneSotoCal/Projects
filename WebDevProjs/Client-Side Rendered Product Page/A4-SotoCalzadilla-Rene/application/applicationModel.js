const fs = require('fs');

//Typically the model will access a database of some type, but this is omitted in this example for simplicity
class applicationModel {
	constructor() {
		this.initialize();
	}

	initialize() {
		this.products = {};
		fs.readFile('./application/data/applicationData.json', (err, data) => {
			this.products = JSON.parse(data.toString());
			this.filters = [];
			this.brands = [];
			this.opSystems = [];
		
			this.colors = [];
			this.models = [];
			this.screenSizes = [];
			
			//Loop through products to create the filters.
			this.products.forEach( item => {
				this.brands.push(item.tags[0].brand);
				this.models.push(item.tags[1].model);
				this.screenSizes.push(item.tags[2].screenSize);
				this.opSystems.push(item.tags[3].operatingSystem);
				this.colors.push(item.tags[4].color);
			});

			this.filters.push(this.mapFilterCounts(this.brands));
			this.filters.push(this.mapFilterCounts(this.models));
			this.filters.push(this.mapFilterCounts(this.screenSizes));
			this.filters.push(this.mapFilterCounts(this.opSystems));
			this.filters.push(this.mapFilterCounts(this.colors));
		});

	}

	mapFilterCounts(filters) {
		let filterCounts = [];

		filters.forEach(filter => {
			if(!filterCounts.map(filterTag => filterTag.filter).includes(filter))
                filterCounts.push({"filter": filter, "count": 1});
            else
                filterCounts.find(filterObj => {
                    if(filterObj.filter == filter)
                        return filterObj.count += 1;
                });
		});

		return filterCounts;
	}

	getAllProducts() {
		return this.products;
	}

	getProductByIndex(index) {
		this.product = this.products.find(item => item.index == index);

		if(this.product.index < this.products.length)
			this.product.nextIndex =  this.product.index + 1;
		else
			this.product.nextIndex = 1;
	
		return this.product;
	}

	getProductsByFilter(filter) {
		filter = filter.toLowerCase();
		this.filtered = [];

		this.products.forEach(item => {
			switch(filter) {
				case item.tags[0].brand.toLowerCase():
				case item.tags[1].model.toLowerCase():
				case item.tags[2].screenSize.toString():
				case item.tags[3].operatingSystem.toLowerCase():
				case item.tags[4].color.toLowerCase():
					this.filtered.push(item);
					break;
			}
		});

		return this.filtered;
	}

	getProductsBySearch(search) {
		search = search.toLowerCase();
		this.searchResults = [];

		this.products.forEach(item => {
			if(item.shortDescription.toLowerCase().includes(search) || item.longDescription.toLowerCase().includes(search))
				this.searchResults.push(item);
		});
		return this.searchResults;
	}

	getAllFilters() {
		return this.filters;
	}
}

module.exports = applicationModel;
	