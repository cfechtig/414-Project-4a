import java.io.File;

/*
  // Constraints:
  // name -> alhpanumeric characters including underscores and periods
      for adding student: if name already exists: error occurs
      for deleting student: if name does not exist: error occurs 
  // key -> string of hex digits
  // assignment name -> alphabet (upper and lower), 0-9, no spaces
      for add assignment: if same name is already existing then an error occurs
      for delete assignment: if name does not exist then error occurs
  // points -> >=0
  // weight -> real number in [0,1] (if new weight + current weights > 1 then error)
  // grade -> >=0
      for adding grade: if student name OR assignment doesnt exist: error occurs
      if grade for this assignment is already there, then replace it 
*/

/**
 * Initialize gradebook with specified name and generate a key.
 */
public class setup {

  /* test whether the file exists */
  private static boolean file_test(String filename) {
    File tempfile = new File(filename);
    if (tempfile.exists()) {
      System.out.print("invalid");
      System.exit(-1);
    }
    return true;
  }

  public static void main(String[] args) {
    //TODO: generate good key
    String key = "hello";

    if (args.length < 2) {
      System.out.println("Usage: setup <logfile pathname>");
      System.exit(1);
    }

    if (!args[0].equals("-N")) {
      System.exit(1);
    }
    String filename = args[1];
    file_test(filename);

    Gradebook gb = new Gradebook();
    gb.writeToFile(filename);
    /* add your code here */

    System.out.println("Key is: " + key);

    return;
  }
}
