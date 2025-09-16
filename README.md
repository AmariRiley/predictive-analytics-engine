# Predictive Analytics Engine

A Java-based GUI application for data analysis and machine learning using the Weka library. This tool provides an intuitive interface for loading CSV data, performing analysis, training predictive models, and generating comprehensive reports.

## Features

- **Data Loading**: Load CSV files with automatic format detection
- **Data Analysis**: Generate comprehensive statistics about your dataset
- **Data Preprocessing**: Automatic handling of missing values and data cleaning
- **Model Training**: Train multiple machine learning models and compare performance
- **Report Generation**: Create detailed reports with analysis results and recommendations
- **User-Friendly GUI**: Simple, step-by-step interface for non-technical users

## Project Structure

```
PredictiveAnalysis/
├── src/
│   └── com/
│       └── analytics/
│           ├── Main.java              # Application entry point
│           ├── DataProcessor.java     # Data loading and preprocessing
│           ├── ModelTraining.java     # Machine learning model training
│           ├── Report.java            # Report generation
│           └── gui/
│               └── AnalyticsGUI.java  # Main GUI interface
├── data/                              # Sample data files (optional)
├── lib/                               # External libraries
├── reports/                           # Generated reports output
└── README.md                          # This file
```

## Prerequisites

- **Java 8 or higher**
- **Weka library** (version 3.8.0 or later)
- **IDE**: IntelliJ IDEA, Eclipse, or similar (recommended)

## Installation

### Method 1: Using IntelliJ IDEA (Recommended)

1. **Clone or download** the project to your local machine
2. **Open IntelliJ IDEA**
3. **Open the project**: File → Open → Select the project folder
4. **Add Weka library**:
    - Go to File → Project Structure → Libraries
    - Click "+" → Java → Browse to weka.jar file
    - Apply and OK

### Method 2: Command Line

1. **Download Weka**: Get `weka.jar` from [https://www.cs.waikato.ac.nz/ml/weka/](https://www.cs.waikato.ac.nz/ml/weka/)
2. **Place Weka jar** in the `lib/` directory
3. **Compile**:
   ```bash
   javac -cp "lib/weka.jar" -d bin src/com/analytics/*.java src/com/analytics/gui/*.java
   ```
4. **Run**:
   ```bash
   java -cp "bin:lib/weka.jar" com.analytics.Main
   ```

## Usage

### Getting Started

1. **Run the application**:
    - In IntelliJ: Right-click on `Main.java` → Run 'Main.main()'
    - Or run the compiled version from command line

2. **Load your data**:
    - Click "Browse" to select a CSV file
    - Click "Load Data" to import the dataset
    - Wait for confirmation message

3. **Select target column**:
    - Choose the column you want to predict from the dropdown
    - This will be your dependent variable

4. **Analyze the data**:
    - Click "Analyze Data" to see dataset statistics
    - Review the information about your data

5. **Train models**:
    - Click "Train Models" to build predictive models
    - The system will test multiple algorithms and show performance

6. **Generate report**:
    - Click "Generate Report" to create a comprehensive analysis report
    - The report will be saved to the `reports/` directory

### Supported Data Formats

- **CSV files** with header row
- **Numeric and categorical data**
- **Missing values** (automatically handled)
- **Various data types** (strings, numbers, dates)

### Sample Workflow

```
Load Data → Analyze Data → Train Models → Generate Report
```

Each step enables the next, ensuring a logical workflow.

## Features in Detail

### Data Processing
- Automatic missing value imputation
- Data type detection and conversion
- Statistical summary generation
- Data quality assessment

### Model Training
- Multiple algorithm comparison
- Cross-validation for reliable results
- Performance metrics calculation
- Best model selection

### Report Generation
- Comprehensive data analysis summary
- Model performance comparison
- Recommendations for improvement
- Exportable format for sharing

## Sample Data

The project works with any CSV file. For testing, you can use datasets like:
- Iris dataset
- Wine quality dataset
- Customer data
- Sales data
- Any tabular data with a target variable

## Troubleshooting

### Common Issues

1. **"Cannot find Weka classes"**
    - Ensure weka.jar is in your classpath
    - Check that the library is properly added to your project

2. **"File not found" error**
    - Verify the CSV file path is correct
    - Ensure the file has proper read permissions

3. **"No data loaded" message**
    - Check that your CSV file has a header row
    - Verify the file is not empty
    - Ensure proper CSV formatting

4. **GUI not responding**
    - Large datasets may take time to process
    - Check the console for error messages
    - Ensure sufficient memory is available

### Performance Tips

- **For large datasets**: Increase JVM heap size with `-Xmx2g`
- **For better performance**: Close other applications while processing
- **For memory issues**: Use smaller dataset samples for testing

## Code Structure

- **Main.java**: Application entry point
- **DataProcessor.java**: Data handling and preprocessing
- **ModelTraining.java**: Machine learning algorithms
- **Report.java**: Output generation
- **AnalyticsGUI.java**: User interface

## Dependencies

- **Weka**: Machine learning library
- **Java Swing**: GUI framework
- **Java IO**: File handling
- **Standard Java libraries**: Collections, utilities

## License

This project is for educational purposes. Please check Weka's license for commercial use.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Verify all dependencies are properly installed
3. Review the console output for error messages
4. Ensure your data format is compatible

---

**Note**: This application is designed for educational and research purposes. Always validate results with domain experts before making business decisions.