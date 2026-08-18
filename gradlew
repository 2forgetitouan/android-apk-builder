#!/bin/sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Some APK build services extract the downloaded Gradle distribution without
# preserving the executable bit on bin/gradle. Retry after fixing it.
java -jar "$WRAPPER_JAR" "$@"
status=$?
if [ $status -ne 0 ]; then
    if [ -d "$HOME/.gradle/wrapper/dists" ]; then
        find "$HOME/.gradle/wrapper/dists" -type f -path '*/bin/gradle' -exec chmod +x {} \; 2>/dev/null
    fi
    java -jar "$WRAPPER_JAR" "$@"
    status=$?
fi
exit $status
