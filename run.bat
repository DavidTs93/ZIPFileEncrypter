@echo off
set arg1=%1
java -jar "%~dp0ZIPFileEncrypter.jar" %arg1%
echo Done!
echo If you paid for this you've been fooled! :)
pause