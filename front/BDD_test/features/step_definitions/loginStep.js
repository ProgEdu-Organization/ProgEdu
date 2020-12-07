const assert = require('assert');
const { Given, When, Then } = require('@cucumber/cucumber');
const {Builder, By, until} = require('selenium-webdriver');
require('dotenv').config( {path:__dirname + '/../../.env'} ); // __dirname is this file dir path

let driver;

Given('navigate to login page', function () {
    driver =  new Builder().forBrowser('chrome').build();
    driver.get(process.env["ProgEdu_HOST"]);
});

When('user logged in using username as {string} and password as {string}', function (username, password) {
    const usernameElement = driver.findElement(By.id("username"));
    const passwordElement = driver.findElement(By.id("password"));
    usernameElement.sendKeys(username);
    passwordElement.sendKeys(password);
    
    const loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
    loginButton.click();
});

// The default timeout in async function with cucumber is 5000ms  
Then("I will see Hi,root", {timeout: 10 * 1000}, async function () {
    
    const helloText = await driver.wait(until.elementLocated(By.xpath("//a[contains(text(),'Hi, root')]")), 10 * 1000);
    assert.strictEqual(await helloText.getText(), "Hi, root");
    driver.quit();
});

