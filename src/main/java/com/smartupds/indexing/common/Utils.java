/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartupds.indexing.common;

import com.smartupds.indexing.api.Splitter;
import com.smartupds.indexing.impl.JSONSplitter;
import com.smartupds.indexing.impl.QueryData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.NamedList;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Manolis Fragiadoulakis <fragiadoulakis at smartupds.com>
 */
public class Utils {

    /**
     * Retrieve resources of a specific type.
     *
     * @param category
     * @param type type of resource
     * @param configurationfile
     * @param constructFolder
     * @param indexfolder
     */
    static HashMap<String, String> termsWeights = new HashMap<>();
    static ArrayList<ArrayList<String>> paths = new ArrayList();

     public static void downloadSubjectFieldsDir(File configurationfile, String constructFolder, String indexfolder, String field) {
        try {
            ArrayList<String> constructQueries = new ArrayList<>();
            for(File file: new File(Resources.FOLDER_QUERIES + "/" + field).listFiles()){                
                String fileContent = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())), StandardCharsets.UTF_8);
                constructQueries.add(fileContent);
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "Loading graph query : \n".concat(fileContent));   
            }
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document doc = reader.read(new FileInputStream(configurationfile));
            Element root = doc.getRootElement();         
            constructQueries.forEach(query -> {
                QueryData downloader = new QueryData(root.elementText("endpoint"), query, Resources.SELECT);
                if (!root.elementText("username").isEmpty() && !root.elementText("password").isEmpty()) {
                    downloader.configure(root.elementText("username"), root.elementText("password"));
                }
                downloader.downloadConstruct(constructFolder, indexfolder);
            });

        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void downloadQueries(String category, String type, File configurationfile, String constructFolder, String indexfolder, String field) {
        try {
            ArrayList<String> constructQuery = null;
            if (type.equals(Resources.TYPE_PHOTOGRAPHERS)){
                constructQuery = extractFieldPatternsByType(category, type, configurationfile, "FILTER (!CONTAINS(STR(?subject),\"nypl\"))");
            } else
                constructQuery = extractFieldPatternsByType(category, type, configurationfile, null);
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document doc = reader.read(new FileInputStream(configurationfile));
            Element root = doc.getRootElement();
            if (!type.equals(Resources.TYPE_PHOTOGRAPHERS)){
                Resources.setLabelQuery(type,null);
                constructQuery.add(Resources.LABEL_QUERY);
            }
            constructQuery.forEach(query -> {
                QueryData downloader = new QueryData(root.elementText("endpoint"), query, Resources.SELECT);
                if (!root.elementText("username").isEmpty() && !root.elementText("password").isEmpty()) {
                    downloader.configure(root.elementText("username"), root.elementText("password"));
                }
                downloader.downloadQuery(Resources.FOLDER_QUERIES + "/" + field);
            });
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void downloadSubjectFields(String category, String type, File configurationfile, String constructFolder, String indexfolder) {
        try {
            ArrayList<String> constructQuery = null;
            if (type.equals(Resources.TYPE_PHOTOGRAPHERS)){
                constructQuery = extractFieldPatternsByType(category, type, configurationfile, "FILTER (!CONTAINS(STR(?subject),\"nypl\"))");
            } else
                constructQuery = extractFieldPatternsByType(category, type, configurationfile, null);
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document doc = reader.read(new FileInputStream(configurationfile));
            Element root = doc.getRootElement();
            if (!type.equals(Resources.TYPE_PHOTOGRAPHERS)){
                Resources.setLabelQuery(type,null);
                constructQuery.add(Resources.LABEL_QUERY);
            }
            constructQuery.forEach(query -> {
                QueryData downloader = new QueryData(root.elementText("endpoint"), query + Resources.LIMIT, Resources.SELECT);
//                QueryData downloader = new QueryData(root.elementText("endpoint"), query + "\n", Resources.SELECT);

                if (!root.elementText("username").isEmpty() && !root.elementText("password").isEmpty()) {
                    downloader.configure(root.elementText("username"), root.elementText("password"));
                }
                downloader.downloadConstruct(constructFolder, indexfolder);
            });

        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> extractFieldPatternsByType(String category, String type, File configurationfile,String addfilter) {
        ArrayList<String> list = new ArrayList<>();
        try {
            String patterns = "SELECT ?query ?datatype ?order ?name WHERE{    \n"
                    + "   ?field a <http://www.researchspace.org/resource/system/fields/Field>;\n"
                    + "   <http://www.researchspace.org/resource/system/fields/category> <" + category + ">;\n"
                    + "   <http://www.researchspace.org/resource/system/fields/selectPattern> ?s.\n"
                    + "   ?s <http://spinrdf.org/sp#text> ?query.\n"
                    + "   ?field <http://www.researchspace.org/resource/system/fields/xsdDatatype> ?datatype.\n"
                    + "   ?field <http://www.researchspace.org/resource/system/fields/order> ?order.\n"
                    + "   ?field <http://www.w3.org/2000/01/rdf-schema#label> ?name.\n"
                    + "}";
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document doc = reader.read(new FileInputStream(configurationfile));
            Element root = doc.getRootElement();
            QueryData downloader = new QueryData(root.elementText("endpoint"), patterns, Resources.SELECT);
            if (!root.elementText("username").isEmpty() && !root.elementText("password").isEmpty()) {
                downloader.configure(root.elementText("username"), root.elementText("password"));
            }
            list = downloader.downloadPatterns(type,addfilter);
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private static ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.getName().contains("desktop.ini")) {
                    //ignore
                } else if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    filePaths.add(folder.getAbsoluteFile() + "/" + fileEntry.getName());
                    //                System.out.println(folder.getAbsoluteFile() + "\\" + fileEntry.getName());
                }
            }
        } else {
            filePaths.add(folder.getAbsolutePath());
        }
        return filePaths;
    }

    public static void updateSolrIndex(String type, String loc, String index_type) {
        ArrayList<String> files = Utils.listFilesForFolder(new File(loc));
        int count = 0;
        for (String file : files) {
            try {
                SolrClient client = new HttpSolrClient.Builder(Resources.SOLR_SERVER + index_type).build();

                ContentStreamUpdateRequest req = new ContentStreamUpdateRequest(Resources.UPDATE_JSON);
                req.setParam(Resources.COMMIT, "true");

                count++;
                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "\t{0} out of {1}{2}", new Object[]{count, files.size(), "\t| Uploading File : ".concat(file)});
                req.addFile(new File(file), Resources.APPLICATION_JSON);
                Logger.getLogger("REQUEST: " + req);
                NamedList<Object> result = client.request(req);
                System.out.println("Result: " + result);
            } catch (IOException | SolrServerException | RemoteSolrException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void createJSON(String file, String loc) {
        // read n3 files as statements
        ArrayList<String[]> statements = new ArrayList<>();
        Dataset dataset = RDFDataMgr.loadDataset(file);
        Model model = dataset.getDefaultModel();
        StmtIterator list = model.listStatements();
        String order = "";
        String field_name = "";
        while (list.hasNext()) {
            Statement stmt = list.next();
            if (stmt.getPredicate().toString().equals("http://www.researchspace.org/resource/system/fields/order")) {
                order = stmt.getObject().toString();
            } else if (stmt.getPredicate().toString().equals("http://www.artresearch.net/custom/fieldLabel")) {
                field_name = stmt.getObject().toString();
            } else {
                statements.add(new String[]{stmt.getSubject().toString(), stmt.getObject().toString().replace("\n", " ").replaceAll("\\\\(?=[^\\\"])", "").replace("\"", "\\\"")});
            }
        }

        if (statements.size() > 0) {
            /* JSON Creator*/
            JSONArray formats = new JSONArray();
            for (String[] statement : statements) {
                JSONObject format = new JSONObject();
                format.put("uri", statement[0]);
                format.put(field_name, statement[1]);
                format.put("field_score", order);
                formats.add(format);
            }
            Path path = Paths.get(loc + "/" + field_name + "_" + new File(file).getName().replace(".n3", "") + ".json");
            File json = new File(path.getParent().toString());
            json.mkdirs();
            try {
                BufferedWriter writer_json = Files.newBufferedWriter(path);
                writer_json.write(formats.toJSONString());
                writer_json.close();
                Logger.getLogger(QueryData.class.getName()).log(Level.INFO, "File Saved at: ".concat(loc + "/" + field_name + "_" + new File(file).getName().replace(".n3", "") + ".json"));
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void splitSpecial(String folderIN) {
//        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Splitting Multiple Files Started.");
//        ArrayList<String> paths = Utils.listFilesForFolder(new File(folderIN));
        int counter = 1;
//        for ( String path : paths){
        try {
//                double percent = (double) counter*100/paths.size();
//                path = path.trim();
//                Splitter splitter = (Splitter) new TTLSplitter(path, Double.parseDouble("1"));
            Splitter splitter = (Splitter) new JSONSplitter(folderIN, Double.parseDouble("0.5"));
            splitter.split();
//                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Process at {0} %", percent);
            counter++;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

//        }
    }

    public static void split(String folderIN) {
        Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Splitting Multiple Files Started.");
        ArrayList<String> json_paths = Utils.listFilesForFolder(new File(folderIN));
        int counter = 1;
        for (String path : json_paths) {
            try {
                double percent = (double) counter * 100 / json_paths.size();
                path = path.trim();
//                Splitter splitter = (Splitter) new TTLSplitter(path, Double.parseDouble("1"));
                Splitter splitter = (Splitter) new JSONSplitter(path, Double.parseDouble("0.5"));
                splitter.split();
                Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Process at {0} %", percent);
                counter++;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String uniqueID(String order, String field_value, String uri) {
        byte[] result = null;
        try {
            String merged = order + field_value + uri;
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.toString(prng.nextInt()) + merged;
            //get its digest
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            result = sha.digest(randomNum.getBytes());

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hexEncode(result);
    }

    static private String hexEncode(byte[] input) {
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < input.length; ++idx) {
            byte b = input[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    public static void addIds(String loc) {
        ArrayList<String> files = Utils.listFilesForFolder(new File(loc));
        int count = 0;
        for (String file : files) {
            count++;
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "Processing file {0} out of {1} :{2}", new Object[]{count, files.size(), file});
            JSONParser parserJson = new JSONParser();
            JSONArray jsonArray = null;
            try {
                InputStreamReader stream = new InputStreamReader(new FileInputStream(file), "UTF8");
                Object json = parserJson.parse(stream);
                jsonArray = (JSONArray) json;
                Iterator iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    JSONObject it = (JSONObject) iterator.next();
                    Object[] itset = it.keySet().toArray();
                    it.put("id", Utils.uniqueID(it.get(itset[0]).toString(), it.get(itset[1]).toString(), it.get(itset[2]).toString()));
                }
                stream.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.INFO, null, ex);
            } catch (IOException | ParseException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.INFO, null, ex);
            }
            if (jsonArray != null) {
                try {
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
                    writer.write(jsonArray.toJSONString());
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "File processed : ", new Object[]{file});
        }
    }

    public static void clean(String jsonDir, String cleanDir)
    {
        new File(cleanDir).mkdirs();
        ArrayList<String> jsonPaths = Utils.listFilesForFolder(new File(jsonDir));
//        System.out.println(jsonPaths.toString());
        
        jsonPaths.forEach((jsonfile) -> {
            JSONParser parser = new JSONParser();
            try {
                JSONObject targetObject = new JSONObject();
                JSONArray jsonContents = (JSONArray) parser.parse(new FileReader(jsonfile));
                Iterator jsonContentsIt = jsonContents.iterator();
                
                while (jsonContentsIt.hasNext()) {                    
                    JSONObject jsonObject = (JSONObject) jsonContentsIt.next();
//                    Unique, puts uri only once
                    targetObject.put("uri", jsonObject.get("uri"));
//                    System.out.println("Contained json object: " + jsonObject.toJSONString());
                    Iterator jsonKeysIt = jsonObject.keySet().iterator();                    
                    while(jsonKeysIt.hasNext())
                    {                        
                        String key = (String) jsonKeysIt.next();
                        System.out.println("Json key: " + key);
                        if (! key.equals("field_score") && ! key.equals("id") && ! key.equals("uri")){
                            if(targetObject.containsKey(key)) {
                                 if (targetObject.get(key) instanceof JSONArray) {
                                    ((JSONArray) targetObject.get(key)).add(jsonObject.get(key));
                                } else {
                                    JSONArray objArray = new JSONArray(); 
                                    objArray.add(targetObject.get(key));
                                    objArray.add(jsonObject.get(key));
                                    targetObject.put(key, objArray);
                                }
                            }
                            else {
                                targetObject.put(key, jsonObject.get(key));
                            }
                        }  
                    }
                }
//                System.out.println("Target object: " + targetObject.toJSONString());               
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(cleanDir + "/" + Paths.get(jsonfile).getFileName()));
                writer.write(targetObject.toJSONString());
                writer.close();
//                System.out.println(Paths.get(jsonfile).getFileName());

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }           
        });
    }
        
    public static void merge(String jsonDir, String mergedDir) {
        new File(mergedDir).mkdirs();
        ArrayList<String> json_paths = Utils.listFilesForFolder(new File(jsonDir));
        JSONArray jsonArray = new JSONArray();
        json_paths.forEach((jsonfile) -> {
            JSONParser parser = new JSONParser();
            try {
                jsonArray.add((JSONObject) parser.parse(new FileReader(jsonfile)));
            } catch (IOException | ParseException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        });        
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(mergedDir + "/merged.json"));
            writer.write(jsonArray.toJSONString());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void change_weigths() throws FileNotFoundException, IOException, ParseException {
        String[] files = new String[1];
        ArrayList<String> temporal0 = new ArrayList();
//        ArrayList<String> temporal1 = new ArrayList();
//        ArrayList<String> temporal2 = new ArrayList();
        paths.add(temporal0);
//        paths.add(temporal1);
//        paths.add(temporal2);

//        files[0] = Resources.ARTIST_TERM_AND_WEIGHTS_FILE;
//        files[1] = Resources.ARTWORKS_TERM_AND_WEIGHTS_FILE;
//        files[2] = Resources.PHOTOS_TERM_AND_WEIGHTS_FILE;
//        listofPaths(new File(Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_SPLIT), 0);
//        listofPaths(new File(Resources.FOLDER_OUTPUT_INDEXING_WORKS_JSON_SPLIT), 1);
//        listofPaths(new File(Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_SPLIT), 2);
        listofPaths(new File(Resources.FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_SPLIT), 0);
        for (int k = 0; k < files.length; k++) {
            termsWeights.clear();
            Scanner sc = new Scanner(new File(files[k]));
            int counter = 0;
            while (sc.hasNextLine()) {
                counter++;
                String line = sc.nextLine();
                String[] tmp = line.split("\\s+");

                if (!termsWeights.containsKey(tmp[0])) {
                    termsWeights.put(tmp[0], tmp[1]);
                }
            }
            JSONParser parser = new JSONParser();
            String fixed_path = "";
//            if (k == 0) {
//                fixed_path = Resources.FOLDER_OUTPUT_INDEXING_ARTISTS_JSON_FIXED;
//            } else if (k == 1) {
//                fixed_path = Resources.FOLDER_OUTPUT_INDEXING_WORKS_JSON_FIXED;
//            } else {
//                fixed_path = Resources.FOLDER_OUTPUT_INDEXING_PHOTOS_JSON_FIXED;
//            }
            fixed_path = Resources.FOLDER_OUTPUT_INDEXING_PHOTOGRAPHERS_JSON_FIXED;
            File newfolder = new File(fixed_path);
            if (newfolder.isDirectory()) {
                String[] entries = newfolder.list();
                for (String s : entries) {
                    File currentFile = new File(newfolder.getPath(), s);
                    currentFile.delete();
                }
            } else {
                //Creating the directory
                boolean bool = newfolder.mkdir();
                if (bool) {
                    System.out.println("Directory " + fixed_path + " created successfully");
                } else {
                    System.out.println("Sorry couldn’t create specified directory");
                }
            }
            for (String str : paths.get(k)) {
                String output_path = fixed_path;
                System.out.println("->" + str);
                String[] first_split = str.split("/");

//            create fixxed file
                String[] dot_split = (first_split[first_split.length - 1]).split("\\.");
                String[] _split = dot_split[0].split("\\_");
                String field_name = "";
                JSONArray a = (JSONArray) parser.parse(new FileReader(str));
                for (Object o : a) {
                    JSONObject person = (JSONObject) o;
                    for (String key : termsWeights.keySet()) {
                        if (person.containsKey(key)) {
                            //System.out.println("found");
                            field_name = key;
                            break;
                        }
                    }
                }

                System.out.println("--------------" + first_split[first_split.length - 1]);
                output_path = output_path + "/" + first_split[first_split.length - 1];
//                for (int i = 0; i < _split.length - 1; i++) {
//                    if (!_split[i].equals("split")) {
//                        if (i == 0) {
//                            field_name = field_name + _split[0];
//                        } else {
//                            field_name = field_name + "_" + _split[i];
//                        }
//                    }
//                }
                System.out.println("output_path " + output_path);
                System.out.println(field_name);
                if (termsWeights.containsKey(field_name)) {

                    for (Object o : a) {
                        JSONObject person = (JSONObject) o;
                        String name = (String) person.get(field_name);
                        if (name == null) {
                            System.out.println("name is null for field_name= " + field_name);
                        } else {
                            String score = (String) person.get("field_score");
                            if (termsWeights.containsKey(field_name)) {
                                person.put("field_score", termsWeights.get(field_name));
                            }
                        }
                    }
                    try (FileWriter jsonfile = new FileWriter(output_path)) {
                        //We can write any JSONArray or JSONObject instance to the file
                        jsonfile.write(a.toJSONString());
                        jsonfile.flush();
                    }

                } else {
                    System.out.println("couldn't find the term " + field_name + "in terms and weights File in k=" + k);
                }

            }

        }
        paths.clear();
        termsWeights.clear();
    }

    private static void listofPaths(final File folder, int type) {
        System.out.println("-> " + folder.getAbsolutePath());
        if (folder.exists()) {
            System.out.println("-> " + folder.getAbsolutePath());
            System.out.println("exists");
            if (folder.isDirectory()) {
                System.out.println("is Directory");
            }
            System.out.println("-> " + folder.getAbsolutePath());
        }
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().contains("desktop.ini")) {
                //ignore
            } else if (fileEntry.isDirectory()) {
                listofPaths(fileEntry, type);
            } else {

                paths.get(type).add(folder.getAbsoluteFile() + "/" + fileEntry.getName());

            }
        }
    }

}
