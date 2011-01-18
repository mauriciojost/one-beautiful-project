echo Subjob C started.
ping 127.0.0.1 -n 5 -w 1000 > nul
echo Output of file C. Parameters of the call were %1 %2. > c.txt
copy a.txt copyofAinC1.txt
copy a.txt copyofAinC2.txt
echo Subjob C finished.
