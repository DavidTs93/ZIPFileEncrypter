import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
	public static void main(String[] args) {
		if (args.length < 1) return;
		byte[] bytes = new byte[1024];
		File inputFile = new File(args[0]);
		if (!inputFile.exists()) return;
		File outputFile = new File(args[0].replaceAll("(?i)\\.zip","_encrypted.zip"));
		if (outputFile.exists()) if (!outputFile.delete()) return;
		try {
			if (!outputFile.createNewFile()) return;
			ZipEntry zip;
			try (ZipInputStream input = new ZipInputStream(new FileInputStream(inputFile)); ZipOutputStream output = new ZipOutputStream(new FileOutputStream(outputFile))) {
				output.setLevel(9);
				output.setMethod(8);
				int i;
				while ((zip = input.getNextEntry()) != null) {
					if (zip.getName().endsWith(".zip") || zip.getName().endsWith(".ogg")) continue;
					zip = new ZipEntry(zip);
					output.putNextEntry(zip);
					while ((i = input.read(bytes)) >= 0) output.write(bytes,0,i);
					output.closeEntry();
					zip.setCrc(0);
					zip.setSize(0);
				}
				output.finish();
				output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}