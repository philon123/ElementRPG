package com.philon.engine.input;

import com.philon.engine.forms.GuiElement;

public class User {
  public Controller controller;

  public GuiElement selectedEle = null;
  public GuiElement hoveredOverEle = null;

  public User(Controller newController) {
    controller = newController;
  }
}