echo Subjob D started.
ping 127.0.0.1 -n 25 -w 1000 > nul
echo Output of file D. Parameters of the call were %1 %2. > d.txt
copy b.txt copyofBinD1.txt
copy c.txt copyofCinD2.txt
echo Subjob D finished.
