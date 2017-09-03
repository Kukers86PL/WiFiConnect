package aprivate.wificonnect;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alek on 03.09.2017.
 */

public class Cache {

    static final int READ_BLOCK_SIZE = 100;
    static final String CACHE_FILE_NAME = "cache.dat";

    static private void writeToCache(Context context, String toWrite) {
        try {
            FileOutputStream fileout =  context.openFileOutput(CACHE_FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(toWrite);
            outputWriter.close();

        } catch (Exception e) {
            // nothing to do
        }
    }

    static private String readFromCache(Context context) {
        try {
            FileInputStream fileIn = context.openFileInput(CACHE_FILE_NAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String fromRead="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                fromRead +=readstring;
            }
            InputRead.close();

            return fromRead;

        } catch (Exception e) {
            return "";
        }
    }

    static public void saveToCache(Context context, String SSID, String Pass) {
        String fromRead = readFromCache(context);
        String toWrite = "";
        String lookForSSID = "*" + SSID + "*";
        int index1 = fromRead.indexOf(lookForSSID);
        if (index1 == -1) {
            toWrite = fromRead + "*" + SSID + "*" + Pass + "*";
        } else {
            if ((fromRead.length() - index1) > 0) {
                int index2 = fromRead.indexOf("*", (index1 + lookForSSID.length()));
                if (index2 != -1) {
                    toWrite = fromRead.substring(0, index1) + "*" + SSID + "*" + Pass + "*" + fromRead.substring(index2, fromRead.length());
                }
            }
        }
        writeToCache(context, toWrite);
    }

    static public String getPassFromCache(Context context, String SSID) {
        String fromRead = readFromCache(context);
        String lookForSSID = "*" + SSID + "*";
        int index1 = fromRead.indexOf(lookForSSID);
        if ((index1 != -1) && ((fromRead.length() - index1 + lookForSSID.length()) > 0)) {
            int index2 = fromRead.indexOf("*", index1 + lookForSSID.length());
            if (index2 != -1) {
                return fromRead.substring((index1 + lookForSSID.length()), index2);
            }
        }
        return "";
    }
}
