package me.onionpie.pandorabox.Model;

import android.os.Parcel;
import android.os.Parcelable;

import me.onionpie.greendao.PasswordTextItem;

/**
 * Created by Gstansen on 2016/4/13.
 */
public class PasswordTextInfoModel implements Parcelable{
    public String jsonString;
    public long id;
    public String description;
    public String time;
    /**
     * 所有规则的名字
     */
    public String ruleName="";
    public String realPassword="";
    public String akString;
    public String passwordPreview="";
    public PasswordTextInfoModel(){

    }
    protected PasswordTextInfoModel(Parcel in) {
        jsonString = in.readString();
        id = in.readLong();
        description = in.readString();
        time = in.readString();
        ruleName = in.readString();
        realPassword = in.readString();
        akString = in.readString();
        passwordPreview= in.readString();
    }

    public static final Creator<PasswordTextInfoModel> CREATOR = new Creator<PasswordTextInfoModel>() {
        @Override
        public PasswordTextInfoModel createFromParcel(Parcel in) {
            return new PasswordTextInfoModel(in);
        }

        @Override
        public PasswordTextInfoModel[] newArray(int size) {
            return new PasswordTextInfoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jsonString);
        dest.writeLong(id);
        dest.writeString(description);
        dest.writeString(time);
        dest.writeString(ruleName);
        dest.writeString(realPassword);
        dest.writeString(akString);
        dest.writeString(passwordPreview);
    }
}
