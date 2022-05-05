#!/bin/bash

source .env

nohup java -jar target/indexing-api-0.1-assembly.jar -u -type repositories -c $REPOSITORY_CORE &>repositories.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -u -type photos -c $PHOTOS_CORE &>photos.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -u -type artworks -c $WORKS_CORE &>artworks.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -u -type photographers -c $PHOTOGRAPHERS_CORE &>photographers.out &
nohup java -jar target/indexing-api-0.1-assembly.jar -u -type artists -c $ARTISTS_CORE &>artists.out &
