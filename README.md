# indexing-api

Indexing API retrieves data from Pharos main categories and process them to create indexes in SOLR. Indexing has been implemented for artists, artworks & photos. It remains to be implemented for institutions, photographers and places.

## Run indexing-api

### Download and build

Download indexing api with the below command :

```bash
$ git clone https://github.com/ArtResearch/indexing-api.git
$ cd indexing-api
$ mvn clean package
```

### Configuration file

Update the configuration file with the proper endpoint, username & password. The configuration file is located in src/main/resources/authentication.xml .

### Options

* **i [required]** (index) : flag to index resources.
* **t [required]** (type) : type to donwload and index ("artists", "artworks" & "photos" are the available types).
* **c [required]** (core) : core that the json files would be imported to. Note: SOLR core is created beforehand, so make sure that the core exists before running the api.

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