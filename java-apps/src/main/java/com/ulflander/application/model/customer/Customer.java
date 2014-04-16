package com.ulflander.application.model.customer;

/**
 * This classe is a POJO representation of a Minethat customer.
 */
public class Customer {

    /**
     * Customer ID.
     */
    private int id;

    /**
     * Customer private key (for authentication).
     */
    private String privateKey;

    /**
     * Customer email address.
     */
    private String email;

    /**
     * Customer firstname.
     */
    private String firstname;

    /**
     * Customer lastname.
     */
    private String lastname;

    /**
     * Customer current plan.
     */
    private String plan;

    /**
     * Get customer ID.
     *
     * @return Customer ID.
     */
    public final int getId() {
        return id;
    }

    /**
     * Set customer ID.
     *
     * @param i Customer ID.
     */
    public final void setId(final int i) {
        this.id = i;
    }

    /**
     * Get customer private key.
     *
     * @return Customer code
     */
    public final String getPrivateKey() {
        return privateKey;
    }

    /**
     * Set customer private key.
     *
     * @param cc Customer private key.
     */
    public final void setPrivateKey(final String cc) {
        this.privateKey = cc;
    }

    /**
     * Get customer email address.
     *
     * @return Customer email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Set customer email address.
     *
     * @param e Customer email
     */
    public final void setEmail(final String e) {
        this.email = e;
    }

    /**
     * Get customer plan value.
     *
     * @return Customer plan
     */
    public final String getPlan() {
        return plan;
    }

    /**
     * Set customer plan value.
     *
     * @param o Customer plan
     */
    public final void setPlan(final String o) {
        this.plan = o;
    }

    /**
     * Get customer firstname value.
     *
     * @return Customer firstname
     */
    public final String getFirstname() {
        return firstname;
    }

    /**
     * Set customer firstname value.
     *
     * @param o Customer firstname
     */
    public final void setFirstname(final String o) {
        this.firstname = o;
    }

    /**
     * Get customer lastname value.
     *
     * @return Customer lastname
     */
    public final String getLastname() {
        return lastname;
    }

    /**
     * Set customer lastname value.
     *
     * @param o Customer lastname
     */
    public final void setLastname(final String o) {
        this.lastname = o;
    }

}
