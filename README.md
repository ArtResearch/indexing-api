# indexing-api #

Indexing API retrieves data from Pharos main categories and process them to create indexes in SOLR. Indexing has been implemented for artists, artworks & photos. It remains to be implemented for institutions, photographers and places.

# Prerequisites 

- [SOLR](https://solr.apache.org/) cores set up

# Run indexing-api

## Manual Run

### Download and build

Download indexing api with the below command :

```bash
$ git clone https://github.com/ArtResearch/indexing-api.git
$ cd indexing-api
$ mvn clean package
```

### Configuration files

- Update the configuration file with the proper sparql endpoint, username & password. Copy **src/main/resources/authentication.xml.template** to **src/main/resources/authentication.xml**.
- Update the configuration file for solr endpoint. Copy **src/main/resources/solr.xml.template** to **src/main/resources/solr.xml**.

### Options

* **i [required]** (index) : flag to index resources.
* **d [required]** (index) : flag to download resources and trasform to JSON documents.
* **u [required]** (index) : flag to upload JSON documents resources to SOLR.
* **t [required]** (type) : type to donwload and index ("artists", "artworks" & "photos" are the available types).
* **c [required]** (core) : core that the json files would be imported to. Note: SOLR core is created beforehand, so make sure that the core exists before running the api. Core name is required if full indexing or just upload is performed, but not required when just download is performed.

### Run

Artists :

```bash
$ java -jar target/indexing-api-0.1-assembly.jar -i -t artists -c [solr_core]
```

Artworks :

```bash
$ java -jar target/indexing-api-0.1-assembly.jar -i -t artworks -c [solr_core]
```

Photos :

```bash
$ java -jar target/indexing-api-0.1-assembly.jar -i -t photos -c [solr_core]
```

## Automatic Run 

### Enviroment

Copy .env.template to .env file and add the names of the cores for each main entity as they are set up in your SOLR database. Furthermore you need to set up the [Configuration files](#configuration-files).

Build with :

```bash
mvn clean package
```

Run the complete indexing process automaticly using the command below :

```bash
./scripts/index.sh
```
 or just the donwload process : 
 
```bash
./scripts/download.sh
```
 or just the upload process : 
 
```bash
./scripts/upload.sh
```

Before you do run them you must insert the correct solr cores in .env file based on the .env.template file.
 
