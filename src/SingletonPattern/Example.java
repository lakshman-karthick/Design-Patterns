package SingletonPattern;

// https://chatgpt.com/c/695c6795-ece4-8320-b3b0-b8276f30a1dc
// Reference link for learning Singleton patterns

// IMPORTANT NOTE (X)
// In Spring Boot, defining a class as a Bean makes it a Singleton by default.
// The Spring container manages the lifecycle, so no manual Singleton handling is required.


// ===============================
// NORMAL SINGLETON (NOT THREAD SAFE)
// ===============================
//
// class ChocolateFactory{
//     // Static variable holds the single instance
//     private static ChocolateFactory instance;
//
//     // Private constructor prevents object creation from outside
//     private ChocolateFactory(){}
//
//     // Global access point
//     public static synchronized ChocolateFactory getInstance(){
//         // Instance created only once
//         if (instance == null){
//             instance = new ChocolateFactory();
//         }
//         return instance;
//     }
// }

// ===============================
// SYNCHRONIZED METHOD SINGLETON
// ===============================
//
// class ChocolateFactory{
//     // Static variable for singleton instance
//     private static ChocolateFactory instance;
//
//     // Private constructor
//     private ChocolateFactory(){}
//
//     // Synchronized method ensures thread safety
//     // But every call is synchronized → performance overhead
//     public static synchronized ChocolateFactory getInstance(){
//         if (instance == null){
//             instance = new ChocolateFactory();
//         }
//         return instance;
//     }
// }

// ===============================
// DOUBLE-CHECKED LOCKING SINGLETON
// ===============================
//
// class ChocolateFactory{
//     // volatile ensures visibility across threads
//     private static volatile ChocolateFactory instance;
//
//     // Private constructor
//     private ChocolateFactory(){}
//
//     public static ChocolateFactory getInstance(){
//         // First check (no locking, fast path)
//         if (instance == null){
//             synchronized (ChocolateFactory.class){
//                 // Second check (with locking, safe path)
//                 if (instance == null){
//                     instance = new ChocolateFactory();
//                 }
//             }
//         }
//         return instance;
//     }
// }

// ===============================
// ENUM SINGLETON (BEST PRACTICE)
// ===============================

// Enum ensures:
// 1. Only one instance (INSTANCE)
// 2. Thread safety (handled by JVM)
// 3. Protection against reflection & serialization
enum ChocolateFactory{
    INSTANCE
}

// ===============================
// MAIN CLASS (SINGLE-THREAD TEST)
// ===============================
//
// public class Example
// {
//     public static void main(String[] args)
//     {
//         // Accessing enum singleton instance
//         ChocolateFactory chocolateFactory = ChocolateFactory.getInstance();
//         ChocolateFactory chocolateFactory2 = ChocolateFactory.getInstance();
//
//         // Both references point to the same instance
//         if(chocolateFactory == chocolateFactory2){
//             System.out.println("Chocolate Factory is equal to chocolate Factory");
//         }
//     }
// }

// ===============================
// MULTI-THREADING SIMULATION
// ===============================
public class Example {

    public static void main(String[] args) {

        // Runnable task executed by multiple threads
        Runnable task = () -> {

            // Accessing enum singleton instance
            // No getInstance() method needed for enum
            ChocolateFactory factory = ChocolateFactory.INSTANCE;

            // Printing thread name and hashcode
            // Hashcode proves the same instance is used across threads
            System.out.println(
                    Thread.currentThread().getName() +
                            " -> " + factory.hashCode()
            );
        };

        // Creating two separate threads
        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");

        // Starting threads simultaneously
        t1.start();
        t2.start();
    }
}
