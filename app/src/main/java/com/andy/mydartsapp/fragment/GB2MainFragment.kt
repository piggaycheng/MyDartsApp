package com.andy.mydartsapp.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.andy.mydartsapp.R
import kotlinx.android.synthetic.main.fragment_gb2_main.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [GB2MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GB2MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_gb2_main, container, false)
        rootView.btn_leave_gb2.setOnClickListener(mOnClick)
        rootView.btn_free_mode.setOnClickListener(mOnClick)

        return rootView
    }

    private val mOnClick = View.OnClickListener {
        when(it.id) {
            R.id.btn_leave_gb2 -> {
                activity!!.finish()
            }
            R.id.btn_free_mode -> {
                val freeModeFragment = FreeModeFragment.newInstance()
                openFragment(freeModeFragment)
            }
        }
    }

    private fun openFragment(fragment: android.support.v4.app.Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.gb2_frameLayout_container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GB2MainFragment.
         */

        @JvmStatic
        fun newInstance() = GB2MainFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
