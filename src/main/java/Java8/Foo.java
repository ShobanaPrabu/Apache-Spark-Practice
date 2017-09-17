package Java8;

/**
 * Created by vdokku on 6/24/2017.
 */
//@FunctionalInterface
public interface Foo {
    void doSomeThing();

    void doSomethingElse();
    // Default method should have body, that means it's defined & declared.

    default void doNothing() {

    }
}
