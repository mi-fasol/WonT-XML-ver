package com.example.xml_ver.util

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.xml_ver.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun setupSpinner(
    spinner: Spinner,
    itemsArrayResId: Int,
    context: Context,
    onItemSelectedAction: (String) -> Unit
) {

    ArrayAdapter.createFromResource(
        context,
        itemsArrayResId,
        R.layout.item_dropdown
    ).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    spinner.setSelection(0)

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View?,
            position: Int,
            id: Long
        ) {
            val selectedItem = parent.getItemAtPosition(position) as String
            if (position != 0) {
                onItemSelectedAction(selectedItem)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
}

fun setupSpinnerClickEvent(
    spinner: Spinner,
    onItemSelectedAction: (String) -> Unit
) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View?,
            position: Int,
            id: Long
        ) {
            val selectedItem = parent.getItemAtPosition(position) as String
            if (position != 0) {
                onItemSelectedAction(selectedItem)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
}

fun setupButtonState(isValid: Boolean, button: Button, context: Context) {
    button.isEnabled = isValid
    button.isClickable = isValid

    if (isValid) {
        button.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.mainColor
            )
        )
    } else {
        button.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.buttonDisabledColor
            )
        )
    }
}