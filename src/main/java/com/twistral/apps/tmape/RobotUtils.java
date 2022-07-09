package com.twistral.apps.tmape;


import com.twistral.utils.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.concurrent.*;


public class RobotUtils {

    public static Runnable getRunnable(final String directory, final String outputDirectory,
                                       final long timeForAudacity, final long timeForEnter, int folderFileLimit) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    exportAudacityProjects(directory, outputDirectory, timeForAudacity, timeForEnter, folderFileLimit);
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        };
    }


    private static void exportAudacityProjects(String directory, String outputDirectory, long timeForAudacity, long timeForEnter, int folderFileLimit) throws Exception {
        if(!isWindows()) throw new RuntimeException("This app only works for Windows...");
        Runtime runtime = Runtime.getRuntime();
        Robot keyboard = new Robot();

        File[] allFiles = new File(directory).listFiles();
        if(allFiles == null) throw new RuntimeException("Folder not found for this directory: " + directory);

        int fileCount = allFiles.length;

        HashSet<Integer> hsFolderIDs = new HashSet<>();

        for(int i = 0; i < fileCount; i++){
            File file = allFiles[i];

            if(i == 0){ // first file
                // open file and don't export it
                openAndWait(runtime, file, timeForAudacity);
            }
            else if(i == fileCount - 1){ // last file
                // open last file => export it => close it => export the first file that we opened and didnt export => DONT CLOSE IT, KEEP IT OPEN
                openAndWait(runtime, file, timeForAudacity);
                exportAndWait(keyboard, timeForEnter);
                closeAndWait(keyboard, timeForEnter);
                exportAndWait(keyboard, timeForEnter);
            }
            else{ // any file in the middle
                // open a file => export it => close it
                openAndWait(runtime, file, timeForAudacity);
                exportAndWait(keyboard, timeForEnter);
                closeAndWait(keyboard, timeForEnter);
            }

            if(fileCount <= folderFileLimit) continue; // dont go below because you dont need multiple folders

            /* check if current amount of files are above the limit, if so move them into a new
            folder that you create in the output directory */
            File[] outputFiles = new File(outputDirectory).listFiles();
            int oggFileCount = outputFiles.length;
            if(oggFileCount >= folderFileLimit){
                // create a new folder for these .ogg's
                int randomFolderID = (int) (Math.random() * 99999999);
                while(hsFolderIDs.contains(randomFolderID)){
                    randomFolderID = (int) (Math.random() * 99999999);
                }
                hsFolderIDs.add(randomFolderID);
                String newFolderName = "\\allCreatedFolders\\soundChunk" + String.valueOf(randomFolderID);
                newFolderName = outputDirectory.substring(0,outputDirectory.lastIndexOf("\\")) + newFolderName;
                File newFolder = new File(newFolderName);
                boolean newFolderCreated = newFolder.mkdirs();
                if(!newFolderCreated) throw new RuntimeException("New folder couldn't be created...");

                // take every .ogg file from the output folder and put it in the folder you created
                for(int j = 0; j < oggFileCount; j++){
                    File oggFile = outputFiles[j];
                    String oggFileDir = oggFile.getAbsolutePath();
                    String newOggDir = newFolderName + "\\" + oggFileDir.substring( oggFileDir.lastIndexOf("\\") + 2 );

                    Path temp = Files.move( Paths.get(oggFileDir), Paths.get(newOggDir) );
                    if(temp == null) throw new RuntimeException("This file couldn't be loaded: " + oggFile.getAbsolutePath());
                }
            }

        }

    }


    private static boolean isWindows(){ return System.getProperty("os.name").startsWith("Windows"); }


    private static void openAndWait(Runtime runtime, File file, long timeForAudacity) throws Exception {
        tryToOpenFile(runtime, file.getAbsolutePath());
        Thread.sleep(timeForAudacity);
    }

    private static void exportAndWait(Robot keyboard, long timeForEnter) throws InterruptedException {
        pressCTRL_SHIFT_L(keyboard);
        Thread.sleep(timeForEnter);
        pressENTER(keyboard);
        Thread.sleep(timeForEnter);
        pressENTER(keyboard);
        Thread.sleep(timeForEnter);
    }


    private static void closeAndWait(Robot keyboard, long timeForEnter) throws InterruptedException {
        pressCTRL_W(keyboard);
        Thread.sleep(timeForEnter);
    }


    private static void pressENTER(Robot keyboard){
        keyboard.keyPress(KeyEvent.VK_ENTER);
        keyboard.keyRelease(KeyEvent.VK_ENTER);
    }

    private static void pressESCAPE(Robot keyboard){
        keyboard.keyPress(KeyEvent.VK_ESCAPE);
        keyboard.keyRelease(KeyEvent.VK_ESCAPE);
    }

    private static void pressCTRL_W(Robot keyboard){
        keyboard.keyPress(KeyEvent.VK_CONTROL);
        keyboard.keyPress(KeyEvent.VK_W);
        keyboard.keyRelease(KeyEvent.VK_CONTROL);
        keyboard.keyRelease(KeyEvent.VK_W);
    }

    private static void pressCTRL_SHIFT_L(Robot keyboard){
        keyboard.keyPress(KeyEvent.VK_CONTROL);
        keyboard.keyPress(KeyEvent.VK_SHIFT);
        keyboard.keyPress(KeyEvent.VK_L);
        keyboard.keyRelease(KeyEvent.VK_CONTROL);
        keyboard.keyRelease(KeyEvent.VK_SHIFT);
        keyboard.keyRelease(KeyEvent.VK_L);
    }

    private static void tryToOpenFile(Runtime runtime, String directory) throws Exception{
        runtime.exec("cmd /c " + "\"" + directory + "\"");
    }



    public static String getEstimatedTimeInMinutesAndSeconds(String directory, long timeForAudacity, long timeForEnter){
        long totalTimeMs = getEstimatedTimeInMiliseconds(directory, timeForAudacity, timeForEnter);
        return (TimeUnit.MILLISECONDS.toMinutes(totalTimeMs)) + " minutes and " + ((TimeUnit.MILLISECONDS.toSeconds(totalTimeMs) % 60)) + " seconds";
    }


    public static long getEstimatedTimeInMiliseconds(String directory, long timeForAudacity, long timeForEnter) {
        File[] allFiles = new File(directory).listFiles();
        if(allFiles == null) throw new RuntimeException("Folder not found for this directory: " + directory);
        int middleFileCount = allFiles.length - 2; // n-2

        // 1 => 1 aud
        // 1 => 1 aud, 7 enter
        // n-2 => 1 aud, 4 enter
        // 2 aud + 7 enter + (n-2) (1 aud + 4 enter)
        return 2 * timeForAudacity + 7 * timeForEnter + middleFileCount * (timeForAudacity + 4 * timeForEnter);
    }


}
