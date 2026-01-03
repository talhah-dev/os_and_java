public class Main {
    public static void main(String[] args) {
        // 1) Integer number (no decimal)
        int age = 25;  // int = 32-bit integer

        // 2) Decimal number
        double price = 19.99;  // double = 64-bit floating-point number
        final double PI = 3.14159;   // constant, cannot change

        // 3) True/false
        boolean isLoggedIn = true;  // boolean = true or false

        char grade = 'A';  // char = single character

        String name = "Talha";  // String = sequence of characters

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Price: " + price);
        System.out.println("Is logged in? " + isLoggedIn);
        System.out.println("Grade: " + grade);
    }
}
=============

public class Main {
    public static void main(String[] args) {

        int age = 20;

        // If the condition is true, this block will run
        if (age >= 18) {
            System.out.println("You are an adult.");  
        } 
        // If the IF is false, this part will run
        else {
            System.out.println("You are NOT an adult.");
        }
    }
}


public class Main {
    public static void main(String[] args) {

        int score = 75;

        // Check the first condition
        if (score >= 90) {
            System.out.println("Grade: A");  

        // If the first is false, check this one
        } else if (score >= 80) {
            System.out.println("Grade: B");

        // This runs only if all above conditions are false
        } else {
            System.out.println("Grade: C or below");
        }
    }
}


| Operator | Meaning          | Example       |
| -------- | ---------------- | ------------- |
| `==`     | equal to         | `a == b`      |
| `!=`     | not equal        | `a != b`      |
| `>`      | greater than     | `age > 18`    |
| `<`      | less than        | `age < 18`    |
| `>=`     | greater or equal | `score >= 90` |
| `<=`     | less or equal    | `score <= 50` |


| Operator | Meaning                            |   |                                          
| -------- | ---------------------------------- | - |
| `&&`     | AND → both conditions must be true |   |                                          
| `||`     | OR → at least one condition must be true 
| `!`      | NOT → reverses true/false          |   |                                          

=================================



public class Main {
    public static void main(String[] args) {

        // Loop from 1 to 5
        for (int i = 1; i <= 5; i++) {

            // This runs every time the loop repeats
            System.out.println("Number: " + i);
        }
    }
}


int count = 1;

// Loop continues as long as the condition is true
while (count <= 3) {
    System.out.println("Count is: " + count);
    count++; // must increase or you get an infinite loop!
}

// function

public static void greet() {
    System.out.println("Hello!");
}

---------------
public class Main {

    // This is a method (function)
    public static void greet() {
        // Code inside this method will run when we call greet()
        System.out.println("Hello from Java!");
    }

    public static void main(String[] args) {

        // Call the method
        greet();
        greet();  // you can call it multiple times
    }
}

-------------

public class Main {

    // Method that receives a name
    public static void sayHello(String name) {
        System.out.println("Hello " + name);
    }

    public static void main(String[] args) {

        // Calling method and passing values
        sayHello("Talha");
        sayHello("John");
    }
}


-----------------

public class Main {

    // Method that adds two numbers and returns result
    public static int add(int a, int b) {
        return a + b;  // send result back
    }

    public static void main(String[] args) {

        int result = add(5, 3);  // store returned value
        System.out.println("Result: " + result);
    }
}

---------------
public class Person {

    String name;
    int age;

    public void sayHello() {
        System.out.println("Hello, I'm " + name + " and I am " + age + " years old.");
    }
}

public class Main {
    public static void main(String[] args) {

        // Create object p1
        Person p1 = new Person();
        p1.name = "Talha";
        p1.age = 22;
        p1.sayHello();

        // Create another object p2
        Person p2 = new Person();
        p2.name = "John";
        p2.age = 30;
        p2.sayHello();
    }
}


------------------------

public class Person {

    String name;
    int age;

    // This is a constructor
    public Person(String n, int a) {
        name = n;  // set the object's name
        age = a;   // set the object's age
    }

    public void sayHello() {
        System.out.println("Hello, I'm " + name + " and I'm " + age + " years old.");
    }
}


public class Main {
    public static void main(String[] args) {

        // When we create an object, we pass values
        Person p1 = new Person("Talha", 22);
        Person p2 = new Person("John", 30);

        p1.sayHello();
        p2.sayHello();
    }
}

// Getters & Setters


public class Person {
    private String name;  // private = cannot access directly from outside
    private int age;
}


public class Person {

    private String name;
    private int age;

    // Getter for name (returns the name)
    public String getName() {
        return name;
    }

    // Setter for name (sets/updates the name)
    public void setName(String n) {
        name = n;
    }

    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age
    public void setAge(int a) {
        age = a;
    }
}


