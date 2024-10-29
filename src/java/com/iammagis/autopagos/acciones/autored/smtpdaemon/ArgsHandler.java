/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.acciones.autored.smtpdaemon;

import org.apache.commons.cli.Options;

/**
 *
 * @author root
 */
public class ArgsHandler {

    private static final String OPT_AUTOSTART_SHORT = "s";
    private static final String OPT_AUTOSTART_LONG = "start-server";
    private static final String OPT_AUTOSTART_DESC = "Automatically starts the SMTP server at launch";

    private static final String OPT_BACKGROUNDSTART_SHORT = "b";
    private static final String OPT_BACKGROUNDSTART_LONG = "background";
    private static final String OPT_BACKGROUNDSTART_DESC = "If specified, does not start the GUI. Must be used with the -" + OPT_AUTOSTART_SHORT + " (--" + OPT_AUTOSTART_LONG + ") argument";

    private static final String OPT_BINDADDRESS_SHORT = "a";
    private static final String OPT_BINDADDRESS_LONG = "bind-address";
    private static final String OPT_BINDADDRESS_DESC = "IP address or hostname to bind to. Binds to all local IP addresses if not specified. Only works together with the -" + OPT_BACKGROUNDSTART_SHORT + " (--" + OPT_BACKGROUNDSTART_LONG + ") argument.";

    private final Options options;

    public ArgsHandler(Options options) {
        this.options = options;
        options.addOption(OPT_AUTOSTART_SHORT, OPT_AUTOSTART_LONG, false, OPT_AUTOSTART_DESC);
        options.addOption(OPT_BINDADDRESS_SHORT, OPT_BINDADDRESS_LONG, true, OPT_BINDADDRESS_DESC);         
    }

}
