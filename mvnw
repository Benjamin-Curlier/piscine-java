#!/bin/sh
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup script — shell (POSIX)
# Version : 3.3.2  /  Cible Maven : 3.9.9
# ----------------------------------------------------------------------------

set -e

# Résolution du répertoire du projet
if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
  SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
  MAVEN_PROJECTBASEDIR="$SCRIPT_DIR"
fi

WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

# Téléchargement du JAR du wrapper s'il est absent
if [ ! -f "$WRAPPER_JAR" ]; then
  DOWNLOAD_URL=$(grep '^wrapperUrl' "$WRAPPER_PROPERTIES" | sed 's/^wrapperUrl=//')
  echo "[mvnw] Téléchargement de maven-wrapper.jar depuis :"
  echo "       $DOWNLOAD_URL"
  if command -v curl > /dev/null 2>&1; then
    curl -fsSL -o "$WRAPPER_JAR" "$DOWNLOAD_URL"
  elif command -v wget > /dev/null 2>&1; then
    wget -q -O "$WRAPPER_JAR" "$DOWNLOAD_URL"
  else
    echo "[mvnw] ERREUR : ni curl ni wget disponibles. Installez l'un d'eux." >&2
    exit 1
  fi
fi

# Localisation de Java
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

if ! command -v "$JAVACMD" > /dev/null 2>&1; then
  echo "[mvnw] ERREUR : Java introuvable. Définissez JAVA_HOME ou ajoutez java au PATH." >&2
  exit 1
fi

# Affichage de la version Java pour diagnostic
JAVA_VERSION=$("$JAVACMD" -version 2>&1 | head -1)
echo "[mvnw] $JAVA_VERSION"

# Lancement de Maven via le wrapper
exec "$JAVACMD" \
  $MAVEN_OPTS \
  $MAVEN_DEBUG_OPTS \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  "org.apache.maven.wrapper.MavenWrapperMain" \
  "$@"
