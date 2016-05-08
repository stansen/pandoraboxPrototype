package me.onionpie.greendao;

import android.content.Context;

import de.greenrobot.dao.async.AsyncSession;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;

/**
 * Created by Gstansen on 2016/5/8.
 */
public class DBHelper {
    private static DBHelper instance;
    private static Context mContext;

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private PasswordTextItemDao passwordTextItemDao;

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    "pandoraBox.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private DBHelper() {
    }

    public static void init(Context context) {
        mContext = context;
        instance = new DBHelper();
        // 数据库对象
        DaoSession daoSession = getDaoSession(mContext);
        instance.passwordTextItemDao = daoSession.getPasswordTextItemDao();

    }

    public PasswordTextItemDao getPasswordTextItemDao() {
        return passwordTextItemDao;
    }

    public void setPasswordTextItemDao(PasswordTextItemDao passwordTextItemDao) {
        this.passwordTextItemDao = passwordTextItemDao;
    }

    public static DBHelper getInstance() {
        return instance;
    }






}
