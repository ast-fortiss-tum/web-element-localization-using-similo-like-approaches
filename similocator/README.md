# Similocator: Enhanced Selenium Locators with Similo Algorithm

## Overview

Similocator is a Java library designed to enhance the reliability of automated web testing in the Selenium framework. By implementing the Similo algorithm, it provides a robust solution to the challenge of dynamically changing web elements. This library seamlessly integrates with Selenium WebDriver, ensuring minimal adaptation effort for existing projects.

## Key Features

- **Seamless Integration with Selenium**: Wraps existing locators, activating Similo when the original locator fails.
- **Support for Multiple Locators**: Compatible with Selenium's native locators such as XPath, CSS selectors, and ID.
- **Intelligent Locator Processing**: Automated workflow, with database-backed element information storage.
- **Dynamic Element Information Capture**: Captures and stores detailed element info, enhancing future accuracy.
- **Self-Repair Capability**: Automatically updates broken locators in the database.
- **Element Change Detection**: Monitors and updates element records in response to significant changes.
- **Similo Score Warnings**: Issues warnings for potential mismatches based on Similo score discrepancies.

More explanation of the Similo algorithm and its benefits can be found in the [Similo Paper](https://arxiv.org/abs/2208.00677). A detailed description of the workflow and implementation of Similocator is available in the [docs](docs/details.md).

## Installation

Include Similocator in your project by adding it to your project's build file. The repository is available on GitHub: [Similocator Repository](https://github.com/ast-fortiss-tum/web-element-localization-using-similo-like-approaches/tree/main/similocator).

## Usage

### Basic Usage

Wrap your existing Selenium locators with Similocator to enable the enhanced locating functionality:

```java
// Original Selenium locator
WebElement originalElement = driver.findElement(By.id("login-button"));

// Enhanced with Similocator
WebElement enhancedElement = driver.findElement(Similocator.similo(By.id("login-button")));
```

### Advanced Usage
Utilize different strategies (Similo, VON Similo, Similo-VON combination) based on the specific needs of your test cases. Each strategy has its own advantages and disadvantages, so it is recommended to experiment with different strategies to find the best fit for your project.


## Configuration

### Database Connection
Create `application.conf` in your project's resources folder with your database connection details:

```conf
artifact_database {
  host = "your_host"
  port = "3306"
  user = "your_user"
  password = "your_password"
}
```

### Configurable Options

- **Deactivation**: Temporarily disable Similocator for testing (`similo.deactivate`).
- **Warning Threshold**: Set a threshold for similarity score divergences indicating potential locator issues (`similo.warning_threshold`).

## Acknowledgements

The concept of Similo was developed by Michel Nass et al. in their paper "Similarity-based web element localization for robust test automation". The original paper is available at [Arxiv](https://arxiv.org/abs/2208.00677).

This project uses code adapted from the Apache Commons Lang and Apache Commons Text libraries, which are part of the [Apache Commons Project](https://commons.apache.org/). These libraries are used under the terms of the Apache License 2.0.
