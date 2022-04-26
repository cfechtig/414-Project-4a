import java.util.Arrays;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    return actionsArgs;
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

  static final Hashtable<String, String[]> actions = new Hashtable<String, String[]>() {{
    put("-AA", new String[]{"-AN", "-P", "-W"});
    put("-DA", new String[]{"-AN"});
    put("-AS", new String[]{"-FN", "-LN"});
    put("-DS", new String[]{"-FN", "-LN"});
    put("-AG", new String[]{"-FN", "-LN", "-AN", "-G"});
  }};

  private static boolean verify_filename(String filename) {
    // add '.json' requirement?
    Pattern p = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
    return p.matcher(filename).find();
  }

  private static boolean verify_key(String key) {
    Pattern p = Pattern.compile("^([0-9A-Fa-f]{2})+$");
    return p.matcher(key).find();
  }

  private static boolean verify_action(String action) {
    Pattern p = Pattern.compile("^\\-AA$|^\\-DA$|^\\-AS$|^\\-DS$|^\\-AG$");
    return p.matcher(action).find();
  }

  private static boolean parse_actionArgs(String action, String[] actionArgs) {
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
          return false;
        }
      }
      if (assignmentName.length() != 0 &&
          assignmentPoints.length() != 0 &&
          assignmentWeight.length() != 0) {
        okay
      } else {
        not okay
      }

    } else if (action.equals("-DA")) {
      // verify delete assignment
      

    } else if (action.equals("-AS")) {
      // verify add student

      
    } else if (action.equals("-DS")) {
      // verify delete student

      
    } else if (action.equals("-AG")) {
      // verify add grade

      
    } else {
      // if none of the above
      System.out.println("This should not be possible...");
    }
    
    return false;
  }

  /* parses the cmdline to keep main method simplified */
  private static ParsedObject parse_cmdline(String[] args) {
    ParsedObject po = new ParsedObject();

    System.out.println("\nNumber Of Arguments Passed: " + args.length);
    System.out.println("----Following Are The Command Line Arguments Passed----");
    for(int counter=0; counter < args.length; counter++)
      System.out.println("args[" + counter + "]: " + args[counter]);

    if (args.length>5 &&
        args[0].equals("-N") &&
        verify_filename(args[1]) &&
        args[2].equals("-K") &&
        verify_key(args[3]) &&
        verify_action(args[4]) &&
        verify_actionArgs(args[4], Arrays.copyOfRange(args, 5, args.length))) {
      po.set_vals(args[1], args[3], args[4], Arrays.copyOfRange(args, 5, args.length));
    }

    return po;
  }

  public static void main(String[] args) {
    ParsedObject po = parse_cmdline(args);

    if(po.get_legit()) {
      Gradebook gb = Gradebook.loadFromFile(po.get_filename());

      try {
        if (po.get_action().equals("AA")) {
          gb.addAssignment(assignmentName, points, weight);
        } else if (po.get_action().equals("DA")) {
          gb.deleteAssignment(assignmentName);
        } else if (po.get_action().equals("AS")) {
          gb.addStudent(first, last);
        } else if (po.get_action().equals("DS")) {
          gb.deleteStudent(first, last);
        } else if (po.get_action().equals("AG")) {
          gb.addGrade(assignmentName, first, last, grade);
        } else {
          System.out.println("This should not be possible...");
          System.out.println("Invalid");
          System.exit(255);
        }
      } catch(Exception E) {
        System.out.println("Invalid");
        System.exit(255);
      }
    } else {
      System.out.println("Invalid");
      System.exit(255);
    }
  }
}
