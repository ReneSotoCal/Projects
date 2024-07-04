const fs = require('fs');
const path = require("path");

class application {
    constructor(req) {// instantiating the class variables
        this.req = req;
        this.template = fs.readFileSync('./application/template/application.template').toString();
        this.data = JSON.parse(fs.readFileSync(path.resolve(__dirname, './applicationData.json')));
        this.contentTemplate = fs.readFileSync('./application/template/content.template').toString();
    }

    getFilters(){

        this.brands = [];
        this.models = [];
        this.colors = [];
        this.screenSizes = [];
        this.operatingSystems = [];

        this.data.forEach(item => {
            this.brands.push(item.tags[0].brand);
            this.models.push(item.tags[1].model);
            this.screenSizes.push(item.tags[2].screenSize);
            this.operatingSystems.push(item.tags[3].operatingSystem);
            this.colors.push(item.tags[4].color);
        });

        this.brandTemplate = this.generateFilters(this.brands);
        this.modelTemplate = this.generateFilters(this.models);
        this.screenSizeTemplate = this.generateFilters(this.screenSizes);
        this.opSysTemplate = this.generateFilters(this.operatingSystems);
        this.colorTemplate = this.generateFilters(this.colors);

        this.template = this.template.replace("{{brands}}", this.brandTemplate);
        this.template = this.template.replace("{{models}}", this.modelTemplate);
        this.template = this.template.replace("{{screen sizes}}", this.screenSizeTemplate);
        this.template = this.template.replace("{{operating systems}}", this.opSysTemplate);
        this.template = this.template.replace("{{colors}}", this.colorTemplate);

    }

    generateFilters(tags) {
        
        this.filterTemplate = `<ul>`;
        this.set = new Set(tags);

        this.set.forEach(tag => {
            this.filterTemplate += `<li><a class="filterLinks" href="{{urlpath}}${tag}">${tag}</a><span class="tagCounts">{{${tag}Counter}}</span></li> `;
        });

        this.filterTemplate += `</ul>`;
        let tagCtArr = [];

        tags.forEach(tag =>{

            if(!tagCtArr.map(tagEl => tagEl.tag).includes(tag))
                tagCtArr.push({"tag": tag, "count": 1});
            else
                tagCtArr.find(tagObj => {
                    if(tagObj.tag == tag)
                        return tagObj.count += 1;
                });
        });

        tagCtArr.forEach(tagObj => {
            this.filterTemplate = this.filterTemplate.replace(`{{${tagObj.tag}Counter}}`, tagObj.count)
        });

        return this.filterTemplate;
    }

    getQueriedPage(reqBody){

        let cards = '';
        this.resultsCt = 0;

        this.data.forEach(item => {//Loop through the products and write HTML for there data as product cards
            if(item.shortDescription.toLowerCase().includes(reqBody.search.toLowerCase()) || item.longDescription.toLowerCase().includes(reqBody.search.toLowerCase())){
                cards += `<div id="product" class="product-card">
                            <img src="../${item.image}" alt="${item.name}">
                            <h3 class="product-name"><a href="../application${item.path}">${item.name}</a></h3>
                            <h3 class="price">$${item.price}</h3>
                            <p class="brief-description">
                            ${item.shortDescription}</p>
                            </div>`;
                this.resultsCt++;
            } 
        });

        if(this.resultsCt > 1)
            this.results = `<h4 id="resultCtr">${this.resultsCt} results shown for search text "${reqBody.search}" </h4>`;
        else
            this.results = `<h4 id="resultCtr">${this.resultsCt} result shown for search text "${reqBody.search}" </h4>`;

        this.template = this.template.replace('{{results counter}}', this.results);
        this.template = this.template.replace("img/logo.png", "../img/logo.png");
        this.template = this.template.replace('{{products}}', cards);//Change the template to include the generated HTML

        this.getFilters();
        this.template = this.template.replaceAll("{{urlpath}}", "./application/filter/");
        return this.template;
    }

