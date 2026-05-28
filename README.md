# Fulfilment & Warehouse Management Assignment

This repository contains my implementation of the warehouse management code assignment.

The project is built using **Java**, **Quarkus**, **Hibernate Panache**, **REST APIs**, and **PostgreSQL**, and focuses on warehouse lifecycle management, fulfilment constraints, transaction consistency, testing strategy, and architectural reasoning.

---

# Features Implemented

## 1. Location Resolution

Implemented location resolution logic in:

```text
com.fulfilment.application.monolith.location
```

### Implemented

* Resolve location by identifier
* Validation for existing locations

---

## 2. Store Management Improvements

Implemented transaction-safe synchronization with the legacy system.

### Problem

The original implementation invoked the legacy gateway before transaction completion, which could result in downstream systems receiving uncommitted data.

### Solution

Refactored the flow so that:

* database transaction commits first
* legacy synchronization occurs only after successful commit

### Benefits

* prevents inconsistent synchronization
* improves reliability
* better transaction boundary handling

---

## 3. Warehouse Management

Implemented complete warehouse lifecycle management.

### Implemented Operations

* Create Warehouse
* Retrieve Warehouse
* Replace Warehouse
* Archive Warehouse

### Business Validations

#### Business Unit Code Validation

* active warehouse business unit code must be unique

#### Location Validation

* warehouse location must exist

#### Warehouse Capacity Validation

* warehouse capacity must not exceed location limits
* warehouse stock must fit within capacity

#### Warehouse Replacement Validation

* replacement warehouse must accommodate existing stock
* replacement warehouse stock must match previous warehouse

### Warehouse Replacement Logic

Warehouse replacement was implemented as:

1. archive existing warehouse
2. create replacement warehouse
3. preserve same business unit code

This preserves business continuity and historical tracking.

---

# BONUS Feature — Warehouse Fulfilment

Implemented warehouse fulfilment association feature.

## Supported Functionality

Associate warehouses as fulfilment units for products per store.

### Constraints Implemented

#### Product Constraint

Each product can be fulfilled by a maximum of:

```text
2 warehouses per store
```

#### Store Constraint

Each store can be fulfilled by a maximum of:

```text
3 warehouses
```

#### Warehouse Constraint

Each warehouse can store maximally:

```text
5 product types
```

### Design Decision

Fulfilment association uses:

```text
warehouseBusinessUnitCode
```

instead of warehouse database ID.

This ensures fulfilment relationships survive warehouse replacement operations.

---

# Architecture Notes

The repository intentionally demonstrates multiple architectural approaches used in the assignment.

## Patterns Present

### Repository Pattern

Used in:

```text
Products module
```

### Active Record Pattern

Used in:

```text
Stores module
```

### Hexagonal / Layered Architecture

Used in:

```text
Warehouses module
```

---

# Testing

The project includes:

* Unit Tests
* Integration Tests
* API Tests
* Business Validation Tests

## Focus Areas

Priority was given to testing:

* warehouse business rules
* transaction boundaries
* fulfilment constraints
* replacement logic
* API behavior

Additional details are available in:

```text
Test-Coverage-Readme.md
```

---

# CI / Build Pipeline

A build pipeline was added to automatically validate the project.

## Pipeline Responsibilities

* Maven build validation
* automated test execution
* early detection of:

  * compilation issues
  * failing tests
  * Hibernate mapping issues
  * OpenAPI generation problems

This ensures the project remains continuously verifiable.

---

# Design & Architecture Questions

Answers to the assignment questions are available in:

```text
QUESTIONS
```

Topics covered include:

* database access patterns
* architectural tradeoffs
* testing strategy
* OpenAPI vs direct REST implementation
* maintainability considerations

---

# Case Studies

The repository also includes descriptions for the suggested case studies.

These discuss:

* possible architectural extensions
* scaling considerations
* future enhancements
* business and operational implications

---

# Technology Stack

* Java 17+
* Quarkus
* Hibernate ORM Panache
* RESTEasy Reactive
* PostgreSQL
* Maven
* JUnit 5
* Jacoco

---

# Running the Project

## Build

```bash
mvn clean install
```

## Run

```bash
mvn quarkus:dev
```

## Run Tests

```bash
mvn test
```

---

# Notes

This assignment was implemented with focus on:

* clean separation of concerns
* business rule validation
* transactional consistency
* maintainability
* pragmatic architecture decisions
* testability
* incremental extensibility
