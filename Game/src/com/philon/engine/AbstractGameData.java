package com.philon.engine;

import com.philon.rpg.GameDatabase;

public abstract class AbstractGameData {
  
  public void loadMedia() {
    Object[][] result = GameDatabase.db.execQuery("SELECT * FROM " + getTableName());
    execInitArrays(result.length);
    
    for (int i=0; i<result.length; i++) {
      execLoadRow((Integer)(result[i][0]), result[i]);
    }
    
    execFinishedLoad();
  }
  
  protected abstract String getTableName();
  
  protected abstract void execInitArrays(int rowCount);
  
  protected abstract void execLoadRow(int rowId, Object[] row);
  
  protected void execFinishedLoad() {
  }
  
}
