# JBL Website Scraper

A comprehensive Java-based web scraper for extracting product data from the JBL Home Audio website using Selenium WebDriver.

## Features

### Task 1: Basic Web Scraping
- ✅ Opens the JBL website in a web browser using Selenium
- ✅ Finds and interacts with various elements (links, buttons, text boxes)
- ✅ Extracts product data (name, price, availability, images, URLs)
- ✅ Saves scraped data in CSV and JSON formats

### Task 2: Multi-Page Crawling
- ✅ Crawls multiple pages from the same website
- ✅ Combines results from all pages
- ✅ Handles pagination automatically

### Task 3: Advanced Selenium Features
- ✅ Waits for elements to load using WebDriverWait
- ✅ Handles popup windows and cookie consent
- ✅ Uses JavaScript execution for scrolling
- ✅ Implements robust error handling
- ✅ Uses explicit waits and implicit waits

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser installed on your system

## Installation

1. **Clone or download the project files**

2. **Install dependencies using Maven:**
   ```bash
   mvn clean install
   ```

3. **Ensure Chrome browser is installed:**
   - The scraper uses Chrome WebDriver
   - WebDriverManager will automatically download the appropriate ChromeDriver version

## Usage

### Running the Scraper

1. **Compile and run using Maven:**
   ```bash
   mvn compile exec:java -Dexec.mainClass="JBLScrapper"
   ```

2. **Or compile and run manually:**
   ```bash
   mvn compile
   mvn exec:java -Dexec.mainClass="JBLScrapper"
   ```

### What the Scraper Does

1. **Initialization:**
   - Sets up Chrome WebDriver with optimized options
   - Configures anti-detection measures
   - Sets up implicit and explicit waits

2. **Navigation:**
   - Opens the JBL Home Audio page
   - Handles popups and cookie consent
   - Interacts with page elements (sorting, filtering)

3. **Data Extraction:**
   - Extracts product information from each product tile
   - Captures: name, price, original price, discount, availability, image URL, product URL, color, category
   - Handles multiple pages automatically

4. **Data Export:**
   - Saves data to `jbl_products.csv`
   - Saves data to `jbl_products.json`
   - Prints a summary of scraped data

## Output Files

### CSV Output (`jbl_products.csv`)
Contains the following columns:
- Name
- Price
- Original Price
- Discount
- Availability
- Image URL
- Product URL
- Category
- Color
- Description

### JSON Output (`jbl_products.json`)
Contains the same data in JSON format for easy programmatic access.

## Advanced Features

### Anti-Detection Measures
- Custom user agent
- Disabled automation flags
- Human-like behavior simulation

### Robust Error Handling
- Try-catch blocks for all critical operations
- Graceful handling of missing elements
- Continues scraping even if individual products fail

### Wait Strategies
- Explicit waits for element visibility
- Implicit waits for page loads
- Custom timeouts for different operations

### Multi-Page Support
- Automatic pagination detection
- Scroll-to-element before clicking
- Page load verification

## Configuration

You can modify the following parameters in the code:

- `maxPages`: Maximum number of pages to crawl (default: 3)
- `waitTimeout`: WebDriverWait timeout in seconds (default: 20)
- `implicitWait`: Implicit wait timeout in seconds (default: 10)

## Troubleshooting

### Common Issues

1. **ChromeDriver not found:**
   - Ensure Chrome browser is installed
   - WebDriverManager should handle this automatically

2. **Timeout errors:**
   - Increase wait timeouts in the code
   - Check internet connection
   - Verify the website is accessible

3. **No products found:**
   - The website structure might have changed
   - Check the CSS selectors in the `extractProductData()` method
   - Verify the website URL is correct

### Debug Mode

To enable more verbose logging, you can modify the Chrome options:

```java
options.addArguments("--verbose");
options.addArguments("--log-level=0");
```

## Project Structure

```
├── pom.xml                 # Maven configuration with dependencies
├── JBLScrapper.java        # Main scraper class
├── README.md              # This file
├── jbl_products.csv       # Generated CSV output
└── jbl_products.json      # Generated JSON output
```

## Dependencies

- **Selenium Java 4.15.0** (dev.selenium): WebDriver automation with updated imports
- **WebDriverManager 5.6.2**: Automatic driver management
- **OpenCSV 5.8**: CSV file handling
- **Jackson 2.15.2**: JSON processing
- **SLF4J 2.0.9**: Logging

### Updated Import Statements
The scraper uses the new Selenium 4.x import structure:
```java
import dev.selenium.*;
import dev.selenium.chrome.ChromeDriver;
import dev.selenium.chrome.ChromeOptions;
import dev.selenium.support.ui.WebDriverWait;
import dev.selenium.support.ui.ExpectedConditions;
import dev.selenium.interactions.Actions;
```

## References

- [Selenium Documentation](https://www.selenium.dev/documentation/en/)
- [WebDriver Getting Started](https://www.selenium.dev/documentation/en/getting_started_with_webdriver/third_party_drivers_and_plugins/#java)
- [Selenium Web Scraping Example](https://www.selenium.dev/documentation/en/webdriver/browser_manipulation/#scraping)

## License

This project is for educational purposes as part of the ACC Assignment 1.
# advanced-computing-concepts-assignment-1
