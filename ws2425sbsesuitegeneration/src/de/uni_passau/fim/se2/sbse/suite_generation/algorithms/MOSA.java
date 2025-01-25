package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.TestCaseChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.util.*;
import java.util.stream.Collectors;

public class MOSA implements GeneticAlgorithm<TestCaseChromosome> {
    private final Set<IBranch> branchesToCover;
    private final Random random;
    private final IBranchTracer branchTracer;
    private final int populationSize;
    private final StoppingCondition stoppingCondition;
    private final List<TestCaseChromosome> population;
    private final Map<IBranch, TestCaseChromosome> archive;

    public MOSA(Set<IBranch> branchesToCover, Random random, IBranchTracer branchTracer,
                int populationSize, StoppingCondition stoppingCondition) {
        this.branchesToCover = branchesToCover;
        this.random = random;
        this.branchTracer = branchTracer;
        this.populationSize = populationSize;
        this.stoppingCondition = stoppingCondition;
        this.population = new ArrayList<>();
        this.archive = new HashMap<>();
    }

    @Override
    public List<TestCaseChromosome> evolve() {
        initializePopulation();

        while (!stoppingCondition.searchMustStop()) {
            List<TestCaseChromosome> offspring = generateOffspring();
            evaluateAndSelect(offspring);
        }
        return new ArrayList<>(archive.values());
    }

    private void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            TestCaseChromosome individual = new TestCaseChromosome(random);
            population.add(individual);
        }
    }

    private List<TestCaseChromosome> generateOffspring() {
        List<TestCaseChromosome> offspring = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            TestCaseChromosome parent = population.get(random.nextInt(population.size()));
            TestCaseChromosome mutant = parent.mutate();
            offspring.add(mutant);
        }
        return offspring;
    }

    private void evaluateAndSelect(List<TestCaseChromosome> offspring) {
        for (TestCaseChromosome individual : offspring) {
            Map<Integer, Double> branchDistances = individual.call();
            updateArchive(individual, branchDistances);
        }
        population.clear();
        population.addAll(selectNextGeneration());
    }

    private void updateArchive(TestCaseChromosome individual, Map<Integer, Double> branchDistances) {
        for (IBranch branch : branchesToCover) {
            if (branchDistances.containsKey(branch.getId())) {
                double distance = branchDistances.get(branch.getId());
                if (!archive.containsKey(branch) || distance < archive.get(branch).call().get(branch.getId())) {
                    archive.put(branch, individual);
                }
            }
        }
    }

    private List<TestCaseChromosome> selectNextGeneration() {
        return archive.values().stream().limit(populationSize).collect(Collectors.toList());
    }

    @Override
    public List<TestCaseChromosome> findSolution() {
        return null;
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return null;
    }
}
