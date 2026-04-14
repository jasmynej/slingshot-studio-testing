# Test Reconciliation Report

## Executive Summary

This report reconciles test execution results between the **Legacy Application (Java/Maven)** and the **Modern Application (Python/unittest)** to identify functional discrepancies.

**Overall Result:** ⚠️ **DISCREPANCIES DETECTED** - The modern application has 3 failing tests that pass in the legacy application, indicating functional differences between the two systems.

---

## Test Execution Overview

### Legacy Application (Java - Maven)

- **Test Framework:** JUnit (via Maven Surefire)
- **Total Tests Run:** 33
- **Passed:** 33 ✅
- **Failed:** 0
- **Errors:** 0
- **Skipped:** 0
- **Status:** SUCCESS
- **Execution Time:** 2.396s

#### Test Suites:
- `com.showroom.CarTest`: 10 tests, 0 failures
- `com.showroom.ShowroomTest`: 23 tests, 0 failures

---

### Modern Application (Python - unittest)

- **Test Framework:** Python unittest
- **Total Tests Run:** 32
- **Passed:** 29 ✅
- **Failed:** 3 ❌
- **Errors:** 0
- **Skipped:** 0
- **Status:** FAILED
- **Execution Time:** 0.003s

#### Test Suites:
- `test_car.TestCar`: 10 tests, 0 failures
- `test_showroom.TestShowroom`: 22 tests, 3 failures

---

## Test Count Discrepancy

⚠️ **Note:** The legacy application reports **33 tests** while the modern application reports **32 tests**. This indicates a potential missing test in the Python implementation.

- **Legacy (Java):** 33 tests total
  - CarTest: 10 tests
  - ShowroomTest: 23 tests
  
- **Modern (Python):** 32 tests total
  - TestCar: 10 tests
  - TestShowroom: 22 tests (1 test missing compared to Java)

**Impact:** One test case from the legacy application may not be implemented in the modern application, representing a gap in test coverage.

---

## Failing Tests in Modern Application

The following tests **pass in the legacy application** but **fail in the modern application**, indicating functional discrepancies:

### 1. `test_filter_by_price_inclusive_bounds`

**Suite:** `test_showroom.TestShowroom`

**Error Type:** AssertionError

**Error Message:**
```
AssertionError: 1 != 0
```

**Stack Trace:**
```
File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 120, in test_filter_by_price_inclusive_bounds
    self.assertEqual(1, len(results))
```

**Expected Behavior:** Filter should return 1 car when filtering by price with inclusive bounds.

**Actual Behavior:** Filter returns 0 cars (empty list).

**Root Cause Analysis:** The Python implementation's `filter_by_price` method likely does not handle boundary conditions inclusively (i.e., it may use `<` and `>` instead of `<=` and `>=`), or the boundary logic is incorrect.

---

### 2. `test_search_is_case_insensitive`

**Suite:** `test_showroom.TestShowroom`

**Error Type:** AssertionError

**Error Message:**
```
AssertionError: 2 != 0
```

**Stack Trace:**
```
File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 102, in test_search_is_case_insensitive
    self.assertEqual(2, len(results))
```

**Expected Behavior:** Search should return 2 cars when searching with different case variations (e.g., "TOYOTA" should match "Toyota").

**Actual Behavior:** Search returns 0 cars (empty list).

**Root Cause Analysis:** The Python implementation's search functionality is **case-sensitive**, while the Java implementation performs **case-insensitive** searches. The Python code likely does not convert search terms and car attributes to a common case (e.g., `.lower()`) before comparison.

---

### 3. `test_sell_car_returns_false_for_already_sold_car`

**Suite:** `test_showroom.TestShowroom`

**Error Type:** AssertionError

**Error Message:**
```
AssertionError: True is not false
```

**Stack Trace:**
```
File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 68, in test_sell_car_returns_false_for_already_sold_car
    self.assertFalse(result)
```

**Expected Behavior:** Attempting to sell an already-sold car should return `False`.

**Actual Behavior:** The method returns `True`.

**Root Cause Analysis:** The Python implementation's `sell_car` method does not check whether a car has already been sold (i.e., is unavailable) before attempting to sell it again. The Java version includes this validation, but the Python version is missing this business logic.

---

## Functional Discrepancies Summary

The following functional differences exist between the legacy and modern applications:

| **Feature** | **Legacy (Java)** | **Modern (Python)** | **Impact** |
|-------------|-------------------|---------------------|------------|
| **Price filter inclusive bounds** | Correctly includes boundary values | Does not include boundary values | High - Users may miss cars at exact price points |
| **Case-insensitive search** | Implemented | Not implemented | High - Users cannot search flexibly (e.g., "HONDA" vs "honda") |
| **Double-sell prevention** | Validates and prevents selling already-sold cars | Allows selling already-sold cars | Critical - Business logic violation, data integrity issue |
| **Test coverage** | 33 tests | 32 tests (1 missing) | Medium - Potential gap in validation |

