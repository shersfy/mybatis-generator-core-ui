@echo off
rem author: shersfy@163.com
rem date: 2020-04-08
cd %~dp0
cd ..

rem set title
rem set /p dhversion= < version/version
title Mybatis Generator UI

set javahome=%JAVA_HOME%
if "%javahome%"=="" (
	echo error: JAVA_HOME not exist  
	pause
)
echo JAVA_HOME=%javahome%

rem read java version
if not exist "%javahome%/bin/java.exe" (
    echo error: %javahome%/bin/java.exe not exist  
    pause
)
if not exist logs (
   md logs
)

"%javahome%/bin/java" -version 2>version/java.version
set /p jversion= < version/java.version
echo JAVA_VERSION=%jversion%

set v1=1.8
set v2=%jversion:~14,3%

if not %v1% equ %v2% (
	echo error: Mybatis UI rely on JRE version least is 1.8
	pause
)

set encoding=-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8

echo Welcome to Mybatis UI
echo %DATE:~0,10% %TIME:~0,8% Mybatis UI is running...

rem set DEBUG_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,address=7788,server=y,suspend=y
"%javahome%/bin/java" %encoding% %DEBUG_OPTS% -cp conf;lib/*;./ org.mybatis.app.boot.BootApplication