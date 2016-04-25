package me.onionpie.pandorabox.Utils;

import android.text.TextUtils;

/**
 * Created by stansen on 2015/4/12.
 */
public class InputTypeCheck {
    /**
     * 判断文本是否符合要求
     *
     * @param text 输入的内容
     * @return 是否符合要求，符合为true
     */
    public static boolean CheckByRules(String text) {
        boolean success = true;
//        String failedReason = "符合要求";
        if (text.length() > 5 && text.length() < 13) {
            for (char temp : text.toCharArray()) {
                int ascllValue = (int) temp;
                if (!isNumber(ascllValue) && !isLowLetter(ascllValue) && !isUpLetter(ascllValue)) {
//                    failedReason = "只能为数字与大小写字母组合，长度在6-12位之间";
                    success = false;
                    return success;
                }
            }
        } else
            success = false;
        return success;
    }

    public static boolean CheckByRulesOnlyNumber(String text){
        boolean success = true;
        if (text.length() == 6){
            for (char temp : text.toCharArray()) {
                int ascllValue = (int) temp;
                if (!isNumber(ascllValue)) {
//                    failedReason = "只能为数字与大小写字母组合，长度在6-12位之间";
                    success = false;
                    return success;
                }
            }
        }
        else {
            success = false;
        }
        return success;
    }

    public static boolean simpleCheck(String text){
        boolean success;
        success = text.length() > 5 && text.length() < 13;
        return success;
    }
    /**
     *判断输入的内容是否为空
     *  @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        boolean isEmpty = false;
        if (TextUtils.isEmpty(text)) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public static boolean isNumber(int ascllValue) {
        return ascllValue >= 48 && ascllValue <= 57;
    }

    public static boolean isUpLetter(int ascllValue) {
        return ascllValue >= 65 && ascllValue <= 90;
    }

    public static boolean isLowLetter(int ascllValue) {
        return ascllValue >= 97 && ascllValue <= 122;
    }
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
//        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3587]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    public static boolean CheckIllegalChar(String text){
        return text.startsWith(" ") || text.startsWith("\n");
    }


}
