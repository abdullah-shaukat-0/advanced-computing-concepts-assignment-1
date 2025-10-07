#!/bin/bash

echo "JBL Website Scraper"
echo "==================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven first: https://maven.apache.org/install.html"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

echo "Installing dependencies..."
mvn clean install

echo ""
echo "Starting JBL scraper..."
echo "This will open a Chrome browser window and scrape the JBL website."
echo "Press Ctrl+C to stop the scraper if needed."
echo ""

# Run the scraper
mvn exec:java -Dexec.mainClass="JBLScrapper"

echo ""
echo "Scraping completed!"
echo "Check the following files for results:"
echo "- jbl_products.csv"
echo "- jbl_products.json"
