@echo off
:: base dir
set BASE=%~dp0\..
:: lib
set LIB=%BASE%\lib\
:: log4j
set LOG4J=%BASE%\etc\log4j.properties

set JVM_OPT=-Dlog4j=%LOG4J%
set JVM_OPT=%JVM_OPT% -Droot.dir=%BASE%
set JVM_OPT=%JVM_OPT% -Djava.ext.dirs=%LIB%
set MAIN_CLASS=cn.letterme.tools.patcher.GUILauncher

java %JVM_OPT% %MAIN_CLASS%
