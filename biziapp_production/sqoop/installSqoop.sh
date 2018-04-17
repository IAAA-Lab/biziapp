#!/bin/bash
# version 1.0
# autor: Daniel Cabrera
# https://www.tutorialspoint.com/sqoop/sqoop_installation.htm

# Remove previsous installations
rm -r /usr/lib/sqoop/

mkdir /usr/lib/sqoop
tar -zxf ./sqoop-1.4.6.bin__hadoop-2.0.4-alpha.tar.gz --directory .
mv ./sqoop-1.4.6.bin__hadoop-2.0.4-alpha/* /usr/lib/sqoop/

echo "Configuring bashrc"
echo "export SQOOP_HOME=/usr/lib/sqoop export" >> ~/.bashrc
echo "export PATH=$PATH:$SQOOP_HOME/bin" >> ~/.bashrc
source ~/.bashrc

echo "Configuring Sqoop"
cp ./sqoop-env.sh $SQOOP_HOME/conf

# Download and Configure mysql-connector-java
tar -zxf ./mysql-connector-java-5.1.43.tar.gz --directory .
mv ./mysql-connector-java-5.1.43/mysql-connector-java-5.1.43-bin.jar /usr/lib/sqoop/lib

source ~/.bashrc

rm -f $SQOOP_HOME/conf/sqoop-env-template.sh
