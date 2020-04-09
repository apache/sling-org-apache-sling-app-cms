#!/bin/bash

PACKAGE=$1
unzip $PACKAGE -d out
IFS=$'\n'
for CONTENT_XML in $(find out -name .content.xml) 
do
    echo "Replacing Sling Namespace in ${CONTENT_XML}"
    sed -i "" "s|http\://www.sling.apache.org/sling/1.0|http://sling.apache.org/jcr/sling/1.0|g" "$CONTENT_XML"
done
for CND in $(find . -name *.cnd) 
do
    echo "Replacing Sling Namespace in ${CND}"
    sed -i "" "s|http\://www.sling.apache.org/sling/1.0|http://sling.apache.org/jcr/sling/1.0|g" "$CND"
done

cd out
zip -r "../replaced-$PACKAGE" * 
cd ..
rm -rf out
echo "Sling Namespace replaced successfully in replaced-$PACKAGE"