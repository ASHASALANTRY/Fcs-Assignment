# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and
   manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
## Database Access Layer - Would I Refactor?

### What I Noticed

The codebase uses three different approaches:

**Products Module:** Repository pattern (`PanacheRepository<Product>`)  
**Stores Module:** Active Record pattern (`Store extends PanacheEntity`)  
**Warehouses Module:** Hexagonal architecture with separate domain/persistence layers

---

### Direct Answer: Yes, I Would Refactor

**Specifically: The Stores module**

### Why Stores Needs Refactoring

The Stores module has a **mismatch between its complexity and its architecture**:

**The Problem:**
- It has sophisticated business logic (Transaction management for legacy system integration)
- But uses the simplest persistence pattern (Active Record with static methods)
- This makes it difficult to test the transaction logic properly
- Static methods like `Store.listAll()` are hard to mock in unit tests

**The Impact:**
- Testing the transaction synchronization requires full database setup
- Can't easily unit test the business logic in isolation
- Complex transaction behavior is hidden inside an entity class
- Risky to modify or extend the legacy system integration

**The Fix:**
Move to a repository pattern (like Products) or hexagonal approach (like Warehouses) to separate the complex
transaction logic from the persistence layer, making it testable and maintainable.

---

### What I Would NOT Refactor

**Products Module: Leave as-is**

**Why:**
- Simple CRUD operations with no complex business logic
- Current repository pattern works fine
- No testing or maintenance problems
- Refactoring would add unnecessary complexity

---

### About Warehouses

The Warehouses approach makes sense for its complexity level (multiple business validations, location constraints,
capacity checks). But applying this level of separation to something as simple as Products would be overkill.

-------
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which
   we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would
   be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
## Pros and Cons of OpenAPI vs Direct REST Implementation

### Using OpenAPI Specification (e.g. Warehouse API)

**Advantages:**
- Clearly defines the API contract upfront
- Helps keep APIs consistent across implementations
- Makes API documentation easier and more reliable
- Useful when multiple teams or external clients depend on the same API

**Disadvantages:**
- Adds extra overhead to maintain the OpenAPI specification
- Code needs to be regenerated when the contract changes
- Can slow down development for small or frequently changing APIs

---

### Coding Endpoints Directly (e.g. Product and Store APIs)

**Advantages:**
- Faster to implement and simpler to maintain initially
- More flexible during early development stages
- Easier to make quick changes without updating a separate specification

**Disadvantages:**
- API contract is less explicit
- Documentation can fall out of sync with the implementation
- Harder to maintain consistency as the application grows

---

### My Choice

- Use **direct REST implementations** for:
    - Simpler APIs
    - Internal services
    - APIs that are still evolving quickly

- Use **OpenAPI-based code generation** for:
    - More complex APIs
    - Stable APIs
    - APIs exposed to external consumers or multiple teams

----


3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?



**Answer:**
## Testing Strategy and Prioritization

Given the time and resource constraints, I would focus testing on the areas where defects would have the highest business impact: warehouse lifecycle rules, transaction boundaries, and API contracts.

### 1. Prioritize Business Rule Tests

The highest priority would be the Warehouse use cases because they contain most of the business rules:

- business unit code must be unique for active warehouses
- warehouse location must exist
- warehouse capacity must not exceed location capacity
- warehouse stock must fit within capacity
- replace operation must preserve the business unit code
- replacement warehouse must match the previous stock
- archive operation should prevent further use of the archived warehouse

These should be tested mostly at use-case/service level, because that gives fast feedback without always going through HTTP.

### 2. Add API/Integration Tests for Critical Flows

I would add a smaller number of end-to-end API tests for the most important user journeys:

- create warehouse successfully
- reject invalid warehouse creation
- replace warehouse successfully
- archive warehouse successfully
- create/update store and verify legacy synchronization behavior

For the Store module, I would specifically test that the legacy gateway is called only after the database transaction is committed. That is a critical integration rule.

### 3. Keep Simple CRUD Tests Lightweight

For Product and Location, I would keep tests lighter because the business logic is relatively simple. I would cover:

- happy path creation/retrieval
- not-found cases
- basic validation errors

I would avoid over-testing simple CRUD code unless bugs appear there later.

### 4. Test the Bonus Fulfilment Rules

For the fulfilment feature, I would test the three constraints directly:

- a product can be fulfilled by maximum 2 warehouses per store
- a store can be fulfilled by maximum 3 warehouses
- a warehouse can store maximum 5 product types

I would also test duplicate assignment handling and assignment to non-existing or archived warehouses.

### 5. Use Different Test Types Carefully

My preferred testing mix would be:

- **Unit tests** for pure business decisions and validations
- **Repository/integration tests** for persistence queries and constraints
- **API tests** for request/response behavior and OpenAPI contract compliance
- **A few transaction-focused tests** for store-to-legacy synchronization

I would not aim for 100% line coverage. Instead, I would aim for meaningful coverage of business rules, edge cases, and failure paths.

### 6. Keeping Coverage Effective Over Time

To keep tests useful over time, I would:

- add tests together with every new business rule
- keep test data builders/fixtures simple and reusable
- run tests automatically in CI for every pull request
- track coverage trends, but not use coverage percentage as the only quality metric
- prefer readable tests that document business behavior
- include regression tests whenever a production bug is found

The goal would be to have a test suite that is fast enough to run often, focused enough to catch real business regressions, and clear enough to act as documentation for future maintainers.