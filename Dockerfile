FROM docker-images.labcorp.com/mi/jre21:latest

# Lock container down with SSL
#ENV SERVER_PORT=8443
#ENV SERVER_SSL_KEY_STORE=/etc/ssl/labcorp/camp_default.p12
#ENV SERVER_SSL_KEY_STORE_TYPE=PKCS12
#ENV SERVER_SSL_KEY_ALIAS=tomcat
#ENV SERVER_SSL_KEY_STORE_PASSWORD=changeit

#ADD ./ssl/* /etc/ssl/labcorp/
COPY ./target/eps-scheduler-services.war /opt/apps/eps-scheduler-services.war

WORKDIR /opt/apps

CMD java $JAVA_OPTIONS	\
         -jar \
         eps-scheduler-services.war

#EXPOSE 8443
#EXPOSE 8080
