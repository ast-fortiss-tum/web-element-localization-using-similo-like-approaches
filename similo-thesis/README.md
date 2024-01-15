# Similo Thesis

This project contains the source code for the groundwork for my bachelor thesis covering teh Similo approach.

## Structure

The project is structured as follows:

- `elements`: contains a class holding an element and functionality.
- `evaluation`: contains the scripts to compute the Classification of the elements, create a dataset on which the evaluation can run, runs the evaluation and creates a statistics object from which all metrics can be calculated. Usage would look like this to benchmark a locator strategy implementing the Locator trait:
    
    ```scala 3
    val locator: Locator = new MyLocator()
    val stats: EvaluationStatistics = EvaluationComputing.computeAll(locator)
    val fitness: Double = stats.score
    ```
  Where the computeAll methods allows for multiple parameters to individually configure the evaluation. Additionally, the `EvaluationStatistics` object contains many more metrics that can be used to evaluate the locator.
- `optimize` - contains the scripts to run the different types of optimizations described in the thesis.
- `scrape` - contains the scripts to scrape the data from the webpages. 
- `scripts` - contains a set of scripts which were used to compute various metrics and statistics for the thesis.
- `similo` - contains the various implementations of the Similo approach and the different strategies to compute the similarity between elements.
- `utils` - contains various utility classes and objects used throughout the project. Notably the `Load` object which is used to load the data from `sites` folder and allows for easy access to the data.

### Installation

The project requires `sbt` to be installed. The project can be run by executing `sbt run` in the root directory. This will run the `Main` class which will run the evaluation on the default parameters. What exactly is run can be configured in the `Main` class. It contains all steps used for the evaluation which just need to be called in the main method. 

The project requires Java 17 to be installed and configured as the default Java version. This can be setting the `JAVA_HOME` environment variable to the path of the Java 17 installation.

The project also requires the `chromedriver` and `geckodriver` to be installed and configured. The `driver` folder contains two scripts which download the corresponding driver for your system and place it in the `drivers` folder. 

The sites data needs to be downloaded and put into a folder in the root of the project called `sites`. 
