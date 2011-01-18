echo Subjob B started.
ping 127.0.0.1 -n 15 -w 1000 > nul
echo Output of file B. Parameters of the call were %1 %2. > b.txt
copy a.txt b2.txt
echo Subjob B finished.
