
package com.nani.vnto.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Authentication {

    @SerializedName("value")
    private String value;
    private Account account;
    private String expirationDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 
     * @return
     *     The account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * 
     * @return
     *     The expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * 
     * @param expirationDate
     *     The expirationDate
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
