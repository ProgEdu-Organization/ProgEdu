const assert = require('assert');
const { Given, When, Then } = require('@cucumber/cucumber');
const {Builder, By, until} = require('selenium-webdriver');
require('dotenv').config( {path:__dirname + '/../../.env'} ); // __dirname is this file dir path


class SingletDriver {
    
    constructor() {
        this.driver;
        console.log("test");
    }

    //靜態方法
    static getInstance() {
        if(!this.instance) {
            this.instance = new SingletDriver();
            this.instance.driver = new Builder().forBrowser('chrome').build();
        }

        return this.instance;
    }

    static getDriver() {
        return SingletDriver.getInstance().driver;
    }

}
exports.SingletDriver = SingletDriver;