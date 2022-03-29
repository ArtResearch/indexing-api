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
public class ArtistIndexGenerator implements IndexGenerator {

    private File configurationFile;

    public ArtistIndexGenerator(File configurationFile) {
        this.configurationFile = configurationFile;
    }

    @Override
    public void indexResources(String core_name) {
        long start = System.nanoTime();
        Logger.getLogger(ArtistIndexGenerator.class.getName()).log(Level.INFO, "START: Indexing Artists");

        Utils.downloadSubjectFields(Resources.CATEGORY_PERSON_INFO,Resources.TYPE_ARTIST,
                this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON);
        Utils.downloadSubjectFields(Resources.CATEGORY_PERSON_RELATIONSHIPS, Resources.TYPE_ARTIST,
                this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON);
//        Utils.merge(Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON, Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED);        
//        Utils.split(Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED);
        Utils.downloadSubjectFieldsDir(this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT, Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON, Resources.ARTISTS);
//        Utils.updateSolrIndex(Resources.TYPE_ARTIST, Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_SPLIT, Resources.SOLR_CORE);
//        Utils.updateSolrIndex(Resources.TYPE_ARTIST, Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_FIXED, Resources.SOLR_CORE);
//        Utils.updateSolrIndex(Resources.TYPE_ARTIST, Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED_SPLIT, Resources.SOLR_ARTISTS_CORE);

        long stop = System.nanoTime();
        long time = TimeUnit.SECONDS.convert(stop - start, TimeUnit.NANOSECONDS);
        Logger.getLogger(ArtistIndexGenerator.class.getName()).log(Level.INFO, "FINISH: Indexing artists in {0} secs", time);
    }

    public static ArtistIndexGenerator create(File configurationfile) {
        return new ArtistIndexGenerator(configurationfile);
    }

    @Override
    public void downloadQueries() {
        Utils.downloadQueries(Resources.CATEGORY_PERSON_INFO, Resources.TYPE_ARTIST,
                this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON, Resources.ARTISTS);
        Utils.downloadQueries(Resources.CATEGORY_PERSON_RELATIONSHIPS, Resources.TYPE_ARTIST,
                this.configurationFile,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT,Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON, Resources.ARTISTS);
    }
}
