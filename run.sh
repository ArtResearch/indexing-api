#!/bin/bash
nohup java -jar target/indexing-api-0.1-assembly.jar -i -type repositories -c repositories_v5 &>repositories.txt &
nohup java -jar target/indexing-api-0.1-assembly.jar -i -type photos -c photos_v5 &>photos.txt &
nohup java -jar target/indexing-api-0.1-assembly.jar -i -type artworks -c artworks_v5 &>artworks.txt &
nohup java -jar target/indexing-api-0.1-assembly.jar -i -type photographers -c photographers_v5 &>photographers.txt &
nohup java -jar target/indexing-api-0.1-assembly.jar -i -type artists -c artists_v5 &>artists.txt &
