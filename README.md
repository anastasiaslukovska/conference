# conference
BE: Spring Boot, Spring Data Jpa (in memory db, auto fill), Spring Security, Gradle / Maven, jUnit

FE:  Bootstrap, HTML, CSS, jQuery(Select2)

The user is able to register for the conference and view the presentations. Presenters can create presentations and choose classroom time for it. The presentations do not overlap each other in time in one classroom, i.e. there cannot be 2 lectures in the room at the same time. The schedule is accessible via the REST API.  

The application contains a minimum set of tables.  
Tables: 
User - contains user data, password (encrypted), role, etc. 
Presentation - Contains data about the presentation. One user can have many presentations, and one presentation can have many presenters. 
Schedule - schedule, in which room, when and which presentation is taking place 
Room - the audience where the presentation takes place
