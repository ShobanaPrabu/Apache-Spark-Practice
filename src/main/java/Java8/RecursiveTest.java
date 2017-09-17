package Java8;

import java.util.function.IntToDoubleFunction;

/**
 * Created by vdokku on 6/24/2017.
 */
public class RecursiveTest {
    public static void main(String[] args) {

        System.out.println("FACTORIALWITHJAVA8 :> " + factorialWithJava8(5));
        System.out.println("FACTORIAWITHOUTJAVA8WITHOUTRECURSION :> " + factoriaWithOutJava8WithOutRecursion(5));
        System.out.println("FACTORIAWITHOUTJAVA8WITHRECURSION :> " + factoriaWithOutJava8WithRecursion(5));

    }

    public static int factoriaWithOutJava8WithRecursion(int number /*It's the number to calculate */) {

        if (number ==0) {
            return 1;
        } else {
            return number * (factoriaWithOutJava8WithRecursion(number - 1));
        }
    }

    public static int factoriaWithOutJava8WithOutRecursion(int number /*It's the number to calculate */) {
        int fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }

        return fact;
    }

    public static double factorialWithJava8(int n) {

        Recursive<IntToDoubleFunction> recursive = new Recursive<>();
        recursive.func = x -> (x == 0) ? 1 : x * recursive.func.applyAsDouble(x - 1);

        return recursive.func.applyAsDouble(n);
    }
}
