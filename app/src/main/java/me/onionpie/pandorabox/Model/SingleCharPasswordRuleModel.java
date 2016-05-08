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
    public boolean mIsRuleSetted = false;
    /**
     * 更改前的密码字符
     */
    public String mTargetChar;
    /**
     * 选择的规则的位置
     */
    public int mSelectedPosition = -1;
    /**
     * 更改后的密码字符
     */
    public String mDestinyChar;
    public int mExchangePosition;

    public SingleCharPasswordRuleModel() {

    }

    public SingleCharPasswordRuleModel(int ruleId, String targetChar) {
        this.mRuleId = ruleId;
        this.mTargetChar = targetChar;
        this.mDestinyChar = targetChar;
    }

    protected SingleCharPasswordRuleModel(Parcel in) {
        mRuleId = in.readInt();
        mRuleName = in.readString();
        mRuleDescription = in.readString();
        mIsRuleSetted = in.readByte() != 0;
        mTargetChar = in.readString();
        mSelectedPosition = in.readInt();
        mDestinyChar = in.readString();
        mExchangePosition = in.readInt();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mRuleId);
        parcel.writeString(mRuleName);
        parcel.writeString(mRuleDescription);
        parcel.writeByte((byte) (mIsRuleSetted ? 1 : 0));
        parcel.writeString(mTargetChar);
        parcel.writeInt(mSelectedPosition);
        parcel.writeString(mDestinyChar);
        parcel.writeInt(mExchangePosition);
    }
//    public SingleCharPasswordRuleModel(int ruleId, String ruleName, String ruleDescription){
//        this.mRuleId = ruleId;
//        this.mRuleName = ruleName;
//        this.mRuleDescription = ruleDescription;
//    }


}
