package global.msnthrp.haine

import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.Button
import android.widget.TextView
import global.msnthrp.haine.login.LoginActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule @JvmField
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @UiThreadTest
    @Test
    fun activity_inputHint() {
        val activity = rule.activity
        val tvInputHint = activity.findViewById<TextView>(R.id.tvInputHint)
        val hintFirst = tvInputHint.text
        tvInputHint.performClick()
        val hintSecond = tvInputHint.text
        tvInputHint.performClick()
        val hintThird = tvInputHint.text
        Assert.assertFalse(hintFirst == hintSecond)
        Assert.assertTrue(hintFirst == hintThird)
    }

    @UiThreadTest
    @Test
    fun activity_buttonText() {
        val activity = rule.activity
        val tvInputHint = activity.findViewById<TextView>(R.id.tvInputHint)
        val button = activity.findViewById<Button>(R.id.btnLogin)
        val textFirst = button.text
        tvInputHint.performClick()
        val textSecond = button.text
        tvInputHint.performClick()
        val textThird = button.text
        Assert.assertFalse(textFirst == textSecond)
        Assert.assertTrue(textFirst == textThird)
    }

}