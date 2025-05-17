# INVENTORY-MANGEMENT-GUI-JAVA

A Java Swing desktop application to view and interact with inventory and supplier data stored in a relational database. The application allows users to:

* Select a database table (`Inventory` or `Supplier`) to view.
* Choose columns to display (all or selected columns).
* Load data into a table with sorting by any selected column.
* Select the underlying Java collection type (`ArrayList`, `LinkedList`, `HashSet`, `TreeSet`) used to hold and sort data.
* View data in a clean, sortable, tabular format.
* See real-time status updates for data loading.

## Features

* Dynamic UI to select table, columns, and collection type.
* Flexible column selection for customized views.
* Sorting support on all columns.
* Supports common collection types to demonstrate differences.
* Error handling with descriptive messages.
* Uses JDBC for database connection (requires `DBHelper` class for connection).

## Database Schema

### Inventory Table

```sql
CREATE TABLE Inventory (
  itemId INT PRIMARY KEY,
  itemName VARCHAR(100),
  price FLOAT,
  category CHAR(1),
  inStock BOOLEAN
);
```

### Supplier Table

```sql
CREATE TABLE Supplier (
  supplierId INT PRIMARY KEY,
  supplierName VARCHAR(100),
  rating FLOAT,
  region CHAR(1),
  active BOOLEAN
);
```

## Requirements

* Java 17 or higher
* Swing GUI framework (part of Java SE)
* JDBC driver for your database
* `DBHelper` class configured to connect to your database

## How to Run

1. Setup your database with the `Inventory` and `Supplier` tables.
2. Configure the `DBHelper` class with your database credentials.
3. Compile and run `DataViewer.java`.
4. Use the GUI to select tables, columns, and collection types.
5. Load and explore your data interactively.

## Notes

* The collection selector allows you to experiment with different Java collection implementations and observe their effects on data display and sorting.
* This is a learning/demo project aimed at demonstrating JDBC integration, Swing GUI programming, and collection handling.

