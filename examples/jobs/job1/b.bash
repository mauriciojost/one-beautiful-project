#!/bin/bash

echo Subjob B started.
ping 127.0.0.1 -c 15 
echo File output of subjob B. Parameters of the call were $1 $2. > b.txt
type a.txt >> b.txt
echo More processing on subjob B... >> b.txt
echo End of processing of subjob B... >> b.txt
echo Subjob B finished.

