import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		if (args.length < 1) return;
		File inputFile = new File(args[0]);
		if (!inputFile.exists()) return;
		String name = args[0].replaceAll("(?i)\\.zip","_encrypted.zip");
		if (name.equals(args[0])) {
			System.out.println("Only ZIP files allowed!");
			return;
		}
		File outputFile = new File(name);
		if (outputFile.exists()) if (!outputFile.delete()) {
			System.out.println("Encrypted ZIP already exists and couldn't be overwritten");
			return;
		}
		try {
			if (!outputFile.createNewFile()) return;
		} catch (Exception e) {
			System.out.println("Couldn't create the encrypted file");
			return;
		}
		ZipEntry zip;
		String error = "Couldn't open the original ZIP file";
		try (ZipInputStream input = new ZipInputStream(new FileInputStream(inputFile))) {
			error = "Couldn't open the new ZIP file";
			try (ZipOutputStream output = new ZipOutputStream(new FileOutputStream(outputFile))) {
				output.setLevel(9);
				output.setMethod(8);
				int i;
				byte[] bytes = new byte[1024];
				error = "Couldn't read the original ZIP file";
				while ((zip = input.getNextEntry()) != null) {
					if (zip.getName().endsWith(".zip") || zip.getName().endsWith(".ogg")) continue;
					zip = new ZipEntry(zip);
					output.putNextEntry(zip);
					while ((i = input.read(bytes)) >= 0) output.write(bytes,0,i);
					output.closeEntry();
					zip.setCrc(0);
					zip.setSize(0);
				}
				error = "Couldn't write to the new ZIP file";
				output.finish();
				output.flush();
				error = null;
				float time = (System.currentTimeMillis() - start) / 1000f;
				System.out.println("Successfully encrypted the file!");
				System.out.println("Time elapsed: " + time + " seconds");
			} catch (Exception e) {
				System.out.println(error);
				e.printStackTrace(System.out);
				outputFile.delete();
			}
		} catch (Exception e) {
			System.out.println(error);
			e.printStackTrace(System.out);
		}
	}
}