# Setup Instructions for JBL Scraper

## Prerequisites Installation

### 1. Install Java 11 or Higher
```bash
# On macOS with Homebrew
brew install openjdk@11

# Or download from Oracle/OpenJDK website
# https://adoptium.net/
```

### 2. Install Maven
```bash
# On macOS with Homebrew
brew install maven

# Or download from Apache Maven website
# https://maven.apache.org/download.cgi
```

### 3. Install Chrome Browser
- Download and install Chrome from: https://www.google.com/chrome/

## Alternative: Manual Dependency Management

If you prefer not to use Maven, you can manually download the JAR files:

### Required JAR Files:
1. **Selenium Java** (selenium-java-4.15.0.jar)
2. **WebDriverManager** (webdrivermanager-5.6.2.jar)
3. **OpenCSV** (opencsv-5.8.jar)
4. **Jackson Core** (jackson-core-2.15.2.jar)
5. **Jackson Databind** (jackson-databind-2.15.2.jar)
6. **Jackson Annotations** (jackson-annotations-2.15.2.jar)
7. **SLF4J Simple** (slf4j-simple-2.0.9.jar)
8. **SLF4J API** (slf4j-api-2.0.9.jar)

### Manual Compilation:
```bash
# Create lib directory
mkdir lib

# Download JAR files to lib directory
# Then compile with classpath
javac -cp "lib/*" JBLScrapper.java

# Run with classpath
java -cp ".:lib/*" JBLScrapper
```

## Quick Start (with Maven)

Once Maven is installed:

```bash
# Navigate to project directory
cd "/Users/abdullahshaukat/Desktop/ACC Assignment 1"

# Install dependencies and compile
mvn clean compile

# Run the scraper
mvn exec:java -Dexec.mainClass="JBLScrapper"

# Or use the run script
./run.sh
```

### Note: Updated Selenium Imports
The project uses the new Selenium 4.x import structure with `dev.selenium.*` instead of `org.openqa.selenium.*`. This is the correct format for the latest Selenium version.

## Troubleshooting

### Maven Not Found
- Install Maven using Homebrew: `brew install maven`
- Or download from: https://maven.apache.org/download.cgi
- Add Maven to your PATH

### Java Not Found
- Install Java 11+: `brew install openjdk@11`
- Or download from: https://adoptium.net/
- Set JAVA_HOME environment variable

### ChromeDriver Issues
- The WebDriverManager will handle this automatically
- Ensure Chrome browser is installed
- Check internet connection for driver download

## Project Structure After Setup

```
├── pom.xml                 # Maven configuration
├── JBLScrapper.java        # Main scraper class
├── README.md              # Documentation
├── setup_instructions.md  # This file
├── run.sh                 # Run script
├── target/                # Maven build directory
│   ├── classes/           # Compiled classes
│   └── dependency-jars/   # Downloaded dependencies
├── jbl_products.csv       # Generated output
└── jbl_products.json      # Generated output
```
