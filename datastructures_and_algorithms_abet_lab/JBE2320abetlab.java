// finished processing techniques: 1,
import java.io.*;
import java.util.*;

public class JBE2320abetlab extends Thread
{
  private int maxThread;
  private int curThread;
  private Integer[] arrayM;
  private Integer[] arrayN;
  private static PrintStream out = new PrintStream(System.out);

  public static void main(String args[]) throws Exception
  {
    int maxThreads = Runtime.getRuntime().availableProcessors();

    // this section handles generating the random values for the two tables
    Random rand = new Random(System.currentTimeMillis());
    int scale = 500;
    int m = rand.nextInt(scale * 10) + scale * 1000;
    int n = (int) (m * (rand.nextDouble() + 1));
    int k = (int) (n * (rand.nextDouble() + 1));

    out.println("Generating " + m + " new M values and " + n + " new N values");
    HashSet<Integer> M = new HashSet<Integer>();
    HashSet<Integer> N = new HashSet<Integer>();

    while (M.size() < m)
      M.add(rand.nextInt(k - 1));

    while (N.size() < n)
      N.add(rand.nextInt(k - 1));

    Integer[] A = (Integer[]) M.toArray(new Integer[M.size()]);
    Integer[] B = (Integer[]) N.toArray(new Integer[N.size()]);

    out.println("Finished generating all values between 0 and " + k);

    if (maxThreads >= 4)
      maxThreads = 4;
    if (maxThreads == 3)
      maxThreads = 2;

    Thread[] threads = new Thread[maxThreads];
    for (int i = 1; i <= maxThreads; i++)
    {
      out.println("Launching thread " + i);
      threads[i - 1] = new JBE2320abetlab(maxThreads, i, A, B);
      threads[i - 1].start();
    }
    out.println("Finished allocating threads");

    // Loop until all threads are finished
    boolean isAnyAlive = true;
    while (isAnyAlive)
    {
      isAnyAlive = false;
      for (Thread cur : threads)
      {
        if (cur.isAlive())
        {
          isAnyAlive = true;
          break;
        }
      }
      Thread.sleep(100);
    }
    // Now we know were finished with all threads
    out.println("TERMINATED ALL FOR TEST x");
  }

  public JBE2320abetlab(int max, int cur, Integer[] a, Integer[] b)
  {
    this.maxThread = max;
    this.curThread = cur;
    // Make a copy of a and b and store in class
    this.arrayM = a.clone();
    this.arrayN = b.clone();
  }
  public void run()
  {
    switch (this.maxThread)
    {
      case 1 :
        this.doubleQSData(this.arrayM, this.arrayN);
        this.binarySearchWQS(this.arrayM, this.arrayN);
        this.slowSearch(this.arrayM, this.arrayN);
        this.slowSearch2_0(this.arrayM, this.arrayN);
        break;
      case 2 :
        switch (this.curThread)
        {
          case 1 :
            this.doubleQSData(this.arrayM, this.arrayN);
            this.slowSearch(this.arrayM, this.arrayN);
            break;
          case 2 :
            this.binarySearchWQS(this.arrayM, this.arrayN);
            this.slowSearch2_0(this.arrayM, this.arrayN);
            break;
        }
      case 4 :
        switch (this.curThread)
        {
          case 1 :
            this.doubleQSData(this.arrayM, this.arrayN);
            break;
          case 2 :
            this.binarySearchWQS(this.arrayM, this.arrayN);
            break;
          case 3 :
            this.slowSearch(this.arrayM, this.arrayN);
            break;
          case 4 :
            this.slowSearch2_0(this.arrayM, this.arrayN);
            break;
        }
    }
  }
  // sort both tables and use a merge-like intersection
  public void doubleQSData(Integer[] a, Integer[] b)
  {
    out.println("\tEntering Algorithm 1...");
    long totTime = 0;
    long startTime;
    long stopTime;
    int arrayA[] = new int[a.length];
    int arrayB[] = new int[b.length];
    int shortestArray = 0;
    if ((this.arrayM.length - this.arrayN.length) >= 0)
      shortestArray = this.arrayN.length;
    else if ((this.arrayM.length - this.arrayN.length) < 0)
      shortestArray = this.arrayM.length;
    else
      System.out.println("Error with data inputs");
    int arrayC[] = new int[shortestArray];

    for (int i = 0; i < a.length; i++)
    {
      arrayA[i] = this.arrayM[i];
    }
    for (int i = 0; i < b.length; i++)
    {
      arrayB[i] = this.arrayN[i];
    }
    // start of the timer
    startTime = System.currentTimeMillis();
    quickSort(arrayA, 0, (arrayA.length - 1));
    quickSort(arrayB, 0, (arrayB.length - 1));
    mergeLikeSort(arrayA, arrayB, arrayC, shortestArray);
    stopTime = System.currentTimeMillis();
    // stop the timer
    totTime = stopTime - startTime;
    out
      .println("\tFinishing Algorithm 1:\n\tTime to complete processing technique 1: "
        + totTime);
  }// end processing technique 1

