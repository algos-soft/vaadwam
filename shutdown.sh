#!/bin/sh
# -----------------------------------------------------------------------------
# Shutdown Script for a SpringBoot jar application
# -----------------------------------------------------------------------------
kill $(cat ./pid.file)
echo SHUTTING DOWN...
