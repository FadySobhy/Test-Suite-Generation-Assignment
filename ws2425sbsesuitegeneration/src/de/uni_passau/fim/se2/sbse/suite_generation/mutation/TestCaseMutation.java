package de.uni_passau.fim.se2.sbse.suite_generation.mutation;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.TestCaseChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ReflectionStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class TestCaseMutation implements Mutation<TestCaseChromosome> {

    private static final Random RANDOM = Randomness.random();
    private static Class<?> targetClass;

    public static void setTargetClass(Class<?> clazz) {
        targetClass = clazz;
    }

    @Override
    public TestCaseChromosome apply(TestCaseChromosome chromosome) {
        List<Statement> statements = chromosome.getStatements();
        if (statements.isEmpty()) {
            return chromosome;
        }

        TestCaseChromosome mutated = chromosome.copy();
        List<Statement> mutatedStatements = mutated.getStatements();

        int mutationType = RANDOM.nextInt(3);
        int index = RANDOM.nextInt(mutatedStatements.size());

        switch (mutationType) {
            case 0: // Replace a statement
                mutatedStatements.set(index, createRandomReflectionStatement());
                break;
            case 1: // Remove a statement
                if (mutatedStatements.size() > 1) {
                    mutatedStatements.remove(index);
                }
                break;
            case 2: // Insert a new statement
                mutatedStatements.add(index, createRandomReflectionStatement());
                break;
        }

        return mutated;
    }

    private Statement createRandomReflectionStatement() {
        if (targetClass == null) {
            throw new IllegalStateException("Target class is not set");
        }

        try {
            if (RANDOM.nextBoolean()) {
                Constructor<?>[] constructors = targetClass.getConstructors();
                if (constructors.length > 0) {
                    Constructor<?> constructor = constructors[RANDOM.nextInt(constructors.length)];
                    Object[] params = generateRandomParams(constructor.getParameterTypes());
                    return ReflectionStatement.createConstructorStatement(constructor, params);
                }
            }

            Method[] methods = targetClass.getDeclaredMethods();
            if (methods.length > 0) {
                Method method = methods[RANDOM.nextInt(methods.length)];
                Object instance = targetClass.getDeclaredConstructor().newInstance();
                Object[] params = generateRandomParams(method.getParameterTypes());
                return new ReflectionStatement(instance, method, params);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating random reflection statement", e);
        }

        throw new RuntimeException("No valid constructor or method found for test generation");
    }

    private Object[] generateRandomParams(Class<?>[] paramTypes) {
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == int.class || paramTypes[i] == Integer.class) {
                params[i] = RANDOM.nextInt(100);
            } else if (paramTypes[i] == double.class || paramTypes[i] == Double.class) {
                params[i] = RANDOM.nextDouble() * 100;
            } else if (paramTypes[i] == boolean.class || paramTypes[i] == Boolean.class) {
                params[i] = RANDOM.nextBoolean();
            } else if (paramTypes[i] == String.class) {
                params[i] = "test" + RANDOM.nextInt(100);
            } else {
                params[i] = null;
            }
        }
        return params;
    }
}