---

## Recommendations

### Critical Priority

1. **Fix double-sell prevention logic** in the Python `sell_car` method:
   - Add a check to verify the car's availability status before marking it as sold.
   - Return `False` if the car is already unavailable.

### High Priority

2. **Implement case-insensitive search** in the Python `search` method:
   - Convert search parameters and car attributes to lowercase before comparison.
   - Ensure behavior matches the Java implementation.

3. **Fix price filter boundary handling** in the Python `filter_by_price` method:
   - Use inclusive comparisons (`<=` and `>=`) for min and max price bounds.
   - Verify edge cases where price exactly matches the boundary.

### Medium Priority

4. **Investigate missing test case:**
   - Identify which test from the Java ShowroomTest suite (23 tests) is missing from the Python TestShowroom suite (22 tests).
   - Implement the missing test to ensure equivalent coverage.

---

## Test Alignment Analysis

### Tests Passing in Both Applications

The following test categories pass in both applications, indicating functional equivalence:

- **Car construction and validation** (10 tests)
  - Constructor field assignment
  - Validation of blank make/model
  - Validation of invalid year and negative price
  - Availability state management
  - Price updates and validation
  - String representation

- **Showroom basic operations** (19 tests)
  - Constructor and name validation
  - Adding cars and inventory management
  - Finding cars by ID
  - Removing cars
  - Getting all cars and available cars
  - Selling cars (basic case)
  - Price range filtering (non-boundary cases)
  - Search by make and model (case-matching scenarios)

### Tests Failing Only in Modern Application

- `test_filter_by_price_inclusive_bounds` (boundary condition handling)
- `test_search_is_case_insensitive` (case-insensitive string matching)
- `test_sell_car_returns_false_for_already_sold_car` (business rule validation)

---

## Conclusion

The modern Python application **does not achieve functional equivalence** with the legacy Java application. Three critical functional discrepancies have been identified:

1. Missing case-insensitive search capability
2. Incorrect price filter boundary handling
3. Missing validation to prevent double-selling of cars

Additionally, there is a test coverage gap (32 vs 33 tests) that should be investigated.

**Recommendation:** The modern application should **not be promoted to production** until all identified discrepancies are resolved and all tests pass.

---

## Appendix: Raw Test Output

### Legacy Application Output

```
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< com.showroom:car-showroom >----------------------
[INFO] Building car-showroom 1.0.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ car-showroom ---
[INFO] skip non existing resourceDirectory /app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-java/src/main/resources
[INFO] 
[INFO] --- compiler:3.15.0:compile (default-compile) @ car-showroom ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 3 source files with javac [debug target 17] to target/classes
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ car-showroom ---
[INFO] skip non existing resourceDirectory /app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-java/src/test/resources
[INFO] 
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ car-showroom ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 2 source files with javac [debug target 17] to target/test-classes
[INFO] 
[INFO] --- surefire:3.2.2:test (default-test) @ car-showroom ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.showroom.CarTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.075 s -- in com.showroom.CarTest
[INFO] Running com.showroom.ShowroomTest
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.032 s -- in com.showroom.ShowroomTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 33, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.396 s
[INFO] Finished at: 2026-04-14T22:09:53Z
[INFO] ------------------------------------------------------------------------
```

### Modern Application Output

