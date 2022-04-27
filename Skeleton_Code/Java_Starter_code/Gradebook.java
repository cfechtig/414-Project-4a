import java.util.Map;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonSerializable.Base;

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
  // {studentName}
  public Set<String> students;
  // [studentName]
  public ArrayList<String> studentNames;
  // {assignmentName: (assignmentName, assignmentPoints, assignmentWeight)}
  public Hashtable<String, Assignment> assignments;
  // [assignmentName]
  public ArrayList<String> assignmentNames;
  // {studentName: {assignmentName: grade}}
  public Hashtable<String, Hashtable<String, Integer>> s_grades;
  // {assignmentName: {studentName: grade}}
  public Hashtable<String, Hashtable<String, Integer>> a_grades;

  /* Create a new gradebook */
  public Gradebook() {
    students = new HashSet<String>();
    studentNames = new ArrayList<String>();
    assignments = new Hashtable<String, Assignment>();
    assignmentNames = new ArrayList<String>();
    s_grades = new Hashtable<String, Hashtable<String, Integer>>();
    a_grades = new Hashtable<String, Hashtable<String, Integer>>();
  }

  public static byte[] encrypt(String plainText, SecretKey key) throws Exception {
    try {
      byte[] clean = plainText.getBytes();

      // Generating IV.
      int ivSize = 16;
      byte[] iv = new byte[ivSize];
      SecureRandom random = new SecureRandom();
      random.nextBytes(iv);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
      // Encrypt.
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encrypted = cipher.doFinal(clean);

      // Combine IV and encrypted part.
      byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
      System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
      System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

      return encryptedIVAndText;
    } catch (Exception e) {
      System.out.println("FAIL");
    }
    return null;
  }



  public static String decrypt(byte[] encryptedIvTextBytes, SecretKey key) throws Exception {
    try { 
      int ivSize = 16;
  
      // Extract IV.
      byte[] iv = new byte[ivSize];
      System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
  
      // Extract encrypted part.
      int encryptedSize = encryptedIvTextBytes.length - ivSize;
      byte[] encryptedBytes = new byte[encryptedSize];
      System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);
  
      // Decrypt.
      Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
      cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);
  
      return new String(decrypted);
    } catch (Exception e) {
      System.out.println("FAIL2");
      System.out.println(e);
    }
    return null;
  }
 
  public static Gradebook loadFromFile(String filename, String secretKeyHex) {
    String json = null;
    // convert base16 key string to SecretKey
    byte[] decoded = Base64.getDecoder().decode(secretKeyHex);
    SecretKey secretKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");

    try {
      json = decrypt(Files.readAllBytes(Paths.get(filename)), secretKey);
      if (json == null) {
        System.out.println("Decryption failed");
        return null;
      }
    } catch (Exception e) {
      System.out.println("Failed to read to " + filename + ":");
      System.out.println(e);
      return null;
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      return mapper.readValue(json, Gradebook.class);
    } catch (JsonProcessingException e) {
      System.out.println("Failed to map file to Gradebook:");
      System.out.println(e);
      return null;
    }
  }

  public boolean writeToFile(String filename, String secretKeyHex) {
    String json;
    // convert base16 key string to SecretKey
    byte[] decoded = Base64.getDecoder().decode(secretKeyHex);
    SecretKey secretKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      json = mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      System.out.println("Failed to map Gradebook to file:");
      System.out.println(e);
      return false;
    }

    try {
      File f = new File(filename);
      f.delete();
      f.createNewFile();

      Files.write(Paths.get(filename), encrypt(json, secretKey));
    } catch (Exception e) {
      System.out.println("Failed to write to " + filename + ":");
      System.out.println(e);
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
      assignmentNames.add(assignmentName);
    } else {
      throw new Exception("Exception: Add Assignment");
    }
  }

  /* Deletes an assinment from the gradebook */
  public void deleteAssignment(String assignmentName) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z0-9]+$");

    if (assignmentName_p.matcher(assignmentName).find() && 
        assignments.containsKey(assignmentName)) {
      assignments.remove(assignmentName);
      assignmentNames.remove(assignmentName);
      a_grades.remove(assignmentName);
      for (String studentName: s_grades.keySet()) {
        s_grades.get(studentName).remove(assignmentName);
      }
    } else {
      throw new Exception("Exception: Delete Assignment");
    }
  }

  /* Adds a student to the gradebook */
  public void addStudent(String first, String last) throws Exception {
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");

    if (name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        !students.contains(last + ", " + first)) {
      students.add(last + ", " + first);
      studentNames.add(last + ", " + first);
    } else {
      throw new Exception("Exception: Add Student");
    }
  }

  /* Deletes a student from the gradebook */
  public void deleteStudent(String first, String last) throws Exception {
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");

    if (name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        students.contains(last + ", " + first)) {
      students.remove(last + ", " + first);
      studentNames.remove(last + ", " + first);
      s_grades.remove(last + ", " + first);
      for (String assignmentName: a_grades.keySet()) {
        a_grades.get(assignmentName).remove(last + ", " + first);
      }
    } else {
      throw new Exception("Exception: Delete Student");
    }
  }

  /* Adds a grade to the gradebook */
  public void addGrade(String first, String last, String assignmentName, String grade) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z]+$");
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");
    Pattern grade_p = Pattern.compile("^[0-9]+$");

    if (assignmentName_p.matcher(assignmentName).find() && 
        name_p.matcher(first).find() && 
        name_p.matcher(last).find() && 
        grade_p.matcher(grade).find() && 
        students.contains(last + ", " + first) && 
        assignments.containsKey(assignmentName)) {
      String studentName = last + ", " + first;
      // update s_grades
      if (s_grades.containsKey(studentName)) {
        s_grades.get(studentName).put(assignmentName, Integer.valueOf(grade));
      } else {
        s_grades.put(studentName, new Hashtable<String, Integer>());
        s_grades.get(studentName).put(assignmentName, Integer.valueOf(grade));
      }
      // update a_grades
      if (a_grades.containsKey(assignmentName)) {
        a_grades.get(assignmentName).put(studentName, Integer.valueOf(grade));
      } else {
        a_grades.put(assignmentName, new Hashtable<String, Integer>());
        a_grades.get(assignmentName).put(studentName, Integer.valueOf(grade));
      }
    } else {
      throw new Exception("Exception: Add Grade");
    }
  }

  public void printAssignment(String assignmentName, String order) throws Exception {
    Pattern assignmentName_p = Pattern.compile("^[a-zA-Z]+$");
    Pattern order_p = Pattern.compile("^\\-A$|^\\-G$");
    
    if (assignmentName_p.matcher(assignmentName).find() && 
        order_p.matcher(order).find()) {
      Hashtable<String, Integer> assignment = a_grades.get(assignmentName);
      String[][] student_arr = Gradebook.toArray(assignment);
      if (order.equals("-A")) {
        sort_2D(student_arr, 0, true);
      } else {
        sort_2D(student_arr, 1, false);
      }
      for (String[] x: student_arr) {
        System.out.println("(" + x[0] + ", " + x[1] + ")");
      }
    } else {
      throw new Exception("Exception: Print Assignment");
    }
  }

  public void printStudent(String first, String last) throws Exception {
    Pattern name_p = Pattern.compile("^[a-zA-Z]+$");
    
    if (name_p.matcher(first).find() && 
        name_p.matcher(last).find()) {
      String studentName = last + ", " + first;
      Hashtable<String, Integer> student = s_grades.get(studentName);
      for (String assignmentName: assignmentNames) {
        if (student.containsKey(assignmentName))
          System.out.println("(" + assignmentName + ", " + student.get(assignmentName) + ")");
      } 
    } else {
      throw new Exception("Exception: Print Student");
    }
  }

  public void printFinal(String order) throws Exception {
    Pattern order_p = Pattern.compile("^\\-A$|^\\-G$");
    
    if (order_p.matcher(order).find()) {
      Hashtable<String, Double> scores = calulateFinal();
      String[][] student_arr = Gradebook.toArray2(scores);
      if (order.equals("-A")) {
        sort_2D(student_arr, 0, true);
      } else {
        sort_2D(student_arr, 1, false);
      }
      for (String[] x: student_arr) {
        System.out.println("(" + x[0] + ", " + x[1] + ")");
      }
    } else {
      throw new Exception("Exception: Print Final");
    }
  }

  private Hashtable<String, Double> calulateFinal() {
    Hashtable<String, Double> scores = new Hashtable<String, Double>();
    for (String student: students) {
      double finalScore = 0.0;
      for (String assignment: s_grades.get(student).keySet()) {
        finalScore += ((double)s_grades.get(student).get(assignment) / (double)assignments.get(assignment).points) * assignments.get(assignment).weight;
      }

      scores.put(student, finalScore);
    }

    return scores;
  }

  private float sumWeights() {
    float sum = 0;
    for (Assignment a: assignments.values()){
      sum += a.weight;
    }

    return sum;
  }

  private static String[][] toArray(Map<String,Integer> map) {
    if(map == null) {
        return null;
    }
    String[][] result = new String[map.size()][];
    int index = 0;
    for(Map.Entry<String,Integer> e : map.entrySet()) {
        result[index++] = new String[] {e.getKey(),e.getValue().toString()};
    }
    return result;
  }

  private static String[][] toArray2(Map<String,Double> map) {
    if(map == null) {
        return null;
    }
    String[][] result = new String[map.size()][];
    int index = 0;
    for(Map.Entry<String,Double> e : map.entrySet()) {
        result[index++] = new String[] {e.getKey(),e.getValue().toString()};
    }
    return result;
  }

  private static void sort_2D(String[][] arr, int i, boolean reverse) {
    if (reverse) {
      Arrays.sort(arr, (a, b) -> a[i].compareTo(b[i]));
    } else {
      Arrays.sort(arr, (a, b) -> b[i].compareTo(a[i]));
    }
  }

  public String toString() {
    return "";
  }
}
