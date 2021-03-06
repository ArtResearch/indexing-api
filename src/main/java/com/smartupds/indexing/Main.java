/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing;

import com.smartupds.indexing.api.IndexGenerator;
import com.smartupds.indexing.common.Resources;
import com.smartupds.indexing.impl.ArtistIndexGenerator;
import com.smartupds.indexing.impl.PhotoIndexGenerator;
import com.smartupds.indexing.impl.PhotographersIndexGenerator;
import com.smartupds.indexing.impl.RepositoriesIndexGenerator;
import com.smartupds.indexing.impl.WorkIndexGenerator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */

public class Main {
    
    static final CommandLineParser PARSER = new DefaultParser();
    static Options options = new Options();
    
    public static void main(String[] args) throws IOException{
        try {
            createOptionsList();
            // args = new String [] {"-u" , "-type", "artworks", "-core", "artworks_v8Test"};
            // args = new String [] {"-d" , "-type", "artworks", "-core", "artworks_v6"};
//            args = new String [] {"-d", "-i", "-type", "artists", "-core", "artists_v5"};
//            args = new String [] {"-d", "-i", "-type", "photographers", "-core", "photographers_v5"};
//            args = new String [] {"-d", "-i", "-type", "repositories", "-core", "repositories_v5"};
//            args = new String [] {"-d", "-i", "-type", "photos", "-core", "photos_v5"};
//            args = new String [] {"-i", "-type", "photographers", "-core", "photographers_v5"};
//            args = new String [] {"-i", "-type", "repositories", "-core", "repositories_v5"};
        //    args = new String [] {"-i", "-type", "merge", "-core", "photos_v5"};
            CommandLine line = PARSER.parse(options, args);
            handleCommandLine(line);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void createOptionsList(){
        Option index = new Option("i", "index", false,"Flag to index data.");
        Option download = new Option("d", "download", false,"Flag to download data.");
        Option upload = new Option("u", "upload", false,"Flag to upload data.");
        Option type = new Option("t", "type", true,"Type to index: -t [type].");
        Option core = new Option("c", "core", true,"Core to add data: -c [coreName].");
        
        type.setRequired(true);
        
        options.addOption(index)
                .addOption(download)
                .addOption(upload)
                .addOption(type)
                .addOption(core);
    }

    private static void handleCommandLine(CommandLine line) throws IOException{
       
        if(line.hasOption("type")){
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Indexing Processing Started");
            IndexGenerator indexGenerator = null;
            /*Index Resources for artists*/
            if (line.getOptionValue("t").equals("artists")) 
                indexGenerator = ArtistIndexGenerator.create(new File(Resources.CONFIGURATION_FILE));                
            /*Index Resources for work*/
            if (line.getOptionValue("t").equals("artworks"))
                indexGenerator = WorkIndexGenerator.create(new File(Resources.CONFIGURATION_FILE));
            /*Index Resources for photos*/
            if (line.getOptionValue("t").equals("photos"))
                indexGenerator = PhotoIndexGenerator.create(new File(Resources.CONFIGURATION_FILE));
            /*Index Resources for photographers*/
            if (line.getOptionValue("t").equals("photographers"))
                indexGenerator = PhotographersIndexGenerator.create(new File(Resources.CONFIGURATION_FILE));
             /*Index Resources for repositories*/
            if (line.getOptionValue("t").equals("repositories"))
                indexGenerator = RepositoriesIndexGenerator.create(new File(Resources.CONFIGURATION_FILE));
            
            if (indexGenerator!=null){
                // Core
                if (line.hasOption("c"))
                    Resources.setSolrCore(line.getOptionValue("core"));
                
                // Process
                if (line.hasOption("d"))
                    indexGenerator.downloadResources();
                else if (line.hasOption("u")){
                    Resources.setSolrServer();
                    indexGenerator.uploadResources();
                } else if (line.hasOption("i")){
                    Resources.setSolrServer();
                    indexGenerator.indexResources();
                }
            }
            
        } else {
            printOptions();
        }
       Logger.getLogger(Main.class.getName()).log(Level.INFO, "Indexing Processing Completed");
    }
    
    private static void printOptions(){
        String header = "\nChoose from options below:\n\n";
        String footer = "\nParsing failed.";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar indexing-api-0.1-assembly.jar", header, options, footer, true);
    }
}
