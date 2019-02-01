# JavaFX FXQuery
This project is not complete...
if you see a bug,please fix it

tutorial: <br>
```
Parent root = ...

$ fxq = $.get(root);
fxq.get("child-id").fadeOn();
```
```
Parent root = ...

$.get(root).get("child-id").[style("...")].[setText()].[...];
```


```
Parent root = ...

Node child = $.get(root).find("child-id");
```
