package ru.otus.cookbook.ui

import java.util.UUID

interface ItemListener {
    fun onItemClick(id: Int)
    fun onSwipe(id: Int)

}