public class Main {
    public static void main(String[] args) {

        Person p = new Person();

        // Using setter methods to set data
        p.setName("Talha");
        p.setAge(22);

        // Using getter methods to access data
        System.out.println(p.getName());
        System.out.println(p.getAge());
    }
}


// Inheritance

public class Animal {

    String name;

    public void eat() {
        System.out.println(name + " is eating...");
    }

    public void sleep() {
        System.out.println(name + " is sleeping...");
    }
}

public class Dog extends Animal {

    public void bark() {
        System.out.println(name + " is barking!");
    }
}

public class Main {
    public static void main(String[] args) {

        Dog d = new Dog();
        d.name = "Buddy";

        // inherited methods
        d.eat();
        d.sleep();

        // Dog-specific method
        d.bark();
    }
}


// Method Overriding

public class Animal {

    public void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

public class Dog extends Animal {

    @Override
    public void makeSound() {
        System.out.println("Dog barks: Woof Woof!");
    }
}

public class Cat extends Animal {

    @Override
    public void makeSound() {
        System.out.println("Cat meows: Meow Meow!");
    }
}

public class Main {
    public static void main(String[] args) {

        Dog d = new Dog();
        d.makeSound();   // Dog version (override)

        Cat c = new Cat();
        c.makeSound();   // Cat version (override)
    }
}


// Polymorphism

public class Animal {
    public void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Dog barks");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Cat meows");
    }
}

public class Main {
    public static void main(String[] args) {

        Animal a1 = new Dog();  // parent reference, child object
        Animal a2 = new Cat();

        a1.makeSound(); // Dog's version
        a2.makeSound(); // Cat's version
    }
}

// static keyword

public class Counter {
    static int count = 0; // shared by all objects
}

public class Main {
    public static void main(String[] args) {

        System.out.println(Counter.count); // access without object
    }
}

// Static Method

public class Utils {

    public static void greet() {
        System.out.println("Hello!");
    }
}

public class Main {
    public static void main(String[] args) {
        Utils.greet(); // call without creating an object
    }
}

final int MAX_USERS = 100;
MAX_USERS = 200; // ❌ ERROR

--------------------
public class Main {
    public static void main(String[] args) {
        try {
            int a = 10;
            int b = 0;
            int c = a / b;   // This will cause an exception
            
            System.out.println("Result: " + c); // will not run
        } 
        catch (Exception e) {
            System.out.println("Error happened: " + e.getMessage());
        }
    }
}

// Array

Method 1 — Direct values
int[] numbers = {10, 20, 30, 40};


Method 2 — Create empty array with size
int[] ages = new int[5];  // array of size 5



int[][] matrix = {
    {1, 2, 3}, 
    {4, 5, 6}
};

System.out.println(matrix[0][1]);

-------------------------
// ArrayList
import java.util.ArrayList;


ArrayList<String> names = new ArrayList<>();
ArrayList<Integer> numbers = new ArrayList<>();


names.add("Talha");
names.add("Ali");
names.add("Sara");


System.out.println(names.get(0)); // Talha
System.out.println(names.get(1)); // Ali

Change an element
names.set(1, "John"); 

names.remove(0);  // removes "Talha"


// Loop through ArrayList

for (int i = 0; i < names.size(); i++) {
    System.out.println(names.get(i));
}

for (String n : names) {
    System.out.println(n);
}

if (names.contains("Talha")) {
    System.out.println("Found!");
}

names.clear();

// linklist

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {

        LinkedList<String> names = new LinkedList<>();

        names.add("Talha");
        names.add("Ali");
        names.add("Sara");

        names.addFirst("Start");
        names.addLast("End");

            System.out.println(names);
    }
}

// methods exist
addFirst()
addLast()
getFirst()
getLast()
removeFirst()
removeLast()

// hashmap
import java.util.HashMap;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> user = new HashMap<>();

        user.put("name", "Talha");
        user.put("email", "talha@gmail.com");
        user.put("city", "Karachi");
        user.put("city", "Lahore"); // update

        // Convert keys to a list so we can use index-based loop
        ArrayList<String> keys = new ArrayList<>(user.keySet());

        // Classic loop: i = 0; i < length; i++
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);          // key at index i
            String value = user.get(key);      // get value using the key
            System.out.println(key + " = " + value);
        }
    }
}

// -----second loop

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> user = new HashMap<>();

        user.put("name", "Talha");
        user.put("email", "talha@gmail.com");
        user.put("city", "Karachi");

        user.put("city", "Lahore"); // update

        for (String key : user.keySet()) {
            System.out.println(key + " = " + user.get(key));
        }
    }
}
