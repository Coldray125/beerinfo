package api.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class LoggingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestWatcher {

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        startTime.set(System.currentTimeMillis());
        String displayName = context.getDisplayName();
        Optional<Method> testMethod = context.getTestMethod();
        String methodName = testMethod.map(Method::getName).orElse("unknown");
        log.info("Starting test: {} (method: {})", displayName, methodName);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        long duration = System.currentTimeMillis() - startTime.get();
        String displayName = context.getDisplayName();
        log.info("Finished test: {} (Duration: {} ms)", displayName, duration);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("Test Disabled: {}, reason: {}", context.getDisplayName(), reason.orElse("No reason provided"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("Test Successful: {}", context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.info("Test Aborted: {}, cause: {}", context.getDisplayName(), cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.info("Test Failed: {}, cause: {}", context.getDisplayName(), cause.getMessage());
    }
}