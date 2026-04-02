# EPS Scheduler Spring Boot Service

This repository contains EPS Scheduler application migrated from old application.

---

## How to run application locally

### Required JVM args

```
-Dcom.labcorp.app.env=local
```

### Required Environment variables

```
database.username=[DB_CONNECTION_STRING];
database.password=[USERNAME];
database.connection.string=[PASSWORD];
quartz.database.username=[USERNAME]
quartz.database.password=[PASSWORD]
spring.profiles.active=local
```

### Eclipse

To import the project, go to `File` > `Import`.

![img.png](readme-img/img.png)

Search for `Maven` and chose `Existing Maven Project`.

![img_1.png](readme-img/img_1.png)

Select directory of the project in `Root Directory` selector, select parent pom and click `Finish`
button.

![img_2.png](readme-img/img_2.png)

To set up the running configuration, click on small down arrow next to the green circular button with white triangle and
chose `Run configurations...`.

![img_3.png](readme-img/img_3.png)

In this window make sure that `Main class` is pointing to the main class of your spring boot
application.

![img_4.png](readme-img/img_4.png)

To finish the setup process, you need to add environment variables and VM options for this application.

To add VM options,
open `Arguments` tab and put all **Required JVM args** from the top of this document to the `VM arguments` field.
(Make sure that all lines started with `-D`);

![img_5.png](readme-img/img_5.png)

To add environment variables, open `Environment` tab and put all **Required Environment variables** from the top of this document.

![img_6.png](readme-img/img_6.png)

### IntelliJ IDEA

To open the project, go to `File` > `Open`.

![img_7.png](readme-img/img_7.png)

Select directory of the project and click `OK`.

![img_8.png](readme-img/img_8.png)

To set up the running configuration,
click on small down arrow next to the selected button in the right-top corner of the IDE and
chose `Edit configurations...`.

![img_9.png](readme-img/img_9.png)

Make sure that the main class of spring boot application is chosen correctly.

![img_10.png](readme-img/img_10.png)

To finish the setup process, you need to add environment variables and VM options for this application.

To add VM options, click on `Modify options` button and select `Add VM options`;

![img_11.png](readme-img/img_11.png)

Put all **Required JVM args** from the top of this document to the new field that should have appeared.
(Make sure that all lines started with `-D`).

![img_12.png](readme-img/img_12.png)

To add environment variables, click on `Modify options` button and select `Environment variables`;

![img_13.png](readme-img/img_13.png)

Put all **Required Environment variables** from the top of this document to the new field that should have appeared.
(Paste a string of values separated by semicolon)

![img_14.png](readme-img/img_14.png)
# quartz_scheduler
