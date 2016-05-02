package me.onionpie.pandorabox.Model;

/**
 * Created by Gstansen on 2016/4/30.
 */
public class PasswordRuleModel {
    public int mRuleId;
    public String mRuleName;
    public String mRuleDescription;
    public PasswordRuleModel(){

    }
    public PasswordRuleModel(int ruleId,String ruleName,String ruleDescription){
        this.mRuleId = ruleId;
        this.mRuleName = ruleName;
        this.mRuleDescription = ruleDescription;
    }
}
