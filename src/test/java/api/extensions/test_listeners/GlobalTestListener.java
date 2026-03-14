package api.extensions.test_listeners;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

@Slf4j
public class GlobalTestListener implements TestExecutionListener {
    private final SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();

    @Override
    public void testPlanExecutionStarted(@NonNull TestPlan testPlan) {
        summaryGeneratingListener.testPlanExecutionStarted(testPlan);
        log.info("Test plan execution started. Preparing for test run.");
    }

    @Override
    public void testPlanExecutionFinished(@NonNull TestPlan testPlan) {
        summaryGeneratingListener.testPlanExecutionFinished(testPlan);
        TestReportLogger.logResultsOfTestRun(summaryGeneratingListener);
    }

    @Override
    public void reportingEntryPublished(@NonNull TestIdentifier testIdentifier, @NonNull ReportEntry entry) {
        summaryGeneratingListener.reportingEntryPublished(testIdentifier, entry);
    }

    @Override
    public void dynamicTestRegistered(@NonNull TestIdentifier testIdentifier) {
        summaryGeneratingListener.dynamicTestRegistered(testIdentifier);
    }

    @Override
    public void executionSkipped(@NonNull TestIdentifier testIdentifier, @NonNull String reason) {
        summaryGeneratingListener.executionSkipped(testIdentifier, reason);
    }

    @Override
    public void executionStarted(@NonNull TestIdentifier testIdentifier) {
        summaryGeneratingListener.executionStarted(testIdentifier);
    }

    @Override
    public void executionFinished(@NonNull TestIdentifier testIdentifier, @NonNull TestExecutionResult testExecutionResult) {
        summaryGeneratingListener.executionFinished(testIdentifier, testExecutionResult);
    }
}