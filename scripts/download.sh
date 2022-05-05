#!/bin/bash

source .env

nohup java -jar target/indexing-api-0.1-assembly.jar -d -type repositories -c $REPOSITORY_CORE &>repositories.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -d -type photos -c $PHOTOS_CORE &>photos.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -d -type artworks -c $WORKS_CORE &>artworks.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -d -type photographers -c $PHOTOGRAPHERS_CORE &>photographers.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -d -type artists -c $ARTISTS_CORE &>artists.out &
