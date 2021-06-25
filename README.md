# BizHub-app

A Project Management app for android, written in java



* Software languages used :
  *  Java
  *  Js
 
* Android SDK version : 
  * Min 29, Compiled 30
  
* Database :
  * MongoDB Atlas and Realm 


# Screenshots
<img src="https://user-images.githubusercontent.com/25072114/122976227-c607bc80-d3b1-11eb-85cd-4a9f4bac99b8.jpg" width="250" height="500">
<img src="https://user-images.githubusercontent.com/25072114/122976240-cacc7080-d3b1-11eb-8faa-a23b9f4a56de.jpg" width="250" height="500">



## Custom Database 
This app will work properly without any extra configuration needed. But if you want to setup your own database, then follow the steps below.

### Set Up your own Backend 
For Backend, Mongodb Atlas with Mongodb Ream has been used
To set up Backend, check instructions here [Backend Setup](https://github.com/Nishidh25/BizHub/tree/master/bizhub_realm_backend#get-started).

* Schemas can be found [here](https://github.com/Nishidh25/BizHub/tree/master/bizhub_realm_backend/services/mongodb-atlas/rules)

* Functions can be found in this [folder](https://github.com/Nishidh25/BizHub/tree/master/bizhub_realm_backend/functions)


### Link Backend with the android app
1. Change app id. Open realm https://realm.mongodb.com/ -> Realm -> BizHub App -> Copy the App id. 

![image](https://user-images.githubusercontent.com/25072114/122970124-221b1280-d3ab-11eb-8a5b-e3c98de5c666.png)

2. Replace the app id in the app gradle [here](https://github.com/Nishidh25/BizHub/blob/972bb96849b240c7eaf97c7930cc588ff7732ea2/app/build.gradle#L22);

3. Optional: If you enabled google authentication, Copy the same clientID and paste it in Login Activity [here](https://github.com/Nishidh25/BizHub/blob/972bb96849b240c7eaf97c7930cc588ff7732ea2/app/src/main/java/com/bd/bizhub/LoginActivity.java#L176).
