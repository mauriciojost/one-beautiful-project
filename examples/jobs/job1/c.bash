#!/bin/bash

echo Subjob C started.
ping 127.0.0.1 -c 10
echo File output of subjob C. Parameters of the call were $1 $2. > c.txt
type a.txt >> c.txt
echo More processing on subjob C... >> c.txt
echo End of processing of subjob C... >> c.txt
echo Subjob C finished.

