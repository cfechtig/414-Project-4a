//import ...

/**
 * Allows the user to add a new student or assignment to a gradebook,
 * or add a grade for an existing student and existing assignment
 */
public class gradebookadd {

  /* parses the cmdline to keep main method simplified */
  private something parse_cmdline(String[] args) {

    if(args.length==1)
      System.out.println("\nNo Extra Command Line Argument Passed Other Than Program Name");
    if(args.length>=2) {
      System.out.println("\nNumber Of Arguments Passed: " + args.length);
      System.out.println("----Following Are The Command Line Arguments Passed----");
      for(int counter=0; counter < args.length; counter++)
        System.out.println("args[" + counter + "]: " + args[counter]);
        // Decide what is the setting we are in
    }
    //TODO ...

    return something;
  }

  public static void main(String[] args) {
    something = parse_cmdline(args);

    if(argumentsmakesense) {
      //read from gradebook

      //TODO do things here.

      //write the result back out to the gradebook
    }

    return;
  }
}
