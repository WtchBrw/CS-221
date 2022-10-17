import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList. 
 * This is a set of a myriad of black box tests that should (emphasis on the word "should") 
 * work for any implementation of this interface. No promises though and and no refunds, sold ad-is
 * 
 * 
 * NOTE: this has a 99.5% success rate, I couldn't figure out what kept going wrong 
 * and ran out of time. I also got a little carries away since I didn't understand the assignment 
 * at first and tried to do all the tests instead of just the 7 we were supposed to which is partially 
 * why I ran out of time and still have things not working (;_;)
 * 
 * UPDATE 1.1: so now the success rate is a little wacky but its still cool, it does the same stuff
 * but the success rate is a little lower since there's a lot of tests being run and not
 * all of them pass (which is to be expected). This newer version includes ArrayList testing
 * along with my sweat blood and tears (and maybe hair i ripped out over it too)
 * 
 * it says that there are problems launching it but just click proceed anyways, they're not
 * important problems, just the compiler being angry for some reason
 * 
 * UPDATE 1.2: single-linked lists are now supported and work when the list type/to use is set
 * to "LIST_TO_USE = ListToUse.singleLinkedList". The Node class is also required to run the single-linked
 * lists stuff and as of this version at 6:13PM 4/6/2022, the program returns all tests being 
 * passed with zero failed.
 * 
 * @author mvail, mhthomas, awinters, Nick Codispoti
 */

