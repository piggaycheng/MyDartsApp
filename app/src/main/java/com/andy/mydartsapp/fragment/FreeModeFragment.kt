package com.andy.mydartsapp.fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.andy.mydartsapp.R
import com.andy.mydartsapp.DataMap
import com.andy.mydartsapp.service.BluetoothLeService
import kotlinx.android.synthetic.main.fragment_free_mode.view.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [FreeModeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FreeModeFragment : Fragment() {
    val dataMap: DataMap = DataMap()
    lateinit var scoreMap: JSONObject
    private var ratioTextView: TextView? = null
    private var scoreTextView: TextView? = null

    private val soundId = HashMap<Number, Number>()
    private val mSoundPool = SoundPool.Builder().setMaxStreams(8).build()
    private val soundFolder = "sounds"
    private val hitSoundFile = "Hit.mp3"
    private val bullSoundFile = "Bull.mp3"
    private val blackSoundFile = "Black.mp3"
//    private val mAudioManager = activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_free_mode, container, false)
        ratioTextView = rootView.textView_ratio
        scoreTextView = rootView.textView_score

        activity!!.registerReceiver(mUpdateReceiver, mUpdateIntentFilter())

        val mAssets = context!!.assets
        soundId[0] = mSoundPool.load(mAssets.openFd("$soundFolder/$hitSoundFile"), 1)
        soundId[1] = mSoundPool.load(mAssets.openFd("$soundFolder/$bullSoundFile"), 1)
        soundId[2] = mSoundPool.load(mAssets.openFd("$soundFolder/$blackSoundFile"), 1)

        return rootView
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(mUpdateReceiver)
    }

    override fun onResume() {
        super.onResume()
        activity!!.registerReceiver(mUpdateReceiver, mUpdateIntentFilter())
    }

    private val mUpdateReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            when(action) {
                BluetoothLeService.ACTION_SCORE_RETRIEVE -> {
                    val data = intent.getStringExtra("data")
                    if(data != "BTN@") {
                        scoreMap = dataMap.getScoreMap(data) as JSONObject

                        ratioTextView!!.text = scoreMap.getString("ratio")
                        scoreTextView!!.text = scoreMap.getString("score")

                        Thread(Runnable {
                            kotlin.run {
                                if(scoreMap.getString("score") == "50") {
                                    when (scoreMap.getString("ratio")) {
                                        "single" -> {
                                            //资源Id，左音量，右音量，优先级，循环次数,速率
                                            mSoundPool.play(soundId[1]!!.toInt(), 1.0f, 1.0f, 1, 0, 1.0f)
                                        }
                                        "double" -> {
                                            mSoundPool.play(soundId[2]!!.toInt(), 1.0f, 1.0f, 1, 0, 1.0f)
                                        }
                                    }
                                } else {
                                    when (scoreMap.getString("ratio")) {
                                        "double" -> {
                                            mSoundPool.play(soundId[0]!!.toInt(), 1.0f, 1.0f, 1, 1, 7.0f)
                                        }
                                        "triple" -> {
                                            mSoundPool.play(soundId[0]!!.toInt(), 1.0f, 1.0f, 1, 2, 7.0f)
                                        }
                                        else -> {
                                            mSoundPool.play(soundId[0]!!.toInt(), 1.0f, 1.0f, 1, 0, 1.0f)
                                        }
                                    }
                                }

                                Log.d(TAG, "sound thread interrupt")
                                Thread.currentThread().interrupt()
                            }
                        }).start()

                    } else {

                    }
                }
            }
        }
    }

    private fun mUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_SCORE_RETRIEVE)
        return intentFilter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FreeModeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FreeModeFragment().apply {
                arguments = Bundle().apply {

                }
            }

        private val TAG: String = FreeModeFragment::class.java.simpleName
    }
}
