import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class ProjectWalker {
    public static void main(String[] args) throws IOException {
        final Path projectDir = Paths.get(""); // Set your project directory here

        Files.walkFileTree(projectDir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Exclude files in .git directory and build directory
                        if (!file.getFileName().toString().equals(".git") &&
                            !file.startsWith(projectDir.resolve("build"))) {
                            System.out.println("File: " + file);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        // Exclude .git directory and build directory
                        if (dir.getFileName().toString().equals(".git") ||
                            dir.equals(projectDir.resolve("build"))) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
    }
}