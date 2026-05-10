# SuperDank

**Requirement**:

- java >= 24.0.2

- javafx-sdk >= 24.0.1

How to start:
> jar file in SuperDank/build/libs/
- Linux 

  `java -Xms2G -Xmx2G -XX:+UseG1GC -XX:MaxGCPauseMillis=5 -XX:G1ReservePercent=20 
  -Dprism.order=es2 
  --module-path "path/to/javafx-sdk/lib" 
  --add-modules javafx.controls,javafx.graphics,javafx.fxml,javafx.media 
  -jar path/to/SuperDank-1.jar`
- Window

  `java -Xms2G -Xmx2G -XX:+UseG1GC -XX:MaxGCPauseMillis=5 -XX:G1ReservePercent=20 
    -Dglass.win.uiScale=1 
    -Dprism.allowhidpi=true 
    --module-path "path\to\javafx-sdk-26.0.1\lib" 
    --add-modules javafx.controls,javafx.graphics,javafx.fxml,javafx.media 
    -jar "path\to\SuperDank-1.jar`
> Make sure to put javafx-sdk inside where the jar file is.

Download java at [Oracle](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html)

Download javafx-sdk at [GluonHQ](https://gluonhq.com/products/javafx/)

## How to play


**Controls**

- Movement > W, A, S, D
- Map > TAB
- Dash > SHIFT, RIGHT CLICK
- Skill > [ LEFT CLICK / J ], [ E / K ], [ R / L ]

## Game Manual

*For a **complete guide** and **javadoc** please open the **[Game Manual](https://htmlpreview.github.io/?https://github.com/Fuyuki0/SuperDank/blob/main/SuperDank_Manual.html)***

### Make sure to check it out!
