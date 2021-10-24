package com.parallelcodes.monkeytech.Utils

import com.parallelcodes.monkeytech.models.GiphySearchImagePoko

interface AsyncResponseGif {
    fun processGifFinish(output: List<GiphySearchImagePoko.GiphySearchDatum>)
}