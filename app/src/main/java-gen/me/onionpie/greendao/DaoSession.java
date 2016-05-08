package me.onionpie.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import me.onionpie.greendao.PasswordTextItem;

import me.onionpie.greendao.PasswordTextItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig passwordTextItemDaoConfig;

    private final PasswordTextItemDao passwordTextItemDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        passwordTextItemDaoConfig = daoConfigMap.get(PasswordTextItemDao.class).clone();
        passwordTextItemDaoConfig.initIdentityScope(type);

        passwordTextItemDao = new PasswordTextItemDao(passwordTextItemDaoConfig, this);

        registerDao(PasswordTextItem.class, passwordTextItemDao);
    }
    
    public void clear() {
        passwordTextItemDaoConfig.getIdentityScope().clear();
    }

    public PasswordTextItemDao getPasswordTextItemDao() {
        return passwordTextItemDao;
    }

}