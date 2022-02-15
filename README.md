# Keycloak Session Restrictor

A demo event listener for Keycloak, allowing only the last session to survive, if a user logs in on multiple browsers/devices.



 - for build -> run on cmd below command


```
mvn clean package
```

 - copy "\\target\\*.jar" file to keycloak standalone\deployments folder
 
 - add session-restrictor to "Event Listeners" on keycloak

![image](https://user-images.githubusercontent.com/2211635/154008083-14da524a-44b7-4826-856d-4dd7ca133aa3.png)
