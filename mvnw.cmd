@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM   https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.

@REM ---------------------------------------------------------------------------
@REM Apache Maven Wrapper startup script — Windows (cmd.exe)
@REM Version : 3.3.2  /  Cible Maven : 3.9.9
@REM ---------------------------------------------------------------------------

@echo off
setlocal enableextensions enabledelayedexpansion

@REM Résolution du répertoire du projet
IF "%MAVEN_PROJECTBASEDIR%"=="" (
  SET "MAVEN_PROJECTBASEDIR=%~dp0"
  IF "!MAVEN_PROJECTBASEDIR:~-1!"=="\" SET "MAVEN_PROJECTBASEDIR=!MAVEN_PROJECTBASEDIR:~0,-1!"
)

SET "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET "WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"

@REM Téléchargement du JAR du wrapper s'il est absent
IF NOT EXIST "%WRAPPER_JAR%" (
  FOR /F "usebackq tokens=1,* delims==" %%A IN ("%WRAPPER_PROPERTIES%") DO (
    IF "%%A"=="wrapperUrl" SET "DOWNLOAD_URL=%%B"
  )
  echo [mvnw] Telechargement de maven-wrapper.jar depuis :
  echo        !DOWNLOAD_URL!
  powershell -Command ^
    "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; " ^
    "(New-Object System.Net.WebClient).DownloadFile('!DOWNLOAD_URL!', '%WRAPPER_JAR%')"
  IF ERRORLEVEL 1 (
    echo [mvnw] ERREUR : impossible de telecharger maven-wrapper.jar >&2
    exit /B 1
  )
)

@REM Localisation de Java
IF NOT "%JAVA_HOME%"=="" (
  SET "JAVACMD=%JAVA_HOME%\bin\java.exe"
) ELSE (
  SET "JAVACMD=java.exe"
)

WHERE "%JAVACMD%" >NUL 2>&1
IF ERRORLEVEL 1 (
  WHERE java.exe >NUL 2>&1
  IF ERRORLEVEL 1 (
    echo [mvnw] ERREUR : Java introuvable. Definissez JAVA_HOME ou ajoutez java au PATH. >&2
    exit /B 1
  )
  SET "JAVACMD=java.exe"
)

@REM Affichage de la version Java pour diagnostic
FOR /F "tokens=*" %%V IN ('"%JAVACMD%" -version 2^>^&1 ^| findstr /i "version"') DO (
  echo [mvnw] %%V
  goto :java_version_done
)
:java_version_done

@REM Lancement de Maven via le wrapper
"%JAVACMD%" ^
  %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% ^
  -classpath "%WRAPPER_JAR%" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  "org.apache.maven.wrapper.MavenWrapperMain" ^
  %*

SET MAVEN_EXIT_CODE=%ERRORLEVEL%
exit /B %MAVEN_EXIT_CODE%
