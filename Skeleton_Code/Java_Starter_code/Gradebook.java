import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

class Grade {
  public String assignment;
  public Integer grade;

  public Grade(String as, Integer g) {
    this.assignment = as;
    this.grade = g;
  }

  public Grade() {}

};

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

  public Assignment() {}

};

public class Gradebook {
  public Set<Assignment> assignments;
  public Set<String> students; /* Student first-last name */
  public Map<String /* Student first-last name */, Grade> grades;

  /* Create a new gradebook */
  public Gradebook() {
    assignments = new HashSet<>();
    students = new HashSet<>();
    grades = new HashMap<>();
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

  /* Adds a student to the gradebook */
  public void addStudent(String first, String last) {
    // TODO: Validate
    
    students.add(first + "-" + last);
  }

  /* Adds an assinment to the gradebook */
  public void addAssignment(String name, int points, float weight) {
    // TODO: Validate

    assignments.add(new Assignment(name, points, Float.valueOf(weight)));
  }

  /* Adds a grade to the gradebook */
  public void addGrade(String assignment, String first, String last, int grade) {
    String firstlast = first + "-" + last;

    // TODO: Validate

    if (!assignments.contains(assignment))
      return;
    if (!students.contains(firstlast))
      return;

    grades.put(firstlast, new Grade(assignment, grade));
  }

  public String toString() {
    return "";
  }
}
