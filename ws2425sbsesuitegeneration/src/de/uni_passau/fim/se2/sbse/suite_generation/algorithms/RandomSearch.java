package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.TestCaseChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implements the Random Search algorithm for test case generation.
 */
public class RandomSearch {

    private final FitnessFunction<TestCaseChromosome> fitnessFunction;
    private final StoppingCondition stoppingCondition;
    private final Random random;
    private final List<TestCaseChromosome> archive;

    /**
     * Constructs a Random Search algorithm.
     *
     * @param fitnessFunction   The fitness function to evaluate test cases.
     * @param stoppingCondition The stopping condition to define when to stop.
     */
    public RandomSearch(FitnessFunction<TestCaseChromosome> fitnessFunction, StoppingCondition stoppingCondition) {
        this.fitnessFunction = fitnessFunction;
        this.stoppingCondition = stoppingCondition;
        this.random = Randomness.random();
        this.archive = new ArrayList<>();
    }

    /**
     * Runs the Random Search algorithm until the stopping condition is met.
     *
     * @return The best test cases found during the search.
     */
    public List<TestCaseChromosome> run() {
        stoppingCondition.notifySearchStarted();

        while (stoppingCondition.searchCanContinue()) {
            // Generate a random test case
            TestCaseChromosome candidate = TestCaseChromosome.random();

            // Evaluate the fitness
            double fitness = fitnessFunction.evaluate(candidate);

            // Store the candidate if it provides new coverage
            if (!archive.contains(candidate)) {
                archive.add(candidate);
            }

            stoppingCondition.notifyFitnessEvaluation();
        }

        return archive;
    }
}
