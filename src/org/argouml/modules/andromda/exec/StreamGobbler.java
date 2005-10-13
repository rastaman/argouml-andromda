package org.argouml.modules.andromda.exec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * The communication thread/stream with the MavenLauncher.
 * 
 * @author lmaitre
 * @see <a
 *      href="http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html">When
 *      Runtime.exec() won't</a>
 */
public class StreamGobbler extends Thread {

    private InputStream is;

    private OutputStream os;

    public StreamGobbler(InputStream is, OutputStream redirect) {
        this.is = is;
        this.os = redirect;
    }

    public void run() {
        try {
            PrintWriter pw = new PrintWriter(os);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                pw.println(line);
                pw.flush();                    
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

}
