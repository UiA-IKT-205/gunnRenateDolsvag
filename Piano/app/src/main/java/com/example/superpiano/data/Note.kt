package com.example.superpiano.data

data class NoteSheet(val keyValue:String, val keyActivatedStartTime:String, val keyTotalActivatedTime:String) {
    override fun toString(): String {
        return "Play key: $keyValue | Relative activation time: $keyActivatedStartTime seconds | Activate key for: $keyTotalActivatedTime seconds"
    }
}