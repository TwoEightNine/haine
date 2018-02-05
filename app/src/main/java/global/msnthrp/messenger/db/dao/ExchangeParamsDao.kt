package global.msnthrp.messenger.db.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import global.msnthrp.messenger.model.ExchangeParams

/**
 * Created by twoeightnine on 2/5/18.
 */
class ExchangeParamsDao(connectionSource: ConnectionSource,
                        dataClass: Class<ExchangeParams>) : BaseDaoImpl<ExchangeParams, Int>(connectionSource, dataClass) {



}