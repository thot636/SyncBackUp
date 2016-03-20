package services;

import java.nio.file.*;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import java.io.IOException;
import java.util.*;

public class CopyAll implements FileVisitor<Path>{
	private final Path source;
    private final Path target;
	private boolean checkJustOne=true;
	
	CopyAll(Path source, Path target) {
            this.source = source;
            this.target = target;
    }
	
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		//terminate if target folder is not empty
		if( target.toFile().listFiles().length != 0 && checkJustOne ){
			System.out.println("Target folder is not empty!!");
			return TERMINATE;
		}
		checkJustOne = false;
		Path newdir = target.resolve(source.relativize(dir));
		CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES };
		try {
                Files.copy(dir, newdir, options);
            } catch (FileAlreadyExistsException x) {
                // ignore
            } catch (IOException x) {
                System.err.format("Unable to create: %s: %s%n", newdir, x);
                return SKIP_SUBTREE;
            }
        return CONTINUE;
	}
	
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING };
		try {
                Files.copy(file, target.resolve(source.relativize(file)), options);
            } catch (IOException x) { 
                System.err.format("Unable to copy: %s: %s%n", source, x);
            }
		return CONTINUE;
	}
	
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            // set up the same modification times for target's folders as for source's folders
            if (exc == null) {
                Path newdir = target.resolve(source.relativize(dir));
                try {
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(newdir, time);
                } catch (IOException x) {
                    System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
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
	
	public static void main(String [] args){
		
		try {
			Path source = Paths.get("C:\\HYW_Data");
			Path dest = Paths.get("C:\\test");
			EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
			CopyAll ca = new CopyAll(source, dest);
			Files.walkFileTree(source, opts, Integer.MAX_VALUE, ca);
		} catch (IOException e){
			System.out.println("IOException !!!");
		}
	
	}
	
}