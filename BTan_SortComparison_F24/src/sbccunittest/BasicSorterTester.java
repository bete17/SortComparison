package sbccunittest;

import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;
import static sbcc.Core.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.lang3.*;

import cs106.*;
import org.junit.*;


/**
 * Unit test code to grade the sort comparison assignment.
 * 
 * 5/15/2024
 *
 * @author spstrenn
 */
public class BasicSorterTester {

    Sorter sorter;

    static final int INSERTION_SORT = 1;
    static final int MERGE_SORT = 2;
    static final int QUICK_SORT = 3;
    static final int HEAP_SORT = 4;
    final String newLine = getProperty("line.separator");
    static final int RANDOM_DATA = 0;
    static final int SORTED_DATA = 1;
    static final int DUPLICATE_DATA = 2;
    public static int baseScore = 0;
    public static int extraCredit = 0;
    public static int maxScore = 42;

    public static ArrayList<String> suggestions = new ArrayList<>();


    @BeforeClass
    public static void beforeTesting() {
        baseScore = 0;
        extraCredit = 0;
        suggestions.add("""
                                
                🧐 ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
                """);
    }


    @AfterClass
    public static void afterTesting() throws IOException {
        println();
        println("Estimated score out of " + maxScore + " (w/o late penalties, etc.) = " + baseScore);
        println("Estimated extra credit (assuming on time submission) = " + extraCredit);

        provideSuggestions();

    }


    private static void provideSuggestions() throws IOException {

        checkForCompareToIgnoreCase();

        if (suggestions.size() > 1)
            println(join(suggestions, ""));
    }


    private static void checkForCompareToIgnoreCase() throws IOException {
        // Check for use of compareToIgnoreCase() rather than compareTo()
        var sortCode = readFile("./src/cs106/BasicSorter.java");
        if (sortCode.contains(".compareToIgnoreCase(")) {
            suggestions.add("""
                                            
                    💡  It appears that BasicSorter is using .compareToIgnoreCase() when comparing some strings.
                        All of the sorts must use 👉 .compareTo() 👈 when comparing strings.
                    """);

        }
    }


    @Before
    public void setUp() {
        sorter = new BasicSorter();
    }


    @After
    public void tearDown() {
    }


    @Test(timeout = 30000)
    public void testQuickSort() {
        int wordLength = 5;
        int minN = 150000;
        int maxN = 600000;
        int stepN = 450000;
        int numSamplesPerTest = 3;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];
        double[] elapsedTimesSortedData = new double[numTests];

        standardSortTest(QUICK_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                RANDOM_DATA, true, 10, true, wordCounts, elapsedTimes);

        baseScore += 3;

        // Now verify that it handles sorted data sets properly.
        standardSortTest(QUICK_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                SORTED_DATA, true, 10, true, wordCounts, elapsedTimesSortedData);

        double sortedFactor = 2.0;
        if (elapsedTimesSortedData[elapsedTimesSortedData.length - 1] > sortedFactor
                * elapsedTimes[elapsedTimes.length - 1])
            fail("Sorted-data time limit exceeded.  Expected:  <= " + String.format("%.3f", sortedFactor
                    * elapsedTimes[elapsedTimes.length - 1]) + " sec, but was "
                    + String.format("%.3f", elapsedTimesSortedData[elapsedTimesSortedData.length - 1])
                    + " sec.  The time required to QuickSort " + wordCounts[wordCounts.length - 1]
                    + " already-sorted words must be less than " + sortedFactor
                    + " times the time to required to QuickSort " + wordCounts[wordCounts.length - 1]
                    + " words that are in random order.");

