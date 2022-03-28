/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing;

import com.smartupds.indexing.common.Resources;
import com.smartupds.indexing.common.Utils;
import com.smartupds.indexing.impl.ArtistIndexGenerator;
import com.smartupds.indexing.impl.PhotoIndexGenerator;
import com.smartupds.indexing.impl.PhotographersIndexGenerator;
import com.smartupds.indexing.impl.RepositoriesIndexGenerator;
import com.smartupds.indexing.impl.WorkIndexGenerator;
import java.io.File;
import java.io.FileNotFoundException;
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
            
//            args = new String [] {"-i" , "-type", "artworks", "-core", "artworks_v5"};
//            args = new String [] {"-i", "-type", "artists", "-core", "artists_v5"};
//            args = new String [] {"-i", "-type", "photographers", "-core", "photographers_v5"};
            args = new String [] {"-i", "-type", "repositories", "-core", "repositories_v5"};
//            args = new String [] {"-i", "-type", "photos", "-core", "photos_v5"};

            CommandLine line = PARSER.parse(options, args);
            handleCommandLine(line);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void createOptionsList(){
        Option index = new Option("i", "index", false,"Flag to index data.");
        Option type = new Option("t", "type", true,"Type to index: -t [type].");
        Option core = new Option("c", "core", true,"Core to add data: -c [core_name].");
        Option weights = new Option("w", "weights", false, "Flag to change weigths.");
        options.addOption(index)
               .addOption(type)
               .addOption(core)
               .addOption(weights);
    }

    private static void handleCommandLine(CommandLine line) throws IOException{
       
        if(line.hasOption("i") && line.hasOption("type")){
             Logger.getLogger(Main.class.getName()).log(Level.INFO, "Indexing Processing Started");
            if(line.hasOption("core"))
                Resources.setSolrCore(line.getOptionValue("core"));
            /*Index Resources for artists*/
            if (line.getOptionValue("t").equals("artists"))
                ArtistIndexGenerator.create(new File(Resources.CONFIGURATION_FILE)).indexResources(Resources.SOLR_CORE);

            /*Index Resources for work*/
            if (line.getOptionValue("t").equals("artworks"))
                WorkIndexGenerator.create(new File(Resources.CONFIGURATION_FILE)).indexResources(Resources.SOLR_CORE);

            /*Index Resources for photos*/
            if (line.getOptionValue("t").equals("photos"))
                PhotoIndexGenerator.create(new File(Resources.CONFIGURATION_FILE)).indexResources(Resources.SOLR_CORE);
 
            /*Index Resources for photographers*/
            if (line.getOptionValue("t").equals("photographers"))
                PhotographersIndexGenerator.create(new File(Resources.CONFIGURATION_FILE)).indexResources(Resources.SOLR_CORE);
            
             /*Index Resources for repositories*/
            if (line.getOptionValue("t").equals("repositories"))
                RepositoriesIndexGenerator.create(new File(Resources.CONFIGURATION_FILE)).indexResources(Resources.SOLR_CORE);
                
        } else if(line.hasOption("w")){
             Logger.getLogger(Main.class.getName()).log(Level.INFO, "change weights Processing Started");
            try {
                Utils.change_weigths();
            } catch (FileNotFoundException | org.json.simple.parser.ParseException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
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
