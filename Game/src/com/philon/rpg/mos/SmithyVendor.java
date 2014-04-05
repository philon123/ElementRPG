package com.philon.rpg.mos;

import com.philon.rpg.forms.AbstractVendorForm;
import com.philon.rpg.forms.SmithyForm;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;

public class SmithyVendor extends AbstractVendor {

  public AbstractItem createItem() {
    AbstractItem result = ItemData.createRandomItem(20);
    return result;
  }

  @Override
  protected Class<? extends AbstractVendorForm> getFormClass() {
    return SmithyForm.class;
  }

}