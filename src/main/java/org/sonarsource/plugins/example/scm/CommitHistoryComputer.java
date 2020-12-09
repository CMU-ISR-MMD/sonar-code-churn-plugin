package org.sonarsource.plugins.example.scm;


import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.example.measures.ExampleMetrics.FILENAME_SIZE;
import static org.sonarsource.plugins.example.scm.ScmMetrics.COMMIT_HISTORY;

public class CommitHistoryComputer implements MeasureComputer {
    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
                .setOutputMetrics(COMMIT_HISTORY.key())
                .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        // measure is already defined on files by {@link SetSizeOnFilesSensor}
        // in scanner stack
//        if (context.getComponent().getType() == Component.Type.PROJECT) {
//            int sum = 0;
//            int count = 0;
//            for (Measure child : context.getChildrenMeasures(FILENAME_SIZE.key())) {
//                sum += child.getIntValue();
//                count++;
//            }
//            int average = count == 0 ? 0 : sum / count;
//            context.addMeasure(FILENAME_SIZE.key(), average);
//
//            context.
//
//            context.addMeasure(COMMIT_HISTORY.key(), "");

    }

}