        baseScore += 2;
    }


    /**
     * This is just a speed test for quicksort.
     */
    // @Test(timeout = 30000)
    public void testQuickSortLarger() {
        int wordLength = 10;
        int minN = 1000000;
        int maxN = 1000000;
        int stepN = 1000000;
        int numSamplesPerTest = 10;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];

        standardSortTest(QUICK_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest, RANDOM_DATA, true, 10, true,
                wordCounts, elapsedTimes);

    }


    // @Test(timeout = 15000)
    public void testQuickSortDuplicates() {
        int wordLength = 10;
        int minN = 150000;
        int maxN = 600000;
        int stepN = 450000;
        int numSamplesPerTest = 3;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];

        standardSortTest(QUICK_SORT, wordLength, minN, maxN, stepN,
                numSamplesPerTest, DUPLICATE_DATA, true, 10, true,
                wordCounts, elapsedTimes);

    }


    /**
     * This is just a speed test for merge sort.
     */
    // @Test(timeout = 30000)
    public void testMergeSortLarger() {
        int wordLength = 10;
        int minN = 1000000;
        int maxN = 1000000;
        int stepN = 1000000;
        int numSamplesPerTest = 10;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];

        standardSortTest(MERGE_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                RANDOM_DATA, true, 10, true, wordCounts, elapsedTimes);
    }


    @Test(timeout = 15000)
    public void testPartition() {
        var words = createRandomStrings(1024, 5); // 10);
        var expectedWords = words.clone();
        Arrays.sort(expectedWords);

        int startNdx = 512;
        int segmentLength = 256;
        int indexOfPivot = sorter.partition(words, startNdx, segmentLength);

        // Verify that all values before indexOfPivot are <= words[indexOfPivot]
        for (int ndx = startNdx; ndx < indexOfPivot; ndx++)
            assertTrue(words[ndx].compareTo(words[indexOfPivot]) <= 0);

        // Verify that all values after indexOfPivot are >= words[indexOfPivot]
        for (int ndx = indexOfPivot + 1; ndx < (startNdx + segmentLength - 1); ndx++)
            assertTrue(words[ndx].compareTo(words[indexOfPivot]) >= 0);

        // Verify that a single partition does not result in a sorted array.
        words = createRandomStrings(100000, 5); // 10);
        sorter.partition(words, 0, 100000);
        int badNdx = verifyOrderMatches(expectedWords, words);
        assertNotEquals("The sorted data is out of order at index " + badNdx, -1, badNdx);

        baseScore += 10;
    }


    @Test(timeout = 30000)
    public void testMergeSort() {
        int wordLength = 5;
        int minN = 150000;
        int maxN = 600000;
        int stepN = 450000;
        int numSamplesPerTest = 3;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];
        double[] elapsedTimesSortedData = new double[numTests];

        standardSortTest(MERGE_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                RANDOM_DATA, true, 10, true, wordCounts, elapsedTimes);

        baseScore += 3;

        standardSortTest(MERGE_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                SORTED_DATA, true, 10, true, wordCounts, elapsedTimesSortedData);

        double sortedFactor = 0.95;
        if (elapsedTimesSortedData[elapsedTimesSortedData.length - 1] > sortedFactor
                * elapsedTimes[elapsedTimes.length - 1])
            fail("The elapsed time required to MergeSort " + wordCounts[wordCounts.length - 1]
                    + " already-sorted words must be less than " + sortedFactor
                    + " times the time to MergeSort " + wordCounts[wordCounts.length - 1]
                    + " words that are in random order.");
        baseScore += 2;
    }


    @Test(timeout = 30000)
    public void testMerge() {
        var originalWords = new String[]{"M", "B", "Z", "A", "F", "D", "C", "P", "Q", "E", "V", "X"};

        var words = new String[]{"M", "B", "Z", "A", "F", "D", "C", "P", "Q", "E", "V", "X"};

        var expectedWords = ArrayUtils.subarray(words, 6, 12);
        Arrays.sort(expectedWords);

        sorter.merge(words, 6, 3, 3);

        for (int ndx = 0; ndx < 6; ndx++)
            assertEquals(originalWords[ndx], words[ndx]);

        int badNdx = verifyOrderMatches(expectedWords, ArrayUtils.subarray(words, 6, 12));
        assertTrue("The merged data is out of order at index " + badNdx, badNdx == -1);

        baseScore += 10;
    }


    @Test(timeout = 30000)
    public void testHeapSort() {
        int wordLength = 5;
        int minN = 150000;
        int maxN = 600000;
        int stepN = 450000;
        int numSamplesPerTest = 3;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];

        standardSortTest(HEAP_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                RANDOM_DATA, true, 10, true,
                wordCounts, elapsedTimes);

        extraCredit += 2;
    }


    @Test(timeout = 30000)
    public void testHeapify() {
        var words = createRandomStrings(100000, 5); // 10);
        sorter.heapify(words);
        verifyIsHeap(words, 0);

        extraCredit += 2;
    }


    @Test // (timeout = 30000)
    public void testInsertionSort() throws IOException {
        Files.deleteIfExists(Path.of("testInsertionSort_result"));

        int wordLength = 5;
        int minN = 10000;
        int maxN = 40000;
        int stepN = 30000;
        int numSamplesPerTest = 2;
        int numTests = (maxN - minN) / stepN + 1;
        double[] wordCounts = new double[numTests];
        double[] elapsedTimes = new double[numTests];

        standardSortTest(INSERTION_SORT, wordLength, minN, maxN, stepN, numSamplesPerTest,
                RANDOM_DATA, false, 8.5, true, wordCounts, elapsedTimes);

        baseScore += 7;

        // Verify that insertionSort() works on just part of an array
        println(lineSeparator() + "Insertion Sort - Part of array");

        var originalWords = createRandomStrings(2000, 5);
        var firstNWordsSorted = ArrayUtils.subarray(originalWords.clone(), 0, 20);
        Arrays.sort(firstNWordsSorted);
        var expectedWords = originalWords.clone();
        Arrays.sort(expectedWords, 1000, 1020);

        var words = originalWords.clone();
        sorter.insertionSort(words, 1000, 20);       // Should sort only the 20 words starting at 1000

        checkForWrongSectionSorted(firstNWordsSorted, words);

        checkForArrayPartNotSorted(expectedWords, words);
        println("\tpassed");

        baseScore += 3;
    }

    private void checkForArrayPartNotSorted(String[] expectedWords, String[] words) {
        // Verify that the 20 elements starting at 1000 are sorted correctly
        int partResult = verifyOrderMatches(ArrayUtils.subarray(expectedWords, 1000, 1020), ArrayUtils.subarray(words, 1000, 1020));
        if (partResult != -1) {
            fail("The resulting array is out of order at index " + (1000 + partResult) + lineSeparator() +
                    """
                                                
                            🧐 ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
                                                
                            💡  One typical reason for this is an insertionSort() whose outer loop looks something like: 
                                
                                for (int i = fi + 1; i < n; i++) 
                                
                                Note that in this fragment, i is initialized with with an ★index★, fi.  But then it gets compared
                                to a ★count★, n.  👉 Indices cannot be compared with counts 👈.  To see why, consider the case where 
                                fi is 1000 and n is 8 (see https://tinyurl.com/yf5w8zmm).  If i gets initialized to 1001 and then
                                compared to 8 at the start of the loop, the loop will exit immediately.
                                
                                If you think that this analysis applies to your code, then you might want to consider the following 
                                outer loop fragment:
                                
                                👉 for (int i = fi + 1; i < fi + n; i++) 👈
                                
                            💡  Another typical reason for this problem is that the inner loop keeps searching for the proper insertion 
                                point all the way back to the beginning of the array.  But when sorting only part of the array, the last 
                                value to be compared should be at fi.  I.e. decrement as long as 👉 j >= fi 👈, not j >= 0.
                            """
            );
        }
    }

    private void checkForWrongSectionSorted(String[] firstNWordsSorted, String[] words) {
        int firstNResult = verifyOrderMatches(firstNWordsSorted, ArrayUtils.subarray(words, 0, 20));
        if (firstNResult == -1) {
            fail("The words from index 0 to 19 are sorted, but ★only★ the words from 1000 to 1019 should be sorted." + lineSeparator() +
                    """
                                                
                            🧐 ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
                                    
                            💡  One typical reason for this is that insertionSort() is initializing the array access  
                                index to 0 or 1 instead of fi + 1, so the wrong part of the array is being sorted.  You can use this 
                                diagram (https://tinyurl.com/yf5w8zmm) to reason about sorting part of an array. 
                                
                                If you think that this analysis applies to your code, then you might want to consider the following 
                                outer loop fragment:
                                
                                👉 for (int i = fi + 1; i < fi + n; i++) 👈
                                                                
                            💡  Another typical reason for this problem is that the inner loop keeps searching for the proper insertion 
                                point all the way back to the beginning of the array.  But when sorting only part of the array, the last 
                                value to be compared should be at fi.  I.e. decrement as long as 👉 j >= fi 👈, not j >= 0.
                            """);
        }
    }


    void standardSortTest(int sortId, int wordLength, int minN, int maxN, int stepN,
                          int numSamplesPerTest, int dataOrder, boolean ensureTimeRatioBelowThreshold,
                          double lastFirstTimeRatioThreshold, boolean printResults, double[] wordCounts,
                          double[] elapsedTimes) {

        String[] words;
        double sumTime;
        int testNdx = 0;
        long startTime;
        double elapsedTime;

        if (printResults) {
            switch (sortId) {
                case INSERTION_SORT:
                    out.print("\nInsertion Sort");
                    break;

                case QUICK_SORT:
                    out.print("\nQuick Sort");
                    break;

                case MERGE_SORT:
                    out.print("\nMerge Sort");
                    break;

                case HEAP_SORT:
                    out.print("\nHeap Sort");
                    break;
            }
        }

        switch (dataOrder) {
            case SORTED_DATA:
                println(" sorted data");
                break;
            case RANDOM_DATA:
                println(" random data");
                break;
            case DUPLICATE_DATA:
                println(" duplicated data");
                break;
        }

        for (int count = minN; count <= maxN; count += stepN) {
            sumTime = 0;
            for (int ndx = 0; ndx < numSamplesPerTest; ndx++) {
                String[] expectedWords;
                if (dataOrder == DUPLICATE_DATA) {
                    var word = RandomStringUtils.randomAlphabetic(wordLength)
                            .toUpperCase();
                    words = new String[count];
                    Arrays.fill(words, word);
                    expectedWords = words.clone();
                } else {
                    words = createRandomStrings(count, wordLength); // 10000 -> 0.3 s, 40000 -> 4.3 s
                    expectedWords = words.clone();
                    Arrays.sort(expectedWords);
                    if (dataOrder == SORTED_DATA)
                        Arrays.sort(words);
                }

                startTime = nanoTime();
                switch (sortId) {
                    case INSERTION_SORT:
                        sorter.insertionSort(words, 0, words.length);
                        break;

                    case QUICK_SORT:
                        sorter.quickSort(words, 0, words.length);
                        break;

                    case MERGE_SORT:
                        sorter.mergeSort(words, 0, words.length);
                        break;

                    case HEAP_SORT:
                        sorter.heapSort(words);
                        break;
                }
                elapsedTime = (nanoTime() - startTime) / 1.0e9;
                sumTime += elapsedTime;
                int badNdx = verifyOrderMatches(expectedWords, words);
                assertEquals("The sorted data is out of order at index " + badNdx, -1, badNdx);
            }
            wordCounts[testNdx] = count;
            elapsedTimes[testNdx] = sumTime / numSamplesPerTest;
            // println("N = " + count + "\tTime: " + elapsedTimes[testNdx]);
            testNdx++;
        }

        double countRatio = wordCounts[elapsedTimes.length - 1] / wordCounts[0];
        double timeRatio = elapsedTimes[elapsedTimes.length - 1] / elapsedTimes[0];

        if (printResults) {
            println("\tN = " + wordCounts[0] + ", time = " + String.format("%.3f sec", elapsedTimes[0]));
            println("\tN = " + wordCounts[elapsedTimes.length - 1] + ", time = "
                    + String.format("%.3f sec", elapsedTimes[elapsedTimes.length - 1]));
            println("\tCount:  " + countRatio + "x,  Time: " + String.format("%.1f", timeRatio) + "x");
        }

        boolean failedTimeRatioRequirement = false;
        String failureMessage = "";

        if (ensureTimeRatioBelowThreshold) {
            if (timeRatio > lastFirstTimeRatioThreshold) {
                failedTimeRatioRequirement = true;
                failureMessage = "Expected the ratio of the time for " + wordCounts[wordCounts.length - 1]
                        + " data elements to the time for " + wordCounts[0] + " data elements to be <= "
                        + lastFirstTimeRatioThreshold + ", but it was measured as " + timeRatio + ".";
            }
        } else {
            if (timeRatio < lastFirstTimeRatioThreshold) {
                failedTimeRatioRequirement = true;
                failureMessage = "Expected the ratio of the time for " + wordCounts[wordCounts.length - 1]
                        + " data elements to the time for " + wordCounts[0] + " data elements to be >= "
                        + lastFirstTimeRatioThreshold + ", but it was measured as " + timeRatio + ".";
            }
        }

        if (failedTimeRatioRequirement) {
            StringBuilder sb = new StringBuilder();

            switch (sortId) {
                case INSERTION_SORT:
                    sb.append("Insertion Sort")
                            .append(newLine);
                    break;

                case QUICK_SORT:
                    sb.append("Quick Sort")
                            .append(newLine);
                    break;

                case MERGE_SORT:
                    sb.append("Merge Sort")
                            .append(newLine);
                    break;

                case HEAP_SORT:
                    sb.append("Heap Sort")
                            .append(newLine);
                    break;
            }

            for (int ndx = 0; ndx < elapsedTimes.length; ndx++) {
                sb.append(wordCounts[ndx]).append("\t").append(elapsedTimes[ndx])
                        .append(newLine);
            }

            sb.append(failureMessage);
            fail(failureMessage);
        }

    }


    private void verifyIsHeap(String[] words, int i) {
        if ((2 * i + 1) > words.length)
            return;
        if (words[i].compareTo(words[2 * i + 1]) < 0)
            fail("heapify() failed.  The parent (" + words[i] + ") is less than its left child ("
                    + words[2 * i + 1] + ").");

        if ((2 * i + 2) < words.length)
            if (words[i].compareTo(words[2 * i + 2]) < 0)
                fail("heapify() failed.  The parent (" + words[i] + ") is less than its right child ("
                        + words[2 * i + 2] + ").");

        verifyIsHeap(words, 2 * i + 1);
        if ((2 * i + 2) < words.length)
            verifyIsHeap(words, 2 * i + 2);
    }


    public String[] createRandomStrings(int n, int wordLength) {
        var words = new String[n];
        for (int ndx = 0; ndx < n; ndx++)
            words[ndx] = RandomStringUtils.randomAlphabetic(wordLength)
                    .toUpperCase();
        return words;
    }


    public int verifyOrderMatches(String[] expectedWords, String[] words) {
        int badNdx = -1;
        for (int ndx = 0; ndx < words.length; ndx++) {
            if (words[ndx].compareTo(expectedWords[ndx]) != 0) {
                badNdx = ndx;
                break;
            }
        }

        return badNdx;
    }


    @Test
    public void testPmd() {

        // Try to ensure that PMD can run on Mac/linux
        try {
            var f = new File("./pmd_min/bin/run.sh");
            if (!f.canExecute())
                f.setExecutable(true);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        try {
            execPmd("src" + File.separator + "cs106", "cs106.ruleset");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        baseScore += 2;

    }


    private static void execPmd(String srcFolder, String rulePath) throws Exception {

        File srcDir = new File(srcFolder);
        File ruleFile = new File(rulePath);

        verifySrcAndRulesExist(srcDir, ruleFile);

        ProcessBuilder pb;
        if (getProperty("os.name").toLowerCase()
                .indexOf("win") >= 0) {
            String pmdBatPath = ".\\pmd_min\\bin\\pmd.bat";
            String curPath = Paths.get(".")
                    .toAbsolutePath()
                    .toString();

            // Handle CS lab situation where the current dir is a UNC path
            if (curPath.startsWith("\\\\NEBULA\\cloud$")) {
                curPath = "N:\\" + substringAfter(curPath, "cloud$\\");
                pmdBatPath = curPath + pmdBatPath.substring(1);
            }
            pb = new ProcessBuilder(
                    pmdBatPath,
                    "-f", "text",
                    "-d", srcDir.getAbsolutePath(),
                    "-R", ruleFile.getAbsolutePath());
        } else {
            pb = new ProcessBuilder(
                    "./pmd_min/bin/run.sh", "pmd",
                    "-d", srcDir.getAbsolutePath(),
                    "-R", ruleFile.getAbsolutePath());
        }
        Process process = pb.start();
        int errCode = process.waitFor();

        switch (errCode) {

            case 1:
                String errorOutput = getOutput(process.getErrorStream());
                fail("Command Error:  " + errorOutput);
                break;

            case 4:
                String output = trimFullClassPaths(getOutput(process.getInputStream()));
                output = updateOutputForArraycopy(output);
                output = updateOutputForCyclomaticComplexity(output);
                fail(output);
                break;

        }

    }

    private static String updateOutputForCyclomaticComplexity(String output) {
        if (output.contains("System.arraycopy is more efficient")) {
            var hint = """
                                                
                    🧐 ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
                                                
                    💡  This generally occurs when your code is copying a block of array elements, but using a loop to do it one
                        element at a time.  It is more efficient move the whole block with a single call to System.arraycopy().
                        
                        Note that for methods like insertionSort(), the inner loop is still needed to find the proper index to 
                        perform the insertion.  But you can perform the "make room ... by copying elements... one spot to the right" 
                        step by calling arraycopy() ★after★ the inner loop completes.                                
                        
                        Here (https://www.geeksforgeeks.org/system-arraycopy-in-java) is a good discussion of using arraycopy(), 
                        but an important point for our assignment is that the source array and the destination array can be 
                        the ★same★ array variable.    
                    """;
            output += hint;
        }
        return output;
    }

    private static String updateOutputForArraycopy(String output) {
        if (output.contains("has a Standard Cyclomatic Complexity of")) {
            var hint = """
                                                
                    🧐 ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
                                                
                    💡  To reduce the complexity of your method, consider extracting branching statements like
                        if's, switch's, or loops into a separate method.  NOTE: make a backup of your project first \uD83D\uDE05 
                        See https://blog.jetbrains.com/idea/2020/12/3-ways-to-refactor-your-code-in-intellij-idea/.
                        
                        Alternatively, check to see if your branching statements can be modified to reduce the number of branches.
                        See https://www.youtube.com/watch?v=Mz-dAHpRung.
                    """;
            output += hint;
        }
        return output;
    }


    private static String trimFullClassPaths(String output) {
        // Shorten output to just the short class name, line, and error.
        String[] lines = output.split(getProperty("line.separator"));
        StringBuilder sb = new StringBuilder();
        for (String line : lines)
            sb.append(substringAfterLast(line, File.separator))
                    .append(lineSeparator());

        return sb.toString();
    }


    private static void verifySrcAndRulesExist(File fileFolderToCheck, File ruleFile) throws Exception {
        if (!fileFolderToCheck.exists())
            throw new FileNotFoundException(
                    "The folder to check '" + fileFolderToCheck.getAbsolutePath() + "' does not exist.");

        if (!fileFolderToCheck.isDirectory())
            throw new FileNotFoundException(
                    "The folder to check '" + fileFolderToCheck.getAbsolutePath() + "' is not a directory.");

        if (!ruleFile.exists())
            throw new FileNotFoundException(
                    "The rule set file '" + ruleFile.getAbsolutePath() + "' could not be found.");
    }


    private static String getOutput(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(getProperty("line.separator"));
            }
        } finally {
            if (br != null) br.close();
        }
        return sb.toString();

    }
}
