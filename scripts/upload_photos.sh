#!/bin/bash

source .env


nohup java -jar target/indexing-api-0.1-assembly.jar -u -type photos -c $PHOTOS_CORE &>photos.out &



