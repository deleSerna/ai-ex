package org.example;

import hat.Accelerator;
import hat.ComputeContext;
import hat.NDRange;
import hat.KernelContext;
import hat.backend.Backend;
import hat.buffer.S32Array;
import hat.ifacemapper.MappableIface.RO;
import hat.ifacemapper.MappableIface.WO;
import jdk.incubator.code.Reflect;

import java.lang.invoke.MethodHandles;
import java.util.Random;

// The sample is ataken from https://github.com/openjdk/babylon/blob/code-reflection/hat/examples/squares/src/main/java/squares/
// To build and run
// Build Openjdk as mentioned [here](https://github.com/openjdk/babylon/blob/code-reflection/hat/docs/hat-01-02-building-babylon.md).
// Build HAT as mentioned [here](https://github.com/openjdk/babylon/blob/code-reflection/hat/docs/hat-01-03-building-hat.md).
// Compile the MainWithHAT class
//   - $JDK_26/javac -cp $HAT_JAR/hat-core-1.0.jar --source 26  -sourcepath src/main/java  src/main/java/org/example/MainWithHAT.java -d build
// Run the MainWithHAT. Here, I am using `opencl` as the backend. Therefore, passing  hat-backend-ffi-opencl-1.0.jar and it's native part in library path.
// If you want to experiment with another backend, change the backend jar accodingly.
//  - $JDK_26/java --add-modules=jdk.incubator.code --enable-preview --enable-native-access=ALL-UNNAMED \\
//  --add-exports=java.base/jdk.internal=ALL-UNNAMED  \\
//  -cp $HAT_JAR/hat-core-1.0.jar:$HAT_JAR/hat-backend-ffi-opencl-1.0.jar:$HAT_JAR/hat-backend-ffi-shared-1.0.jar:build  \\
//  -Djava.library.path=$HAT_JAR \\
//  org.example.MainWithHAT
public class MainWithHAT {

    @Reflect
    public static int squareit(int v) {
        return  v * v;
    }

    // Kernel function that need to execute on GPU.
    // Need to annotate the paramters accordingly so that correct access set on the data.
    @Reflect
    public static void squareKernel(@WO KernelContext kc, @RO S32Array array) {
        if (kc.gix < kc.gsx){
            int value = array.array(kc.gix);
            array.array(kc.gix, squareit(value));
        }
    }

    // Compute function that will invoke the kernel function.
   @Reflect
    public static void square(ComputeContext cc, S32Array array) {
        cc.dispatchKernel(NDRange.of1D(array.length()),
                kc -> squareKernel(kc, array)
        );
    }

    public static void main(String[] args) {
               final int size = 64;
               // Backend.FIRST will select the 1st backend that is found in the jar that is passed at runtime.
               // Otherwise, just pass the JavaMultiThreadedBackend or JavaSequentialBackend.
               var accelerator = new Accelerator(MethodHandles.lookup(), Backend.FIRST/*new JavaMultiThreadedBackend()*/);
               var inputArray = S32Array.create(accelerator, size);

               // Initialize array
               for (int i = 0; i < inputArray.length(); i++) {
                   inputArray.array(i, i);
               }

               // Execute on GPU.
               accelerator.compute(cc -> square(cc, inputArray));

               for (int i = 0; i < inputArray.length(); i++) {
                   System.out.println(inputArray.array(i));
               }
    }
}