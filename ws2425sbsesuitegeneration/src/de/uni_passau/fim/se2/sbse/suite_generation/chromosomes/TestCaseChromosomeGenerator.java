package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random test case chromosomes.
 */
public class TestCaseChromosomeGenerator implements ChromosomeGenerator<TestCaseChromosome> {

    private final int maxStatements; // Max number of statements in a test case
    private final Mutation<TestCaseChromosome> mutation;
    private final Crossover<TestCaseChromosome> crossover;

    /**
     * Creates a generator for test case chromosomes.
     *
     * @param maxStatements Maximum number of statements per chromosome.
     * @param mutation The mutation operator.
     * @param crossover The crossover operator.
     */
    public TestCaseChromosomeGenerator(int maxStatements, Mutation<TestCaseChromosome> mutation, Crossover<TestCaseChromosome> crossover) {
        this.maxStatements = maxStatements;
        this.mutation = mutation;
        this.crossover = crossover;
    }

    /**
     * Generates a random chromosome.
     *
     * @return A new test case chromosome.
     */
    @Override
    public TestCaseChromosome get() {
        Random random = Randomness.random();
        int numStatements = random.nextInt(maxStatements) + 1; // Ensure at least 1 statement
        List<Statement> statements = new ArrayList<>();

        for (int i = 0; i < numStatements; i++) {
            statements.add(Statement.random(random)); // Assuming Statement has a factory method
        }

        TestCaseChromosome chromosome = new TestCaseChromosome(mutation, crossover);
        chromosome.getStatements().addAll(statements);
        return chromosome;
    }
}
