package me.onionpie.pandorabox.Model;

/**
 * Created by jiudeng007 on 2016/5/6.
 */
public class PasswordRuleNameModel {
    public int ruleId;
    public String ruleName;
    public String ruleDescription;

    public PasswordRuleNameModel(int ruleId, String ruleName, String description) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.ruleDescription = description;
    }
}
