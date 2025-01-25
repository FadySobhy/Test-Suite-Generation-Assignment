package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a statement in a test case, which can be executed using Java Reflection.
 */
public class ReflectionStatement implements Statement {

    private final Object instance;
    private final Method method;
    private final Object[] parameters;

    /**
     * Constructs a statement representing a method call.
     *
     * @param instance  the object on which the method is invoked
     * @param method    the method to invoke
     * @param parameters the parameters to pass to the method
     */
    public ReflectionStatement(Object instance, Method method, Object... parameters) {
        this.instance = instance;
        this.method = method;
        this.parameters = parameters;
    }

    /**
     * Executes the statement using Java Reflection.
     */
    @Override
    public void run() {
        try {
            method.invoke(instance, parameters);
        } catch (Exception e) {
            throw new RuntimeException("Error executing statement: " + this, e);
        }
    }

    /**
     * Returns the Java code representation of this statement.
     *
     * @return a string representing the statement as Java code.
     */
    @Override
    public String toString() {
        return instance.getClass().getSimpleName() + "." + method.getName() + "(" + Arrays.toString(parameters) + ");";
    }

    /**
     * Creates a statement representing an object constructor.
     *
     * @param constructor the constructor to invoke
     * @param parameters  the parameters for the constructor
     * @return a ReflectionStatement representing an object instantiation
     */
    public static ReflectionStatement createConstructorStatement(Constructor<?> constructor, Object... parameters) {
        try {
            Object instance = constructor.newInstance(parameters);
            return new ReflectionStatement(instance, null, parameters);
        } catch (Exception e) {
            throw new RuntimeException("Error creating instance: " + constructor, e);
        }
    }
}
