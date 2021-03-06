@echo off
:: base dir
set BUILD=%~dp0
set BASE=%BUILD%\..

:: source
set SOURCE=%BASE%\source

:: lib
set LIB=%BASE%\run\lib

:: clean lib
pushd %LIB%
del /F/S/Q .
popd

:: clean log dir0
rd /S/Q %BASE%\run\log

del /F/S/Q %BASE%\*.zip

pushd %SOURCE%
mvn clean install package -Dmaven.skip.test=true
popd

cd /d %BUILD%
