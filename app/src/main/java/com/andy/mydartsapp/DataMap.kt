package com.andy.mydartsapp

import org.json.JSONObject

class DataMap {
    companion object {
        private val TAG: String = DataMap::class.java.simpleName
    }

    private val dataMap = HashMap<String, JSONObject>()
    private val scoreMap = JSONObject()

    init {
        //---------------1---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "1")
        dataMap["2.3@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "1")
        dataMap["2.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "1")
        dataMap["2.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "1")
        dataMap["2.4@"] = scoreMap
        //---------------2---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "2")
        dataMap["9.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "2")
        dataMap["9.2@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "2")
        dataMap["8.2@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "2")
        dataMap["9.0@"] = scoreMap
        //---------------3---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "3")
        dataMap["7.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "3")
        dataMap["7.2@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "3")
        dataMap["8.4@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "3")
        dataMap["7.0@"] = scoreMap
        //---------------4---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "4")
        dataMap["0.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "4")
        dataMap["0.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "4")
        dataMap["0.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "4")
        dataMap["0.3@"] = scoreMap
        //---------------5---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "5")
        dataMap["5.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "5")
        dataMap["5.4@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "5")
        dataMap["4.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "5")
        dataMap["5.2@"] = scoreMap
        //---------------6---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "6")
        dataMap["1.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "6")
        dataMap["1.3@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "6")
        dataMap["4.4@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "6")
        dataMap["1.1@"] = scoreMap
        //---------------7---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "7")
        dataMap["11.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "7")
        dataMap["11.4@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "7")
        dataMap["8.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "7")
        dataMap["11.2@"] = scoreMap
        //---------------8---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "8")
        dataMap["6.2@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "8")
        dataMap["6.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "8")
        dataMap["6.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "8")
        dataMap["6.4@"] = scoreMap
        //---------------9---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "9")
        dataMap["9.3@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "9")
        dataMap["9.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "9")
        dataMap["9.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "9")
        dataMap["9.4@"] = scoreMap
        //---------------10---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "10")
        dataMap["2.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "10")
        dataMap["2.2@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "10")
        dataMap["4.3@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "10")
        dataMap["2.1@"] = scoreMap
        //---------------11---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "11")
        dataMap["7.3@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "11")
        dataMap["7.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "11")
        dataMap["7.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "11")
        dataMap["7.4@"] = scoreMap
        //---------------12---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "12")
        dataMap["5.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "12")
        dataMap["5.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "12")
        dataMap["5.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "12")
        dataMap["5.3@"] = scoreMap
        //---------------13---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "13")
        dataMap["0.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "13")
        dataMap["0.4@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "13")
        dataMap["4.5@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "13")
        dataMap["0.2@"] = scoreMap
        //---------------14---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "14")
        dataMap["10.3@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "14")
        dataMap["10.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "14")
        dataMap["10.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "14")
        dataMap["10.4@"] = scoreMap
        //---------------15---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "15")
        dataMap["3.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "15")
        dataMap["3.2@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "15")
        dataMap["4.2@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "15")
        dataMap["3.1@"] = scoreMap
        //---------------16---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "16")
        dataMap["11.0@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "16")
        dataMap["11.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "16")
        dataMap["11.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "16")
        dataMap["11.3@"] = scoreMap
        //---------------17---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "17")
        dataMap["10.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "17")
        dataMap["10.2@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "17")
        dataMap["8.3@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "17")
        dataMap["10.0@"] = scoreMap
        //---------------18---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "18")
        dataMap["1.2@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "18")
        dataMap["1.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "18")
        dataMap["1.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "18")
        dataMap["1.4@"] = scoreMap
        //---------------19---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "19")
        dataMap["6.1@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "19")
        dataMap["6.3@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "19")
        dataMap["8.5@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "19")
        dataMap["6.0@"] = scoreMap
        //---------------20---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "20")
        dataMap["3.3@"] = scoreMap
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "20")
        dataMap["3.5@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "20")
        dataMap["3.6@"] = scoreMap
        scoreMap.put("ratio", "triple")
        scoreMap.put("score", "20")
        dataMap["3.4@"] = scoreMap
        //---------------50---------------
        scoreMap.put("ratio", "single")
        scoreMap.put("score", "50")
        dataMap["8.0@"] = scoreMap
        scoreMap.put("ratio", "double")
        scoreMap.put("score", "50")
        dataMap["4.0@"] = scoreMap
    }

    fun getScoreMap(data: String): JSONObject? {
        return dataMap[data]
    }
}