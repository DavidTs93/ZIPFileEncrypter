@echo off
title ZIP File Encrypter
IF "%1" == "" GOTO :eof
java -jar "%~dp0ZIPFileEncrypter.jar" %1
echo(
pause
:eof