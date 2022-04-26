import java.util.Map;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

class Assignment {
  public String name;
  public Integer points;
  public Float weight;

  @Override
  public boolean equals(Object a) {
    if (a == this)
      return true;
    if (!(a instanceof Assignment))
      return false;

    Assignment a1 = (Assignment) a;
    return a1.name.equals(name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  public Assignment(String n, int p, Float w) {
    name = n;
    points = p;
    weight = w;
  }
};

public class Gradebook {
  // {studentName}
  public Set<String> students;
  // {assignmentName: (assignmentName, assignmentPoints, assignmentWeight)}
  public Hashtable<String, Assignment> assignments;
  // {studentName: {assignmentName: grade}}
  public Hashtable<String, Hashtable<String, Integer>> s_grades;
  // {assignmentName: {studentName: grade}}
  public Hashtable<String, Hashtable<String, Integer>> a_grades;

  /* Create a new gradebook */
  public Gradebook() {
    students = new HashSet<String>();
    assignments = new Hashtable<String, Assignment>();
    s_grades = new Hashtable<String, Hashtable<String, Integer>>();
    a_grades = new Hashtable<String, Hashtable<String, Integer>>();
  }

  public static Gradebook loadFromFile(String filename) {
    /*
     * Ensure file exists and readable
     * Verify signature
     * Decrypt
     * Read decrypted JSON string into GradeBook object
     */

    String json = null;
    try {
      File f = new File(filename);

      FileReader fr = new FileReader(f);

      char[] buf = new char[(int) f.length()];
      fr.read(buf);
      fr.close();

      json = new String(buf);
    } catch (Exception e) {
      System.out.println("Failed to read to " + filename + " " + e);
      return null;
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper.readValue(json, Gradebook.class);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public boolean writeToFile(String filename) {
    // Convert this to a JSON
    // Create ObjectMapper object.
    String json;
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      json = mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return false;
    }

    /*
     * TODO:
     * Encrypt the JSON-string
     * Sign the the encrypted data
     * Write output to supplied file
     */

    /*
     * Recreate the file
     */
    try {
      File f = new File(filename);

      f.delete();
      f.createNewFile();

      FileWriter fw = new FileWriter(f);
      fw.write(json);
      fw.close();
    } catch (Exception e) {
      System.out.println("Failed to write to " + filename + " " + e);
      return false;
    }
    return true;
  }

  /* Adds an assinment to the gradebook */
  public void addAssignment(String assignmentName, String points, String weight) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z0-9]+$");
    Pattern points_p = Pattern.compile("^[0-9]+$");
    Pattern weight_p = Pattern.compile("^(?:0*(?:\\.\\d+)?|1(\\.0*)?)$");
    if (assignmentName_p.matcher(assignmentName).find() && 
        points_p.matcher(points).find() && 
        weight_p.matcher(weight).find() && 
        !assignments.containsKey(assignmentName) && 
        sumWeights()+Float.valueOf(weight) <= 1) {
      assignments.put(assignmentName, new Assignment(assignmentName, Integer.valueOf(points), Float.valueOf(weight)));
    } else {
      throw new Exception();
    }
  }

  /* Deletes an assinment from the gradebook */
  public void deleteAssignment(String assignmentName) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z0-9]+$");
    if (assignmentName_p.matcher(assignmentName).find() && 
        assignments.containsKey(assignmentName)) {
      assignments.remove(assignmentName);
    } else {
      throw new Exception();
    }
  }

  /* Adds a student to the gradebook */
  public void addStudent(String first, String last) throws Exception {
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");
    if (name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        !students.contains(last + ", " + first)) {
      students.add(last + ", " + first);
    } else {
      throw new Exception();
    }
  }

  /* Deletes a student from the gradebook */
  public void deleteStudent(String first, String last) throws Exception {
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");
    if (name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        students.contains(last + ", " + first)) {
      students.remove(last + ", " + first);
    } else {
      throw new Exception();
    }
  }

  /* Adds a grade to the gradebook */
  public void addGrade(String assignmentName, String first, String last, String grade) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z]+$");
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");
    Pattern grade_p = Pattern.compile("^[0-9]+$");
    if (assignmentName_p.matcher(assignmentName).find() && 
        name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        grade_p.matcher(grade).find() && 
        students.contains(last + ", " + first) && 
        assignments.containsKey(assignmentName)) {

      String firstlast = last + ", " + first;

      if (s_grades.containsKey(firstlast)) {
        s_grades.get(firstlast).put(assignmentName, Integer.valueOf(grade));
      } else {
        s_grades.put(firstlast, new Hashtable<String, Integer>());
        s_grades.get(firstlast).put(assignmentName, Integer.valueOf(grade));
      }

      if (a_grades.containsKey(assignmentName)) {
        a_grades.get(assignmentName).put(firstlast, Integer.valueOf(grade));
      } else {
        a_grades.put(assignmentName, new Hashtable<String, Integer>());
        a_grades.get(assignmentName).put(firstlast, Integer.valueOf(grade));
      }

    } else {
      throw new Exception();
    }
  }

  public void printAssignment(String assignmentName, String order) {
    
  }

  public void printStudent(String first, String last) {
    
  }

  public void printFinal(String order) {
    
  }

  private float sumWeights() {
    float sum = 0;
    for (Assignment a: assignments.values()){
      sum += a.weight;
    }

    return sum;
  }

  public String toString() {
    return "";
  }
}