    getApplicationPage() {

        this.page = 'application';
        this.resultsCt = 0;
        let cards = "";//Creating an empty string to concatenate generated HTML
        
        this.data.forEach(item => {//Loop through the products and write HTML for there data as product cards
            this.resultsCt++;

            cards += `<div id="product" class="product-card">
            <img src="${item.image}" alt="${item.name}">
            <h3 class="product-name"><a href="../application${item.path}">${item.name}</a></h3>
            <h3 class="price">$${item.price}</h3>
            <p class="brief-description">
            ${item.shortDescription}</p>
            </div>`;
        });

        this.result = `<h4 id="resultCtr">${this.resultsCt} products shown</h4>`
        this.template = this.template.replace('{{results counter}}', this.result);
        this.template = this.template.replace('{{products}}', cards);//Change the template to include the generated HTML

        this.getFilters();
        this.template = this.template.replaceAll("{{urlpath}}", "./application/filter/");
        return this.template;
    }
    



    getContentPage() {
        console.log(this.req.path);
        this.page = 'content';
        this.currentContent = this.data.find(item => { return item.path == this.req.path });
        this.next = "";
        this.content = "";

        if(this.currentContent.index < this.data.length)
            this.next = this.data.find(item => { return item.index == this.currentContent.index + 1; });
        else
            this.next = this.data[0];

        if(this.currentContent != null){
            this.content = `<a id="next-link" href="../application${this.next.path}"><span>Next &#10157;</span></a>
            <img id="prod-img" src="../${this.currentContent.image}" alt="${this.currentContent.name}">
            <div class="content-container">
            <h3 id="prod-name">${this.currentContent.name}</h3>
            <h2 id="prod-price">$${this.currentContent.price}</h2>
            <h3 class="descript-header">About this item</h3>
            <p id="prod-description">
                ${this.currentContent.longDescription}
            </p>
            <h3 class="tag-header">Tags</h3> 
            <p class="tags" id="prod-tags">
                <b>Brand</b> ${this.currentContent.tags[0].brand}
                <br><br><b>Model</b> ${this.currentContent.tags[1].model}
                <br><br><b>Screen Size</b> ${this.currentContent.tags[2].screenSize}"
                <br><br><b>Operating System</b> ${this.currentContent.tags[3].operatingSystem}
                <br><br><b>Color</b> ${this.currentContent.tags[4].color}
            </p>
        </div>	`;
        }
    
        if(this.content !== "")
            this.contentTemplate = this.contentTemplate.replace('{{product}}', this.content);	

        return this.contentTemplate;
    }

    getFilteredPage(reqParam) {

        this.page = 'application';
        this.resultsCt = 0;
        this.matchedContent = [];
        let cards = '';

        this.data.forEach(item => {
            if(item.tags.some(tag => tag.brand == reqParam || tag.color == reqParam || tag.operatingSystem == reqParam || tag.screenSize == reqParam || tag.model == reqParam))
                this.matchedContent.push(item);
        });

        this.matchedContent.forEach(item => {
            cards += `<div id="product" class="product-card">
                            <img src="../../${item.image}" alt="${item.name}">
                            <h3 class="product-name"><a href="..${item.path}">${item.name}</a></h3>
                            <h3 class="price">$${item.price}</h3>
                            <p class="brief-description">
                            ${item.shortDescription}</p>
                            </div>`;
            this.resultsCt++;
        });

        this.result = `<h4 id="resultCtr">${this.resultsCt} products shown</h4>`;
        this.template = this.template.replace('{{results counter}}', this.result);
        this.template = this.template.replace("img/logo.png", "../../img/logo.png");
        this.template = this.template.replace('{{products}}', cards);//Change the template to include the generated HTML
       
        this.getFilters();
        this.template = this.template.replaceAll("{{urlpath}}", "../application/filter");
        return this.template;
    }
}

module.exports = application;