  // performs a merge-like sort of two arrays to find the intersection of
  // the two arrays
  private void mergeLikeSort(int[] arrayA, int[] arrayB, int[] arrayC,
    int shortestArray)
  {
    int i = 0;
    int j = 0;
    int k = 0;

    while ((i < (arrayA.length - 1)) && (j < (arrayB.length - 1))
      && (k < (arrayC.length - 1)))
    {
      if (arrayA[i] == arrayB[j])
      {
        arrayC[k] = arrayA[i];
        k++;
        i++;
        j++;
        continue;
      }
      if (arrayA[i] < arrayB[j])
        i++;
      if (arrayA[i] > arrayB[j])
        j++;
    }
  }

  // sorts only the larger table and then uses binary search for ea. val in the
  // smaller table
  public void binarySearchWQS(Integer[] a, Integer[] b)
  {
    out.println("\tEntering Algorithm 2...");
    long totTime = 0;
    long startTime;
    long stopTime;
    int arrayABSQS[] = new int[this.arrayM.length];
    int arrayBBSQS[] = new int[this.arrayN.length];
    int shortestArray = 0;
    int tempVar = -1;
    if ((this.arrayM.length - this.arrayN.length) >= 0)
      shortestArray = this.arrayN.length;
    else if ((this.arrayM.length - this.arrayN.length) < 0)
      shortestArray = this.arrayM.length;
    else
      System.out.println("Error with data inputs");
    int arrayC[] = new int[shortestArray];

    for (int i = 0; i < arrayABSQS.length; i++)
    {
      arrayABSQS[i] = this.arrayM[i];
    }
    for (int i = 0; i < arrayBBSQS.length; i++)
    {
      arrayBBSQS[i] = this.arrayN[i];
    }

    // start timer for the processing
    startTime = System.currentTimeMillis();
    if (arrayABSQS.length > arrayBBSQS.length)
    {
      quickSort(arrayABSQS, 0, (arrayABSQS.length - 1));
      int j = 0;
      for (int i = 0; i < arrayBBSQS.length; i++)
      {
        tempVar = binarySearch(arrayABSQS, i, 0, (arrayABSQS.length - 1));
        if (tempVar > -1)
        {
          arrayC[j] = tempVar;
          j++;
        }
      }
    }
    else if (arrayBBSQS.length >= arrayABSQS.length)
    {
      quickSort(arrayBBSQS, 0, (arrayBBSQS.length - 1));
      int j = 0;
      for (int i = 0; i < arrayABSQS.length; i++)
      {
        int tempVal = arrayABSQS[i];
        tempVar = binarySearch(arrayBBSQS, tempVal, 0, (arrayBBSQS.length - 1));
        if (tempVar > -1)
        {
          arrayC[j] = tempVar;
          j++;
        }
      }
    }

    stopTime = System.currentTimeMillis();
    // stop timer from processing
    totTime = stopTime - startTime;
    out
      .println("\tFinishing Algorithm 2:\n\tTime to complete processing technique 2: "
        + totTime);
  }// end of technique 2 for sorting

  private int binarySearch(int[] temp, int temp2, int low, int high)
  {
    int mid = (low + high) / 2;
    while ((low != mid) && (mid != high))
    {
      
      if (temp[mid] == temp2)
      {
        return temp[mid];
      }
      else if (temp[mid] < temp2)
      {
        low = mid + 1;
        mid = (high + low) / 2;
      }
      else if (temp[mid] > temp2)
      {
        high = mid - 1;
        mid = (high + low) / 2;
      }
    }
    if (temp[mid] == temp2)
    {
      return temp[mid];
    }
    else
      return -1;
  }

