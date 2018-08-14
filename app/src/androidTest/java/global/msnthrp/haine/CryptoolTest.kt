package global.msnthrp.haine

import android.support.test.runner.AndroidJUnit4
import global.msnthrp.haine.utils.Cryptool
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by twoeightnine on 2/9/18.
 */
@RunWith(AndroidJUnit4::class)
class CryptoolTest {

    @Test
    fun cryptool_isCorrect() {
        val cry11 = Cryptool(SHARED1)
        val cry12 = Cryptool(SHARED1)
        assert(cry11.getFingerPrint() == cry12.getFingerPrint())

        val enc = cry11.encrypt(TEXT)
        val dec = cry12.decrypt(enc)
        assert(TEXT == dec)

        val cry2 = Cryptool(SHARED2)
        assert(cry11.isSharedOther(SHARED2))
        assert(cry2.isSharedOther(SHARED1))
    }

    companion object {
        val SHARED1 = "938784623293"
        val SHARED2 = "93878462329sdsdsds3"

        val TEXT = "iwyriw oworyuewv oirtr qwrhuoapwcnuqw owtqwryuq q0wt8q0etq t084u t0q"
    }

}