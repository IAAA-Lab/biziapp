!#/bin/bash
# version 1.0
# autor: Daniel Cabrera
# https://www.tutorialspoint.com/sqoop/sqoop_installation.htm
# Install SQOOP and configuration
tar -xvf /compartida/sqoop-1.4.6.bin__hadoop-2.0.4-alpha.tar.gz --directory /compartida
mkdir /usr/lib/sqoop
mv /compartida/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/* /usr/lib/sqoop
echo "Configuring bashrc"
echo "export SQOOP_HOME=/usr/lib/sqoop export PATH=$PATH:$SQOOP_HOME/bin" >> ~/.bashrc
source ~/.bashrc
echo "Configuring Sqoop"
cp /compartida/sqoop-env.sh $SQOOP_HOME/conf/

# Download and Configure mysql-connector-java
tar -zxf /compartida/mysql-connector-java-5.1.43.tar.gz --directory /compartida
mv /compartida/mysql-connector-java-5.1.43/mysql-connector-java-5.1.43-bin.jar /usr/lib/sqoop/lib

source ~/.bashrc

rm -f $SQOOP_HOME/conf/sqoop-env-template.sh
