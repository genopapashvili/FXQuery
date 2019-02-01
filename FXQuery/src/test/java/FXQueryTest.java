import com.fxquery.$;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JfxRunner.class)
public class FXQueryTest {

    @Test
    @TestInJfxThread
    public void GroupChildTest() throws Exception{

        Pane root = new Pane();
        Group group = new Group();

        root.getChildren().add(group);

        Button button = new Button();
        button.setId("childButton");
        group.getChildren().add(button);


        assertNotNull($.get(root).find("childButton"));
    }

    @Test
    @TestInJfxThread
    public void ScrollPaneChildTest() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        assertNotNull($.get(root).find("scrollPaneChildTest"));
    }

    @Test
    @TestInJfxThread
    public void AccordionChildTitledPaneTest() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        assertNotNull($.get(root).find("accoTitledPane"));
    }


    @Test
    @TestInJfxThread
    public void AccordionChildTitledPaneChdildTest() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
       assertNotNull($.get(root).find("accTitlePaneChild"));
    }

    @Test
    @TestInJfxThread
    public void DialogPaneTest() throws Exception{
        Parent r = FXMLLoader.load(getClass().getResource("dialogPaneTest.fxml"));
        assertNotNull($.get(r).find("dialogPaneChild"));
    }


    @Test
    @TestInJfxThread
    public void ifNotFind() throws Exception{
        StackPane r = new StackPane();
        assertNull($.get(r).find("ifNotFind"));
    }

    @Test
    @TestInJfxThread
    public void TabPaneTab() throws Exception{
        Parent r = FXMLLoader.load(getClass().getResource("tabPaneTest.fxml"));
        assertNotNull($.get(r).find("tabId"));
    }

    @Test
    @TestInJfxThread
    public void TabPaneTabChild() throws Exception{
        Parent r = FXMLLoader.load(getClass().getResource("tabPaneTest.fxml"));
        assertNotNull($.get(r).find("tabChild"));
    }

    @Test
    @TestInJfxThread
    public void ButtonBarChild() throws Exception{
        ButtonBar buttonBar = new ButtonBar();
        Button btn = new Button();
        btn.setId("buttonBarTest");
        buttonBar.getButtons().add(btn);

        assertNotNull($.get(buttonBar).find("buttonBarTest"));
    }
}
