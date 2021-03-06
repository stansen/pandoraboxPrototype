package me.onionpie.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PASSWORD_TEXT_ITEM".
 */
public class PasswordTextItem {

    private Long id;
    /** Not-null value. */
    private String jsonString;
    private String description;
    private String ak;
    private String date;

    public PasswordTextItem() {
    }

    public PasswordTextItem(Long id) {
        this.id = id;
    }

    public PasswordTextItem(Long id, String jsonString, String description, String ak, String date) {
        this.id = id;
        this.jsonString = jsonString;
        this.description = description;
        this.ak = ak;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getJsonString() {
        return jsonString;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
