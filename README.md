## Starting the application 

Make sure to enter the project directory for file reading/writing to work correctly.  

```sh
cd TDT4100-PROJECT 
```

Make sure that Java-FX verison is currect for your system in `billing/pom.xml`. For Mac's with M1 chipset you need `18-ea+6` for the application to run correctly. The app has been developed on an M1 mac so this version is set. 

Run MainApp in 
```sh
billing/src/main/java/billing_app/MainApp.java
```


## Example savefile

The savefile located in
```sh
/exampleCompanySave/fiskermann_as.txt
```
will load all data correctly, but since the path for the logo is already set the logo will not be loaded and an error message will be shown. Alternativly change the path in the savefile to the location of the picture located in the folder. 


