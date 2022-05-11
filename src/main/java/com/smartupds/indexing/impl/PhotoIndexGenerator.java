
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.impl;

import com.smartupds.indexing.api.IndexGenerator;
import com.smartupds.indexing.common.Resources;
import com.smartupds.indexing.common.Utils;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public class PhotoIndexGenerator  implements IndexGenerator{
    private File configurationFile;

    public PhotoIndexGenerator(File configurationFile){
        this.configurationFile = configurationFile;
    }
    
    @Override
    public void downloadResources() {
        long start = System.nanoTime();
        Logger.getLogger(WorkIndexGenerator.class.getName()).log(Level.INFO,"START: Indexing photographs");
        Utils.downloadSubjectFieldsDir(this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_CONSTRUCT, Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON, Resources.PHOTOS);
        Utils.clean(Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON, Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_CLEAN);
        Utils.merge(Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_CLEAN, Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED);
        Utils.split(Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED);
        long stop = System.nanoTime();
        long time = TimeUnit.SECONDS.convert(stop - start, TimeUnit.NANOSECONDS);
        Logger.getLogger(ArtistIndexGenerator.class.getName()).log(Level.INFO, "FINISH: Indexing photographs in {0} secs", time);
    }
            
    @Override
    public void uploadResources() {
        long start = System.nanoTime();
        Logger.getLogger(RepositoriesIndexGenerator.class.getName()).log(Level.INFO, "START: Updating photographs index");
        Utils.updateSolrIndex(Resources.TYPE_PHOTO,Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED_SPLIT, Resources.SOLR_CORE);
        long stop = System.nanoTime();
        long time = TimeUnit.SECONDS.convert(stop - start, TimeUnit.NANOSECONDS);
        Logger.getLogger(RepositoriesIndexGenerator.class.getName()).log(Level.INFO, "FINISH: Indexing photographs completed in {0} secs", time);
    }

    @Override
    public void indexResources() {
        this.downloadResources();
        this.uploadResources();
    }

    public static PhotoIndexGenerator create(File configurationfile){
        return new PhotoIndexGenerator(configurationfile);
    }
}
