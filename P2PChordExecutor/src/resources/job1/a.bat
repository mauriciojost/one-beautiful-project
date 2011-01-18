echo Subjob A started.
ping 127.0.0.1 -n 10 -w 1000 > nul
copy input.txt a.txt
echo Output of file A. Parameters of the call were %1 %2. >> a.txt
echo Subjob A finished.