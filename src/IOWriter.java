import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOWriter {
	public static void outWriter(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		int len = 0;

		while ((len = in.read(buf)) > 0)
			out.write(buf, 0, len);
	}
}
