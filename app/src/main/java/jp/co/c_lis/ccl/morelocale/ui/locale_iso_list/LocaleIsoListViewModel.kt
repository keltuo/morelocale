package jp.co.c_lis.ccl.morelocale.ui.locale_iso_list

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.c_lis.ccl.morelocale.R
import jp.co.c_lis.ccl.morelocale.entity.LocaleIsoItem
import jp.co.c_lis.ccl.morelocale.repository.LocaleIsoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocaleIsoListViewModel(
        private val localeIsoRepository: LocaleIsoRepository
) : ViewModel() {

    val localeIsoList = MutableLiveData<List<LocaleIsoItem>>()

    fun init(resources: Resources) {
        viewModelScope.launch {
            val list = localeIsoRepository.findAll()
            if (list.isNotEmpty()) {
                localeIsoList.postValue(list)
                return@launch
            }

            init(
                    resources.getStringArray(R.array.iso_3166_title),
                    resources.getStringArray(R.array.iso_3166_value)
            )
            init(
                    resources.getStringArray(R.array.iso_639_title),
                    resources.getStringArray(R.array.iso_639_value)
            )

            localeIsoList.postValue(localeIsoRepository.findAll())
        }
    }

    suspend fun init(
            titles: Array<String>,
            values: Array<String>
    ) = withContext(Dispatchers.Default) {
        titles.forEachIndexed { index, title ->
            val value = values[index]
            localeIsoRepository.add(LocaleIsoItem(label = title, value = value, isListed = true))
        }
    }
}