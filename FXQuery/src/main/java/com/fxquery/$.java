/*
Copyright 2018 g. papashvili

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.fxquery;

import com.fxquery.event.ChangeEvent;
import com.fxquery.event.SelectedTarget;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.fxquery.FXQConstant.FAST;
import static com.fxquery.FXQConstant.SLOW;

public class $ {

    private static Boolean debug = false;

    public static Logger logger = Logger.getLogger("JFXQuery");

    private Object target;
    private boolean hideLock = false;


    private $(Object target) {
        this.target = target;
    }

    public static $ get(Object node) {
        return new $(node);
    }

    public String id() {
        String id = null;
        String method = "getId";
        try {
            id = (String) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return id;
    }

    public $ id(String id) {
        String method = "setId";
        try {
            target.getClass().getMethod(method, String.class).invoke(target, id);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public $ get(String id) {
        $ dollar = null;
        try {
            dollar = new $(find(target, id));
            if (dollar.target == null && debug) {
                logger.log(Level.INFO, "Could not find id '" + id + "'");
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            if (debug) {
                logger.log(Level.SEVERE, "Bug", e);
            }
        }
        return dollar;
    }

    private Object find(Object target, String id) throws InvocationTargetException, IllegalAccessException {
        if (target instanceof Node) {
            Node t = (Node) target;
            if (t.getId() != null && t.getId().equals(id)) {
                return target;
            }
        } else if (target instanceof Tab) {
            Tab t = (Tab) target;
            if (t.getId() != null && t.getId().equals(id)) {
                return target;
            }
        } else {
            return null;
        }


        Object obj = null;
        for (Method m : target.getClass().getMethods()) {
            if (m.getName().equals("getChildren")
                    || m.getName().equals("getItems")
                    || m.getName().equals("getPanes")
                    || m.getName().equals("getTabs")
                    || m.getName().equals("getButtons")) {

                ObservableList<?> children = (ObservableList<?>) m.invoke(target);
                if (children.size() > 0 && (children.get(0) instanceof Node || children.get(0) instanceof Tab)) {
                    for (Object o : children) {
                        obj = find(o, id);
                        if (obj != null) {
                            return obj;
                        }
                    }
                }

            }
            if (m.getName().equals("getContent") || m.getName().equals("getClip")
                    || m.getName().equals("getGraphic")) {
                Object o = m.invoke(target);
                obj = find(o, id);
                if (obj != null) {
                    return obj;
                }

            }

        }
        return obj;
    }

    public Object find(String id) {
        try {
            return find(target, id);
        } catch (Exception e) {
            if (debug) {
                logger.log(Level.SEVERE, null, e);
            }
            return null;
        }
    }

    public $ text(String text) {
        String method = "setText";
        try {
            target.getClass().getMethod(method, String.class).invoke(target, text);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public String text() {
        String method = "getText";
        String text = null;
        try {
            text = (String) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return text;
    }

    public $ style(String style) {
        String method = "setStyle";
        String existing = this.style();
        if (existing == null) existing = "";
        existing += style;
        try {
            target.getClass().getMethod(method, String.class).invoke(target, existing);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public String style() {
        String method = "getStyle";
        String style = null;
        try {
            style = (String) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return style;
    }

    public $ click(SelectedTarget callBack) {
        EventHandler<MouseEvent> run = e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (callBack != null) {
                    callBack.handle(this);

                }
            }
        };

        String method = "setOnMouseClicked";
        try {
            target.getClass().getMethod(method, EventHandler.class).invoke(target, run);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }


    public static void setDebug(Boolean debug) {
        $.debug = debug;
    }


    private void logNotContains(String method) {
        if (target != null && debug) {
            logger.log(Level.INFO, target.getClass().getSimpleName() + " does not contains '" + method + "' method");
        }
    }

    public $ hide(int speed, SelectedTarget callBack) {
        String getClip = "getClip";
        String setClip = "setClip";
        try {
            Node clip = (Node) target.getClass().getMethod(getClip).invoke(target);
            if (!(clip != null && clip instanceof QueryHelper)) {

                if (maxWidth() <= 0) {
                    maxWidth(width());
                }
                if (maxHeight() <= 0) {
                    maxHeight(height());
                }

                clip = new QueryHelper(maxWidth(), maxHeight());
                target.getClass().getMethod(setClip, Node.class).invoke(target, clip);
            }
            QueryHelper helper = (QueryHelper) clip;
            if (helper.getHeight() == helper.getOriginalHeight()) {
                Transition transition = new Transition() {
                    {
                        setCycleDuration(Duration.millis(speed));
                    }

                    @Override
                    protected void interpolate(double frac) {
                        helper.setHeight(helper.getOriginalHeight() - helper.getOriginalHeight() * frac);
                    }
                };
                if (callBack != null) {
                    transition.setOnFinished(e -> {
                        callBack.handle(this);
                    });
                }
                transition.play();
            }

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logNotContains(getClip + "' or '" + setClip);
        }
        return this;
    }

    public $ hide() {
        return hide(FAST);
    }

    public $ hide(int speed) {
        return this.hide(speed, null);
    }

    public $ hide(SelectedTarget callBack) {
        return hide(FAST, callBack);
    }

    public $ show(int speed) {
        return show(speed, null);
    }

    public $ show() {
        return show(FAST, null);
    }

    public $ show(SelectedTarget callBack) {
        return show(FAST, callBack);
    }

    public $ show(int speed, SelectedTarget callBack) {

        String getClip = "getClip";
        String setClip = "setClip";
        try {
            Node clip = (Node) target.getClass().getMethod(getClip).invoke(target);
            if (!(clip != null && clip instanceof QueryHelper)) {

                if (maxWidth() <= 0) {
                    maxWidth(width());
                }
                if (maxHeight() <= 0) {
                    maxHeight(height());
                }

                clip = new QueryHelper(maxWidth(), maxHeight());
                target.getClass().getMethod(setClip, Node.class).invoke(target, clip);
            }
            QueryHelper helper = (QueryHelper) clip;
            if (helper.getHeight() == 0) {
                Transition transition = new Transition() {
                    {
                        setCycleDuration(Duration.millis(speed));
                    }

                    @Override
                    protected void interpolate(double frac) {
                        helper.setHeight(helper.getOriginalHeight() - helper.getOriginalHeight() * (-frac + 1));
                    }
                };
                if (callBack != null) {
                    transition.setOnFinished(e -> {
                        callBack.handle(this);
                    });
                }

                transition.play();
            }

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logNotContains(getClip + "' or '" + setClip);
        }

        return this;
    }

    public Parent parent() {
        Parent parent = null;
        String method = "getParent";
        try {
            parent = (Parent) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return parent;
    }


    //width

    public double width() {
        String method = "getWidth";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return 0;
    }

    public $ width(double width) {
        String method = "setWidth";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, width);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    //height
    public double height() {
        String method = "getHeight";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return 0;
    }

    public $ height(double height) {
        String method = "setHeight";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, height);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    //pref
    public double prefWidth() {
        String method = "getPrefWidth";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ prefWidth(double width) {
        String method = "setPrefWidth";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, width);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public double prefHeight() {
        String method = "getPrefHeight";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ prefHeight(double width) {
        String method = "setPrefHeight";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, width);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    //max
    public double maxWidth() {
        String method = "getMaxWidth";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ maxWidth(double width) {
        String method = "setMaxWidth";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, width);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public double maxHeight() {
        String method = "getMaxHeight";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ maxHeight(double height) {
        String method = "setMaxHeight";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, height);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }


    //min
    public double minWidth() {
        String method = "getMinWidth";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ minWidth(double width) {
        String method = "setMinWidth";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, width);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public double minHeight() {
        String method = "getMinHeight";
        try {
            return (double) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }

        return Double.NEGATIVE_INFINITY;
    }

    public $ minHeight(double height) {
        String method = "setMinHeight";
        try {
            target.getClass().getMethod(method, Double.TYPE).invoke(target, height);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }


    public $ toggle(int speed, ChangeEvent callBack) {
        String getClip = "getClip";
        String setClip = "setClip";
        try {
            Node clip = (Node) target.getClass().getMethod(getClip).invoke(target);
            if (!(clip != null && clip instanceof QueryHelper)) {
                if (maxWidth() <= 0) {
                    maxWidth(width());
                }
                if (maxHeight() <= 0) {
                    maxHeight(height());
                }

                clip = new QueryHelper(maxWidth(), maxHeight());
                target.getClass().getMethod(setClip, Node.class).invoke(target, clip);
            }
            QueryHelper helper = (QueryHelper) clip;
            if (helper.getHeight() == 0) {
                if (callBack != null) {
                    show(speed, e -> {
                        callBack.change(ChangeEvent.SHOW);
                    });
                } else {
                    show(speed);
                }
            } else {
                if (callBack != null) {
                    hide(speed, e -> {
                        callBack.change(ChangeEvent.HIDE);
                    });
                } else {
                    hide(speed);
                }

            }

        } catch (Exception e) {
            logNotContains(getClip + "' or '" + setClip);
        }
        return this;
    }


    public $ toggle(int speed) {
        return toggle(speed, null);
    }

    public $ toggle() {
        return toggle(FAST);
    }


    public $ toggle(ChangeEvent callBack) {
        return toggle(FAST, callBack);
    }


    public Object get() {
        return target;
    }

    public $ visible(boolean value) {
        String method = "setVisible";
        try {
            target.getClass().getMethod(method, Boolean.TYPE).invoke(target, value);
        } catch (Exception e) {
            logNotContains(method);
        }
        return this;
    }

    public boolean visible() {
        String method = "isVisible";
        try {
          return (boolean) target.getClass().getMethod(method).invoke(target);
        } catch (Exception e) {
            logNotContains(method);
        }
        return false;
    }


    public $ fadeTo(double value,int speed, SelectedTarget callBack) {

            if (target instanceof Node) {
                Node target = (Node) this.target;
                if(target.getAccessibleHelp() == null) {
                    target.setAccessibleHelp(target.getClass().getSimpleName());
                    if (value < 0.0) {
                        value = 0;
                    } else if (value > 1.0) {
                        value = 1.0;
                    }
                    FadeTransition transition = new FadeTransition(Duration.millis(speed), target);
                    transition.setToValue(value);

                        transition.setOnFinished(e -> {
                            if (callBack != null) {
                                callBack.handle(this);
                            }
                            target.setAccessibleHelp(null);
                        });

                    transition.play();
                }
            }

            return this;
    }

    public $ fadeTo(double value,int speed){
        return fadeTo(value,speed,null);
    }

    public $ fadeTo(double value){
        return fadeTo(value,FAST,null);
    }

    public $ fadeTo(double value,SelectedTarget callBack){
        return fadeTo(value,FAST,callBack);
    }

    public $ fadeIn(int speed,SelectedTarget callBack){
        return fadeTo(1.0,speed,callBack);
    }

    public $ fadeIn(int speed){
        return fadeIn(speed,null);
    }

    public $ fadeIn(){
        return fadeIn(FAST,null);
    }

    public $ fadeOut(int speed,SelectedTarget callBack){
        return fadeTo(0.0,speed,callBack);
    }

    public $ fadeOut(int speed){
        return fadeIn(speed,null);
    }

    public $ fadeOut(){
        return fadeIn(FAST,null);
    }
}
