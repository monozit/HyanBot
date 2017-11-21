import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileSort {
	public static File[] sort(String dir){
		File directory = new File("data/image/" + dir);
		File[] files = directory.listFiles();
		Arrays.sort(files, new Comparator<File>(){
			@Override
			public int compare(File src, File target) {
				String src_str = src.getName().replaceAll("[^0-9]", "");
				String tar_str = target.getName().replaceAll("[^0-9]", "");
				return (new Integer(src_str)).compareTo(new Integer(tar_str));
			}
		});
		
		return files;
	}
}
