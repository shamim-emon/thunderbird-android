package com.fsck.k9.ui.managefolders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import app.k9mail.legacy.account.LegacyAccount
import app.k9mail.legacy.ui.folder.DisplayFolder
import app.k9mail.legacy.ui.folder.DisplayFolderRepository

class ManageFoldersViewModel(
    private val folderRepository: DisplayFolderRepository,
) : ViewModel() {
    fun getFolders(account: LegacyAccount): LiveData<List<DisplayFolder>> {
        return folderRepository.getDisplayFoldersFlow(account, includeHiddenFolders = true).asLiveData()
    }
}
