package api.db_query;

/// Exception for query used in tests
/// Extends {@link AssertionError} to ensure Allure reports
/// treat the test as FAILED, not BROKEN.
public class ExpectedEntityNotFoundException extends AssertionError {
    public ExpectedEntityNotFoundException(String message) {
        super(message);
    }
}