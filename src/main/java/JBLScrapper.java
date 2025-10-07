import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.opencsv.CSVWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JBLScrapper {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private List<Product> products;
    private ObjectMapper objectMapper;
    
    // Product data class
    public static class Product {
        private String name;
        private String price;
        private String originalPrice;
        private String discount;
        private String availability;
        private String imageUrl;
        private String productUrl;
        private String category;
        private String color;
        private String description;
        
        // Constructors
        public Product() {}
        
        public Product(String name, String price, String availability, String imageUrl, String productUrl) {
            this.name = name;
            this.price = price;
            this.availability = availability;
            this.imageUrl = imageUrl;
            this.productUrl = productUrl;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        
        public String getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(String originalPrice) { this.originalPrice = originalPrice; }
        
        public String getDiscount() { return discount; }
        public void setDiscount(String discount) { this.discount = discount; }
        
        public String getAvailability() { return availability; }
        public void setAvailability(String availability) { this.availability = availability; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getProductUrl() { return productUrl; }
        public void setProductUrl(String productUrl) { this.productUrl = productUrl; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    ", availability='" + availability + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    '}';
        }
    }
    
    public JBLScrapper() {
        this.products = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Initialize the WebDriver with Chrome browser
     */
    public void initializeDriver() {
        System.out.println("Initializing Chrome WebDriver...");
        
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Initialize driver
        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Maximize window
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        System.out.println("WebDriver initialized successfully!");
    }
    
    /**
     * Navigate to the JBL home audio page
     */
    public void navigateToJBLHomeAudio() {
        System.out.println("Navigating to JBL Home Audio page...");
        String url = "https://ca.jbl.com/en_CA/home-audio/";
        driver.get(url);
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        System.out.println("Successfully navigated to: " + url);
    }
    
    /**
     * Handle popup windows and cookies
     */
    public void handlePopups() {
        System.out.println("Handling popups and cookies...");
        
        try {
            // Wait for potential popup and close it
            Thread.sleep(2000);
            
            // Look for common popup selectors
            String[] popupSelectors = {
                "button[aria-label='Close']",
                ".modal-close",
                ".popup-close",
                "button[class*='close']",
                ".cookie-accept",
                "#cookie-accept"
            };
            
            for (String selector : popupSelectors) {
                try {
                    WebElement popup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    popup.click();
                    System.out.println("Closed popup using selector: " + selector);
                    break;
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
        } catch (Exception e) {
            System.out.println("No popups found or already closed");
        }
    }
    
    /**
     * Interact with page elements (sorting, filtering)
     */
    public void interactWithPageElements() {
        System.out.println("Interacting with page elements...");
        
        try {
            // Wait for page to be fully loaded
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-tile")));
            
            // Try to interact with sort dropdown
            try {
                WebElement sortDropdown = driver.findElement(By.cssSelector("select[name='sort']"));
                if (sortDropdown.isDisplayed()) {
                    sortDropdown.click();
                    System.out.println("Clicked on sort dropdown");
                }
            } catch (Exception e) {
                System.out.println("Sort dropdown not found or not interactable");
            }
            
            // Try to interact with filter options
            try {
                WebElement priceFilter = driver.findElement(By.xpath("//input[@type='checkbox' and contains(@value, '100-200')]"));
                if (priceFilter.isDisplayed()) {
                    priceFilter.click();
                    System.out.println("Applied price filter");
                }
            } catch (Exception e) {
                System.out.println("Price filter not found");
            }
            
        } catch (Exception e) {
            System.out.println("Error interacting with page elements: " + e.getMessage());
        }
    }
    
    /**
     * Extract product data from the current page
     */
    public void extractProductData() {
        System.out.println("Extracting product data...");
        
        try {
            // Wait for products to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-tile")));
            
            // Find all product tiles
            List<WebElement> productTiles = driver.findElements(By.className("product-tile"));
            System.out.println("Found " + productTiles.size() + " products on current page");
            
            for (WebElement productTile : productTiles) {
                try {
                    Product product = new Product();
                    
                    // Extract product name
                    try {
                        WebElement nameElement = productTile.findElement(By.cssSelector(".product-name, .product-title, h3, h4"));
                        product.setName(nameElement.getText().trim());
                    } catch (Exception e) {
                        product.setName("N/A");
                    }
                    
                    // Extract price
                    try {
                        WebElement priceElement = productTile.findElement(By.cssSelector(".price, .product-price, .sales-price"));
                        product.setPrice(priceElement.getText().trim());
                    } catch (Exception e) {
                        product.setPrice("N/A");
                    }
                    
                    // Extract original price (if on sale)
                    try {
                        WebElement originalPriceElement = productTile.findElement(By.cssSelector(".standard-price, .original-price"));
                        product.setOriginalPrice(originalPriceElement.getText().trim());
                    } catch (Exception e) {
                        product.setOriginalPrice("N/A");
                    }
                    
                    // Extract discount percentage
                    try {
                        WebElement discountElement = productTile.findElement(By.cssSelector(".save-percent, .discount"));
                        product.setDiscount(discountElement.getText().trim());
                    } catch (Exception e) {
                        product.setDiscount("N/A");
                    }
                    
                    // Extract availability
                    try {
                        WebElement availabilityElement = productTile.findElement(By.cssSelector(".availability, .stock-status"));
                        product.setAvailability(availabilityElement.getText().trim());
                    } catch (Exception e) {
                        product.setAvailability("In Stock"); // Default assumption
                    }
                    
                    // Extract image URL
                    try {
                        WebElement imageElement = productTile.findElement(By.cssSelector("img"));
                        product.setImageUrl(imageElement.getAttribute("src"));
                    } catch (Exception e) {
                        product.setImageUrl("N/A");
                    }
                    
                    // Extract product URL
                    try {
                        WebElement linkElement = productTile.findElement(By.cssSelector("a"));
                        product.setProductUrl(linkElement.getAttribute("href"));
                    } catch (Exception e) {
                        product.setProductUrl("N/A");
                    }
                    
                    // Extract color information
                    try {
                        WebElement colorElement = productTile.findElement(By.cssSelector(".color, .product-color"));
                        product.setColor(colorElement.getText().trim());
                    } catch (Exception e) {
                        product.setColor("N/A");
                    }
                    
                    // Set category
                    product.setCategory("Home Audio");
                    
                    // Only add products with valid names
                    if (!product.getName().equals("N/A") && !product.getName().isEmpty()) {
                        products.add(product);
                        System.out.println("Extracted: " + product.getName() + " - " + product.getPrice());
                    }
                    
                } catch (Exception e) {
                    System.out.println("Error extracting product data: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error during data extraction: " + e.getMessage());
        }
    }
    
    /**
     * Navigate to multiple pages and extract data
     */
    public void crawlMultiplePages() {
        System.out.println("Starting multi-page crawling...");
        
        try {
            int maxPages = 3; // Limit to 3 pages for demo
            int currentPage = 1;
            
            while (currentPage <= maxPages) {
                System.out.println("Crawling page " + currentPage + "...");
                
                // Extract data from current page
                extractProductData();
                
                // Try to find and click next page button
                try {
                    WebElement nextButton = driver.findElement(By.cssSelector(".pagination-next, .next-page, [aria-label='Next']"));
                    if (nextButton.isDisplayed() && nextButton.isEnabled()) {
                        // Scroll to element
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
                        Thread.sleep(1000);
                        
                        // Click next button
                        nextButton.click();
                        
                        // Wait for page to load
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-tile")));
                        currentPage++;
                        
                        System.out.println("Navigated to page " + currentPage);
                    } else {
                        System.out.println("No more pages available");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Could not find next page button: " + e.getMessage());
                    break;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error during multi-page crawling: " + e.getMessage());
        }
    }
    
    /**
     * Save scraped data to CSV file
     */
    public void saveToCSV(String filename) {
        System.out.println("Saving data to CSV file: " + filename);
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            // Write header
            String[] header = {
                "Name", "Price", "Original Price", "Discount", "Availability", 
                "Image URL", "Product URL", "Category", "Color", "Description"
            };
            writer.writeNext(header);
            
            // Write product data
            for (Product product : products) {
                String[] row = {
                    product.getName(),
                    product.getPrice(),
                    product.getOriginalPrice(),
                    product.getDiscount(),
                    product.getAvailability(),
                    product.getImageUrl(),
                    product.getProductUrl(),
                    product.getCategory(),
                    product.getColor(),
                    product.getDescription()
                };
                writer.writeNext(row);
            }
            
            System.out.println("Successfully saved " + products.size() + " products to " + filename);
            
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }
    
    /**
     * Save scraped data to JSON file
     */
    public void saveToJSON(String filename) {
        System.out.println("Saving data to JSON file: " + filename);
        
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                       .writeValue(new File(filename), products);
            System.out.println("Successfully saved data to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to JSON: " + e.getMessage());
        }
    }
    
    /**
     * Print summary of scraped data
     */
    public void printSummary() {
        System.out.println("\n=== SCRAPING SUMMARY ===");
        System.out.println("Total products scraped: " + products.size());
        
        if (!products.isEmpty()) {
            System.out.println("\nSample products:");
            for (int i = 0; i < Math.min(5, products.size()); i++) {
                System.out.println((i + 1) + ". " + products.get(i));
            }
        }
        
        // Count products by category
        Map<String, Integer> categoryCount = new HashMap<>();
        for (Product product : products) {
            categoryCount.merge(product.getCategory(), 1, Integer::sum);
        }
        
        System.out.println("\nProducts by category:");
        categoryCount.forEach((category, count) -> 
            System.out.println("- " + category + ": " + count + " products"));
    }
    
    /**
     * Close the WebDriver
     */
    public void closeDriver() {
        if (driver != null) {
            System.out.println("Closing WebDriver...");
            driver.quit();
        }
    }
    
    /**
     * Main method to run the scraper
     */
    public static void main(String[] args) {
        JBLScrapper scraper = new JBLScrapper();
        
        try {
            // Initialize WebDriver
            scraper.initializeDriver();
            
            // Navigate to JBL Home Audio page
            scraper.navigateToJBLHomeAudio();
            
            // Handle popups
            scraper.handlePopups();
            
            // Interact with page elements
            scraper.interactWithPageElements();
            
            // Crawl multiple pages
            scraper.crawlMultiplePages();
            
            // Save data to files
            scraper.saveToCSV("jbl_products.csv");
            scraper.saveToJSON("jbl_products.json");
            
            // Print summary
            scraper.printSummary();
            
        } catch (Exception e) {
            System.err.println("Error during scraping: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the driver
            scraper.closeDriver();
        }
    }
}
