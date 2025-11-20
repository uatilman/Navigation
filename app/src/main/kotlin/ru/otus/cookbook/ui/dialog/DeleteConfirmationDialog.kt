package ru.otus.cookbook.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.otus.cookbook.R

/**
 * Диалог подтверждения удаления рецепта
 */
class DeleteConfirmationDialog : DialogFragment() {
    companion object {
        const val CONFIRMATION_RESULT = "delete_confirmation_result"
    }

    private val recipeName get() = DeleteConfirmationDialogArgs.fromBundle(requireArguments()).recipeName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_recipe_question) + " " + recipeName + "?")
            .setTitle(getString(R.string.delete_dialog_title))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                dismiss()
                setResult(true)
            }
            .setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                dismiss()
                setResult(false)
            }
            .create()

    private fun setResult(result: Boolean) {
        with(findNavController()) {
            previousBackStackEntry?.savedStateHandle?.set(CONFIRMATION_RESULT, result)
            findNavController().popBackStack()
        }
    }
}