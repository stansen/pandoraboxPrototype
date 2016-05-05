package me.onionpie.pandorabox.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gstansen on 2016/4/30.
 */
public class SingleCharPasswordRuleModel implements Parcelable{
    public int mRuleId;
    public String mRuleName;
    public String mRuleDescription;
    /**
     * 是否设置了规则
     */
    public boolean mIsRuleSetted;
    public String mTargetChar;
    public SingleCharPasswordRuleModel(){

    }
    public SingleCharPasswordRuleModel(int ruleId, String ruleName, String ruleDescription){
        this.mRuleId = ruleId;
        this.mRuleName = ruleName;
        this.mRuleDescription = ruleDescription;
    }

    protected SingleCharPasswordRuleModel(Parcel in) {
        mRuleId = in.readInt();
        mRuleName = in.readString();
        mRuleDescription = in.readString();
        mIsRuleSetted = in.readByte() != 0;
        mTargetChar = in.readString();
    }

    public static final Creator<SingleCharPasswordRuleModel> CREATOR = new Creator<SingleCharPasswordRuleModel>() {
        @Override
        public SingleCharPasswordRuleModel createFromParcel(Parcel in) {
            return new SingleCharPasswordRuleModel(in);
        }

        @Override
        public SingleCharPasswordRuleModel[] newArray(int size) {
            return new SingleCharPasswordRuleModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRuleId);
        dest.writeString(mRuleName);
        dest.writeString(mRuleDescription);
        dest.writeByte((byte) (mIsRuleSetted ? 1 : 0));
        dest.writeString(mTargetChar);
    }
}
