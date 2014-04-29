@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\mockito\mockito-all\1.9.5\mockito-all-1.9.5.jar;"%REPO%"\org\apache\logging\log4j\log4j-api\2.0-rc1\log4j-api-2.0-rc1.jar;"%REPO%"\org\apache\logging\log4j\log4j-core\2.0-rc1\log4j-core-2.0-rc1.jar;"%REPO%"\org\log4mongo\log4mongo-java\0.7.4\log4mongo-java-0.7.4.jar;"%REPO%"\commons-codec\commons-codec\1.7\commons-codec-1.7.jar;"%REPO%"\cglib\cglib\2.2.2\cglib-2.2.2.jar;"%REPO%"\asm\asm\3.3.1\asm-3.3.1.jar;"%REPO%"\com\ulflander\JPipes\0.1.0\JPipes-0.1.0.jar;"%REPO%"\org\apache\commons\commons-lang3\3.1\commons-lang3-3.1.jar;"%REPO%"\com\google\code\gson\gson\2.2.4\gson-2.2.4.jar;"%REPO%"\com\hazelcast\hazelcast\3.1.5\hazelcast-3.1.5.jar;"%REPO%"\javax\mail\mail\1.4\mail-1.4.jar;"%REPO%"\javax\activation\activation\1.1\activation-1.1.jar;"%REPO%"\com\rabbitmq\amqp-client\3.3.0\amqp-client-3.3.0.jar;"%REPO%"\org\mongodb\mongo-java-driver\2.11.4\mongo-java-driver-2.11.4.jar;"%REPO%"\org\apache\pdfbox\pdfbox\1.8.4\pdfbox-1.8.4.jar;"%REPO%"\org\apache\pdfbox\fontbox\1.8.4\fontbox-1.8.4.jar;"%REPO%"\org\apache\pdfbox\jempbox\1.8.4\jempbox-1.8.4.jar;"%REPO%"\commons-logging\commons-logging\1.1.1\commons-logging-1.1.1.jar;"%REPO%"\org\apache\tika\tika-parsers\1.4\tika-parsers-1.4.jar;"%REPO%"\org\apache\tika\tika-core\1.4\tika-core-1.4.jar;"%REPO%"\org\gagravarr\vorbis-java-tika\0.1\vorbis-java-tika-0.1.jar;"%REPO%"\edu\ucar\netcdf\4.2-min\netcdf-4.2-min.jar;"%REPO%"\org\apache\james\apache-mime4j-core\0.7.2\apache-mime4j-core-0.7.2.jar;"%REPO%"\org\apache\james\apache-mime4j-dom\0.7.2\apache-mime4j-dom-0.7.2.jar;"%REPO%"\org\apache\commons\commons-compress\1.5\commons-compress-1.5.jar;"%REPO%"\org\tukaani\xz\1.2\xz-1.2.jar;"%REPO%"\org\bouncycastle\bcmail-jdk15\1.45\bcmail-jdk15-1.45.jar;"%REPO%"\org\bouncycastle\bcprov-jdk15\1.45\bcprov-jdk15-1.45.jar;"%REPO%"\org\apache\poi\poi\3.9\poi-3.9.jar;"%REPO%"\org\apache\poi\poi-scratchpad\3.9\poi-scratchpad-3.9.jar;"%REPO%"\org\apache\poi\poi-ooxml\3.9\poi-ooxml-3.9.jar;"%REPO%"\org\apache\poi\poi-ooxml-schemas\3.9\poi-ooxml-schemas-3.9.jar;"%REPO%"\org\apache\xmlbeans\xmlbeans\2.3.0\xmlbeans-2.3.0.jar;"%REPO%"\dom4j\dom4j\1.6.1\dom4j-1.6.1.jar;"%REPO%"\org\apache\geronimo\specs\geronimo-stax-api_1.0_spec\1.0.1\geronimo-stax-api_1.0_spec-1.0.1.jar;"%REPO%"\org\ccil\cowan\tagsoup\tagsoup\1.2.1\tagsoup-1.2.1.jar;"%REPO%"\org\ow2\asm\asm-debug-all\4.1\asm-debug-all-4.1.jar;"%REPO%"\com\googlecode\mp4parser\isoparser\1.0-RC-1\isoparser-1.0-RC-1.jar;"%REPO%"\org\aspectj\aspectjrt\1.6.11\aspectjrt-1.6.11.jar;"%REPO%"\com\drewnoakes\metadata-extractor\2.6.2\metadata-extractor-2.6.2.jar;"%REPO%"\com\adobe\xmp\xmpcore\5.1.2\xmpcore-5.1.2.jar;"%REPO%"\rome\rome\0.9\rome-0.9.jar;"%REPO%"\jdom\jdom\1.0\jdom-1.0.jar;"%REPO%"\org\gagravarr\vorbis-java-core\0.1\vorbis-java-core-0.1.jar;"%REPO%"\com\googlecode\juniversalchardet\juniversalchardet\1.0.3\juniversalchardet-1.0.3.jar;"%REPO%"\de\l3s\boilerpipe\boilerpipe\1.2.0\boilerpipe-1.2.0.jar;"%REPO%"\net\sourceforge\nekohtml\nekohtml\1.9.20\nekohtml-1.9.20.jar;"%REPO%"\xerces\xercesImpl\2.9.1\xercesImpl-2.9.1.jar;"%REPO%"\xml-apis\xml-apis\1.3.04\xml-apis-1.3.04.jar;"%REPO%"\org\jsoup\jsoup\1.7.3\jsoup-1.7.3.jar;"%REPO%"\com\joestelmach\natty\0.8\natty-0.8.jar;"%REPO%"\org\antlr\antlr-runtime\3.2\antlr-runtime-3.2.jar;"%REPO%"\org\antlr\stringtemplate\3.2\stringtemplate-3.2.jar;"%REPO%"\antlr\antlr\2.7.7\antlr-2.7.7.jar;"%REPO%"\org\mnode\ical4j\ical4j\1.0.2\ical4j-1.0.2.jar;"%REPO%"\commons-lang\commons-lang\2.6\commons-lang-2.6.jar;"%REPO%"\backport-util-concurrent\backport-util-concurrent\3.1\backport-util-concurrent-3.1.jar;"%REPO%"\org\apache\opennlp\opennlp-tools\1.5.3\opennlp-tools-1.5.3.jar;"%REPO%"\org\apache\opennlp\opennlp-maxent\3.0.3\opennlp-maxent-3.0.3.jar;"%REPO%"\net\sf\jwordnet\jwnl\1.3.3\jwnl-1.3.3.jar;"%REPO%"\org\openrdf\sesame\sesame-repository-api\2.7.9\sesame-repository-api-2.7.9.jar;"%REPO%"\org\openrdf\sesame\sesame-query\2.7.9\sesame-query-2.7.9.jar;"%REPO%"\org\openrdf\sesame\sesame-rio-api\2.7.9\sesame-rio-api-2.7.9.jar;"%REPO%"\org\openrdf\sesame\sesame-model\2.7.9\sesame-model-2.7.9.jar;"%REPO%"\org\openrdf\sesame\sesame-util\2.7.9\sesame-util-2.7.9.jar;"%REPO%"\org\slf4j\slf4j-api\1.6.1\slf4j-api-1.6.1.jar;"%REPO%"\edu\stanford\stanford-postagger\3.3.1\stanford-postagger-3.3.1.jar;"%REPO%"\nz\ac\waikato\cms\weka\weka-stable\3.6.10\weka-stable-3.6.10.jar;"%REPO%"\net\sf\squirrel-sql\thirdparty-non-maven\java-cup\0.11a\java-cup-0.11a.jar;"%REPO%"\com\maxmind\geoip2\geoip2\0.7.0\geoip2-0.7.0.jar;"%REPO%"\com\maxmind\db\maxmind-db\0.3.1\maxmind-db-0.3.1.jar;"%REPO%"\com\google\http-client\google-http-client\1.17.0-rc\google-http-client-1.17.0-rc.jar;"%REPO%"\com\google\code\findbugs\jsr305\1.3.9\jsr305-1.3.9.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.0.1\httpclient-4.0.1.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.0.1\httpcore-4.0.1.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.2.3\jackson-databind-2.2.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.2.3\jackson-annotations-2.2.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.2.3\jackson-core-2.2.3.jar;"%REPO%"\Minethat\Minethat\0.1.0\Minethat-0.1.0.jar
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="extractor_service" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" com.ulflander.app.services.ExtractorService %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
