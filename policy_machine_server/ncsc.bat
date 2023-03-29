CLS
ECHO Building and Deploying [POLICY_SYSTEM]...
mvn clean install && del "E:\apache-tomcat-9.0.62\webapps\pm.war" && move "target\pm.war" "E:\apache-tomcat-9.0.62\webapps"
ECHO Deployed [POLICY_SYSTEM]