  // slowsearch searches the smaller table for ea. val in the larger table
  public void slowSearch(Integer[] a, Integer[] b)
  {
    out.println("\tEntering Algorithm 3...");
    long totTime = 0;
    long startTime;
    long stopTime;
    int arrayA[] = new int[a.length];
    int arrayB[] = new int[b.length];
    int shortestArray = 0;
    if ((this.arrayM.length - this.arrayN.length) >= 0)
      shortestArray = this.arrayN.length;
    else if ((this.arrayM.length - this.arrayN.length) < 0)
      shortestArray = this.arrayM.length;
    else
      System.out.println("Error with data inputs");
    int arrayC[] = new int[shortestArray];

    for (int i = 0; i < a.length; i++)
    {
      arrayA[i] = this.arrayM[i];
    }
    for (int i = 0; i < b.length; i++)
    {
      arrayB[i] = this.arrayN[i];
    }
    // start the timer to computer the total time to sort and find intersection
    startTime = System.currentTimeMillis();

    if (arrayA.length <= arrayB.length)
    {
      int k = 0;
      for (int i = 0; i < arrayB.length; i++)
      {
        for (int j = 0; j < arrayA.length; j++)
        {
          if (arrayA[j] == arrayB[i])
          {
            arrayC[k] = arrayA[j];
            k++;
            break;
          }
        }
      }
    }
    if (arrayB.length > arrayA.length)
    {
      int k = 0;
      for (int i = 0; i < arrayA.length; i++)
      {
        for (int j = 0; j < arrayB.length; j++)
        {
          if (arrayA[i] == arrayB[j])
          {
            arrayC[k] = arrayA[i];
            k++;
            break;
          }
        }
      }
    }

    stopTime = System.currentTimeMillis();
    // stop timer for processing
    totTime = stopTime - startTime;
    out
      .println("\tFinishing Algorithm 3:\n\tTime to complete processing technique 3: "
        + totTime);
  }

  // slowsearch2_0 searches the larger table for ea. val in the smaller table
  public void slowSearch2_0(Integer[] a, Integer[] b)
  {
    out.println("\tEntering Algorithm 4...");
    long totTime = 0;
    long startTime;
    long stopTime;
    int arrayA[] = new int[a.length];
    int arrayB[] = new int[b.length];
    int shortestArray = 0;
    if ((this.arrayM.length - this.arrayN.length) >= 0)
      shortestArray = this.arrayN.length;
    else if ((this.arrayM.length - this.arrayN.length) < 0)
      shortestArray = this.arrayM.length;
    else
      System.out.println("Error with data inputs");
    int arrayC[] = new int[shortestArray];

    for (int i = 0; i < a.length; i++)
    {
      arrayA[i] = this.arrayM[i];
    }
    for (int i = 0; i < b.length; i++)
    {
      arrayB[i] = this.arrayN[i];
    }
    // start the timer to computer the total time to sort and find intersection
    startTime = System.currentTimeMillis();

    if (arrayA.length <= arrayB.length)
    {
      int k = 0;
      for (int i = 0; i < arrayB.length; i++)
      {
        for (int j = 0; j < arrayA.length; j++)
        {
          if (arrayA[j] == arrayB[i])
          {
            arrayC[k] = arrayA[j];
            k++;
            break;
          }
        }
      }
    }
    if (arrayB.length > arrayA.length)
    {
      int k = 0;
      for (int i = 0; i < arrayA.length; i++)
      {
        for (int j = 0; j < arrayB.length; j++)
        {
          if (arrayA[i] == arrayB[j])
          {
            arrayC[k] = arrayA[i];
            k++;
            break;
          }
        }
      }
    }

    stopTime = System.currentTimeMillis();
    // stop timer for processing
    totTime = stopTime - startTime;
    out
      .println("\tFinishing Algorithm 4:\n\tTime to complete processing technique 4: "
        + totTime);
  }

  public void quickSort(int[] temp, int lo, int hi)
  {
    int i = lo, j = hi, h;
    int x = temp[(lo + hi) / 2];

    do
    {
      while (temp[i] < x)
        i++;
      while (temp[j] > x)
        j--;
      if (i <= j)
      {
        h = temp[i];
        temp[i] = temp[j];
        temp[j] = h;
        i++;
        j--;
      }
    } while (i <= j);

    if (lo < j)
      quickSort(temp, lo, j);
    if (i < hi)
      quickSort(temp, i, hi);
  }

}