```
test_constructor_new_car_is_available_by_default (test_car.TestCar.test_constructor_new_car_is_available_by_default) ... ok
test_constructor_sets_fields_correctly (test_car.TestCar.test_constructor_sets_fields_correctly) ... ok
test_constructor_throws_on_blank_make (test_car.TestCar.test_constructor_throws_on_blank_make) ... ok
test_constructor_throws_on_blank_model (test_car.TestCar.test_constructor_throws_on_blank_model) ... ok
test_constructor_throws_on_invalid_year (test_car.TestCar.test_constructor_throws_on_invalid_year) ... ok
test_constructor_throws_on_negative_price (test_car.TestCar.test_constructor_throws_on_negative_price) ... ok
test_set_available_changes_availability (test_car.TestCar.test_set_available_changes_availability) ... ok
test_set_price_throws_on_negative (test_car.TestCar.test_set_price_throws_on_negative) ... ok
test_set_price_updates_price (test_car.TestCar.test_set_price_updates_price) ... ok
test_str_contains_key_fields (test_car.TestCar.test_str_contains_key_fields) ... ok
test_add_car_increases_inventory_size (test_showroom.TestShowroom.test_add_car_increases_inventory_size) ... ok
test_add_car_returns_car_with_unique_id (test_showroom.TestShowroom.test_add_car_returns_car_with_unique_id) ... ok
test_available_count_decreases_after_sale (test_showroom.TestShowroom.test_available_count_decreases_after_sale) ... ok
test_constructor_sets_name (test_showroom.TestShowroom.test_constructor_sets_name) ... ok
test_constructor_throws_on_blank_name (test_showroom.TestShowroom.test_constructor_throws_on_blank_name) ... ok
test_filter_by_price_inclusive_bounds (test_showroom.TestShowroom.test_filter_by_price_inclusive_bounds) ... FAIL
test_filter_by_price_returns_cars_in_range (test_showroom.TestShowroom.test_filter_by_price_returns_cars_in_range) ... ok
test_filter_by_price_throws_when_min_exceeds_max (test_showroom.TestShowroom.test_filter_by_price_throws_when_min_exceeds_max) ... ok
test_find_by_id_returns_car_when_present (test_showroom.TestShowroom.test_find_by_id_returns_car_when_present) ... ok
test_find_by_id_returns_none_for_missing_id (test_showroom.TestShowroom.test_find_by_id_returns_none_for_missing_id) ... ok
test_get_all_cars_returns_all_cars (test_showroom.TestShowroom.test_get_all_cars_returns_all_cars) ... ok
test_get_available_cars_excludes_sold_cars (test_showroom.TestShowroom.test_get_available_cars_excludes_sold_cars) ... ok
test_remove_car_returns_false_for_nonexistent_id (test_showroom.TestShowroom.test_remove_car_returns_false_for_nonexistent_id) ... ok
test_remove_car_returns_true_and_reduces_size (test_showroom.TestShowroom.test_remove_car_returns_true_and_reduces_size) ... ok
test_search_by_make_and_model_returns_exact_match (test_showroom.TestShowroom.test_search_by_make_and_model_returns_exact_match) ... ok
test_search_by_make_returns_matching_cars (test_showroom.TestShowroom.test_search_by_make_returns_matching_cars) ... ok
test_search_is_case_insensitive (test_showroom.TestShowroom.test_search_is_case_insensitive) ... FAIL
test_search_no_match_returns_empty_list (test_showroom.TestShowroom.test_search_no_match_returns_empty_list) ... ok
test_search_with_none_filters_returns_all (test_showroom.TestShowroom.test_search_with_none_filters_returns_all) ... ok
test_sell_car_marks_car_as_unavailable (test_showroom.TestShowroom.test_sell_car_marks_car_as_unavailable) ... ok
test_sell_car_returns_false_for_already_sold_car (test_showroom.TestShowroom.test_sell_car_returns_false_for_already_sold_car) ... FAIL
test_sell_car_returns_false_for_nonexistent_car (test_showroom.TestShowroom.test_sell_car_returns_false_for_nonexistent_car) ... ok

======================================================================
FAIL: test_filter_by_price_inclusive_bounds (test_showroom.TestShowroom.test_filter_by_price_inclusive_bounds)
----------------------------------------------------------------------
Traceback (most recent call last):
  File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 120, in test_filter_by_price_inclusive_bounds
    self.assertEqual(1, len(results))
AssertionError: 1 != 0

======================================================================
FAIL: test_search_is_case_insensitive (test_showroom.TestShowroom.test_search_is_case_insensitive)
----------------------------------------------------------------------
Traceback (most recent call last):
  File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 102, in test_search_is_case_insensitive
    self.assertEqual(2, len(results))
AssertionError: 2 != 0

======================================================================
FAIL: test_sell_car_returns_false_for_already_sold_car (test_showroom.TestShowroom.test_sell_car_returns_false_for_already_sold_car)
----------------------------------------------------------------------
Traceback (most recent call last):
  File "/app/temp/pea3c000d-7e50-4632-8182-c4da21724a2f/9e4fcac0-a9f5-48a7-b58a-77a93a05f886/5f770576-95b8-4538-bee5-9a2bdb30366b/src/car-showroom-python/test_showroom.py", line 68, in test_sell_car_returns_false_for_already_sold_car
    self.assertFalse(result)
AssertionError: True is not false

----------------------------------------------------------------------
Ran 32 tests in 0.003s

FAILED (failures=3)
```

---

```json
{
  "passed": false,
  "failed_tests": [
    "test_filter_by_price_inclusive_bounds",
    "test_search_is_case_insensitive",
    "test_sell_car_returns_false_for_already_sold_car"
  ]
}
```