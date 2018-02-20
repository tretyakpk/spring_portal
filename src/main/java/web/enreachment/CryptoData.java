package web.enreachment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.env.Environment;

public class CryptoData {

    private static String purebrosKey;
    private static String vodafoneKey;
    private static String windKey;

    private static String AES_ENCRYPTION_SCHEME = "AES";

    static {
        purebrosKey = "_WinterIsComing_";
        vodafoneKey = "UF3BHH0Pb1d22g85FTg3v0DgYuyGHaWI";
    }

    public static Optional<String> decryptPureBros(String inputBlankSpace) {
        Optional<String> output = Optional.empty();
        try {
            String input = inputBlankSpace.replaceAll(" ", "+");
            SecretKeySpec skSpec = new SecretKeySpec(purebrosKey.getBytes(), AES_ENCRYPTION_SCHEME);
            Cipher c = Cipher.getInstance(AES_ENCRYPTION_SCHEME);
            c.init(Cipher.DECRYPT_MODE, skSpec);
            output = Optional.ofNullable(new String(c.doFinal(Base64.decodeBase64(input.getBytes()))).trim());
        } catch (Exception e) {

        }
        return output;
    }

    public static String getMsisdn(HttpServletRequest request, Environment env) {
        if (env.getProperty("carrier").equals("vodafone") && request.getHeader("SM_MS_CR_AGR5") != null)
            return decryptVodafone(request.getHeader("SM_MS_CR_AGR5")).get()
                    .split("_")[1].replaceAll("[^\\d]", "");
        else if (env.getProperty("carrier").equals("wind") && !request.getParameter("m").isEmpty())
            return decryptM(decryptPureBros(request.getParameter("m")).get()
                    .split("-")[0]);
        else
            return "";
    }

    private static Optional<String> decryptVodafone(String inputBlankSpace) {
        Optional<String> output = Optional.empty();
        try {
            String input = inputBlankSpace.replaceAll(" ", "+");
            SecretKeySpec skSpec = new SecretKeySpec( vodafoneKey.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, skSpec);
            output = Optional.ofNullable(new String(c.doFinal(Base64.decodeBase64(input.getBytes()))).trim());
        } catch (Exception e) {
//            log.warn("Cannot decrypt msisdn: msisdn [{}], vdfKey [{}] ", inputBlankSpace, vodafoneKey);
        }
        return output;
    }

    private static String decryptM(String m) throws NoSuchElementException {
        String decripted = "";
        try {
            decripted = decryptM(getKey(), m);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalArgumentException e) {
            renewKey();
            String renewedKey = getKey();
            try {
                decripted = decryptM(renewedKey, m);
            } catch (Exception e1) {
//                log.error("Cannot Decrypt m [{}], renewedKey [{}]", m, renewedKey, e1);
                throw new NoSuchElementException("Cannot Decrypt m [" + m + "]. ");
            }
        } catch (Exception e2) {
//            log.error("Cannot Decrypt m [{}]. ", m, e2);
            throw new NoSuchElementException("Cannot Decrypt m [" + m + "]. ");
        }
        return decripted;
    }

    private static String decryptM(String key, String m) throws InvalidKeyException, IllegalBlockSizeException,
            NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException {
        String msisdn = "";

        byte[] keyHex = key.getBytes();
        String cipherAlgorithm = "DES";

        try {
            Cipher cipher = Cipher.getInstance("DES");

            SecretKeySpec skeySpec = new SecretKeySpec(keyHex, cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            Base64 dec = new Base64();
            m = m.replaceAll(" ", "+");
            byte[] plainBytes = cipher.doFinal(dec.decode(m.getBytes()));
            msisdn = new String(plainBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("NoSuchAlgorithmException msisdn[" + m + "], key[" + key + "]");
        } catch (NoSuchPaddingException e) {
            throw new NoSuchPaddingException("NoSuchPaddingException msisdn[" + m + "], key[" + key + "]");
        } catch (BadPaddingException e) {
            throw new BadPaddingException("BadPaddingException msisdn[" + m + "], key[" + key + "]");
        }

        return msisdn;
    }

    private static void renewKey() {
        try {
            ArrayList<String> w = getKeyFromWind();
            windKey = w.get(0);
        } catch (IOException e) {}
    }

    private static String getKey() {
        if(windKey == null || windKey.isEmpty())
            renewKey();
        return windKey;
    }

    private static ArrayList<String> getKeyFromWind() throws IOException {

        URLConnection con = new URL("http://wap.purecontent.it/windhub/paz?i=4&p=iframepwd&u=iframepb").openConnection();
        con.setConnectTimeout(10000);

        BufferedReader    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        ArrayList<String> ret = new ArrayList<String>();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            ret.add(inputLine);
        }
        in.close();
        return ret;
    }

}