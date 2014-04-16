package com.ulflander.application.services.email;

import com.ulflander.application.Conf;

/**
 * Creates a Gmail dedicated IMAP connector.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class IMAP4Gmail extends IMAPConnection {

    /**
     * Instanciate a new instance, setup configuration.
     */
    public IMAP4Gmail() {

        this.setHost("gmail");
        this.setLogin(Conf.getEmailServiceUser());
        this.setPass(Conf.getEmailServicePass());

    }

}
