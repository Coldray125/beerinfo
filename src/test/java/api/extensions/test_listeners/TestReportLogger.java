package api.extensions.test_listeners;

import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestReportLogger {

    private TestReportLogger() {
    }

    private static final Logger log = LoggerFactory.getLogger(TestReportLogger.class);

    public static void logResultsOfTestRun(SummaryGeneratingListener listener) {
        TestExecutionSummary summary = listener.getSummary();

        double testPlanStartSeconds = milliToSeconds(summary.getTimeStarted());
        double testPlanEndSeconds = milliToSeconds(summary.getTimeFinished());

        double totalTimeSeconds = testPlanEndSeconds - testPlanStartSeconds;
        double totalTimeMinutes = totalTimeSeconds / 60;

        String timeMessage = String.format("""
                        \n⏱️ Execution summary:
                        [Test plan execution: %3.3f seconds (%3.2f minutes)]
                        """,
                totalTimeSeconds,
                totalTimeMinutes
        );

        log.info(timeMessage);

        String logMessage = String.format("""
                        \n🧪 Tests:
                          - Found       : %d
                          - Skipped     : %d
                          - Started     : %d
                          - Aborted     : %d
                          - Successful  : %d
                          - Failed      : %d
                        
                          📦 Containers:
                          - Found       : %d
                          - Skipped     : %d
                          - Started     : %d
                          - Aborted     : %d
                          - Successful  : %d
                          - Failed      : %d
                        """,
                summary.getContainersFoundCount(),
                summary.getContainersSkippedCount(),
                summary.getContainersStartedCount(),
                summary.getContainersAbortedCount(),
                summary.getContainersSucceededCount(),
                summary.getContainersFailedCount(),
                summary.getTestsFoundCount(),
                summary.getTestsSkippedCount(),
                summary.getTestsStartedCount(),
                summary.getTestsAbortedCount(),
                summary.getTestsSucceededCount(),
                summary.getTestsFailedCount()
        );

        log.info(logMessage);
    }

    private static double milliToSeconds(long nanoTime) {
        return nanoTime / 1_000.0;
    }
}