package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import jdk.incubator.code.Reflect;
import jdk.incubator.code.CodeTransformer;
import jdk.incubator.code.Op;
import jdk.incubator.code.bytecode.BytecodeGenerator;
import jdk.incubator.code.dialect.core.CoreOp;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    @Reflect
    static double f(double i, double j) {
        i = i + j;

        double k = 4.0;
        k += i;
        return k;
    }

    static MethodHandle generate(CoreOp.FuncOp f) {
        System.out.println(f.toText());

        CoreOp.FuncOp lf = f.transform(CodeTransformer.LOWERING_TRANSFORMER);
        // Print the method code model.
        System.out.println(lf.toText());

        return BytecodeGenerator.generate(MethodHandles.lookup(), lf);
    }

    static CoreOp.FuncOp getFuncOp(String name) {
        Optional<Method> om = Stream.of(Main.class.getDeclaredMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst();

        Method m = om.get();
        return Op.ofMethod(m).get();
    }

    public static void main(String[] args) {
        CoreOp.FuncOp f = getFuncOp("f");
        MethodHandle mh = generate(f);
    }
}