public class ListTester {
	//possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList
	};
	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.singleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement, 
		ConcurrentModification, UnsupportedOperation, 
		NoException, UnexpectedException,
		True, False, Pass, Fail, 
		MatchingValue,
		ValidString
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = 1;
	private static final Integer ELEMENT_B = 2;
	private static final Integer ELEMENT_C = 3;
	private static final Integer ELEMENT_D = 4;
	private static final Integer ELEMENT_X = -1;//element that should appear in no lists
	private static final Integer ELEMENT_Z = -2;//element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; //initialized in constructor
	
	//tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	//control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 *  -a : print results from all tests (default is to print failed tests, only)
	 *  -s : hide Strings from toString() tests
	 *  -m : hide section summaries in output
	 * @param args not used
	 */
	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();
	}

	/** tester constructor
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
		case doubleLinkedList:
			SUPPORTS_LIST_ITERATOR = true;
			break;
		default:
			SUPPORTS_LIST_ITERATOR = false;
			break;
		}
	}

	/** Print test results in a consistent format
	 * @param testDesc description of the test
	 * @param result indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) { passes++; }
		else { failures++; }
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; //reset for next section
		secPasses = passes;
		secFails = failures;		
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
	}
	
	/////////////////////
	// XXX runTests()
	/////////////////////

	/** Run tests to confirm required functionality from list constructors and methods */
	private void runTests() {
		//Possible list contents after a scenario has been set up
		Integer[] LIST_A = {ELEMENT_A};
		String STRING_A = "A";
		Integer[] LIST_B = {ELEMENT_B};
		String STRING_B = "B";
		Integer[] LIST_AB = {ELEMENT_A, ELEMENT_B};
		String STRING_AB = "AB";
		Integer[] LIST_BA = {ELEMENT_B, ELEMENT_A};
		String STRING_BA = "BA";
		Integer[] LIST_AC = {ELEMENT_A, ELEMENT_C};
		String STRING_AC = "AC";
		Integer[] LIST_BC = {ELEMENT_B, ELEMENT_C};
		String STRING_BC = "BC";
		Integer[] LIST_CB = {ELEMENT_C, ELEMENT_B};
		String STRING_CB = "CB";
		Integer[] LIST_ABC = {ELEMENT_A, ELEMENT_B, ELEMENT_C};
		String STRING_ABC = "ABC";
		Integer[] LIST_ACB = {ELEMENT_A, ELEMENT_C, ELEMENT_B};
		String STRING_ACB = "ACB";
		Integer[] LIST_CAB = {ELEMENT_C,ELEMENT_A, ELEMENT_B};
		String STRING_CAB = "CAB";
		Integer[] LIST_ABD = {ELEMENT_A, ELEMENT_B, ELEMENT_D};
		String STRING_ABD = "ABD";
		Integer[] LIST_ADC = {ELEMENT_A, ELEMENT_D, ELEMENT_C};
		String STRING_ADC = "ADC";
		Integer[] LIST_DBC = {ELEMENT_D, ELEMENT_B, ELEMENT_C};
		String STRING_DBC = "ABC";
		
		//newly constructed empty list
		testEmptyList(newList, "newList");
		//empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addToRearA_A,"emptyList_addToRearA_A",LIST_A,STRING_A);
		testSingleElementList(emptyList_addAtIndex0A_A,"emptyList_addAtIndex0A_A",LIST_A,STRING_A);
		testSingleElementList(emptyList_add_A,"emptyList_add_A",LIST_A,STRING_A);
		//1-element to empty list
		testEmptyList(A_removeFirst_emptyList,"A_removeFirst_emptyList");
		testEmptyList(A_removeLast_emptyList,"A_removeLast_emptyList");
		testEmptyList(A_removeA_emptyList,"A_removeA_emptyList");
		testEmptyList(A_removeAtIndex0_emptyList,"A_removeAtIndex0_emptyList");
		//1-element to 2-element
		testTwoElementList(A_addToFrontB_BA,"A_addToFrontB_BA",LIST_BA, STRING_BA);
		testTwoElementList(A_addToRearB_AB,"A_addToRearB_AB",LIST_AB,STRING_AB);
		testTwoElementList(A_addAfterBA_AB,"A_addAfterB_AB",LIST_AB,STRING_AB);	
    	testTwoElementList(A_addB_AB,"A_addB_AB",LIST_AB,STRING_AB);
		testTwoElementList(A_addAtIndex0B_BA,"A_addAtIndex0B_BA",LIST_BA,STRING_BA);
		testTwoElementList(A_addAtIndex1B_AB,"A_addAtIndex1B_AB",LIST_AB,STRING_AB);
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA", LIST_BA, STRING_BA);
		//1-element to changed 1-element via set()
		testSingleElementList(A_setAtIndex0B_B,"A_setAtIndex0B_B()",LIST_B,STRING_B);
		//2-element to 1-element
		testSingleElementList(AB_removeFirst_B,"AB_removeFirst_B",LIST_B,STRING_B);
		testSingleElementList(AB_removeLast_A,"AB_removeLast_A",LIST_A,STRING_A);
		testSingleElementList(AB_removeA_B,"AB_removeA_B",LIST_B,STRING_B);
		testSingleElementList(AB_removeB_A,"AB_removeB_A",LIST_A,STRING_A);
		testSingleElementList(AB_removeAtIndex0_B,"AB_removeAtIndex0_B",LIST_B,STRING_B);
		testSingleElementList(AB_removeAtIndex1_A,"AB_removeAtIndex1_A",LIST_A,STRING_A);
		//2-element to 3-element
		testThreeElementList(AB_addToFrontC_CAB,"AB_addToFrontC_CAB",LIST_CAB,STRING_CAB);
		testThreeElementList(AB_addToRearC_ABC,"AB_addToRearC_ABC",LIST_ABC,STRING_ABC);		
		testThreeElementList(AB_addAfterCA_ACB,"AB_addAfterCA_ACB",LIST_ACB,STRING_ACB);
		testThreeElementList(AB_addAfterCB_ABC,"AB_addAfterCB_ABC",LIST_ABC,STRING_ABC);
		testThreeElementList(AB_addC_ABC,"AB_addC_ABC",LIST_ABC,STRING_ABC);
		testThreeElementList(AB_addAtIndex0C_CAB," AB_addAtIndex0C_CAB",LIST_CAB,STRING_CAB);
		testThreeElementList(AB_addAtIndex1C_ACB,"AB_addAtIndex1C_ACB",LIST_ACB,STRING_ACB);
		testThreeElementList(AB_addAtIndex2C_ABC,"AB_addAtIndex2C_ABC",LIST_ABC,STRING_ABC);
		//2-element to changed 2-element via set()
		testTwoElementList(AB_setAtIndex0C_CB,"AB_setAtIndex0C_CB",LIST_CB,STRING_CB);
		testTwoElementList(AB_setAtIndex1C_AC,"AB_setAtIndex1C_AC",LIST_AC,STRING_AC);
		//3-element to 2-element
		testTwoElementList(ABC_removeFirst_BC,"ABC_removeFirst_BC",LIST_BC,STRING_BC);
		testTwoElementList(ABC_removeLast_AB,"ABC_removeLast_AB",LIST_AB,STRING_AB);
		testTwoElementList(ABC_removeA_BC,"ABC_removeA_BC",LIST_BC,STRING_BC);
		testTwoElementList(ABC_removeB_AC,"ABC_removeB_AC",LIST_AC,STRING_AC);
		testTwoElementList(ABC_removeC_AB,"ABC_removeC_AB",LIST_AB,STRING_AB);
		testTwoElementList(ABC_removeAtIndex0_BC,"ABC_removeAtIndex0_BC",LIST_BC,STRING_BC);
		testTwoElementList(ABC_removeAtIndex1_AC,"ABC_removeAtIndex1_AC",LIST_AC,STRING_AC);
		testTwoElementList(ABC_removeAtIndex2_AB,"ABC_removeAtIndex2_AB",LIST_AB,STRING_AB);
		//3-element to changed 3-element via set()
	    testEmptyList(A_iterator_remove_AfterNext_emptyList, "A_iterator_remove_AfterNext_emptyList");
	    testSingleElementList(AB_iterator_remove_afterNext_B,"AB_iterator_remove_afterNext_B",LIST_B,STRING_B);
	    testSingleElementList(AB_iterator_remove_afterIndex2Next_A ,"AB_iterator_remove_afterIndex2Next_A ",LIST_A,STRING_A);
	    testTwoElementList(ABC_iterator_remove_afterNext_BC,"ABC_iterator_remove_afterNext_BC",LIST_BC,STRING_BC);
	    testTwoElementList(ABC_iterator_remove_afterIndex2Next_AC,"ABC_iterator_remove_afterIndex2Next_AC",LIST_AC,STRING_AC);
	    testTwoElementList(ABC_iterator_remove_afterIndex3Next_AB,"ABC_iterator_remove_afterIndex3Next_AB",LIST_AB,STRING_AB);
		//Iterator concurrency tests
	    
	    
	    // new - added in double linked list project
	    // list iterator tests
	    
	    // 0-elements
	    testEmptyList(A_listiterator_remove_afterNextA_emptyList, "A_listiterator_remove_afterNextA_emptyList");
		testEmptyList(A_listiterator_remove_afterPreviousA_emptyList, "A_listiterator_remove_afterPreviousA_emptyList");
	    // 1-elements
	    testSingleElementList(AB_listiterator_remove_afterNextA_B, "AB_listiterator_remove_afterNextA_B", LIST_B, "STRING_B");
		testSingleElementList(AB_listiterator_remove_afterNextB_A, "AB_listiterator_remove_afterNextB_A", LIST_A, "STRING_A");
		testSingleElementList(AB_listiterator_remove_afterPreviousA_B, "AB_listiterator_remove_afterPreviousA_B", LIST_B, "STRING_B");
		testSingleElementList(AB_listiterator_remove_afterPreviousB_A, "AB_listiterator_remove_afterPreviousB_A", LIST_A, "STRING_A");
		testSingleElementList(emptyList_listiterator_addA_A, "emptyList_listiterator_addA_A", LIST_A, "STRING_A");
		testSingleElementList(A_listiterator_set_BAfterNextA_B, "A_listiterator_set_BAfterNextA_B", LIST_B, "STRING_B");
		testSingleElementList(A_listiterator_set_BAfterPreviousA_B, "A_listiterator_set_BAfterPreviousA_B", LIST_B, "STRING_B");
	    // 2-elements
	    testTwoElementList(ABC_listiterator_remove_afterPreviousC_AB, "ABC_IterRemoveAfterPreviousC_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_listiterator_remove_afterNextA_BC, "ABC_listiterator_remove_afterNextA_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_listiterator_remove_AfterNextB_AC, "ABC_listiterator_remove_AfterNextB_AC", LIST_AC, STRING_AC);
		testTwoElementList(ABC_listiterator_remove_afterNextC_AB, "ABC_listiterator_remove_afterNextC_AB", LIST_AB, STRING_AB);
		testTwoElementList(ABC_listiterator_remove_afterPreviousA_BC, "ABC_listiterator_remove_afterPreviousA_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_listiterator_remove_afterPreviousB_AC, "ABC_listiterator_remove_afterPreviousB_AC", LIST_AC, STRING_AC);
		testTwoElementList(A_listiterator_add_BAfterNextA_AB, "A_listiterator_add_BAfterNextA_AB", LIST_AB, STRING_AB);
		testTwoElementList(A_listiterator_add_AfterPreviousB_BA, "A_listiterator_add_AfterPreviousB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_listiterator_addWithNewIteratorB_BA, "AB_listiterator_addWithNewIteratorB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_listiterator_set_CAfterNextA_CB, "A_listiterator_set_CAfterNextA_CB", LIST_CB, STRING_CB);
		testTwoElementList(AB_listiterator_set_CAfterNextB_AC, "AB_listiterator_set_CAfterNextB_AC", LIST_AC, STRING_AC);
		testTwoElementList(AB_listiterator_set_CAfterpreviousA_CB, "AB_listiterator_set_CAfterpreviousA_CB", LIST_CB, STRING_CB);
		testTwoElementList(AB_listiterator_set_CAfterpreviousB_AC, "AB_listiterator_set_CAfterpreviousB_AC", LIST_AC, STRING_AC);
	    // 3-elements
	    testThreeElementList(AB_listiterator_addWithNewIteratorC_CAB, "AB_listiterator_addWithNewIteratorC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_listiterator_add_CAfterNextA_ACB, "AB_listiterator_add_CAfterNextA_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(AB_listiterator_add_CAfterNextB_ABC, "AB_listiterator_add_CAfterNextB_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_listiterator_add_CAfterPreviousA_CAB, "AB_listiterator_add_CAfterPreviousA_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_listiterator_add_CAfterPreviousB_ACB, "AB_listiterator_add_CAfterPreviousB_ACB", LIST_ACB, STRING_ACB);
		testThreeElementList(ABC_listiterator_setD_afterNextA_DBC, "ABC_listiterator_setD_afterNextA_DBC", LIST_DBC, STRING_DBC);
		testThreeElementList(ABC_listiterator_setD_afterNextB_ADC, "ABC_listiterator_setD_afterNextB_ADC", LIST_ADC, STRING_ADC);
		testThreeElementList(ABC_listiterator_setD_afterNextC_ABD, "ABC_listiterator_setD_afterNextC_ABD", LIST_ABD, STRING_ABD);
		testThreeElementList(ABC_listiterator_setD_afterPreviousA_DBC, "ABC_listiterator_setD_afterPreviousA_DBC", LIST_DBC, STRING_DBC);
		testThreeElementList(ABC_listiterator_setD_afterPreviousB_ADC, "ABC_listiterator_setD_afterPreviousB_ADC", LIST_ADC, STRING_ADC);
		testThreeElementList(ABC_listiterator_setD_afterPreviousC_ABD, "ABC_listiterator_setD_afterPreviousC_ABD", LIST_ABD, STRING_ABD);
		
	    
	    
	    
		test_IterConcurrency();
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: Comment out cases for any implementations not currently available
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
//		case goodList:
//			listToUse = new GoodList<Integer>();
//			break;
//		case badList:
//			listToUse = new BadList<Integer>();
//			break;
//		case arrayList:
//			listToUse = new IUArrayList<Integer>();
//			break;
//		case singleLinkedList:
//			listToUse = new IUSingleLinkedList<Integer>();
//			break;
		case doubleLinkedList:
			listToUse = new IUDoubleLinkedList<Integer>();
//			break;
		default:
			//plants are like sponges but for air if you think about it; they suck up air and are sometimes used to clean
			listToUse = new IUDoubleLinkedList<Integer>();
		}
		return listToUse;
	}
	// The following creates a "lambda" reference that allows us to pass a scenario
	//  builder method as an argument. You don't need to worry about how it works -
	//  just make sure each scenario building method has a corresponding Scenario 
	//  assignment statement as in these examples. 

	// 1-43
	private Scenario<Integer> newList = () -> newList(); 
	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A();
	private Scenario<Integer> emptyList_addToRearA_A = () -> emptyList_addToRearA_A();
	private Scenario<Integer> emptyList_add_A =() ->  emptyList_addA_A();
	private Scenario<Integer> emptyList_addAtIndex0A_A = () -> emptyList_addAtIndex0A_A();
	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();
	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();
	private Scenario<Integer> A_addAfterBA_AB = () -> A_addAfterB_AB();
	private Scenario<Integer> A_addB_AB = () -> A_addB_AB();
	private Scenario<Integer> A_addAtIndex0B_BA = () -> A_addAtIndex0B_BA();
	private Scenario<Integer> A_addAtIndex1B_AB = () -> A_addAtIndex1B_AB();
	private Scenario<Integer> A_removeFirst_emptyList = () -> A_removeFirst_emptyList();
	private Scenario<Integer> A_removeLast_emptyList =  () -> A_removeLast_emptyList();
	private Scenario<Integer> A_removeA_emptyList = () -> A_removeA_emptyList();
	private Scenario<Integer> A_removeAtIndex0_emptyList = () -> A_removeAtIndex0_emptyList();
	private Scenario<Integer> A_setAtIndex0B_B = () -> A_setAtIndex0B_B();
	private Scenario<Integer>  AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();
    private Scenario<Integer>  AB_addToRearC_ABC =  () -> AB_addToRearC_ABC();
    private Scenario<Integer>  AB_addAfterCA_ACB =  () -> AB_addAfterCA_ACB();
    private Scenario<Integer>  AB_addAfterCB_ABC =  () -> AB_addAfterCB_ABC();
    private Scenario<Integer>  AB_addC_ABC =  ()-> AB_addC_ABC();
    private Scenario<Integer>  AB_addAtIndex0C_CAB = ()-> AB_addAtIndex0C_CAB();
    private Scenario<Integer>  AB_addAtIndex1C_ACB = ()-> AB_addAtIndex1C_ACB ();
    private Scenario<Integer>  AB_addAtIndex2C_ABC = ()-> AB_addAtIndex2C_ABC();
    private Scenario<Integer>  AB_removeFirst_B = () -> AB_removeFirst_B();
    private Scenario<Integer>  AB_removeLast_A =  () -> AB_removeLast_A();
    private Scenario<Integer>  AB_removeA_B = () -> AB_removeA_B();
    private Scenario<Integer>  AB_removeB_A = () -> AB_removeB_A();
    private Scenario<Integer>  AB_removeAtIndex0_B = () -> AB_removeAtIndex0_B();
    private Scenario<Integer>  AB_removeAtIndex1_A = () -> AB_removeAtIndex1_A();  
    private Scenario<Integer>  AB_setAtIndex0C_CB = () -> AB_setAtIndex0C_CB();
    private Scenario<Integer>  AB_setAtIndex1C_AC = () -> AB_setAtIndex1C_AC();	
    private Scenario<Integer>  ABC_removeAtIndex2_AB = () -> ABC_removeAtIndex2_AB();  
    private Scenario<Integer>  ABC_removeFirst_BC = () -> ABC_removeFirst_BC();
    private Scenario<Integer>  ABC_removeLast_AB = () -> ABC_removeLast_AB();
    private Scenario<Integer>  ABC_removeA_BC = () -> ABC_removeA_BC();
    private Scenario<Integer>  ABC_removeB_AC = () -> ABC_removeB_AC();
    private Scenario<Integer>  ABC_removeC_AB = () -> ABC_removeC_AB();
    private Scenario<Integer>  ABC_removeAtIndex0_BC = () -> ABC_removeAtIndex0_BC();
    private Scenario<Integer>  ABC_removeAtIndex1_AC = () -> ABC_removeAtIndex1_AC();
    // 44-49
    private Scenario<Integer> A_iterator_remove_AfterNext_emptyList = () -> A_iterator_remove_AfterNext_emptyList();
    private Scenario<Integer> AB_iterator_remove_afterNext_B = () -> AB_iterator_remove_afternext_B();
    private Scenario<Integer> AB_iterator_remove_afterIndex2Next_A =  () -> AB_iterator_remove_afterIndex2Next_A();
    private Scenario<Integer> ABC_iterator_remove_afterNext_BC = () -> ABC_iterator_remove_afterNext_BC();
    private Scenario<Integer> ABC_iterator_remove_afterIndex2Next_AC = () -> ABC_iterator_remove_afterIndex2Next_AC();  
    private Scenario<Integer> ABC_iterator_remove_afterIndex3Next_AB =  () -> ABC_iterator_remove_afterIndex3Next_AB ();
    // (50-55)
    private Scenario<Integer> A_listiterator_remove_afterNextA_emptyList = () -> A_listiterator_remove_afterNextA_emptyList();
	private Scenario<Integer> AB_listiterator_remove_afterNextA_B = () -> AB_listiterator_remove_afterNextA_B();
	private Scenario<Integer> AB_listiterator_remove_afterNextB_A = () -> AB_listiterator_remove_afterNextB_A();
	private Scenario<Integer> ABC_listiterator_remove_afterNextA_BC = () -> ABC_listiterator_remove_afterNextA_BC();
	private Scenario<Integer> ABC_listiterator_remove_AfterNextB_AC = () -> ABC_listiterator_remove_AfterNextB_AC();
	private Scenario<Integer> ABC_listiterator_remove_afterNextC_AB = () -> ABC_listiterator_remove_afterNextC_AB();
    
	// 55-82
	private Scenario<Integer> A_listiterator_remove_afterPreviousA_emptyList = () -> A_listiterator_remove_afterPreviousA_emptyList();
	private Scenario<Integer> AB_listiterator_remove_afterPreviousA_B = () -> AB_listiterator_remove_afterPreviousA_B();
	private Scenario<Integer> AB_listiterator_remove_afterPreviousB_A = () -> AB_listiterator_remove_afterPreviousB_A();
	private Scenario<Integer> ABC_listiterator_remove_afterPreviousA_BC = () -> ABC_listiterator_remove_afterPreviousA_BC();
	private Scenario<Integer> ABC_listiterator_remove_afterPreviousB_AC = () -> ABC_listiterator_remove_afterPreviousB_AC();
	private Scenario<Integer> ABC_listiterator_remove_afterPreviousC_AB = () -> ABC_listiterator_remove_afterPreviousC_AB();
	private Scenario<Integer> emptyList_listiterator_addA_A = () -> emptyList_listiterator_addA_A();
	private Scenario<Integer> A_listiterator_addWithNewIteratorB_BA = () -> A_listiterator_addWithNewIteratorB_BA();
	private Scenario<Integer> A_listiterator_add_BAfterNextA_AB = () -> A_listiterator_add_BAfterNextA_AB();
	private Scenario<Integer> A_listiterator_add_AfterPreviousB_BA = () -> A_listiterator_add_AfterPreviousB_BA();
	private Scenario<Integer> AB_listiterator_addWithNewIteratorC_CAB = () -> AB_listiterator_addWithNewIteratorC_CAB();
	private Scenario<Integer> AB_listiterator_add_CAfterNextA_ACB = () -> AB_listiterator_add_CAfterNextA_ACB();
	private Scenario<Integer> AB_listiterator_add_CAfterNextB_ABC = () -> AB_listiterator_add_CAfterNextB_ABC();
	private Scenario<Integer> AB_listiterator_add_CAfterPreviousA_CAB = () -> AB_listiterator_add_CAfterPreviousA_CAB();
	private Scenario<Integer> AB_listiterator_add_CAfterPreviousB_ACB = () -> AB_listiterator_add_CAfterPreviousB_ACB();
	private Scenario<Integer> A_listiterator_set_BAfterNextA_B = () ->A_listiterator_set_BAfterNextA_B();
	private Scenario<Integer> A_listiterator_set_BAfterPreviousA_B = () -> A_listiterator_set_BAfterPreviousA_B();
	private Scenario<Integer> A_listiterator_set_CAfterNextA_CB = () -> A_listiterator_set_CAfterNextA_CB();
	private Scenario<Integer> AB_listiterator_set_CAfterNextB_AC = () -> AB_listiterator_set_CAfterNextB_AC();
	private Scenario<Integer> AB_listiterator_set_CAfterpreviousA_CB = () -> AB_listiterator_set_CAfterpreviousA_CB();
	private Scenario<Integer>AB_listiterator_set_CAfterpreviousB_AC = () -> AB_listiterator_set_CAfterpreviousB_AC();
	private Scenario<Integer> ABC_listiterator_setD_afterNextA_DBC = () -> ABC_listiterator_setD_afterNextA_DBC();
	private Scenario<Integer> ABC_listiterator_setD_afterNextB_ADC = () -> ABC_listiterator_setD_afterNextB_ADC();
	private Scenario<Integer> ABC_listiterator_setD_afterNextC_ABD = () -> ABC_listiterator_setD_afterNextC_ABD();
	private Scenario<Integer> ABC_listiterator_setD_afterPreviousA_DBC = () -> ABC_listiterator_setD_afterPreviousA_DBC();
	private Scenario<Integer> ABC_listiterator_setD_afterPreviousB_ADC = () -> ABC_listiterator_setD_afterPreviousB_ADC();
	private Scenario<Integer> ABC_listiterator_setD_afterPreviousC_ABD = () -> ABC_listiterator_setD_afterPreviousC_ABD();


	/** Scenario: empty list -> addToFront(A) -> [A] 
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToFront(ELEMENT_A);
		return list;
	}

	/** Scenario:[] ->addToRear(A) -> [A]
	 * @return [A] after addToRear(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToRearA_A(){
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToRear(ELEMENT_A); 
		return list;
	}
	
	/** Scenario:[] ->add(A) -> [A]
	 * @return [A] after add(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addA_A(){
		IndexedUnsortedList<Integer> list = newList(); 
		list.add(ELEMENT_A);
		return list;
	}
	
	/** Scenario:[] ->add(0,A) -> [A]
	 * @return [A] after add (0,A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addAtIndex0A_A(){
		IndexedUnsortedList<Integer> list = newList(); 
		list.add(0,ELEMENT_A);
		return list;
	}
	
	/**Scenario:[A] -> addToFront(B) -> [B,A]
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA(){
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A();
		list.addToFront(ELEMENT_B);
		return list;
	}
	
	/**Scenario:[A] -> addToRear(B) -> [A,B]
	 * @return [A,B] after addToRear(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB(){
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.addToRear(ELEMENT_B);
		return list;	
	}
	
	/**Scenario: [A] -> A_addAtIndex0B_BA() -> [B,A]
	 * @return [A,B] after A_addAtIndex0B_BA()
	 */
	private IndexedUnsortedList<Integer> A_addAtIndex0B_BA(){
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.add(0,ELEMENT_B);
		return list;
	}
	
	/**Scenario: [A] -> addAfter(B) -> [A,B]
	 * @return [A,B] after addAfter(B)
	 */
	private IndexedUnsortedList<Integer> A_addAfterB_AB(){
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.addAfter(ELEMENT_B, ELEMENT_A);
		return list;
	}
	
	/**Scenario: [A] -> add(B) -> [A,B]
	 * @return [A,B] after add(B)
	 */
	private IndexedUnsortedList<Integer> A_addB_AB()
	{
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.add(ELEMENT_B);
		return list;
	}
	
	/** Scenario: [A] -> add(1,B) -> [A,B]
	 * @return [A,B] after add(1,B)
	 */
	private IndexedUnsortedList<Integer>  A_addAtIndex1B_AB()
	{
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.add(1,ELEMENT_B);
		return list;
	}
	
	/** Scenario: [A] -> removeFirst() -> []
	 * @return [] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> A_removeFirst_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.removeFirst();
		return list;
	}
	/** Scenario: [A] -> removeLast() -> []
	 * @return [] after removeLast()
	 */
	private IndexedUnsortedList<Integer> A_removeLast_emptyList() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.add(ELEMENT_A);
		list.removeLast();
		return list;
	}
	/**Scenario: [A] -> remove(A) -> []
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeA_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.remove(ELEMENT_A);
		return list;
	}
	/** Scenario: [A] -> remove(0) -> []
	 * @return [] after remove(0)
	 */
	private IndexedUnsortedList<Integer> A_removeAtIndex0_emptyList()
	{
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.remove(0);
		return list;
	}
	/**Scenario: [A] -> set(0,B) -> [B]
	 * @return [B] after set(0,B)
	 */
	private IndexedUnsortedList<Integer> A_setAtIndex0B_B()
	{
		IndexedUnsortedList<Integer> list = emptyList_addToRearA_A(); 
		list.set(0, ELEMENT_B);
		return list;
	}
	
	/**Scenario: [A,B] -> addToFront(C) -> [C,A,B]
	 * @return [C,A,B] after addToFront(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToFront(ELEMENT_C);
		return list;
	}
	
	/**Scenario: [A,B] -> addToRear(C) -> [A,B,C]
	 * @return [A,B,C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC(){	
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToRear(ELEMENT_C);
		return list;
	}
	
	/** Scenario: [A,B] -> addAfter(C,A) -> [A,C,B]
	 * @return [A,C,B] after addAfter(C,A)
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCA_ACB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_A);
		return list;
	}
	
	/**Scenario: [A,B] -> addAfter(C,B) -> [A,B,C]
	 * @return [A,B,C] after addAfter(C,B)
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCB_ABC()
	{
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_B);
		return list;
	}
	
	/** Scenario: [A,B] -> add(C) -> [A,B,C]
	 * @return [A,B,C] after add(C)
	 */
	private IndexedUnsortedList<Integer> AB_addC_ABC(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(ELEMENT_C);
		return list;
	}
	
	/**Scenario: [A,B] -> add(0,C) -> [C,A,B]
	 * @return [C,A,B] after add(0,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndex0C_CAB(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(0,ELEMENT_C);
		return list;
	}
	
	/**Scenario: [A,B] -> add(1,C) -> [A,C,B]
	 * @return [A,C,B] after add(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndex1C_ACB (){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(1,ELEMENT_C);
		return list;
	}
	
	/**Scenario: [A,B] -> add(2,C) -> [A,B,C]
	 * @return [A,B,C] after add(2,C)
	 */
	private IndexedUnsortedList<Integer> AB_addAtIndex2C_ABC()
	{
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(2,ELEMENT_C);
		return list;
	}
	
	/** Scenario: [A,B] -> removeFirst() -> [B]
	 * @return [B] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> AB_removeFirst_B(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeFirst();
		return list;
	}
	
	/** Scenario: [A,B] -> removeLast() -> [A]
	 * @return [A] after removeLast()
	 */
	private IndexedUnsortedList<Integer> AB_removeLast_A(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeLast();
		return list;
	}
	
	/** Scenario: [A,B] -> remove(A) -> [B]
	 * @return [B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> AB_removeA_B(){	
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(ELEMENT_A);
		return list;
	}
	
	/** Scenario: [A,B] -> remove(B) -> [A]
	 * @return [A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> AB_removeB_A(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(ELEMENT_B);
		return list;
	}
	
	/** Scenario: [A,B] -> remove(0) -> [B]
	 * @return [B] after remove(0)
	 */
	private IndexedUnsortedList<Integer> AB_removeAtIndex0_B(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(0);
		return list;
	}
	
	/** Scenario: [A,B] -> remove(1) -> [A]
	 * @return [A] after remove(1)
	 */
	private IndexedUnsortedList<Integer> AB_removeAtIndex1_A(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(1);
		return list;
	}
	
	/** Scenario: [A,B] -> set(0,C) -> [C,B]
	 * @return [C,B] after set (0,C)
	 */
	private IndexedUnsortedList<Integer> AB_setAtIndex0C_CB()
	{
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.set(0,ELEMENT_C);
		return list;
	}
	
	/** Scenario: [A,B] -> set(1,C) -> [A,C]
	 * @return [A,C] after set(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_setAtIndex1C_AC(){	
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.set(1,ELEMENT_C);
		return list;
	}	
	
	/** Scenario: [A,B,C] -> removeFirst() -> [B,C]
	 * @return [B,C] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> ABC_removeFirst_BC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.removeFirst();
		return list;
	}
	
	/** Scenario: [A,B,C] -> removeLast() -> [A,B]
	 * @return [A,B] after removeLast()
	 */
	private IndexedUnsortedList<Integer> ABC_removeLast_AB(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.removeLast();
		return list;
	}
	
	/** Scenario: [A,B,C] -> remove(A) -> [B,C]
	 * @return [B,C] after remove(A)
	 */
	private IndexedUnsortedList<Integer> ABC_removeA_BC(){
		
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(ELEMENT_A);
		return list;
	}

	/** Scenario: [A,B,C] -> remove(B) -> [A,C]
	 * @return [A,C] after remove(B)
	 */
	private IndexedUnsortedList<Integer> ABC_removeB_AC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(ELEMENT_B);
		return list;
	}
	
	/** Scenario: [A,B,C] -> remove(C) -> [A,B]
	 * @return [A,B} after remove(C)
	 */
	private IndexedUnsortedList<Integer> ABC_removeC_AB()
	{
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(ELEMENT_C);
		return list;
	}
	
	/** Scenario: [A,B,C] -> remove(0) -> [B,C]
	 * @return [B,C] after remove(0)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndex0_BC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(0);
		return list;
	}
	
	/** Scenario: [A,B,C] -> remove(1) -> [A,C]
	 * @return [A,C] after remove(1)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndex1_AC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(1);
		return list;
	}
	
	/** Scenario: [A,B,C] -> remove(2) -> [A,B]
	 * @return [A,B] after remove(2)
	 */
	private IndexedUnsortedList<Integer> ABC_removeAtIndex2_AB(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(2);
		return list;
	}
	
	private IndexedUnsortedList<Integer> A_iterator_remove_AfterNext_emptyList(){
		IndexedUnsortedList<Integer> list=emptyList_addToRearA_A();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_iterator_remove_afternext_B(){
		IndexedUnsortedList<Integer> list= A_addToRearB_AB();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_iterator_remove_afterIndex2Next_A(){		
		IndexedUnsortedList<Integer> list= A_addToRearB_AB();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.next();
		itr.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer>  ABC_iterator_remove_afterNext_BC(){		
		IndexedUnsortedList<Integer> list=AB_addToRearC_ABC();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> ABC_iterator_remove_afterIndex2Next_AC(){		
		IndexedUnsortedList<Integer> list=AB_addToRearC_ABC();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.next();
		itr.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> ABC_iterator_remove_afterIndex3Next_AB(){		
		IndexedUnsortedList<Integer> list=AB_addToRearC_ABC();
		Iterator<Integer> itr = list.iterator();
		itr.next();
		itr.next();
		itr.next();
		itr.remove();
		return list;
	}
	
	/**
	 * Scenario: [ABC] -> ABC_set0toD_DBC-> [DBC]
	 */
	private IndexedUnsortedList<Integer> ABC_set0toD_DBC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		list.set(0, ELEMENT_D);
		return list;
	}

	/**
	 * Scenario: [ABC] -> ABC_set1toD_ADC-> [ADC]
	 */
	private IndexedUnsortedList<Integer> ABC_se1toD_ADC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		list.set(1, ELEMENT_D);
		return list;
	}

	/**
	 * Scenario: [ABC] -> ABC_set2toD_ADC-> [ABD]
	 */
	private IndexedUnsortedList<Integer> ABC_set2toD_ABD() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		list.set(2, ELEMENT_D);
		return list;
	}

	/**
	 * Scenario: [A,B,C] -> iterator remove() after next() returns A -> [B,C]
	 */
	private IndexedUnsortedList<Integer> ABC_IterRemoveAfterNextA_BC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		Iterator<Integer> iter = list.iterator();
		iter.next();
		iter.remove();
		return list;
	}

	// new stuff
	
	private IndexedUnsortedList<Integer> AB_listiterator_addWithNewIteratorC_CAB() {
		IndexedUnsortedList<Integer> list  = newList();
		ListIterator<Integer> listIter = list.listIterator();
		listIter.add(ELEMENT_A);
		listIter.add(ELEMENT_B);
		ListIterator<Integer> listIt = list.listIterator();
		listIt.add(ELEMENT_C);
	
		return list;
	}
		
	private IndexedUnsortedList<Integer> AB_listiterator_add_CAfterNextA_ACB() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.add(ELEMENT_C);
		return list;
	}

	
	private IndexedUnsortedList<Integer> AB_listiterator_add_CAfterNextB_ABC() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.next();
		literator.add(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> AB_listiterator_add_CAfterPreviousA_CAB() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.add(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> AB_listiterator_add_CAfterPreviousB_ACB() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator(2);
		literator.previous();
		literator.add(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> A_listiterator_set_BAfterNextA_B() {
		IndexedUnsortedList<Integer> list  = emptyList_addA_A();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.set(ELEMENT_B);
		return list;
	}

	
	private IndexedUnsortedList<Integer> A_listiterator_set_BAfterPreviousA_B() {
		IndexedUnsortedList<Integer> list  = emptyList_addA_A();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.set(ELEMENT_B);
		return list;
	}

	private IndexedUnsortedList<Integer> A_listiterator_set_CAfterNextA_CB() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.set(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> AB_listiterator_set_CAfterNextB_AC() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.next();
		literator.set(ELEMENT_C);
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_listiterator_set_CAfterpreviousA_CB() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.set(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> AB_listiterator_set_CAfterpreviousB_AC() {
		IndexedUnsortedList<Integer> list  = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator(2);
		literator.previous();
		literator.set(ELEMENT_C);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterNextA_DBC() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.set(ELEMENT_D);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterNextB_ADC() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.next();
		literator.set(ELEMENT_D);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterNextC_ABD() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(2);
		literator.next();
		literator.set(ELEMENT_D);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterPreviousA_DBC() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.set(ELEMENT_D);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterPreviousB_ADC() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(2);
		literator.previous();
		literator.set(ELEMENT_D);
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_setD_afterPreviousC_ABD() {
		IndexedUnsortedList<Integer> list  = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(3);
		literator.previous();
		literator.set(ELEMENT_D);
		return list;
	}
	
	// new part 2
	
	private IndexedUnsortedList<Integer> A_listiterator_remove_afterPreviousA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addA_A();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.previous();
		literator.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_listiterator_remove_afterPreviousA_B() {
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.previous();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> AB_listiterator_remove_afterPreviousB_A() {
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.next();
		literator.previous();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_remove_afterPreviousA_BC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.remove();
		return list;
	}


	private IndexedUnsortedList<Integer> ABC_listiterator_remove_afterPreviousB_AC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(2);
		literator.previous();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_remove_afterPreviousC_AB() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator(3);
		literator.previous();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> emptyList_listiterator_addA_A() {
		IndexedUnsortedList<Integer> list = newList();
		ListIterator<Integer> literator = list.listIterator();
		literator.add(ELEMENT_A);
		return list;
	}

	
	private IndexedUnsortedList<Integer> A_listiterator_addWithNewIteratorB_BA() {
		IndexedUnsortedList<Integer> list  = newList();
		ListIterator<Integer> listIter = list.listIterator();
		listIter.add(ELEMENT_A);
		ListIterator<Integer> listIt = list.listIterator();
		listIt.add(ELEMENT_B);
		return list;
	}

	private IndexedUnsortedList<Integer> A_listiterator_add_BAfterNextA_AB() {
		IndexedUnsortedList<Integer> list  = emptyList_addA_A();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.add(ELEMENT_B);
		return list;
	}

	private IndexedUnsortedList<Integer> A_listiterator_add_AfterPreviousB_BA() {
		IndexedUnsortedList<Integer> list  = emptyList_addA_A();
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.add(ELEMENT_B);
		return list;
	}
	
	private IndexedUnsortedList<Integer> ABC_listiterator_remove_afterNextC_AB() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.next();
		literator.next();
		literator.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> ABC_listiterator_remove_AfterNextB_AC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.next();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> ABC_listiterator_remove_afterNextA_BC() {
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_listiterator_remove_afterNextB_A() {
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.next();
		literator.remove();
		return list;
	}
	
	private IndexedUnsortedList<Integer> AB_listiterator_remove_afterNextA_B() {
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.remove();
		return list;
	}

	private IndexedUnsortedList<Integer> A_listiterator_remove_afterNextA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> literator = list.listIterator();
		literator.next();
		literator.remove();
		return list;
	}
	
	/////////////////////////////////
	//XXX Tests for 0-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in an empty list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			
			
			// ListIterator
						
			//in testEmptyList()
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testlistiterator_add_", testlistiterator_add_(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
				printTest(scenarioName + "_testlistiterator_set_", testlistiterator_set_(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
	
	//////////////////////////////////
	//XXX Tests for 1-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a single element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			
			// in testSingleElementList()
			if (SUPPORTS_LIST_ITERATOR) {
			      printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testlistiterator_add_", testlistiterator_add_(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testlistiterator_set_", testlistiterator_set_(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testlistiterator_add_(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testlistiterator_set_(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.False));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testlistiterator_add_(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testlistiterator_set_(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			} else {
			      printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	//XXX Tests for 2-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in a two-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_interNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_interNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_interNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
		
			// in testDoubleElementList ()
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
		        printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
	  		    printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			    printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			    printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
			    printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));			      
			    printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			    printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			    printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			    printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			    printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			    printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			    printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			    printTest(scenarioName + "_testlistiterator_add_", testlistiterator_add_(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testlistiterator_set_", testlistiterator_set_(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			    printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			    printTest(scenarioName + "_testListIterNextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIterNextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			    printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			    printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			    printTest(scenarioName + "_testListIterNextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIterNextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			    printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			    printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			    printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			    printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			    printTest(scenarioName + "_testListIter0Add", testlistiterator_add_(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter0Set", testlistiterator_set_(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			    printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			    printTest(scenarioName + "_testListIter0NextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter0NextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			    printTest(scenarioName + "_testListIter0NextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter0NextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
			    printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));    
			    printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue)); 
			    printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			    printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			    printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));	      
			    printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));      
			    printTest(scenarioName + "_testListIter1Add", testlistiterator_add_(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));	    
			    printTest(scenarioName + "_testListIter1Set", testlistiterator_set_(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));  
			    printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));  
			    printTest(scenarioName + "_testListIter1PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIter1PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));      
			    printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			    printTest(scenarioName + "_testListIter1PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter1PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));     
			    // above code given
			    // below code written
			    printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.False));
			    printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
			    printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));      
			    printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
			    printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));		      
			    printTest(scenarioName + "_testListIter2Add", testlistiterator_add_(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter2Set", testlistiterator_set_(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
			    printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));	      
			    printTest(scenarioName + "_testListIter2PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter2PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
			    printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
			    printTest(scenarioName + "_testListIter2PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIter2PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
			
		}


		catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	//XXX Tests for 3-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a three-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
						printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
						printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
						printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
						printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
						printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
						printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
						printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
						printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
						printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
						printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testAddAtIndex4", testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testSet3", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
						printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
						printTest(scenarioName + "_testGet3", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
						printTest(scenarioName + "_testGet2", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
						printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
						printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
						printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
						printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, contents[2], Result.MatchingValue));	
						printTest(scenarioName + "_testRemove3", testRemoveIndex(scenario.build(), 3, null, Result.IndexOutOfBounds));		
						// Iterator
						printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
						printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
						printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
						printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));			
						printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
						printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
						printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
						printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
						printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
						printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
						printTest(scenarioName + "_iterNextNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
						printTest(scenarioName + "_iterNextNext_testIterNext",testIterNext(iterAfterNext(scenario.build(), 2),contents[2] ,Result.MatchingValue));
						printTest(scenarioName + "_iterNextNext_testIterRemove",testIterRemove(iterAfterNext(scenario.build(), 2),Result.NoException));			
						printTest(scenarioName +"_iterNextNextRemove_testIterHasNext",testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)),Result.True));
						printTest(scenarioName +"_iterNextNextRemove_testIterNext",testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)),contents[2],Result.MatchingValue));
						printTest(scenarioName +"_iterNextNextRemove_testIterRemove",testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)),Result.IllegalState));						
						
						// in testThreeElementList ()
						if (SUPPORTS_LIST_ITERATOR) {
							printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
					        printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				  		    printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
						    printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
						    printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
						    printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.NoException));
						    printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));	
						    printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
						    printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
						    printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
						    printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
						    printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
						    printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
						    printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
						    printTest(scenarioName + "_testlistiterator_add_", testlistiterator_add_(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testlistiterator_set_", testlistiterator_set_(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
						    printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
						    printTest(scenarioName + "_testListIterNextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIterNextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
						    printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
						    printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
						    printTest(scenarioName + "_testListIterNextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIterNextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
						    printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
						    printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
						    printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
						    printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
						    printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
						    printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
						    printTest(scenarioName + "_testListIter0Add", testlistiterator_add_(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter0Set", testlistiterator_set_(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
						    printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
						    printTest(scenarioName + "_testListIter0NextAdd", testlistiterator_add_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter0NextSet", testlistiterator_set_(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
						    printTest(scenarioName + "_testListIter0NextPreviousAdd", testlistiterator_add_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter0NextPreviousSet", testlistiterator_set_(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
						    printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));    
						    printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue)); 
						    printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
						    printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
						    printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));	      
						    printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));      
						    printTest(scenarioName + "_testListIter1Add", testlistiterator_add_(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));	    
						    printTest(scenarioName + "_testListIter1Set", testlistiterator_set_(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));  
						    printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));  
						    printTest(scenarioName + "_testListIter1PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter1PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));      
						    printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
						    printTest(scenarioName + "_testListIter1PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter1PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));		
						    printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.True));
						    printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));					      
						    printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));					      
						    printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));  					      
						    printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));					      
						    printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));  
	     				    printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));		      
						    printTest(scenarioName + "_testListIter2Add", testlistiterator_add_(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter2Set", testlistiterator_set_(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));	      
						    printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));	      
						    printTest(scenarioName + "_testListIter2PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter2PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
						    printTest(scenarioName + "_testListIter2PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter2PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));    
						    // new code
						    printTest(scenarioName + "_testListIter3HasNext", testIterHasNext(scenario.build().listIterator(3), Result.False));					      
						    printTest(scenarioName + "_testListIter3Next", testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));							      
						    printTest(scenarioName + "_testListIter3NextIndex", testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));					      
						    printTest(scenarioName + "_testListIter3HasPrevious", testListIterHasPrevious(scenario.build().listIterator(3), Result.True));  					      
						    printTest(scenarioName + "_testListIter3Previous", testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));					      
						    printTest(scenarioName + "_testListIter3PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue)); 					      
	     				    printTest(scenarioName + "_testListIter3Remove", testIterRemove(scenario.build().listIterator(3), Result.IllegalState));		      
						    printTest(scenarioName + "_testListIter3Add", testlistiterator_add_(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter3Set", testlistiterator_set_(scenario.build().listIterator(3), ELEMENT_X, Result.IllegalState));	      
						    printTest(scenarioName + "_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 1), Result.NoException));	      
						    printTest(scenarioName + "_testListIter3PreviousAdd", testlistiterator_add_(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter3PreviousSet", testlistiterator_set_(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
						    printTest(scenarioName + "_testListIter3PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), Result.NoException));
						    printTest(scenarioName + "_testListIter3PreviousNextAdd", testlistiterator_add_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));			
						    printTest(scenarioName + "_testListIter3.PreviousNextSet", testlistiterator_set_(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));
						} else {
							printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
							printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
						}
						
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/** Runs removeFirst() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.removeFirst();
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.removeLast();
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer value = list.remove(element);
			if (value.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs first() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.first();
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs last() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.last();
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs contains() method on a given list and element and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs isEmpty() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs size() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/** Runs toString() method on given list and attempts to confirm non-default or empty String
	 * difficult to test - just confirm that default address output has been overridden
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size()/2 + 2)) { //elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToFront() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront",  e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToRear() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addAfter() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs set(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs get() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.get(index);
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs remove(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer value = list.remove(index);
			if (value.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs indexOf() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/** Runs iterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator hasNext() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator next() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer value = iterator.next();
			if (value.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator remove() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs iterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> iterator_1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> iterator_2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Scenario: [A] -> iteratorRemoveAfterNextA -> [ ] 
	 * @return [ ] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterNextA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	
	/** Scenario: [A] -> iteratorRemoveAfterPreviousA -> [ ] 
	 * @return [ ] after iteratorRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterPreviousA_emptyList() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.remove();
		return list;
	}
	
	/** Scenario: [A] -> iteratorAddBAfterPreviousA -> [B,A] 
	 * @return [B,A] after iteratorAddBAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> A_iterAddBAfterPreviousA_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.add(ELEMENT_B);
		return list;
	}
	
	
	/** Scenario: [A,B] -> iteratorRemoveAfterPreviousA -> [B] 
	 * @return [B] after iteratorRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterPreviousA_B() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		list.addToRear(ELEMENT_B); // [A,B]
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.remove();
		return list;
	}
	
	/** Scenario: [A] -> iteratorSetBAfterPreviousA -> [B] 
	 * @return [B] after iteratorSetBAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> A_iterSetBAfterPreviousA_B() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		ListIterator<Integer> literator = list.listIterator(1);
		literator.previous();
		literator.set(ELEMENT_B);
		return list;
	}
	
	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING ITERATORS
	//Note: You can create other similar helpers if you want
	//something slightly different.
	//////////////////////////////////////////////////////////
	
	/** Helper for testing ListIterators. Return a ListIterator that has been advanced numCallsToNext times.
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}

	/** Helper for testing iterators. Return an Iterator that has had remove() called once.
	 * @param iterator
	 * @return same literator after calling remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/** Runs listIterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator's hasPrevious() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previous() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer value = iterator.previous();
			if (value.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator add() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to add()
	 * @param element new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testlistiterator_add_(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testlistiterator_add_", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator set() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to set()
	 * @param element replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testlistiterator_set_(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testlistiterator_set_", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator nextIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previousIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> iterator_1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> iterator_2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> iterator_1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> iterator_2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}
	

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING LISTITERATORS
	//Note: You can create other similar helpers if you want
	//  something slightly different.
	//////////////////////////////////////////////////////////
	
	/** Helper for testing ListIterators. Return a ListIterator that has been advanced numCallsToNext times.
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has been backed up numCallsToPrevious times.
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");		
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> iterator_1 = list.iterator();
			Iterator<Integer> iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(iterator_2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(iterator_2, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(iterator_2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(iterator_2, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(iterator_2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			iterator_2 = list.iterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));			

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	// Will add tests for double-linked list
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			printTest("emptyList_testConcurrentListIter", testListIterConcurrent(newList(), Result.NoException));
			printTest("emptyList_testConcurrentListIter00", testListIterConcurrent(newList(), 0, 0, Result.NoException));

			IndexedUnsortedList<Integer> list = newList();
			ListIterator<Integer> iterator_1 = list.listIterator();
			ListIterator<Integer> iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasNext", testIterHasNext(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Next", testIterNext(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2AddA", testlistiterator_add_(iterator_2, ELEMENT_A, Result.NoException));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2SetA", testlistiterator_set_(iterator_2, ELEMENT_A, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasNext", testIterHasNext(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Next", testIterNext(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2AddA", testlistiterator_add_(iterator_2, ELEMENT_A, Result.NoException));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2SetA", testlistiterator_set_(iterator_2, ELEMENT_A, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasNext", testIterHasNext(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Next", testIterNext(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2AddA", testlistiterator_add_(iterator_2, ELEMENT_A, Result.NoException));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2SetA", testlistiterator_set_(iterator_2, ELEMENT_A, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasNext", testIterHasNext(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Next", testIterNext(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2AddA", testlistiterator_add_(iterator_2, ELEMENT_A, Result.NoException));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2SetA", testlistiterator_set_(iterator_2, ELEMENT_A, Result.IllegalState));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Next", testIterNext(iterator_2, null, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2SetA", testlistiterator_set_(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = newList();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2HasNext", testIterHasNext(iterator_2, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			printTest("A_ListIter1Next_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2HasNext", testIterHasNext(iterator_2, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2Remove", testIterRemove(iterator_2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.MatchingValue));
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			printTest("A_ListIter1Previous_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.remove();
			printTest("A_ListIter1NextRemove_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			iterator_2 = list.listIterator();
			iterator_1.next();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.previous();
			iterator_1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasNext", testIterHasNext(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Next", testIterNext(iterator_2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Remove", testIterRemove(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasPrevious", testListIterHasPrevious(iterator_2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Previous", testListIterPrevious(iterator_2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2AddB", testlistiterator_add_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2SetB", testlistiterator_set_(iterator_2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2NextIndex", testListIterNextIndex(iterator_2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_2 = list.listIterator();
			iterator_1 = list.listIterator(1);
			iterator_1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2PreviousIndex", testListIterPreviousIndex(iterator_2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.True));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.False));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasNextConcurrent", testIterHasNext(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextConcurrent", testIterNext(iterator_1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterRemoveConcurrent", testIterRemove(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasPreviousConcurrent", testListIterHasPrevious(iterator_1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousConcurrent", testListIterPrevious(iterator_1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testlistiterator_add_BConcurrent", testlistiterator_add_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testlistiterator_set_BConcurrent", testlistiterator_set_(iterator_1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextIndexConcurrent", testListIterNextIndex(iterator_1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			iterator_1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(iterator_1, -1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/**
 * 
 *  
 *⠀  ⠀  (\__/) 
        (•ㅅ•)      - Don’t ever make 
   　 ＿ノ ヽ ノ＼＿        me or my PC
   `/　`/ ⌒Ｙ⌒ Ｙ　ヽ        do this ever again. 
   ( 　(三ヽ人　 /　　|
   |　ﾉ⌒＼ ￣￣ヽ　 ノ
    ヽ＿＿＿＞､＿＿_／         make the pain stop :[
　　     |( 王 ﾉ〈      ____  / 
　　     /ﾐ`ー―彡\     |•^•|  
　     / ╰    ╯ \    |___|
 *                      
 *
 *yes I did it all, yes it took 8 all-nighters and a month of unbearable stress,
 *and yes I do believe that if I'm going to be that dedicated to this stuff I am
 *allowed to throw in easter eggs and jokes of my own
 *
 */

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}
