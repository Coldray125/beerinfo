package api.extensions.test_listeners;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/// Simplified version of JUnit's[SummaryGeneratingListener]
///
/// Collects test execution statistics and failures during a test run
/// and creates a [TestExecutionSummary] when execution finishes.
public class GlobalTestListener implements TestExecutionListener {
    private @Nullable TestPlan testPlan;

    private final AtomicLong containersFound = new AtomicLong();
    private final AtomicLong containersStarted = new AtomicLong();
    private final AtomicLong containersSkipped = new AtomicLong();
    private final AtomicLong containersAborted = new AtomicLong();
    private final AtomicLong containersSucceeded = new AtomicLong();
    private final AtomicLong containersFailed = new AtomicLong();

    private final AtomicLong testsFound = new AtomicLong();
    private final AtomicLong testsStarted = new AtomicLong();
    private final AtomicLong testsSkipped = new AtomicLong();
    private final AtomicLong testsAborted = new AtomicLong();
    private final AtomicLong testsSucceeded = new AtomicLong();
    private final AtomicLong testsFailed = new AtomicLong();

    private final List<DefaultFailure> failures = Collections.synchronizedList(new ArrayList<>());

    private final long timeStarted;
    private long timeFinished;

    public GlobalTestListener() {
        this.timeStarted = System.currentTimeMillis();
    }

    public void testPlanExecutionStarted(@NonNull TestPlan testPlan) {
        this.testPlan = testPlan;

        containersFound.set(testPlan.countTestIdentifiers(TestIdentifier::isContainer));
        testsFound.set(testPlan.countTestIdentifiers(TestIdentifier::isTest));
    }

    public void testPlanExecutionFinished(@NonNull TestPlan testPlan) {
        this.timeFinished = System.currentTimeMillis();
        TestReportLogger.logResultsOfTestRun(getTestRunSummary());
        TestReportLogger.logFailures(getTestRunSummary());
    }

    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        if (testIdentifier.isContainer()) {
            this.containersFound.incrementAndGet();
        }

        if (testIdentifier.isTest()) {
            this.testsFound.incrementAndGet();
        }
    }

    public void executionSkipped(@NonNull TestIdentifier testIdentifier, @NonNull String reason) {
        var testPlan = Objects.requireNonNull(this.testPlan);
        long skippedContainers = Stream.concat(Stream.of(testIdentifier),
                testPlan.getDescendants(testIdentifier).stream()).filter(TestIdentifier::isContainer).count();
        long skippedTests = Stream.concat(Stream.of(testIdentifier),
                testPlan.getDescendants(testIdentifier).stream()).filter(TestIdentifier::isTest).count();
        this.containersSkipped.addAndGet(skippedContainers);
        this.testsSkipped.addAndGet(skippedTests);
    }

    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isContainer()) {
            this.containersStarted.incrementAndGet();
        }

        if (testIdentifier.isTest()) {
            this.testsStarted.incrementAndGet();
        }
    }

    record DefaultFailure(TestIdentifier testIdentifier, Throwable exception) {
    }

    public void executionFinished(@NonNull TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        switch (testExecutionResult.getStatus()) {
            case SUCCESSFUL:
                if (testIdentifier.isContainer()) {
                    this.containersSucceeded.incrementAndGet();
                }

                if (testIdentifier.isTest()) {
                    this.testsSucceeded.incrementAndGet();
                }
                break;
            case ABORTED:
                if (testIdentifier.isContainer()) {
                    this.containersAborted.incrementAndGet();
                }

                if (testIdentifier.isTest()) {
                    this.testsAborted.incrementAndGet();
                }
                break;
            case FAILED:
                if (testIdentifier.isContainer()) {
                    this.containersFailed.incrementAndGet();
                }

                if (testIdentifier.isTest()) {
                    this.testsFailed.incrementAndGet();
                }

                testExecutionResult.getThrowable().ifPresent((throwable) ->
                        this.failures.add(new DefaultFailure(testIdentifier, throwable)));
                break;
            default:
                throw new PreconditionViolationException("Unsupported execution status:" + testExecutionResult.getStatus());
        }
    }

    private TestExecutionSummary getTestRunSummary() {
        return new TestExecutionSummary(
                new TestExecutionSummary.ExecutionStats(
                        testsFound.get(),
                        testsSkipped.get(),
                        testsStarted.get(),
                        testsAborted.get(),
                        testsSucceeded.get(),
                        testsFailed.get()
                ),
                new TestExecutionSummary.ExecutionStats(
                        containersFound.get(),
                        containersSkipped.get(),
                        containersStarted.get(),
                        containersAborted.get(),
                        containersSucceeded.get(),
                        containersFailed.get()
                ),
                failures.stream()
                        .map(failure -> new TestExecutionSummary.Failure(
                                failure.testIdentifier(),
                                failure.exception()
                        ))
                        .toList(),
                timeStarted,
                timeFinished
        );
    }
}