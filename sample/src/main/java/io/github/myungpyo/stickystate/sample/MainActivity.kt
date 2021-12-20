package io.github.myungpyo.stickystate.sample

import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import io.github.myungpyo.ei.R
import io.github.myungpyo.stickystate.StickyState
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize

class MainActivity : AppCompatActivity() {

    private val viewModel: NavViewModel by viewModels()

    @StickyState
    var stringProp: String = "aaa"

    @StickyState
    var stringArrayProp: Array<String> = arrayOf("a", "b", "c")

    @StickyState
    var stringArrayListProp: ArrayList<String> = arrayListOf("a", "b", "c")

    @StickyState
    var byteProp: Byte = 1

    @StickyState
    var byteArrayProp: ByteArray = byteArrayOf(1, 2, 3)

    @StickyState
    var charProp: Char = 'a'

    @StickyState
    var charArrayProp: CharArray = charArrayOf('a', 'b', 'c')

    @StickyState
    var shortProp: Short = 1

    @StickyState
    var shortArrayProp: ShortArray = shortArrayOf(1, 2, 3)

    @StickyState
    var intProp: Int = 1

    @StickyState
    var intArrayProp: IntArray = intArrayOf(1, 2, 3)

    @StickyState
    var intArrayListProp: ArrayList<Int> = arrayListOf(1, 2, 3)

    @StickyState
    var longProp: Long = 1L

    @StickyState
    var longArrayProp: LongArray = longArrayOf(1, 2, 3)

    @StickyState
    var floatProp: Float = 1f

    @StickyState
    var floatArrayProp: FloatArray = floatArrayOf(1f)

    @StickyState
    var doubleProp: Double = 1.0

    @StickyState
    var doubleArrayProp: DoubleArray = doubleArrayOf(1.0, 2.0, 3.0)

    @StickyState
    var booleanProp: Boolean = true

    @StickyState
    var booleanArrayProp: BooleanArray = booleanArrayOf(true, false)

    @StickyState
    var bundleProp: Bundle = bundleOf("v1" to 1, "v2" to 2, "v3" to 3)

    @StickyState
    var charSequenceProp: CharSequence = "abc"

    @StickyState
    var parcelableProp: Parcelable = TempParcel("a", 1)

    @StickyState
    var sizeProp: Size = Size(1, 2)

    @StickyState
    var sizeFProp: SizeF = SizeF(1F, 2F)

    @StickyState
    var sparseParcelableArrayProp: SparseArray<Parcelable> = SparseArray<Parcelable>().apply {
        put(0, TempParcel("a", 1))
        put(1, TempParcel("b", 2))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        MainActivityStateBinding.restore(this, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.navEvent.collect { navEvent ->
                when (navEvent) {
                    is NavEvent.Primary -> navToPrimary()
                    is NavEvent.Secondary -> navToSecondary(navEvent.userName)
                    is NavEvent.Back -> navBack()
                }
            }
        }

        savedInstanceState ?: navToPrimary()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        MainActivityStateBinding.save(this, outState)
        super.onSaveInstanceState(outState)
    }

    private fun navToPrimary() {
        navTo(
            fragment = PrimaryFragment.create(),
        )
    }

    private fun navToSecondary(userName: String) {
        navTo(
            fragment = SecondaryFragment.create(userName),
            addToBackStack = true,
        )
    }

    private fun navBack() {
        if (supportFragmentManager.backStackEntryCount == 0) return
        supportFragmentManager.popBackStackImmediate()
    }

    private fun navTo(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
            if (addToBackStack) addToBackStack(null)
        }
    }

    @Parcelize
    class TempParcel(val data1: String, val data2: Int): Parcelable
}