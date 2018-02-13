package global.msnthrp.messenger

import global.msnthrp.messenger.utils.getNameFromUrl
import global.msnthrp.messenger.utils.isUrl
import org.junit.Test

/**
 * Created by twoeightnine on 2/13/18.
 */
class UtilTest {

    @Test
    fun isUrl_isCorrect() {
        assert(isUrl("https://vk.com"))
        assert(isUrl("http://p.g.com/dfgdfg"))
        assert(isUrl("https://vk.com/?as=jhjd"))
        assert(isUrl("https://vk.com/?asasd=12121#ksdjksjda"))
        assert(isUrl("https://vk.com/qwqeqwe/qasdffh"))
        assert(isUrl("vk.com"))
        assert(isUrl("primavera.hol.es"))
        assert(isUrl("sub.site.com/page/file.mp3"))
        assert(!isUrl("https://"))
        assert(!isUrl("vkcom"))
        assert(!isUrl(""))
    }

    @Test
    fun getName_isCorrect() {
        assert(getNameFromUrl("site.com/path/file.mp3") == "file")
        assert(getNameFromUrl("site.com/path/file.mp3?additional") == "file")
    }

}