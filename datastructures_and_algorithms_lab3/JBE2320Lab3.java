import java.io.*;
import java.util.Scanner;

public class JBE2320Lab3
{

  public static void main(String[] args) throws IOException
  {
    // inputs
    Scanner in = new Scanner(System.in).useDelimiter("[\\s,]");
    PrintStream out = new PrintStream(System.out);
    FileWriter outFile = null;
    try
    {
      outFile = new FileWriter(args[0], true);
    }
    catch (FileNotFoundException FNFE)
    {
      out.println("Error ID:10-T sub error 2.  Invalid or non-existant"
        + " output file");
      FNFE.printStackTrace();
      System.exit(1);
    }

    int netPriority[] = new int[10];
    int netID[] = new int[10];
    String continueCheck = "4";
    int id = -1;

    while (true)
    {
      switch (continueCheck.charAt(0))
      {
        case '1' :
          sink(netPriority, netID);
          break;
        case '3' :
          out.println("Terminating the Program normally.");
          outFile.close();
          System.exit(0);
          break;
        case '2' :
          out.println("Please enter the network ID you are looking for:");
          id = in.nextInt();
          in.nextLine();
          int temp = id;
          for (int i = 0; i < 10; i++)
          {
            if (id == netID[i])
              id = i;
          }
          if (temp != id)
          {
            out.println("Please enter the new Priority for network "
              + netID[id] + ":");
            netPriority[id] = in.nextInt();
            in.nextLine();
            if(netPriority[id] > 100 || netPriority[id] < 0)
            {
              out.println("input error! Exit Status 1!");
              outFile.close();
              System.exit(1);
            }
          }
          sink(netPriority, netID);
          break;
        case '4' :
          // set initial priorities of network id's
          out.println("Please enter the network information as such: "
            + "NetID,Priority for the 10 networks");
          for (int i = 0; i < 10; i++)
          {
            netID[i] = in.nextInt();
            netPriority[i] = in.nextInt();
          }
          in.nextLine();
          heapSort(netID, netPriority);
          break;

        default :
          out.println("invalid input format.  Exiting with error status code:"
            + "ID10-T");
          out.println("Try Entering the proper option number, enter, and then"
            + "all the data separated by commas and spaces.  Ex. ID,Priority "
            + "ID2,Priority2, etc.");
          break;

      }

      out.println(netID[0] + " " + netPriority[0]);
      outFile.append(netID[0] + " " + netPriority[0] + "\n");
      for (int i = 0; i < 10; i++)
      {
        out.print(netID[i] + "," + netPriority[i] + " ");
        outFile.append(netID[i] + "," + netPriority[i] + " ");
      }
      out.println();
      out.println();
      outFile.append("\n\n");

      netPriority[0] = 0; // set the priority of the delivered message back to 0

      out.println("What would you like to do? 1: continue without changing "
        + "priority of a subnet");
      out.println("2: change the priority of a subnet." + "\n3: Terminate the"
        + "Program.");
      continueCheck = in.nextLine();

    }
  }
  
  private static void heapSort(int netID[], int netPriority[])
  {
    for (int i = 0; i < 9; i++)
    {
      sink(netPriority, netID);
    }
  }

  private static void sink(int[] netPriority, int[] netID)
  {
    int j = 0;
    while (j < 9)
    {
      if (netPriority[j] < netPriority[j + 1])
      {
        exch(j, j + 1, netPriority, netID);
      }
      j++;
    }
  }

  private static void exch(int i, int j, int[] netPriority, int[] netID)
  {
    int temp = -1;
    temp = netPriority[i];
    netPriority[i] = netPriority[i + 1];
    netPriority[i + 1] = temp;

    temp = netID[i];
    netID[i] = netID[i + 1];
    netID[i + 1] = temp;
  }

}
