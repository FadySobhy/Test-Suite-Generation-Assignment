package de.uni_passau.fim.se2.sbse.suite_generation.crossover;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.TestCaseChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.util.List;
import java.util.Random;

public class TestCaseCrossover implements Crossover<TestCaseChromosome> {

    private static final Random RANDOM = Randomness.random();

    @Override
    public Pair<TestCaseChromosome> apply(TestCaseChromosome parent1, TestCaseChromosome parent2) {
        List<Statement> p1Statements = parent1.getStatements();
        List<Statement> p2Statements = parent2.getStatements();

        if (p1Statements.isEmpty() || p2Statements.isEmpty()) {
            return Pair.of(parent1.copy(), parent2.copy()); // No crossover possible
        }

        int crossoverPoint1 = RANDOM.nextInt(p1Statements.size());
        int crossoverPoint2 = RANDOM.nextInt(p2Statements.size());

        TestCaseChromosome offspring1 = parent1.copy();
        TestCaseChromosome offspring2 = parent2.copy();

        List<Statement> o1Statements = offspring1.getStatements();
        List<Statement> o2Statements = offspring2.getStatements();

        // Swap segments
        List<Statement> temp = o1Statements.subList(crossoverPoint1, o1Statements.size());
        o1Statements.subList(crossoverPoint1, o1Statements.size()).clear();
        o1Statements.addAll(o2Statements.subList(crossoverPoint2, o2Statements.size()));

        o2Statements.subList(crossoverPoint2, o2Statements.size()).clear();
        o2Statements.addAll(temp);

        return Pair.of(offspring1, offspring2);
    }
}
