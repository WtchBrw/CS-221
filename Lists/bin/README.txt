----------------------------------------------------------------------------
****************
* DLL Project
* BSU CS221
* 4/20/2022
* Nick Codispoti
****************

OVERVIEW:

This program is a double-linked node implementation of the
IndexedUnsortedList class. It uses the List Tester and the methods
defined in the IUDoubleLinkedList class (some of which are
inherited from the IndexedUnsortedList class) to run tests on variables
and lists.


INCLUDED FILES:

 * ListTester.java - source file
 * Node.java - source file
 * IUDoubleLinkedList.java - source file
 * IndexedUnsortedList.java - source file
 * README.txt - this file
 
COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac ListTester.java

 Run the compiled class file with the command:
 $ java ListTester

 Console output will give the results of all tests after the program
 finishes executing.
 
PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 The ListTester.java file contains all the tests that can be run on the list
 being used. The IUDoubleLinkedList class is an extension of the IndexedUnsortedList
 superclass which it inherits most of it's methods from. The Node class contains
 all the methods that can be called on Node<T> objects which are used in all the
 lists as variable types.
 
 The IndexedUnsortedList class contains all the methods that needed to be defined
 within the IUDoubleLinkedList class. However, the implementation of the class could
 easily be removed after the methods are all defined in order to cut down on the
 program size and number of dependencies.

TESTING:

 Getting the program to work was a little tough because there were several days worth
 of trial and error involved, not all the bugs were logged or even remembered but the most
 common issue was the program not running or even compiling properly on different
 computers; the cause of that still isn't exactly known but the issue was simply avoided
 by doing all the development on a computer where the program would load properly.
 
 There were a certain number of tests that were expected to fail on the DLLs such as the
 iterator concurrency tests. Once all the tests passed and the yucky ones consistently failed,
 I knew that I had a program that was mostly functional. To see all the tests that were being
 run, simply take a look at the ListTester class contents.
 
 The program is idiot-proof; I know because I'm an idiot and I built the sucker. However,
 there is the aforementioned faulty loading bug that occasionally happens but that's not exactly
 user-error so I think it's still idiot-proof.
 
 ----------------------------------------------------------------------------