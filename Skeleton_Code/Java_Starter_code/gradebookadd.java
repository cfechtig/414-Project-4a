import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class ParsedObject {
  private String filename, key, action;
  private String[] actionsArgs;
  private boolean legit;

  public ParsedObject() {
    filename = null;
    key = null;
    action = null;
    actionsArgs = null;
    legit = false;
  }

  public void set_vals(String filename, String key, String action, String[]actionsArgs) {
    this.filename = filename;
    this.key = key;
    this.action = action;
    this.actionsArgs = actionsArgs;
    legit = true;
  }

  public String get_filename(){
    return filename;
  }

  public String get_key(){
    return key;
  }

  public String get_action(){
    return action;
  }

  public String[] get_actionArgs(){
    return actionsArgs.clone();
  }

  public boolean get_legit(){
    return legit;
  }
}

/**
 * Allows the user to add a new student or assignment to a gradebook,
 * or add a grade for an existing student and existing assignment
 */
public class gradebookadd {

  private static boolean verify_filename(String filename) {
    Pattern p = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
    if (p.matcher(filename).find()) {
      File tempfile = new File(filename);
      // file SHOULD exist
      if (tempfile.exists()) {
        return true;
      }
    } 
    return false;
  }

  private static boolean verify_key(String key) {
    Pattern p = Pattern.compile("^([0-9A-Fa-f]{2})+$");
    return p.matcher(key).find();
  }

  private static boolean verify_action(String action) {
    Pattern p = Pattern.compile("^\\-AA$|^\\-DA$|^\\-AS$|^\\-DS$|^\\-AG$");
    return p.matcher(action).find();
  }

  private static String[] parse_actionArgs(String action, String[] actionArgs) {
    // check that actionArgs contains even number of arguments
    if (actionArgs.length%2 != 0) return null;
    if (action.equals("-AA")) {
      // verify add assignment
      String assignmentName = "", assignmentPoints = "", assignmentWeight = "";
      for (int i = 0; i < actionArgs.length; i += 2) {
        if (actionArgs[i].equals("-AN")) {
          assignmentName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-P")) {
          assignmentPoints = actionArgs[i+1];
        } else if (actionArgs[i].equals("-W")) {
          assignmentWeight = actionArgs[i+1];
        } else {
          return null;
        }
      }
      if (assignmentName.length() != 0 &&
          assignmentPoints.length() != 0 &&
          assignmentWeight.length() != 0) {
        return new String[]{assignmentName, assignmentPoints, assignmentWeight};
      } else {
        return null;
      }

    } else if (action.equals("-DA")) {
      // verify delete assignment
      String assignmentName = "";
      for (int i = 0; i < actionArgs.length; i += 2) {
        if (actionArgs[i].equals("-AN")) {
          assignmentName = actionArgs[i+1];
        } else {
          return null;
        }
      }
      if (assignmentName.length() != 0) {
        return new String[]{assignmentName};
      } else {
        return null;
      }

    } else if (action.equals("-AS")) {
      // verify add student
      String firstName = "", lastName = "";
      for (int i = 0; i < actionArgs.length; i += 2) {
        if (actionArgs[i].equals("-FN")) {
          firstName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-LN")) {
          lastName = actionArgs[i+1];
        } else {
          return null;
        }
      }
      if (firstName.length() != 0 &&
          lastName.length() != 0) {
        return new String[]{firstName, lastName};
      } else {
        return null;
      }
      
    } else if (action.equals("-DS")) {
      // verify delete student
      String firstName = "", lastName = "";
      for (int i = 0; i < actionArgs.length; i += 2) {
        if (actionArgs[i].equals("-FN")) {
          firstName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-LN")) {
          lastName = actionArgs[i+1];
        } else {
          return null;
        }
      }
      if (firstName.length() != 0 &&
          lastName.length() != 0) {
        return new String[]{firstName, lastName};
      } else {
        return null;
      }
      
    } else if (action.equals("-AG")) {
      // verify add grade
      String firstName = "", lastName = "", assignmentName = "", grade = "";
      for (int i = 0; i < actionArgs.length; i += 2) {
        if (actionArgs[i].equals("-FN")) {
          firstName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-LN")) {
          lastName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-AN")) {
          assignmentName = actionArgs[i+1];
        } else if (actionArgs[i].equals("-G")) {
          grade = actionArgs[i+1];
        } else {
          return null;
        }
      }
      if (firstName.length() != 0 &&
          lastName.length() != 0 &&
          assignmentName.length() != 0 &&
          grade.length() != 0) {
        return new String[]{firstName, lastName, assignmentName, grade};
      } else {
        return null;
      }
      
    } else {
      // if none of the above
      System.out.println("Invalid action");
    }
    
    return null;
  }

  /* parses the cmdline to keep main method simplified */
  private static ParsedObject parse_cmdline(String[] args) {
    ParsedObject po = new ParsedObject();

    // System.out.println("\nNumber Of Arguments Passed: " + args.length);
    // System.out.println("----Following Are The Command Line Arguments Passed----");
    // for(int counter=0; counter < args.length; counter++)
    //   System.out.println("args[" + counter + "]: " + args[counter]);
    // System.out.println();

    if (args.length>5 &&
        args[0].equals("-N") &&
        verify_filename(args[1]) &&
        args[2].equals("-K") &&
        verify_key(args[3]) &&
        verify_action(args[4])) {
      String[] actionArgs = parse_actionArgs(args[4], Arrays.copyOfRange(args, 5, args.length));
      if (actionArgs != null) {
        po.set_vals(args[1], args[3], args[4], actionArgs);
      }
    }

    return po;
  }

  public static void main(String[] args) {
    ParsedObject po = parse_cmdline(args);

    // System.out.println("legit: " + po.get_legit());
    // System.out.println("filename: " + po.get_filename());
    // System.out.println("key: " + po.get_key());
    // System.out.println("action: " + po.get_action());
    // System.out.println("a_rgs: " + Arrays.toString(po.get_actionArgs()));
    // System.out.println();

    if(po.get_legit()) {
      try {
        Gradebook gb = Gradebook.loadFromFile(po.get_filename(), po.get_key());
        if (gb==null) {
          System.out.println("gb is NULL!!!");
          System.out.println();
        }
        String[] a_args = po.get_actionArgs();

        if (po.get_action().equals("-AA")) {
          gb.addAssignment(a_args[0], a_args[1], a_args[2]);
        } else if (po.get_action().equals("-DA")) {
          gb.deleteAssignment(a_args[0]);
        } else if (po.get_action().equals("-AS")) {
          gb.addStudent(a_args[0], a_args[1]);
        } else if (po.get_action().equals("-DS")) {
          gb.deleteStudent(a_args[0], a_args[1]);
        } else if (po.get_action().equals("-AG")) {
          gb.addGrade(a_args[0], a_args[1], a_args[2], a_args[3]);
        } else {
          System.out.println("This should not be possible...");
          System.out.println("Invalid");
          System.exit(255);
        }

        gb.writeToFile(po.get_filename(), po.get_key());
      } catch(Exception E) {
        System.out.println(E);
        System.out.println("Invalid");
        System.exit(255);
      }
    } else {
      System.out.println("Parse Failed");
      System.out.println("Invalid");
      System.exit(255);
    }
  }

}
