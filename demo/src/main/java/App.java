import com.fxquery.$;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

import static com.fxquery.FXQConstant.SLOW;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App  extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXQuery Demo");
        URL url = getClass().getClassLoader().getResource("demo.fxml");
        AnchorPane root = FXMLLoader.load(url);

        $.setDebug(true);

        $ query = $.get(root);

        query.get("container").style("-fx-background-color: orange;").hide(5);


        query.get("hide").click(e->{
            query.get("container").hide(SLOW);
        });

        query.get("show").click(e->{
            query.get("container").show(SLOW);
        });

        query.get("test").click(e->{
            query.get("fadetest").fadeIn(300,null);
        });

        query.get("on").click(e->{
            query.get("fadetest").fadeTo(0.5);
        });

        query.get("knopka").click(e->{
            query.get("container").toggle();
            query.get("child").toggle();
            System.out.println(query.get("visibleTest").visible());
        });


       query.get("visibleTest").style("-fx-background-color: white;").click(e->{
           e.visible(false);
           $.get(e.parent()).style("-fx-background-color: #FFFAAA;");
       });

       query.get("child").style("-fx-background-color: #FFF999");


       query.get("fadeTo").click(t ->{
            query.get("one").fadeTo(0,300);
            query.get("two").fadeTo(0,900);
            query.get("three").fadeTo(0,1200);
       });

       query.get("fadeOn").click(t->{
           query.get("one").fadeIn();
           query.get("two").fadeIn();
           query.get("three").fadeIn();
       });




        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}