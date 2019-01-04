#!/usr/bin/env bash
 
SCRIPT_PATH=$( cd $(dirname $0) ; pwd -P )
 
# The cypath transformation is needed if we are on Windows
if [ -e "/usr/bin/cygpath" ]
then
  LIB_PATH="$(cygpath -w $SCRIPT_PATH/lib/)*"
else
  LIB_PATH="$SCRIPT_PATH/lib/*"
fi
 
# ${1+"$@"} is mandatory if we want to preserve the quotation of the arguments
# otherwise every space will split an argument in two
# See https://stackoverflow.com/questions/743454/space-in-java-command-line-arguments
$SCRIPT_PATH/jre/bin/java -classpath "$LIB_PATH" ${Main.Class} ${1+"$@"}