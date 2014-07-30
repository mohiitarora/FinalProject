package project.parser.language.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import com.main.runner.Constants;
import com.main.runner.OutputWriter;

public class ParserUtils implements Constants{
	
	public static ArrayList<String> filesInDirectory(String fileLocation) throws Exception {
		
		ArrayList<String> projects = new ArrayList<String>();
		
		File folder = new File(fileLocation);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile() && validProjectsType(listOfFiles[i].getName())) {
		    	  projects.add(listOfFiles[i].getName());
		    	  OutputWriter.writeOutputToFile("File " + listOfFiles[i].getName());
		      }
		    }
		return projects;
	}
	
	public static Collection<File> getRecursiveFileList(String fileLocation) throws Exception {
		return FileUtils.listFiles(new File(fileLocation), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
	}
	
	private static boolean validProjectsType(String name) {
		if(name.contains("jar")){
			return true;
		}else if(name.contains(".java") || name.contains(".class")){
			return true;
		}else if(name.contains("bz2")){
			return true;
		}
		return false;
	}

	public static void unzipJar(String jarPath) throws IOException {
		File file = new File(jarPath);
		JarFile jar = new JarFile(file);
		// then make those directory on the destination Path
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			String fileName = Constants.tempWorkArea + File.separator + entry.getName();
			File f = new File(fileName);
 
			if (fileName.endsWith("/")) {
				f.mkdirs();
			}
		}
		//now create all files
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			String fileName = Constants.tempWorkArea + File.separator + entry.getName();
			File f = new File(fileName);
 
			if (!fileName.endsWith("/")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);
				// write contents of 'is' to 'fos'
				while (is.available() > 0) {
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}
		}
	}
    
	public static List<File> unTarProject(final File inputFile, final File outputDir) throws Exception {
	    final List<File> untaredFiles = new LinkedList<File>();
	    final InputStream is = new FileInputStream(inputFile); 
	    final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
	    TarArchiveEntry entry = null; 
	    while ((entry = (TarArchiveEntry)debInputStream.getNextEntry()) != null) {
	        final File outputFile = new File(outputDir, entry.getName());
	        if (entry.isDirectory()) {
	            if (!outputFile.exists()) {
	                if (!outputFile.mkdirs()) {
	                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
	                }
	            }
	        } else {
	            final OutputStream outputFileStream = new FileOutputStream(outputFile); 
	            IOUtils.copy(debInputStream, outputFileStream);
	            outputFileStream.close();
	        }
	        untaredFiles.add(outputFile);
	    }
	    debInputStream.close(); 
	    return untaredFiles;
	}
	
	public static void uncompressBz2File(String fileIn) throws Exception {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream in = new BufferedInputStream(fin);
		FileOutputStream out = new FileOutputStream(fileIn.replace(".bz2", ""));
		BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
		final byte[] buffer = new byte[2000];
		int n = 0;
		while (-1 != (n = bzIn.read(buffer))) {
		    out.write(buffer, 0, n);
		}
		out.close();
		bzIn.close();
	}
	
	public static String readFile( File file ) throws IOException {
		String content = new Scanner(file).useDelimiter("\\Z").next();
		return content;
		
	}
	
}
