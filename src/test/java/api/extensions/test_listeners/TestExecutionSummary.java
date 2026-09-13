package api.extensions.test_listeners;

import org.junit.platform.launcher.TestIdentifier;

import java.util.List;

public record TestExecutionSummary(
        ExecutionStats tests,
        ExecutionStats containers,
        List<Failure> failures,
        long timeStarted,
        long timeFinished
) {

    public record ExecutionStats(
            long found,
            long skipped,
            long started,
            long aborted,
            long succeeded,
            long failed
    ) {
    }

    public record Failure(
            TestIdentifier testIdentifier,
            Throwable exception
    ) {
    }
}