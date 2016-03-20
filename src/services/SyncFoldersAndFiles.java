package services;

import java.nio.file.*;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import java.io.IOException;
import java.util.*;

/* 
it does not touch folders and files in target which matches names with source  location regardless of data modification and files content.
After that it list the files/folders which are in target location but not in a source location and
it's asking user if he want to delete this files/folders.
*/

public class SyncFoldersAndFiles {

private List <Path>listOfOutstandingItems;

	SyncFoldersAndFiles(Path source, Path target){
		try {
			EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
			CopyFoldersAndFiles cf = new CopyFoldersAndFiles(source, target);
			Files.walkFileTree(source, opts, Integer.MAX_VALUE, cf);
			ListAllOfOutstandingItems lai = new ListAllOfOutstandingItems(source, target);
			Files.walkFileTree(target, opts, Integer.MAX_VALUE, lai);
			
			printOutOutstandigTest();
			
			// to be implemented...
			removeOutstanding(listOfOutstandingItems);
			
			} catch(IOException e){
				System.out.println("IOException !!!");
			}
		
	}
	private void printOutOutstandigTest(){
	;
		for (Path temp : listOfOutstandingItems){
			System.out.println("Outstanding.. "+temp);
		}
	}
	private void removeOutstanding(List<Path>list){
		// to be implmeneted
	}

	// copy all files and folders which don't exist in target
	class CopyFoldersAndFiles extends SimpleFileVisitor<Path>{
		private final Path source;
		private final Path target;
		
		
		
		CopyFoldersAndFiles(Path source, Path target) {
				this.source = source;
				this.target = target;
		}
		
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			
			Path newdir = target.resolve(source.relativize(dir));
			// if folder does not exist in target -> copy it
			if (!(Files.isDirectory(newdir))) {
				CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES };
				try {
						Files.copy(dir, newdir, options);
					} catch (FileAlreadyExistsException x) {
						// ignore
					} catch (IOException x) {
						System.err.format("Unable to create: %s: %s%n", newdir, x);
						return SKIP_SUBTREE;
					}
			}
			return CONTINUE;
		}
		
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			
			CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING };
			Path newFile = target.resolve(source.relativize(file));
			if (!Files.exists(newFile))
			{
				try {
						Files.copy(file, newFile, options);
					} catch (IOException x) { 
						System.err.format("Unable to copy: %s: %s%n", source, x);
					}
				
			}
			return CONTINUE;
		}
		
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
				// fix up modification time of directory when done
				if (exc == null) {
					Path newdir = target.resolve(source.relativize(dir));
					if ((Files.isDirectory(newdir))) {
						try {
							FileTime time = Files.getLastModifiedTime(dir);
							Files.setLastModifiedTime(newdir, time);
						} catch (IOException x) {
							System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
						}
					}
				}
				return CONTINUE;
		}
		
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
				if (exc instanceof FileSystemLoopException) {
					System.err.println("cycle detected: " + file);
				} else {
					System.err.format("Unable to copy: %s: %s%n", file, exc);
				}
				return CONTINUE;
		}
	}
	// list all files/dirs which does not exist in source dir
	class ListAllOfOutstandingItems extends SimpleFileVisitor<Path>{
	
		private final Path source;
		private final Path target;
		
		ListAllOfOutstandingItems(Path source, Path target) {
				this.source = source;
				this.target = target;
				listOfOutstandingItems = new ArrayList<Path>();
		}
			
		public FileVisitResult visitFile(Path file,
									   BasicFileAttributes attr) {
			if (Files.isSymbolicLink(source.resolve(target.relativize(file)))) {
				return CONTINUE;
			} else if (Files.isRegularFile(source.resolve(target.relativize(file)))) {
				
				return CONTINUE;
			} 
			
			listOfOutstandingItems.add(file);
			return CONTINUE;
		}

		public FileVisitResult postVisitDirectory(Path dir,
											  IOException exc) {
												
			if (Files.isDirectory(source.resolve(target.relativize(dir))))
				return CONTINUE;
			
			listOfOutstandingItems.add(dir);
			return CONTINUE;
		}

		// If there is some error accessing
		// the file, let the user know.
		// If you don't override this method
		// and an error occurs, an IOException 
		// is thrown.
		
		public FileVisitResult visitFileFailed(Path file,
										   IOException exc) {
			System.err.println(exc);
			return CONTINUE;
		}
		
		
}
	
	
	public static void main(String [] args){
		
		
			Path source = Paths.get("C:\\1");
			Path dest = Paths.get("C:\\2");
			//EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
			SyncFoldersAndFiles ca = new SyncFoldersAndFiles(source, dest);
			//Files.walkFileTree(source, opts, Integer.MAX_VALUE, ca);
		
			
		
		
	}
	
}

class RemoveOutstandingDirAndFiles{
	
}