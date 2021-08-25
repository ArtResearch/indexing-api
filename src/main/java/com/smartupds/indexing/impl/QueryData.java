/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.impl;

import com.smartupds.indexing.api.Downloader;
import com.smartupds.indexing.common.Resources;
import com.smartupds.indexing.common.Utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** Class for querying data of an endpoint
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public class QueryData implements Downloader {
    private final SPARQLRepository repo;
    private final String query;
    private final String format;
    
    public QueryData(String endpoint, String query, String format){
        repo = new SPARQLRepository(endpoint);
        this.query = query;
        this.format = format;
    }
    
    @Override
    public void download(){
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Endpoint : {0}", repo.toString());
        new File(Resources.FOLDER_OUTPUT_INDEXING_REPOSITORIES).mkdirs();
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(Resources.FOLDER_OUTPUT_INDEXING_REPOSITORIES
                +"/"+Resources.SUBJECT + Resources.CSV), "UTF-8")) {
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Initializing repository");
            repo.initialize();
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Getting repository connection");
            try (RepositoryConnection conn = repo.getConnection()) {
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Preparing select query : \n{0}", query);
                TupleQuery select = conn.prepareTupleQuery(QueryLanguage.SPARQL,query);
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Evaluationg select query");
                TupleQueryResult result = select.evaluate();
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Successful query evaluation");
                while (result.hasNext()){
                    BindingSet set = result.next();
                    Iterator<Binding> it = set.iterator();
                    while(it.hasNext())
                        writer.append(it.next().getValue()+"\t");
                    writer.append("\n");
                }
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "File Saved at: ".concat(Resources.FOLDER_OUTPUT_INDEXING_REPOSITORIES+"/"+Resources.SUBJECT));
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Shutting down repository");
                writer.close();
            }
            repo.shutDown();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void configure(String username, String password){
        System.out.println("Configuring endpoint");
        repo.setUsernameAndPassword(username, password);
    }

    public void downloadLabels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<String> downloadPatterns(String type) {
        ArrayList<String> list = new  ArrayList<>();
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Endpoint : {0}", repo.toString());
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Initializing repository");
        repo.initialize();
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Getting repository connection");
        
        RepositoryConnection conn = repo.getConnection();
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Preparing select query : \n{0}", query);
        TupleQuery select = conn.prepareTupleQuery(QueryLanguage.SPARQL,query);
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Evaluationg select query");
        TupleQueryResult result = select.evaluate();
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Successful query evaluation");
        while (result.hasNext()){
            BindingSet set = result.next();
            String pattern = set.getBinding(Resources.QUERY).getValue().stringValue();
            String datatype = set.getBinding(Resources.DATATYPE).getValue().stringValue();
            String order = set.getBinding(Resources.ORDER).getValue().stringValue();
            String field_name = set.getBinding(Resources.NAME).getValue().stringValue()
                    .trim().replace("-", "").replaceAll("(\\s+)", "_").toLowerCase();
            if (!field_name.equals("types")){
                String subjectType = "\t?subject a <" + type +">.\n";
                String subjectTypeAdditional = "\t?subject owl:sameAs ?subject_p.\n\t?subject_p a <" + type +">.\n";
                if (datatype.equals(Resources.XSD_STRING)){
                    list.add(constructPattern( pattern, subjectType, order, field_name, false));
                    list.add(constructPattern( pattern, subjectTypeAdditional, order, field_name, true));
                }else if (datatype.equals(Resources.XSD_VALUE)) {
                    list.add(constructPatternVal( pattern, subjectType, order, field_name, false));
                    list.add(constructPatternVal( pattern, subjectTypeAdditional, order, field_name, true));
                }
            }
        }
        Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Shutting down repository");
        repo.shutDown();
        return list;
    }

    public String downloadConstruct(String constructFolder,String indexfolder) {
        new File(constructFolder).mkdirs();
        new File(indexfolder).mkdirs();
        String field_name = "";
        try {
            OutputStreamWriter writer = null;
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Initializing repository");
            repo.initialize();
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Getting repository connection");
            RepositoryConnection conn = repo.getConnection();
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Preparing graph query : \n".concat(query));
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Query Hash : ".concat(""+Math.abs(query.hashCode())));
            GraphQuery graph = conn.prepareGraphQuery(query);
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Evaluationg graph query");
            GraphQueryResult result = graph.evaluate();
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Successful query evaluation");
            if (result.hasNext())
                writer = new OutputStreamWriter(new FileOutputStream(constructFolder +"/"+ Math.abs(query.hashCode())+".n3",false),"UTF-8");
            
            ArrayList<String[]> statements = new ArrayList<>();
            String order = "";
            while(result.hasNext()){
                Statement stmt = result.next();
                if(stmt.getObject() instanceof IRI)
                    writer.write("<"+stmt.getSubject()+"> <"+stmt.getPredicate()+"> <" + stmt.getObject() +">. \n");
                else {
//                    writer.write("<"+stmt.getSubject()+"> <"+stmt.getPredicate()+"> \"" + stmt.getObject().stringValue().replace("\\", "").replace("\n", " ").replace("\"", "\\\"") +"\".\n");
                    writer.write("<"+stmt.getSubject()+"> <"+stmt.getPredicate()+"> \"" + stmt.getObject().stringValue().replace("\n", " ").replaceAll("\\\\(?=[^\\\"])", "").replace("\"", "\\\"") +"\".\n");
                }
                if(stmt.getPredicate().toString().equals("http://www.researchspace.org/resource/system/fields/order")){
                    order = stmt.getObject().stringValue();
                } else if (stmt.getPredicate().toString().equals("http://www.artresearch.net/custom/fieldLabel")){
                    field_name = stmt.getObject().stringValue();
                } else {
                    statements.add(new String[]{stmt.getSubject().stringValue(),stmt.getObject().stringValue().replace("\n", " ").replaceAll("\\\\(?=[^\\\"])", "").replace("\"", "\\\"")});
                }
            }
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "File Saved at: ".concat(constructFolder +"/"+ Math.abs(query.hashCode())+".n3"));
            if (writer!=null)
                writer.close();
            Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Shutting down repository");
            conn.close();
            repo.shutDown();
            if (statements.size()>0){
                /* JSON Creator*/
                JSONArray formats = new JSONArray();
                for (String[] statement:statements){
                    JSONObject format = new JSONObject();
                    format.put("uri",statement[0]);
                    format.put(field_name,statement[1]);
                    format.put("field_score",order);
                    format.put("id",Utils.uniqueID(order, field_name, statement[0]));
                    formats.add(format);
                }
                Path path = Paths.get(indexfolder +"/"+ field_name +"_"+ Math.abs(query.hashCode())+".json");
                File json = new File(path.getParent().toString());
                json.mkdirs();
                BufferedWriter writer_json = Files.newBufferedWriter(path);
                writer_json.write(formats.toJSONString());
                writer_json.close();
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "File Saved at: ".concat(indexfolder +"/"+ field_name +"_"+ Math.abs(query.hashCode())+".json"));
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QueryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return indexfolder +"/"+ field_name +"_"+ Math.abs(query.hashCode())+".json";
    }
    
    private String constructPattern(String pattern, String subjectType, String order, String field_name, boolean additionalSubjects){
        int index = pattern.indexOf("{");
        pattern = pattern.substring(0, index+1) + subjectType + pattern.substring(index+1, pattern.length());
        pattern = pattern.replaceAll("(SELECT|select).*(?=WHERE|where)",Resources.setConstructQ(order,field_name,additionalSubjects));
        String cunstructpattern = Resources.PREFIXES.concat(pattern);
        cunstructpattern = cunstructpattern.replaceAll("(graph|GRAPH)\\s*\\?graph[\\s\\t\\n]*\\{", " ");
        cunstructpattern = cunstructpattern.replaceAll("\\}[\\s\\t\\n]*\\?(graph|subject)\\s+<https:\\/\\/pharos\\.artresearch\\.net\\/custom\\/has_provider>\\s+\\?prov[a-z]*\\s*\\.[\\s\\t\\n]*\\?prov[a-z]*\\s+rdfs:label\\s+\\?prov[a-z]*Label\\s*\\.", " ");
        return cunstructpattern;
    }
    
    private String constructPatternVal(String pattern, String subjectType, String order, String field_name, boolean additionalSubjects){
        int index = pattern.indexOf("{");
        pattern = pattern.substring(0, index+1) + subjectType + pattern.substring(index+1, pattern.length());
        pattern = pattern.replaceAll("[\\?\\$]"+Resources.VALUE+"(?=[\\s\\.](?!.*("+Resources.WHERE.toUpperCase()+"|"+Resources.WHERE+")))" , "?"+Resources.VALUE_URI+ " ").trim();
        pattern = pattern.substring(0,pattern.lastIndexOf("}")-1).concat("\n\t?"+Resources.VALUE_URI+" <"+Resources.RDFS_LABEL+"> ?" + Resources.VALUE + ".\n}");
        pattern = pattern.replaceAll("(SELECT|select).*(?=WHERE|where)",Resources.setConstructQ(order, field_name, additionalSubjects));
        String cunstructpattern = Resources.PREFIXES.concat(pattern);
        cunstructpattern = cunstructpattern.replaceAll("(graph|GRAPH)\\s*\\?graph[\\s\\t\\n]*\\{", " ");
        cunstructpattern = cunstructpattern.replaceAll("\\}[\\s\\t\\n]*\\?(graph|subject)\\s+<https:\\/\\/pharos\\.artresearch\\.net\\/custom\\/has_provider>\\s+\\?prov[a-z]*\\s*\\.[\\s\\t\\n]*\\?prov[a-z]*\\s+rdfs:label\\s+\\?prov[a-z]*Label\\s*\\.", " ");
        return cunstructpattern;
    }
}
