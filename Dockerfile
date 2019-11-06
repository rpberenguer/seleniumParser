FROM hypriot/rpi-java

ADD /target/selenium-0.0.1-SNAPSHOT-spring-boot.jar /opt/selenium-spring-boot.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/selenium-spring-boot.jar"]