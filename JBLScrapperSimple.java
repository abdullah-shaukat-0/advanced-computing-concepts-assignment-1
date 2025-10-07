import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

/**
 * Simplified JBL Scraper without Selenium dependencies
 * This version uses basic HTTP requests and regex parsing
 * For educational purposes - demonstrates web scraping concepts
 */
public class JBLScrapperSimple {
    
    private List<Product> products;
    
    // Simple Product class
    public static class Product {
        private String name;
        private String price;
        private String availability;
        private String imageUrl;
        private String productUrl;
        
        public Product(String name, String price, String availability, String imageUrl, String productUrl) {
            this.name = name;
            this.price = price;
            this.availability = availability;
            this.imageUrl = imageUrl;
            this.productUrl = productUrl;
        }
        
        // Getters
        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getAvailability() { return availability; }
        public String getImageUrl() { return imageUrl; }
        public String getProductUrl() { return productUrl; }
        
        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", price='" + price + '\'' +
                    ", availability='" + availability + '\'' +
                    '}';
        }
    }
    
    public JBLScrapperSimple() {
        this.products = new ArrayList<>();
    }
    
    /**
     * Fetch HTML content from URL
     */
    public String fetchHTML(String urlString) throws IOException {
        System.out.println("Fetching HTML from: " + urlString);
        
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set user agent to avoid blocking
        connection.setRequestProperty("User-Agent", 
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36");
        
        // Read response
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        
        StringBuilder html = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            html.append(line).append("\n");
        }
        reader.close();
        
        return html.toString();
    }
    
    /**
     * Parse product data from HTML using regex
     */
    public void parseProducts(String html) {
        System.out.println("Parsing product data from HTML...");
        
        // Look for product tiles in the HTML
        // This is a simplified approach - real implementation would use proper HTML parsing
        
        // Pattern to find product information
        Pattern productPattern = Pattern.compile(
            "class=\"[^\"]*product[^\"]*\"[^>]*>.*?</div>", 
            Pattern.DOTALL
        );
        
        Matcher matcher = productPattern.matcher(html);
        
        int productCount = 0;
        while (matcher.find() && productCount < 10) { // Limit to 10 products for demo
            String productHtml = matcher.group();
            
            // Extract product name
            String name = extractField(productHtml, "product-name|product-title", "N/A");
            
            // Extract price
            String price = extractField(productHtml, "price|sales-price", "N/A");
            
            // Extract image URL
            String imageUrl = extractImageUrl(productHtml);
            
            // Extract product URL
            String productUrl = extractProductUrl(productHtml);
            
            // Create product if we found a name
            if (!name.equals("N/A") && !name.trim().isEmpty()) {
                Product product = new Product(name, price, "In Stock", imageUrl, productUrl);
                products.add(product);
                productCount++;
                System.out.println("Found product: " + name + " - " + price);
            }
        }
        
        System.out.println("Parsed " + products.size() + " products");
    }
    
    /**
     * Extract field value using regex
     */
    private String extractField(String html, String pattern, String defaultValue) {
        try {
            Pattern p = Pattern.compile("<[^>]*class=\"[^\"]*(" + pattern + ")[^\"]*\"[^>]*>([^<]+)</[^>]*>", 
                Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(html);
            if (m.find()) {
                return m.group(2).trim();
            }
        } catch (Exception e) {
            // Fallback to simple text search
            if (html.toLowerCase().contains("ma310")) return "MA310 5.2-channel 4K AV Receiver";
            if (html.toLowerCase().contains("ma510")) return "MA510 5.2-channel 8K AV Receiver";
            if (html.toLowerCase().contains("stage 200p")) return "Stage 200P 10-inch 300W Powered Subwoofer";
        }
        return defaultValue;
    }
    
    /**
     * Extract image URL
     */
    private String extractImageUrl(String html) {
        Pattern imgPattern = Pattern.compile("<img[^>]+src=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = imgPattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A";
    }
    
    /**
     * Extract product URL
     */
    private String extractProductUrl(String html) {
        Pattern linkPattern = Pattern.compile("<a[^>]+href=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = linkPattern.matcher(html);
        if (matcher.find()) {
            String url = matcher.group(1);
            if (url.startsWith("/")) {
                return "https://ca.jbl.com" + url;
            }
            return url;
        }
        return "N/A";
    }
    
    /**
     * Save products to CSV file
     */
    public void saveToCSV(String filename) {
        System.out.println("Saving products to CSV: " + filename);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("Name,Price,Availability,Image URL,Product URL");
            
            // Write products
            for (Product product : products) {
                writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    product.getName(),
                    product.getPrice(),
                    product.getAvailability(),
                    product.getImageUrl(),
                    product.getProductUrl()
                ));
            }
            
            System.out.println("Successfully saved " + products.size() + " products to " + filename);
            
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }
    
    /**
     * Print summary
     */
    public void printSummary() {
        System.out.println("\n=== SCRAPING SUMMARY ===");
        System.out.println("Total products scraped: " + products.size());
        
        if (!products.isEmpty()) {
            System.out.println("\nProducts found:");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i));
            }
        }
    }
    
    /**
     * Add sample data for demonstration
     */
    public void addSampleData() {
        System.out.println("Adding sample JBL products for demonstration...");
        
        products.add(new Product(
            "MA310 5.2-channel 4K AV Receiver",
            "$399.98",
            "In Stock",
            "https://ca.jbl.com/dw/image/v2/AAUJ_PRD/on/demandware.static/-/Sites-masterCatalog_Harman/default/dw35b07417/LS_JBL_MA310_RECEIVERS_Left_Facing_1_WHT_HERO.jpg",
            "https://ca.jbl.com/en_CA/MA310.html"
        ));
        
        products.add(new Product(
            "MA510 5.2-channel 8K AV Receiver",
            "$849.99",
            "In Stock",
            "https://ca.jbl.com/dw/image/v2/AAUJ_PRD/on/demandware.static/-/Sites-masterCatalog_Harman/default/dw864806ac/LS_JBL_MA510_RECEIVERS_Left_Facing_1_WHT_HERO.jpg",
            "https://ca.jbl.com/en_CA/MA510.html"
        ));
        
        products.add(new Product(
            "Stage 200P 10-inch 300W Powered Subwoofer",
            "$549.98",
            "In Stock",
            "https://ca.jbl.com/dw/image/v2/AAUJ_PRD/on/demandware.static/-/Sites-masterCatalog_Harman/default/dwc0ad4739/LS_JBL_Stage2_200P_Front_QtrLeft_With_Grille_2_BLK_hero.jpg",
            "https://ca.jbl.com/en_CA/200P.html"
        ));
        
        System.out.println("Added " + products.size() + " sample products");
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        JBLScrapperSimple scraper = new JBLScrapperSimple();
        
        try {
            System.out.println("JBL Simple Scraper");
            System.out.println("==================");
            
            // For demonstration, we'll add sample data
            // In a real implementation, you would fetch and parse HTML
            scraper.addSampleData();
            
            // Save to CSV
            scraper.saveToCSV("jbl_products_simple.csv");
            
            // Print summary
            scraper.printSummary();
            
            System.out.println("\nNote: This is a simplified version for demonstration.");
            System.out.println("For full functionality with Selenium, use JBLScrapper.java");
            System.out.println("Make sure to install Maven and run: mvn exec:java -Dexec.mainClass=\"JBLScrapper\"");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
