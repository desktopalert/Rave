package com.ravemobilesafety;


import com.metissecure.api.alert.AlertModifierController;
import com.metissecure.spi.alert.AlertDistributionMethod;


/**
 *
 * @author Matt McHenry
 */
public class RaveDispatchMethod implements AlertDistributionMethod {

    static final String METHOD_NAME = "Rave Mobile Safety";
    private static final String DESCRIPTION =
            "Generates a CAP formatted message representing the alert to send "
            + "to Rave Mobile Safety.";
    private static final String ICON_URL = "/plugin/rave/img/rave_dsptchmthd.png";
    private static final String ACTIVE_PROPERTY = "rave.enabled";
    private static final boolean ACTIVE_DEFAULT = false;


    @Override
    public String getName() {
        return METHOD_NAME;
    }

    @Override
    public String getIconUrl() {
        return ICON_URL;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getActivateProperty() {
        return ACTIVE_PROPERTY;
    }

    @Override
    public boolean isActiveDefault() {
        return ACTIVE_DEFAULT;
    }

    @Override
    public AlertModifierController newAlertModifier() {
        return null;
    }

}
