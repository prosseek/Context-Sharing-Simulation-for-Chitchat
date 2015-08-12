#! /bin/sh
GCPLIBS=lib/scala-library.jar:lib
java -Xmx512M -cp .:lib/ECLA.jar:lib/DTNConsoleConnection.jar:$GCPLIBS core.DTNSim -b 1 wdm_settings/smcho/context_sharing_settings.txt