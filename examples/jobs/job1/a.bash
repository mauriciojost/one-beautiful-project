#!/bin/bash

echo Subjob A started.
ping -c 10 127.0.0.1 
copy input.txt a.txt
echo File output of subjob A. Parameters of the call were $1 $2. >> a.txt
echo More processing on subjob A. >> a.txt
echo End processing of subjob A. >> a.txt
echo Subjob A finished.

