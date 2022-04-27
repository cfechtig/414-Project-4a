import java.io.File;
import java.util.regex.Pattern;
import java.util.Base64;
import java.util.regex.Matcher;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;


/**
 * Initialize gradebook with specified name and generate a key.
 */
public class setup {

  private static boolean verify_filename(String filename) {
    // add '.json' requirement?
    Pattern p = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
    if (p.matcher(filename).find()) {
      File tempfile = new File(filename);
      // file SHOULD NOT exist
      if (!tempfile.exists()) {
        return true;
      }
    } 
    return false;
  }

  public static String generateAESKeyHexString() throws NoSuchAlgorithmException {
    // generate key
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128); // for example
    SecretKey secretKey = keyGen.generateKey();
    // key to base64 string
    String encoded64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    // base64 string to base16 string
    byte[] decoded = Base64.getDecoder().decode(encoded64Key);
    String b = new BigInteger(1, decoded).toString(16);
    return b;
  }

  public static void main(String[] args) throws NoSuchAlgorithmException {
    if (args.length == 2 &&
        args[0].equals("-N") &&
        verify_filename(args[1])) {
      String secretKeyHex = generateAESKeyHexString();

      Gradebook gb = new Gradebook();
      gb.writeToFile(args[1], secretKeyHex);
      System.out.println("Key is: " + secretKeyHex);
    } else {
      System.out.println("invalid");
      System.exit(255);
    }
  }

}
