/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public class Resources {    

    public static final String FOLDER_WORKSPACE = "workspace";
    public static final String FOLDER_TERMS_AND_WEIGHTS = "terms_and_weights";
    public static final String FOLDER_OUTPUT = FOLDER_WORKSPACE + "/" + "Output";
    public static final String FOLDER_OUTPUT_INDEXING = FOLDER_OUTPUT + "/" + "Indexing";
    public static final String WORKS = "works";
    public static final String ARTISTS = "artists";
    public static final String PHOTOS = "photos";
    public static final String PHOTOGRAPHERS = "photographers";
    public static final String REPOSITORIES = "repositories";
    public static final String ARTIST_TERM_AND_WEIGHTS = "artist_term_and_weigths.txt";
    public static final String ARTWORKS_TERM_AND_WEIGHTS = "artworks_term_and_weigths.txt";
    public static final String PHOTOS_TERM_AND_WEIGHTS = "photos_term_and_weigths.txt";
    public static final String PHOTOGRAPHERS_TERM_AND_WEIGHTS = "photographers_term_and_weigths.txt";
    public static final String REPOSITORIES_TERM_AND_WEIGHTS = "repositories_term_and_weigths.txt";
    public static final String ARTIST_TERM_AND_WEIGHTS_FILE = FOLDER_TERMS_AND_WEIGHTS + "/" + Resources.ARTIST_TERM_AND_WEIGHTS;
    public static final String ARTWORKS_TERM_AND_WEIGHTS_FILE = FOLDER_TERMS_AND_WEIGHTS + "/" + Resources.ARTWORKS_TERM_AND_WEIGHTS;
    public static final String PHOTOS_TERM_AND_WEIGHTS_FILE = FOLDER_TERMS_AND_WEIGHTS + "/" + Resources.PHOTOS_TERM_AND_WEIGHTS;
    public static final String PHOTOGRAPHERS_TERM_AND_WEIGHTS_FILE = FOLDER_TERMS_AND_WEIGHTS + "/" + Resources.PHOTOGRAPHERS_TERM_AND_WEIGHTS;
    public static final String REPOSITORIES_TERM_AND_WEIGHTS_FILE = FOLDER_TERMS_AND_WEIGHTS + "/" + Resources.REPOSITORIES_TERM_AND_WEIGHTS;
  
    public static final String FOLDER_QUERIES = FOLDER_WORKSPACE + "/"+ "Queries";

    public static final String FOLDER_OUTPUT_INDEXING_WORKS = FOLDER_OUTPUT_INDEXING + "/" + Resources.WORKS;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_CONSTRUCT = FOLDER_OUTPUT_INDEXING_WORKS + "/" + Resources.CONSTRUCT;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_CONSTRUCT_SPLIT = FOLDER_OUTPUT_INDEXING_WORKS_CONSTRUCT + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_JSON = FOLDER_OUTPUT_INDEXING_WORKS + "/" + Resources.JSON;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_JSON_MERGED = FOLDER_OUTPUT_INDEXING_WORKS_JSON + "/" + Resources.MERGED;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_JSON_MERGED_SPLIT = FOLDER_OUTPUT_INDEXING_WORKS_JSON_MERGED + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_JSON_SPLIT = FOLDER_OUTPUT_INDEXING_WORKS_JSON + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_WORKS_JSON_FIXED = FOLDER_OUTPUT_INDEXING_WORKS_JSON + "/" + Resources.FIXED;

    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS = FOLDER_OUTPUT_INDEXING + "/" + Resources.ARTISTS;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT = FOLDER_OUTPUT_INDEXING_ARTISTS + "/" + Resources.CONSTRUCT;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT_SPLIT = FOLDER_OUTPUT_INDEXING_ARTISTS_CONSTRUCT + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_JSON = FOLDER_OUTPUT_INDEXING_ARTISTS + "/" + Resources.JSON;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_SPLIT = FOLDER_OUTPUT_INDEXING_ARTISTS_JSON + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED = FOLDER_OUTPUT_INDEXING_ARTISTS_JSON + "/" + Resources.MERGED;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED_SPLIT = FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_MERGED + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_FIXED = FOLDER_OUTPUT_INDEXING_ARTISTS_JSON + "/" + Resources.FIXED;

    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS = FOLDER_OUTPUT_INDEXING + "/" + Resources.PHOTOS;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_CONSTRUCT = FOLDER_OUTPUT_INDEXING_PHOTOS + "/" + Resources.CONSTRUCT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_CONSTRUCT_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOS_CONSTRUCT + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_JSON = FOLDER_OUTPUT_INDEXING_PHOTOS + "/" + Resources.JSON;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED = FOLDER_OUTPUT_INDEXING_PHOTOS_JSON + "/" + Resources.MERGED;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_MERGED + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOS_JSON + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_FIXED = FOLDER_OUTPUT_INDEXING_PHOTOS_JSON + "/" + Resources.FIXED;

    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS = FOLDER_OUTPUT_INDEXING + "/" + Resources.PHOTOGRAPHERS;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_CONSTRUCT = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS + "/" + Resources.CONSTRUCT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_CONSTRUCT_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_CONSTRUCT + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS + "/" + Resources.JSON;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_MERGED = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON + "/" + Resources.MERGED;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_MERGED_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_MERGED + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_SPLIT = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_FIXED = FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON + "/" + Resources.FIXED;

    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES = FOLDER_OUTPUT_INDEXING + "/" + Resources.REPOSITORIES;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_CONSTRUCT = FOLDER_OUTPUT_INDEXING_REPOSITORIES + "/" + Resources.CONSTRUCT;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_CONSTRUCT_SPLIT = FOLDER_OUTPUT_INDEXING_REPOSITORIES_CONSTRUCT + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON = FOLDER_OUTPUT_INDEXING_REPOSITORIES + "/" + Resources.JSON;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON_MERGED = FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON + "/" + Resources.MERGED;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON_MERGED_SPLIT = FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON_MERGED + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON_SPLIT = FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON + "/" + Resources.SPLIT;
    public static final String FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON_FIXED = FOLDER_OUTPUT_INDEXING_REPOSITORIES_JSON + "/" + Resources.FIXED;

    public static final String CONFIGURATION_FILE = "./src/main/resources/authentication.xml";
    public static final String SOLR_CONFIGURATION_FILE = "./src/main/resources/solr.xml";

    //RESERVED WORDS
    public static final String SELECT = "SELECT";
    public static final String CONSTRUCT = "construct";
    public static final String JSON = "json";
    public static final String SPLIT = "split";
    public static final String MERGED = "merged";
    public static final String FIXED = "fixed";
    public static final String SUBJECT = "subject";
    public static final String QUERY = "query";
    public static final String VALUE = "value";
    public static final String WHERE = "where";
    public static final String VALUE_URI = "value_uri";
    public static final String DATATYPE = "datatype";
    public static final String ORDER = "order";
    public static final String NAME = "name";
    //TYPES
    public static final String TYPE_WORK = "https://artresearch.net/resource/fc/work";
    public static final String TYPE_ARTIST = "https://artresearch.net/resource/fc/artist";
    public static final String TYPE_PHOTO = "https://artresearch.net/resource/custom/fc/photo";
    public static final String TYPE_PHOTOGRAPHERS = "https://artresearch.net/resource/fc/photographer";
    public static final String TYPE_REPOSITORIES = "https://artresearch.net/resource/fc/institution";
    public static final String TYPE_ACTOR = "http://www.cidoc-crm.org/cidoc-crm/E39_Actor";

    public static final String INDEX_ARTWORKS = "artworks";
    public static final String INDEX_ARTISTS = "artists";
    public static final String INDEX_PHOTOS = "photos";
    public static final String INDEX_PHOTOGRAPHERS = "photographers";
    public static final String INDEX_REPOSITORIES = "repositories";
    public static final String INDEX_ACTOR = "actor";
    //DATATYPE 
    public static final String XSD_STRING = "http://www.w3.org/2001/XMLSchema#string";
    public static final String XSD_VALUE = "http://www.w3.org/2001/XMLSchema#anyURI";
    // PROPERTIEs
    public static final String RDFS_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
    //CATEGORIES
    public static final String CATEGORY_WORK = "https://artresearch.net/category/work";
    public static final String CATEGORY_PRODUCTION_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/production_info";
    public static final String CATEGORY_PHOTO_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/photograph_info";
    public static final String CATEGORY_WORK_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/work_info";
    public static final String CATEGORY_PERSON_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/person_info";
    public static final String CATEGORY_EXTERNAL_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/external_info";
    public static final String CATEGORY_REPOSITORY_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/repository_info";
    public static final String CATEGORY_PERSON_RELATIONSHIPS = "http://www.researchspace.org/ontologies/platform/FieldCategories/person_relationships";
    public static final String CATEGORY_PLACE_INFO = "http://www.researchspace.org/ontologies/platform/FieldCategories/place_info";
    public static final String CATEGORY_PHOTOGRAPHER = "http://www.researchspace.org/ontologies/platform/FieldCategories/photographer";
    public static final String CATEGORY_PHYSICAL_PROPERTIES = "http://www.researchspace.org/ontologies/platform/FieldCategories/physical_properties";
    public static final String CATEGORY_PROVENANCE = "http://www.researchspace.org/ontologies/platform/FieldCategories/provenance";
    public static final String CATEGORY_SUBJECTS = "http://www.researchspace.org/ontologies/platform/FieldCategories/subjects";
    public static final String CATEGORY_DATING = "http://www.researchspace.org/ontologies/platform/FieldCategories/dating";
    public static final String CATEGORY_NOTES = "http://www.researchspace.org/ontologies/platform/FieldCategories/notes";
    public static final String CATEGORY_ARTWORK_MONUMENT = "http://www.researchspace.org/ontologies/platform/FieldCategories/artwork_monument";
    public static final String CATEGORY_CREATOR_ATTRIBUTION = "http://www.researchspace.org/ontologies/platform/FieldCategories/creator_attribution";
    public static final String CATEGORY_REFERENCES = "http://www.researchspace.org/ontologies/platform/FieldCategories/references";
    //EXTENSIONS
    public static final String CSV = ".csv";
    public static final String LIMIT = "LIMIT 10 \n";
    // QUERY
    public static String CONSTRUCT_Q;// = "CONSTRUCT { ?subject_p rdfs:label ?value }";

    // PREFIXES
    public static final String PREFIXES = "PREFIX : <http://www.researchspace.org/resource/>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX frbroo: <http://iflastandards.info/ns/fr/frbr/frbroo/>\n"
            + "PREFIX crminfluence: <http://www.cidoc-crm.org/cidoc-crm/influence/>\n"
            + "PREFIX crmarchaeo: <http://www.cidoc-crm.org/cidoc-crm/CRMarchaeo/>\n"
            + "PREFIX rshelp: <http://researchspace.org/help/>\n"
            + "PREFIX ontodia: <http://ontodia.org/schema/v1#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX rso: <http://www.researchspace.org/ontology/>\n"
            + "PREFIX sp: <http://spinrdf.org/sp#>\n"
            + "PREFIX Help: <http://help.researchspace.org/resource/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX Admin: <http://www.researchspace.org/resource/admin/>\n"
            + "PREFIX crmgeo: <http://www.ics.forth.gr/isl/CRMgeo/>\n"
            + "PREFIX prov: <http://www.w3.org/ns/prov#>\n"
            + "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>\n"
            + "PREFIX crmba: <http://www.cidoc-crm.org/cidoc-crm/CRMba/>\n"
            + "PREFIX crmsci: <http://www.ics.forth.gr/isl/CRMsci/>\n"
            + "PREFIX crmdig: <http://www.ics.forth.gr/isl/CRMdig/>\n"
            + "PREFIX User: <http://www.researchspace.org/resource/user/>\n"
            + "PREFIX bds: <http://www.bigdata.com/rdf/search#>\n"
            + "PREFIX Platform: <http://www.researchspace.org/resource/system/>\n"
            + "PREFIX fc: <https://artresearch.net/resource/custom/fc/>\n"
            + "PREFIX cfc: <https://artresearch.net/resource/custom/fc/>\n";

// SERVER
    public static String SOLR_CORE = "test";
    public static String SOLR_SERVER = "http://localhost:8983/solr/";

    // REQUESTS
    public static final String UPDATE_JSON = "/update/json";

    // REQUEST PARAMS
    public static final String COMMIT = "commit";

    // MIME TYPE
    public static final String APPLICATION_JSON = "application/json";
    public static final String MULTIPART_FORMDATA = "multipart/form-data";
    public static final String APPLICATION_OCTETSTREAM = "application/octet-stream";

    public static String LABEL_QUERY;
   

    public static String setConstructQ(String order, String field_name, boolean additionalSubjects) {
        if (additionalSubjects) {
            return "CONSTRUCT {\n\t?subject_p rdfs:label ?value.\n\t?subject_p <http://www.artresearch.net/custom/fieldLabel> \"" + field_name + "\".\n\t?subject_p <http://www.researchspace.org/resource/system/fields/order> \"" + order + "\".\n}";
        } else {
            return "CONSTRUCT {\n\t?subject rdfs:label ?value.\n\t?subject <http://www.artresearch.net/custom/fieldLabel> \"" + field_name + "\".\n\t?subject <http://www.researchspace.org/resource/system/fields/order> \"" + order + "\".\n}";
        }
    }

    public static void setSolrCore(String coreName) {
        SOLR_CORE = coreName;
    }
    
    public static void setSolrServer() {
        if (new File(Resources.SOLR_CONFIGURATION_FILE).exists()){
            try {
                SAXReader reader = new SAXReader();
                reader.setEncoding("UTF-8");
                SOLR_SERVER = reader.read(new FileInputStream(Resources.SOLR_CONFIGURATION_FILE)).getRootElement().elementText("endpoint");
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void setLabelQuery(String type, String addFilter) {
        if (addFilter!=null){
            LABEL_QUERY = "CONSTRUCT {\n"
                    + "	?subject rdfs:label ?value.\n"
                    + "	?subject <http://www.artresearch.net/custom/fieldLabel> \"title\".\n"
                    + "	?subject <http://www.researchspace.org/resource/system/fields/order> \"100\".\n"
                    + "}WHERE \n"
                    + "{	\n"
                    + "  ?subject a <" + type + ">.\n"
                    + "  ?subject rdfs:label ?value.\n"
                    + "\t" +addFilter + "\n"
                    + "}";
        } else {
            LABEL_QUERY = "CONSTRUCT {\n"
                + "	?subject rdfs:label ?value.\n"
                + "	?subject <http://www.artresearch.net/custom/fieldLabel> \"title\".\n"
                + "	?subject <http://www.researchspace.org/resource/system/fields/order> \"100\".\n"
                + "}WHERE \n"
                + "{	\n"
                + "  ?subject a <" + type + ">.\n"
                + "  ?subject rdfs:label ?value.\n"
                + "}";
        
        }
    }
}
