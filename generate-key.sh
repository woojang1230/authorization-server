#!/bin/bash

docker buildx build --no-cache --target export --output type=local,dest=./output -f keygen.Dockerfile .
