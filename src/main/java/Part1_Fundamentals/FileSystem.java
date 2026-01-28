package Part1_Fundamentals;

import java.util.*;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * In this exercise, you have to implement a basic filesystem for a new operating system.
 * <p>
 * A filesystem is a tree-based data structure, where each node corresponds to
 * one directory. The root node is the entry point of the filesystem.
 * Each directory can contain multiple subdirectories, as well as multiple files.
 */

public class FileSystem {
    /**
     * Class that represents one file on the filesystem.
     * <p>
     * A file is characterized by a name (e.g., "Hello.txt") and by a
     * size (expressed in bytes).
     */
    static class File {
        private final String name;
        private final int size;

        /**
         * Constructs a file with the given name and size.
         */
        public File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }
    }


    /**
     * Class that represents one directory on the filesystem. A
     * directory is characterized by a name, by its subdirectories,
     * and by the files it stores.
     */
    static class Directory implements Iterable<File> {
        // TODO: Add the member variables you need here
        private final String name;
        private final List<Directory> subdirectories;
        private final List<File> files;

        /**
         * Constructs a new Directory with the given name.
         * The directory is initially empty, with no files or subdirectories.
         *
         * @param name the name of the directory
         */
        public Directory(String name) {
            // TODO: constructor
            this.name = name;
            this.subdirectories = new ArrayList<>();
            this.files = new ArrayList<>();
        }

        /**
         * Returns the name of this directory.
         */
        public String getName() { return name; }

        /**
         * Add a new file to this directory.
         *
         * @param file the file to be added
         */
        public void addFile(File file) {
            // TODO
            files.add(file);
        }

        /**
         * Add a new subdirectory to this directory.
         *
         * @param directory the subdirectory to be added
         */
        public void addDirectory(Directory directory) {
            // TODO
            subdirectories.add(directory);
        }

        public List<File> getAllFiles() {
            List<File> allFiles = new ArrayList<>();
            // add "local" files
            allFiles.addAll(files);
            // add all subfiles
            for (Directory directory : subdirectories) {
                allFiles.addAll(directory.getAllFiles());
            }
            return allFiles;
        }

        /**
         * Returns the total size of all files in this directory and its subdirectories.
         *
         * @return the total size (expressed in bytes)
         */
        public int getTotalSize() {
            // TODO : create a list of all files and subfiles then go through it to get their size
            List<File> allFiles = getAllFiles();
            int total = 0;
            for (File file : allFiles) {
                //System.out.println(file.name+" : "+file.size);
                total += file.size;
            }
            return total;
        }


        /**
         * Returns an iterator over all the files in the Directory,
         * including files in its subdirectories. The order of the files is arbitrary.
         * <p>
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @return the iterator over all the files
         */
        @Override
        public Iterator<File> iterator() { return new OSIterator(this, file -> true); }

        /**
         * Returns an iterator over the files in the Directory that match the given filter,
         * including files in its subdirectories. The order of the files is arbitrary.
         * <p>
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @param filter a predicate to filter the files of interest
         * @return the iterator over the filtered files
         */
        public Iterator<File> iterator(Predicate<File> filter) { return new OSIterator(this, filter); }


        private static class OSIterator implements Iterator<File> {
            private final Stack<File> fileStack;

            public OSIterator(Directory root, Predicate<File> filter) {
                this.fileStack = new Stack<>(); // Stack of file that needs to be filled with all files in directory to infinity and beyond
                List<File> allFiles = root.getAllFiles();
                for (File file : allFiles) {
                    if (filter.test(file)) {
                        fileStack.push(file);
                    }
                }
            }

            @Override
            public boolean hasNext() {
                // if there is still a file in the Stack then there is next
                return !fileStack.isEmpty();
            }

            @Override
            public File next() {
                if (!hasNext()) throw new NoSuchElementException();
                // simply the next file in the Stack
                return fileStack.pop();
            }
        }
    }
}
