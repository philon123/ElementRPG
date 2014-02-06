package com.philon.engine;

import java.util.ArrayList;

import com.philon.engine.forms.AbstractForm;

public class Forms {
  public ArrayList<AbstractForm> activeForms;
  
  public Forms() {
    activeForms = new ArrayList<AbstractForm>();
  }
  
  public void addForm(AbstractForm newForm) {
    if (activeForms.contains(newForm)) throw new IllegalArgumentException("tried to insert form twice");
    activeForms.add(newForm);
  }
  
  public void removeForm(AbstractForm form) {
    activeForms.remove(form);
  }
  
  public AbstractForm getFormByClass( Class<? extends AbstractForm> formClass ) {
    for (AbstractForm currForm : activeForms) {
      if (currForm.getClass().equals(formClass)) {
        return currForm;
      }
    }
    return null;
  }

  public boolean isFormActive( Class<? extends AbstractForm> formClass ) {
    return getFormByClass(formClass)!=null;
  }
  
  public void updateForms() {
    for (AbstractForm form : activeForms) {
      form.update();
    }
  }

}
