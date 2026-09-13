package api.extensions.test_listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestReportLogger {

    private TestReportLogger() {
    }

    private static final Logger log = LoggerFactory.getLogger(TestReportLogger.class);

    public static void logResultsOfTestRun(TestExecutionSummary summary) {
        var tests = summary.tests();
        var containers = summary.containers();

        double testPlanStartSeconds = milliToSeconds(summary.timeStarted());
        double testPlanEndSeconds = milliToSeconds(summary.timeFinished());

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
                tests.found(),
                tests.skipped(),
                tests.started(),
                tests.aborted(),
                tests.succeeded(),
                tests.failed(),

                containers.found(),
                containers.skipped(),
                containers.started(),
                containers.aborted(),
                containers.succeeded(),
                containers.failed()
        );

        log.info(logMessage);
    }

    public static void logFailures(TestExecutionSummary summary) {
        var failures = summary.failures();

        if (failures.isEmpty()) {
            return;
        }

        log.error("Failures ({}):", failures.size());

        failures.forEach(failure -> {
            var identifier = failure.testIdentifier();
            var exception = failure.exception();

            log.error("----------------------------------------");
            log.error("Test name: {}", identifier.getDisplayName());

            identifier.getSource()
                    .ifPresent(source -> log.error("Source: {}", source));

            log.error("Exception: {}", exception, exception);
        });
    }

    private static double milliToSeconds(long nanoTime) {
        return nanoTime / 1_000.0;
    }
}