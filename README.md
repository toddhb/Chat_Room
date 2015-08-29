# Chat Room
To run this program from within Eclipse:
In Project Explorer, right-click on Server.java. Run As...Java Application.
Wait several seconds for the Server to finish initializing.
In Project Explorer, right-click on Main.java.  Run As...Java Application.

Simulation of multiple remote clients:
instanatiate a new Main object for each desired client.

Database of users which have been banned/blocked by the system administrator:
blocked_users.properties

Database of valid usernames and passwords, in plain text:
credentials.properties

Database of all users currently logged in:
active_users.properties

*note: these databases have not yet been encrypted.  They are only plain-text
as of version 1.0.

