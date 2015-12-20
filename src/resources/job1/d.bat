@echo off
echo Subjob D started.
ping 127.0.0.1 -n 25 -w 1000 > nul
echo File output of subjob D. Parameters of the call were %1 %2. > d.txt
echo Taken from B... >> d.txt
type b.txt >> d.txt
echo Taken from C... >> d.txt
type c.txt >> d.txt
echo More processing in subjob D... >> d.txt
echo Subjob D finished.
