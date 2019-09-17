#!/bin/sh
# -----------------------------------------------------------------------------
# Start Script for a SpringBoot jar application
# -----------------------------------------------------------------------------
java -jar -Dserver.port=8090 vaadwam.jar & echo $! > ./pid.file &
