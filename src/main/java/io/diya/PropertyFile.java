package io.diya;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

@Mojo(name="wcpAppend")
public class PropertyFile extends AbstractMojo {

    @Parameter(property="basedir")
    private File propDir;

    public void execute() throws MojoExecutionException, MojoFailureException {

        try{
            BufferedWriter bufferedWriter;
            FileWriter fileWriter;

            File dir = propDir;

            if (!dir.exists()){
                throw new MojoExecutionException( "File don't exit: " + dir);
            }

            File file = new File(this.propDir.getPath() + "/connection.properties");

            Scanner scan1 = new Scanner(file);
            Scanner scan2 = new Scanner(file);
            scan1.nextLine();
            scan1.nextLine();

            //starts iterating through URL connections
            while (scan1.hasNextLine()) {
                scan2 = new Scanner(file);
                String line = scan1.nextLine();
                if (line.substring(0, 1).equals("w")) {
                    //Stops from reading the entire property file, ends at last URL connection
                    break;
                } else {
                    String array[] = line.split("\\.", 3);
                    scan2.nextLine();
                    scan2.nextLine();
                    int count = 0;

                    while (scan2.hasNextLine()) {
                        String line2 = scan2.nextLine();

                        String array2[] = line2.split("\\.", 3);

                        if (line2.substring(0, 1).equals("#")) {
                            //Prevents out of bounds array exception from stopping code
                            continue;
                        } else if (array[1].equals(array2[1])) {
                            count++;
                        }
                    }

                    if (count < 2) {
                        //appending the file
                        fileWriter = new FileWriter(file, true);
                        bufferedWriter = new BufferedWriter(fileWriter);
                        PrintWriter pw = new PrintWriter(bufferedWriter);
                        pw.println("urlConnection." + array[1] + ".authenticationType=none");
                        pw.println("urlConnection." + array[1] + ".connectionClassName=oracle.adf.model.connection.url.HttpURLConnection");
                        pw.println("urlConnection." + array[1] + ".realm=");
                        pw.close();
                    }
                }
            }
            scan1.close();
            scan2.close();
        }catch (Exception e){
            System.out.println("Exception: " + e);
        }finally {

        }

    }
}
